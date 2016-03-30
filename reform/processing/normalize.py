import logging
import string

import re
from nltk import download
from nltk.corpus import stopwords
from reform.parse.models import CorpusNode
from stemming.porter2 import stem


class Normalizer(object):
    """
    Removes common stop words and normalizes a corpus

    """

    def __init__(self):
        self.first_cap_re = re.compile('(.)([A-Z][a-z]+)')
        self.all_cap_re = re.compile('([a-z0-9])([A-Z])')
        self.punctuation_re = re.compile('[%s]' % re.escape(string.punctuation))
        self.common_words = self._load_language_specific_words()
        self.common_words = self.common_words.union(self._load_common_stopwords())

    def process_snippet(self, snippet):
        """
        Full text processing of raw terms
        :param snippet: input snippet
        :return: cleaned, split, normalized snippet
        """
        res = []
        for corpus_term in snippet:
            to_process = []
            if corpus_term.is_split:
                split_terms = self.split(corpus_term.name)
                for term in split_terms:
                    to_process.append(CorpusNode(term, corpus_term.type_name))
            else:
                to_process.append(corpus_term)

            for term in to_process:
                cleaned = self.clean(term.name)
                if not cleaned:
                    continue
                term.name = self.normalize(cleaned)
                res.append(term.name)

        return res

    def process_query(self, query):
        """
        Query normalization
        :param query: input query
        :return: cleaned, split, normalized query
        """
        res = []
        for word in query.split(r' '):
            for no_punckt_word in word.split(r'\w+'):
                cleaned = self.clean(no_punckt_word)
                if not cleaned:
                    continue
                res.append(self.normalize(cleaned))
        return res

    def process_api_term(self, term):
        """
        Query normalization
        :param query: input query
        :return: cleaned, split, normalized query
        """
        cleaned = self.clean(term)
        if not cleaned:
            return None
        res = self.normalize(cleaned)
        return res

    def split(self, term):
        """
        Split camelCase and undescore_terms into separate terms, removes punctuation, spaces
        :param term: term to be split
        :return: list of split terms
        """
        s1 = self.first_cap_re.sub(r'\1_\2', term)
        s2 = filter(lambda x: bool(x), self.all_cap_re.sub(r'\1_\2', s1).split('_'))
        s3 = filter(lambda x: bool(x), [self.punctuation_re.sub(r'', t) for t in s2])
        s4 = []
        for t in s3:
            for to_add in t.split(' '):
                if to_add:
                    s4.append(to_add)
        return s4

    def clean(self, term):
        """
        Removes common stopwords, too short words and language-specific keywords, removes
        :param term: term to be cleaned
        :return: cleaned term or None
        """
        if isinstance(term, basestring) \
                and not term.lower() in self.common_words \
                and len(term) > 1 \
                and not term.isdigit():
            return term

        return None

    def normalize(self, term):
        """
        Normalizes a term
        :param term: input string term
        :return: normalized term
        """

        return stem(term.lower())

    def _load_common_stopwords(self):
        res = set()
        try:
            res = set(stopwords.words('english'))
        except LookupError:
            try:
                download('stopwords')
                res = set(stopwords.words('english'))
            except LookupError:
                logging.error("Cannot load common stopwords")

        return res

    def _load_language_specific_words(self):
        res = set()
        javakeywords = (
        'this', 'class', 'void', 'super', 'extends', 'implements', 'enum', 'interface',
        'byte', 'short', 'int', 'long', 'char', 'float', 'double', 'boolean', 'null',
        'true', 'false',
        'final', 'public', 'protected', 'private', 'abstract', 'static', 'strictfp', 'transient',
        'volatile',
        'synchronized', 'native',
        'throws', 'default',
        'instanceof',
        'if', 'else', 'while', 'for', 'switch', 'case', 'assert', 'do',
        'break', 'continue', 'return', 'throw', 'try', 'catch', 'finally', 'new',
        'package', 'import'
        )

        pythonkeywords = (
        'and', 'as', 'assert', 'break', 'class', 'continue', 'def', 'del', 'elif',
        'else', 'except', 'exec', 'finally', 'for', 'from', 'global', 'if', 'import',
        'in', 'is', 'lambda', 'not', 'or', 'pass', 'print', 'raise', 'return', 'try',
        'while', 'with', 'yield')

        jskeywords = (
        'do', 'if', 'in', 'for', 'let', 'new', 'try', 'var', 'case', 'else', 'enum', 'eval',
        'null', 'this', 'true', 'void', 'with', 'await', 'break', 'catch', 'class', 'const',
        'false', 'super', 'throw', 'while', 'yield', 'delete', 'export', 'import', 'public',
        'return', 'static', 'switch', 'typeof', 'default', 'extends', 'finally', 'package',
        'private', 'continue', 'debugger', 'function', 'arguments', 'interface', 'protected',
        'implements', 'instanceof')

        res = res.union(javakeywords)
        res = res.union(pythonkeywords)
        res = res.union(jskeywords)

        return res
