import unittest

from reform.constants import *
from reform.parse.pythonclassvisitor import PythonClassVisitor
from reform.parse.pythoncorpus import PythonCorpusStorage, python_parse
from reform.parse.pythonmethodvisitor import PythonMethodVisitor

from reform.tests import infer_path

class Test_PythonClassCorpus(unittest.TestCase):
    def setUp(self):
        self.PYTHON8APIPATH_TEST = infer_path("../../data/APIClassesListPython27.txt")
        self.corpus = PythonCorpusStorage(self.PYTHON8APIPATH_TEST, PythonClassVisitor())
        self.corpus.parse = python_parse

    def test_one_class_corpus_len(self):
        filepath = infer_path("../../data/samples/Python/10.py")
        with open(filepath) as f:
            src = f.read()
            st = python_parse(src)
            self.corpus.add(st)
            self.assertEqual(len(self.corpus.snippets), 1)

    def test_one_class_corpus_content(self):
        filepath = infer_path("../../data/samples/Python/7.py")
        with open(filepath) as f:
            src = f.read()
            st = python_parse(src)
            self.corpus.add(st)
            self.assertEqual(self.corpus.snippets[0][0], "command".lower())
            terms = self.corpus.snippets[1]
            self.assertIn("data".lower(), terms)
            self.assertIn("bufsiz".lower(), terms)
            self.assertIn("cwd".lower(), terms)
            self.assertIn("popen".lower(), terms)

    def test_loc_5_limit(self):
        filepath = infer_path("../../data/samples/Python/10.py")
        with open(filepath) as f:
            src = f.read()
            st = self.corpus.parse(src)
            self.corpus.add(st)
            self.assertEqual(len(self.corpus.snippets), 1)


class Test_PythonMethodCorpus(unittest.TestCase):
    def setUp(self):
        self.PYTHON8APIPATH_TEST = infer_path("../../data/APIMethodsListPython27.txt")
        self.corpus = PythonCorpusStorage(self.PYTHON8APIPATH_TEST, PythonMethodVisitor())
        self.corpus.parse = python_parse

    def test_one_class_corpus_len(self):
        filepath = infer_path("../../data/samples/Python/7.py")
        with open(filepath) as f:
            src = f.read()
            st = self.corpus.parse(src)
            self.corpus.add(st)
            self.assertEqual(len(self.corpus.snippets), 6)

    def test_one_class_corpus_content(self):
        filepath = infer_path("../../data/samples/Python/7.py")
        with open(filepath) as f:
            src = f.read()
            st = self.corpus.parse(src)
            self.corpus.add(st)
            self.assertEqual(self.corpus.snippets[0][0], "command".lower())
            terms = self.corpus.snippets[1]
            self.assertIn("target".lower(), terms)
            self.assertIn("thread".lower(), terms)
            self.assertIn("exc".lower(), terms)
            self.assertIn("info", terms)

    def test_loc_5_limit(self):
        filepath = infer_path("../../data/samples/Python/7.py")
        with open(filepath) as f:
            src = f.read()
            st = self.corpus.parse(src)
            self.corpus.add(st)
            self.assertEqual(len(self.corpus.snippets), 6)


class Test_MethodStorage(unittest.TestCase):
    def setUp(self):
        self.PYTHON8APIPATH_TEST = infer_path("../../data/APIMethodsListPython27.txt")
        self.storage = PythonCorpusStorage(self.PYTHON8APIPATH_TEST, PythonMethodVisitor())
        self.storage.parse = python_parse

    def test_apiloaded(self):
        self.assertTrue(len(self.storage.publicapi) > 0)
        with open(self.PYTHON8APIPATH_TEST) as apifile:
            for line in apifile:
                if len(line.strip()) == 0:
                    continue
                self.assertTrue(line.strip() in self.storage.publicapi)

    def test_classes_added(self):
        files = [infer_path("../../data/samples/Python/7.py")
            , infer_path("../../data/samples/Python/6.py")]
        for fn in files:
            with open(fn) as f:
                src = f.read()
                st = self.storage.parse(src)
                self.storage.add(st)

        self.assertEqual(len(self.storage.storage), 43)
        self.assertIn('serverfromstr'.lower(), self.storage.storage)
        self.assertEqual(self.storage.storage['serverfromstr'.lower()].count, 1)
        self.assertTrue(self.storage.storage['kill'].publicapi)
        self.assertFalse(self.storage.storage['shlex'.lower()].publicapi)
