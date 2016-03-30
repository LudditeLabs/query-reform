import unittest

from reform.parse.models import CorpusNode
from reform.processing.normalize import Normalizer


class Test_Normalizer(unittest.TestCase):
    def setUp(self):
        self.normalizer = Normalizer()

    def test_split_camel_1(self):
        term = 'camelCaSe'
        res = self.normalizer.split(term)
        expect = ['camel', 'Ca', 'Se']
        self.assertEqual(res, expect)

    def test_split_camel_2(self):
        term = 'CamelCaSe'
        res = self.normalizer.split(term)
        expect = ['Camel', 'Ca', 'Se']
        self.assertEqual(res, expect)

    def test_split_camel_3(self):
        term = 'camel'
        res = self.normalizer.split(term)
        expect = ['camel']
        self.assertEqual(res, expect)

    def test_split_underscore_1(self):
        term = 'under_score'
        res = self.normalizer.split(term)
        expect = ['under', 'score']
        self.assertEqual(res, expect)

    def test_split_underscore_2(self):
        term = '__under_score'
        res = self.normalizer.split(term)
        expect = ['under', 'score']
        self.assertEqual(res, expect)

    def test_split_underscore_3(self):
        term = 'underscore'
        res = self.normalizer.split(term)
        expect = ['underscore']
        self.assertEqual(res, expect)

    def test_split_mixed_1(self):
        term = 'Mix_ed'
        res = self.normalizer.split(term)
        expect = ['Mix', 'ed']
        self.assertEqual(res, expect)

    def test_split_mixed_2(self):
        term = 'Mix_Ed'
        res = self.normalizer.split(term)
        expect = ['Mix', 'Ed']
        self.assertEqual(res, expect)

    def test_clean_common_stopwords(self):
        terms = ['the', 'python']
        expect = ['python']
        res = filter(lambda w: bool(w), [self.normalizer.clean(t) for t in terms])
        self.assertEqual(res, expect)

    def test_clean_java_keywords(self):
        terms = ['case', 'java']
        expect = ['java']
        res = filter(lambda w: bool(w), [self.normalizer.clean(t) for t in terms])
        self.assertEqual(res, expect)

    def test_clean_short_keywords(self):
        terms = ['r', 'java']
        expect = ['java']
        res = filter(lambda w: bool(w), [self.normalizer.clean(t) for t in terms])
        self.assertEqual(res, expect)

    def test_stemm(self):
        terms = ['playing']
        expect = ['play']
        res = [self.normalizer.normalize(t) for t in terms]
        self.assertEqual(res, expect)

    def test_process_1(self):
        terms = [CorpusNode('playing', "MethodDescription")]
        expect = [CorpusNode('play', "MethodDescription")]
        res = self.normalizer.process_snippet(terms)
        self.assertEqual(len(res), len(expect))
        for (r, e) in zip(res, expect):
            self.assertEqual(r, e.name)

    def test_process_2(self):
        terms = [CorpusNode('playing', "MethodDescription"), CorpusNode('if', "MethodDescription")]
        expect = [CorpusNode('play', "MethodDescription")]
        res = self.normalizer.process_snippet(terms)
        self.assertEqual(len(res), len(expect))
        for (r, e) in zip(res, expect):
            self.assertEqual(r, e.name)

    def test_process_3(self):
        terms = [CorpusNode('playing', "MethodDescription"), CorpusNode('if', "MethodDescription")]
        expect = [CorpusNode('play', "MethodDescription")]
        res = self.normalizer.process_snippet(terms)
        self.assertEqual(len(res), len(expect))
        for (r, e) in zip(res, expect):
            self.assertEqual(r, e.name)

    def test_process_4(self):
        terms = [CorpusNode('playing', "MethodDescription"),
                 CorpusNode('\"\"', "MethodDescription")]
        expect = [CorpusNode('play', "MethodDescription")]
        res = self.normalizer.process_snippet(terms)
        self.assertEqual(len(res), len(expect))
        for (r, e) in zip(res, expect):
            self.assertEqual(r, e.name)

    def test_process_5(self):
        terms = [CorpusNode('playing', "MethodDescription"),
                 CorpusNode('\"calling a method\"', "String")]
        expect = [CorpusNode('play', "MethodDescription"), CorpusNode('call', "String"),
                  CorpusNode('method', "String")]
        res = self.normalizer.process_snippet(terms)
        self.assertEqual(len(res), len(expect))
        for (r, e) in zip(res, expect):
            self.assertEqual(r, e.name)

    def test_process_query_1(self):
        query = "how to play cards"
        expect = ["play", "card"]
        res = self.normalizer.process_query(query)
        self.assertEqual(len(res), len(expect))
        for (r, e) in zip(res, expect):
            self.assertEqual(r, e)

    def test_process_query_2(self):
        query = u"who's to play card's"
        expect = [u"who", u"play", u"card"]
        res = self.normalizer.process_query(query)
        self.assertEqual(len(res), len(expect))
        for (r, e) in zip(res, expect):
            self.assertEqual(r, e)
