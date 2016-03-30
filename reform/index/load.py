import logging
import multiprocessing

from gensim.models.word2vec import LineSentence
from ..constants import *
from ..parse.selector import CorpusSelector
from ..processing.vectorize import Vectorizer
from gensim import utils  # utility fnc for pickling, common scipy operations etc
import itertools


def _file_content(f):
    with open(f) as fn:
        src = fn.read()
        return src


class Loader(object):
    def __init__(self, path, save_path, base_save_name, lang, methods=False, recursive=False,
                 dump=False,
                 skip=False,
                 file_limit=0):
        self.path = path
        self.lang = lang
        self.file_limit = file_limit
        self.save_path = save_path
        self.base_save_name = base_save_name
        self.methods = methods
        self.recursive = recursive
        self.dump = dump
        self.skip = skip
        if self.dump and os.path.exists(self._dump_file_path()) and not self.skip:
            os.remove(self._dump_file_path())

    def build(self, **kwargs):
        """
        builds data for reformulation into storage
        :return: vectorizer
        """
        corpus_storage, parse = CorpusSelector.select(self.lang, self.methods, **kwargs)

        if corpus_storage == NotImplemented:
            logging.error("Not implemented AST refomulation method. "
                          "Please, try other language or another AST type")
            return

        vectorizer = Vectorizer()

        if not self.skip:
            total = 0
            count_files = self._files_to_load()
            for fn in count_files:
                total += 1

            files = self._files_to_load()

            counter = 0
            pool = multiprocessing.Pool(8)
            results = []
            for fn in files:
                counter += 1
                logging.info("Processing file %s, %d/%d", fn, counter, total)
                fc = [_file_content(fn)]
                r = pool.apply_async(parse, fc)
                results.append(r)

                while results and results[0].ready():
                    r = results.pop(0)
                    corpus_storage.add(r.get())
                    if self.dump:
                        self._dump(corpus_storage.snippets)
                        corpus_storage.snippets = []

                # don't let the queue grow too long
                if len(results) >= 1000:
                    try:
                        results[0].wait(0.01)
                        if not results[0].ready():
                            raise multiprocessing.TimeoutError
                    except multiprocessing.TimeoutError:
                        results.pop(0)

            for r in results:
                try:
                    if not r.ready():
                        r.wait(0.01)
                    corpus_storage.add(r.get())
                    if self.dump:
                        self._dump(corpus_storage.snippets)
                        corpus_storage.snippets = []
                except multiprocessing.TimeoutError:
                    pass

        logging.info("Training the model")

        if not self.dump:
            snippets = corpus_storage.snippets
        else:
            snippets = self._dumped_snippets()

        vectorizer.train(snippets, corpus_storage.storage, min_count=MIN_TERM_COUNT)

        logging.info("Saving model to: %s" % self.save_path)
        vectorizer.save(self.save_path, self.base_save_name, self._collect_info())

        return vectorizer

    def load(self):
        """
        loads data for reformulation into storage
        :return: vectorizer
        """
        logging.info("Loading model from: %s" % self.save_path)
        vectorizer = Vectorizer()
        info = vectorizer.load(self.save_path, self.base_save_name)
        self._check_info(info)
        return vectorizer

    def _dump(self, snippets):
        if not hasattr(self, '_dump_file') or self._dump_file.closed:
            self._dump_file = open(self._dump_file_path(), 'a')
        for snippet in snippets:
            self._dump_file.write(' '.join(snippet))
            self._dump_file.write('\n')

    def _dumped_snippets(self):
        return UnicodeLineSentence(self._dump_file_path(), max_sentence_length=9999999)

    def _dump_file_path(self):
        return os.path.join(self.save_path, self.base_save_name) + SNIPPETS_EXT

    def _check_info(self, info):
        if not info:
            return
        if "methods" in info:
            if self.methods != info["methods"]:
                logging.warning("You are using storage that was built for another type of AST. "
                                "Please, specify correct type of AST")
        if "lang" in info:
            if self.lang != info["lang"]:
                logging.warning("You are using storage that was built for another language: %s. "
                                "Please, use another language" % info["lang"])

    def _collect_info(self):
        info = {"methods": self.methods, "lang": self.lang}
        return info

    def _files_to_load(self):
        if os.path.isfile(self.path):
            yield self.path
        else:
            if not self.recursive:
                allfiles = os.listdir(self.path)
                for f in allfiles:
                    if os.path.isfile(os.path.join(self.path, f)):
                        yield os.path.join(self.path, f)
            else:
                for root, dirs, files in os.walk(self.path):
                    for file in files:
                        yield os.path.join(root, file)


class UnicodeLineSentence(LineSentence):
    def __iter__(self):
        """Iterate through the lines in the source."""
        try:
            # Assume it is a file-like object and try treating it as such
            # Things that don't have seek will trigger an exception
            self.source.seek(0)
            for line in itertools.islice(self.source, self.limit):
                line = utils.any2unicode(line, errors='replace').split()
                i = 0
                while i < len(line):
                    yield line[i: i + self.max_sentence_length]
                    i += self.max_sentence_length
        except AttributeError:
            # If it didn't work like a file, use it as a string filename
            with utils.smart_open(self.source) as fin:
                for line in itertools.islice(fin, self.limit):
                    line = utils.any2unicode(line, errors='replace').split()
                    i = 0
                    while i < len(line):
                        yield line[i: i + self.max_sentence_length]
                        i += self.max_sentence_length
