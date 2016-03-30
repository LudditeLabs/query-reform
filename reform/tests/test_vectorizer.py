import logging
import unittest

from reform.parse.models import APINode
from reform.processing.vectorize import Vectorizer

from reform.tests import infer_path

logging.disable(logging.ERROR)

class Test_Vectorizer(unittest.TestCase):
    def test_common_classes(self):
        corpus = [
            ["snippet1", "class1", "sentence1", "snippet1"]
            , ["snippet2", "class2", "sentence2", "snippet2"]
        ]

        ast_classes_storage = {"class1": APINode("class1"), "class2": APINode("class2"),
                               "class3": APINode("class3")}

        vectorizer = Vectorizer()
        vectorizer.train(corpus, ast_classes_storage, min_count=0)

        res = vectorizer.reformulate(["sentence1"], 1)

        self.assertEqual(res, ["class2"])

    def test_common_empty_corpus(self):
        corpus = [
        ]

        ast_classes_storage = {"class1": APINode("class1"), "class2": APINode("class2"),
                               "class3": APINode("class3")}

        vectorizer = Vectorizer()
        vectorizer.train(corpus, ast_classes_storage, min_count=0)

        res = vectorizer.reformulate(["sentence1"], 1)

        self.assertEqual(res, [])

    def test_common_empty_storage(self):
        corpus = [
            ["snippet1", "class1", "sentence1", "snippet1"]
            , ["snippet2", "class2", "sentence2", "snippet2"]
        ]

        ast_classes_storage = {}

        vectorizer = Vectorizer()
        vectorizer.train(corpus, ast_classes_storage, min_count=0)

        res = vectorizer.reformulate(["sentence1"], 1)

        self.assertEqual(res, [])

    def test_common_classes_save_load(self):
        corpus = [
            ["snippet1", "class1", "sentence1", "snippet1"]
            , ["snippet2", "class2", "sentence2", "snippet2"]
        ]

        ast_classes_storage = {"class1": APINode("class1"), "class2": APINode("class2"),
                               "class3": APINode("class3")}

        vectorizer = Vectorizer()
        vectorizer.train(corpus, ast_classes_storage, min_count=0)

        vectorizer.save(infer_path("../../test/storage"), "classes_save_load")

        vectorizer.load(infer_path("../../test/storage"), "classes_save_load")

        res = vectorizer.reformulate(["sentence1"], 1)

        self.assertEqual(res, ["class2"])

    def test_only_public_classes(self):
        corpus = [
            ["snippet1", "class1", "sentence1", "snippet1"]
            , ["snippet2", "class2", "sentence2", "snippet2"]
        ]

        ast_classes_storage = {"class1": APINode("class1"),
                               "class2": APINode("class2", publicapi=True),
                               "class3": APINode("class3")}

        vectorizer = Vectorizer()
        vectorizer.train(corpus, ast_classes_storage, min_count=0)

        res = vectorizer.reformulate(["sentence1"], 2, public_only=True)

        self.assertEqual(res, ["class2"])

    def test_only_most_popular(self):
        corpus = [
            ["snippet1", "class1", "sentence1", "snippet1"]
            , ["snippet2", "class2", "sentence2", "snippet2"]
        ]

        ast_classes_storage = {"class1": APINode("class1", count=1),
                               "class2": APINode("class2", count=2),
                               "class3": APINode("class3")}

        vectorizer = Vectorizer()
        vectorizer.train(corpus, ast_classes_storage, min_count=0)

        res = vectorizer.reformulate(["sentence1"], num_res=1)

        self.assertEqual(res, ["class2"])

    def test_ignore_class_name_case(self):
        corpus = [
            ["snippet1", "class1", "sentence1", "snippet1"]
            , ["snippet2", "class2", "sentence2", "snippet2"]
        ]

        ast_classes_storage = {"class1": APINode("CLASS1", count=1),
                               "class2": APINode("CLASS2", count=2),
                               "class3": APINode("CLASS3")}

        vectorizer = Vectorizer()
        vectorizer.train(corpus, ast_classes_storage, min_count=0)

        res = vectorizer.reformulate(["sentence1"], num_res=1)

        self.assertEqual(res, ["CLASS2"])

    def test_empty_sentence(self):
        corpus = [
            ["snippet1", "class1", "sentence1", "snippet1"]
            , ["snippet2", "class2", "sentence2", "snippet2"]
        ]

        ast_classes_storage = {"class1": APINode("CLASS1", count=1),
                               "class2": APINode("CLASS2", count=2),
                               "class3": APINode("CLASS3")}

        vectorizer = Vectorizer()
        vectorizer.train(corpus, ast_classes_storage, min_count=0)

        res = vectorizer.reformulate([""], num_res=1)

        self.assertEqual(res, [])

    def test_request_corpus(self):
        corpus = [
            ['downloadwebpagesampl', 'address', 'string', 'string', 'client', 'http', 'client',
             'builder', 'creat',
             'build', 'http', 'client', 'httpclient', 'request', 'address', 'http', 'get',
             'httpget', 'http', 'get',
             'httpget', 'respons', 'request', 'client', 'execut', 'http', 'respons', 'httprespons',
             'br', 'respons',
             'get', 'entiti', 'get', 'content', 'input', 'stream', 'reader', 'inputstreamread',
             'buffer', 'reader',
             'bufferedread', 'buffer', 'reader', 'bufferedread', 'line', 'string', 'string',
             'page', '""', 'string',
             'string', 'line', 'br', 'read', 'line', 'page', 'line', '"\\n"', 'br', 'close',
             'page', 'string', 'string',
             'client', 'protocol', 'except', 'clientprotocolexcept', 'io', 'except', 'ioexcept']
        ]

        ast_classes_storage = {"httpclient": APINode("HttpClient", count=1)}

        vectorizer = Vectorizer()
        vectorizer.train(corpus, ast_classes_storage, min_count=0)

        res = vectorizer.reformulate(["request"], num_res=1)

        self.assertEqual(res, ['HttpClient'])

    def test_process(self):
        corpus = [
            ["snippet1", "class1", "sentence1", "snippet1"]
            , ["snippet2", "class2", "sentence2", "snippet2"]
        ]

        ast_classes_storage = {"class1": APINode("class1"),
                               "class2": APINode("class2", publicapi=True),
                               "class3": APINode("class3")}

        vectorizer = Vectorizer()
        vectorizer.train(corpus, ast_classes_storage, min_count=0)

        res = vectorizer.normalize_and_reformulate("sentence1")

        self.assertEqual(res, ["class2"])

    def test_process_public_only(self):
        corpus = [
            ["snippet1", "class1", "sentence1", "snippet1"]
            , ["snippet2", "class2", "sentence2", "snippet2"]
        ]

        ast_classes_storage = {"class1": APINode("class1"),
                               "class2": APINode("class2", publicapi=True),
                               "class3": APINode("class3")}

        vectorizer = Vectorizer()
        vectorizer.train(corpus, ast_classes_storage, min_count=0)

        res = vectorizer.normalize_and_reformulate("sentence1", True)

        self.assertEqual(res, ["class2", "class1"])
