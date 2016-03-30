import logging
import operator
import pickle

from gensim.models.word2vec import Word2Vec
from ..constants import *
from ..processing.normalize import Normalizer


class Vectorizer(object):
    def train(self, corpus, api, min_count):
        """
        :param corpus: snippets, list or another iterable
        :param api: API elements to use for reformulation: classes, methods, etc.
        :param min_count: threshold for term occurrence to be included in the model
        :return:
        """
        try:
            self.model = Word2Vec(corpus, sg=0, min_count=min_count, workers=4)
            self.storage = api
            self.model.init_sims(True)
        except RuntimeError as re:
            logging.warning(re)

    def save(self, path, base_name, info=None):
        """
        Saves the model
        :param path: folder to save the model
        :param base_name: extentions are connected to the base name
        :param info: additional info about file type
        :return:
        """

        if not hasattr(self, "model"):
            return
        base = os.path.join(path, base_name)
        self.model.save(fname_or_handle=base + INDEX_EXT)
        with open(base + CLASSES_EXT, 'w') as fp:
            pickle.dump(self.storage, fp)
        with open(base + INFO_EXT, 'w') as fp:
            pickle.dump(info, fp)

    def load(self, path, base_name):
        """
        Loads the model to the memory
        :param path: base folder path
        :param base_name: name of the file, extentions are connected automatically
        :return:
        """
        info = None
        base = os.path.join(path, base_name)
        self.model = Word2Vec.load(fname=base + INDEX_EXT)
        # self.model.init_sims(True)
        with open(base + CLASSES_EXT, 'r') as fp:
            self.storage = pickle.load(fp)
        if os.path.exists(base + INFO_EXT):
            with open(base + INFO_EXT, 'r') as fp:
                info = pickle.load(fp)
        return info

    def reformulate(self, sentence, num_res=NUM_RESULTS, public_only=False,
                    num_candidates=NUM_CANDIDATES):
        """
        Takes the sentence, cleans it and reformulates
        :param sentence: list of words
        :param num_res: number of output terms
        :param public_only: use only public API methods
        :param num_candidates: number of candidate terms for further filtration
        :return: list of reformulated terms
        """
        if not hasattr(self, "model"):
            logging.warning("No trained model")
            return []

        sentence = [s for s in sentence if s in self.model]
        logging.info(
            "Terms of the normalized query that present in the corpus: %s" % ' '.join(sentence))

        if not sentence:
            return []

        # TODO pre-compute class_similarity
        storage = self.storage
        if public_only:
            storage = self._public_storage()
        res = {}
        for w in storage:
            if w in self.model:
                res[w] = self.model.n_similarity(sentence, [w])

        sorted_candidates = sorted(res.items(), key=operator.itemgetter(1), reverse=True)

        candidate_names_counts = [(k, storage[k].count) for (k, v) in
                                  sorted_candidates[:num_candidates]]

        sorted_candidate_names_counts = sorted(candidate_names_counts, key=operator.itemgetter(1),
                                               reverse=True)
        logging.info("Candidates: %s" % ' '.join([k for (k, v) in sorted_candidate_names_counts]))
        return [storage[k].name for (k, v) in sorted_candidate_names_counts[:num_res]]

    def normalize_and_reformulate(self, query, all_asts=False):
        """
        Normalizes and reformulates raw query
        :param query: input string
        :return: list of ASTs as reformulation
        """
        normalizer = Normalizer()
        normalized_query = normalizer.process_query(query)
        logging.info("Normalized query: %s" % ' '.join(normalized_query))
        res = self.reformulate(normalized_query, public_only=not all_asts)
        return res

    def _public_storage(self):
        return {k: self.storage[k] for k in self.storage if self.storage[k].publicapi}
