import unittest

from reform.constants import *
from reform.parse.javaclassvisitor import JavaClassVisitor
from reform.parse.javacorpus import JavaCorpusStorage, java_parse
from reform.parse.javamethodvisitor import JavaMethodVisitor

from reform.tests import infer_path


class Test_JavaClassCorpus(unittest.TestCase):
    def setUp(self):

        self.JAVA8APIPATH_TEST = infer_path("../../data/APIClassesListJava8.txt")
        self.corpus = JavaCorpusStorage(self.JAVA8APIPATH_TEST, JavaClassVisitor())
        self.corpus.parse = java_parse

    def test_one_class_corpus_len(self):
        filepath = infer_path("../../test/classes/DownloadWebpage.java")
        with open(filepath) as f:
            src = f.read()
            st = self.corpus.parse(src)
            self.corpus.add(st)
            self.assertEqual(len(self.corpus.snippets), 3)

    def test_one_class_corpus_content(self):
        filepath = infer_path("../../test/classes/DownloadWebpage.java")
        with open(filepath) as f:
            src = f.read()
            st = self.corpus.parse(src)
            self.corpus.add(st)
            self.assertEqual(self.corpus.snippets[0][0], "DownloadWebpageSampl".lower())
            terms = self.corpus.snippets[1]
            self.assertIn("URL".lower(), terms)
            self.assertIn("String".lower(), terms)
            self.assertIn("gzip".lower(), terms)
            self.assertIn("address".lower(), terms)

    def test_loc_5_limit(self):
        filepath = infer_path("../../test/classes/FTP_ApacheCommonsNet_Samples.java")
        with open(filepath) as f:
            src = f.read()
            st = self.corpus.parse(src)
            self.corpus.add(st)
            self.assertEqual(len(self.corpus.snippets), 3)


class Test_JavaMethodCorpus(unittest.TestCase):
    def setUp(self):
        self.JAVA8APIPATH_TEST = infer_path("../../data/APIMethodsListJava8.txt")
        self.corpus = JavaCorpusStorage(self.JAVA8APIPATH_TEST, JavaMethodVisitor())
        self.corpus.parse = java_parse

    def test_one_class_corpus_len(self):
        filepath = infer_path("../../test/classes/DownloadWebpage.java")
        with open(filepath) as f:
            src = f.read()
            st = self.corpus.parse(src)
            self.corpus.add(st)
            self.assertEqual(len(self.corpus.snippets), 3)

    def test_one_class_corpus_content(self):
        filepath = infer_path("../../test/classes/DownloadWebpage.java")
        with open(filepath) as f:
            src = f.read()
            st = self.corpus.parse(src)
            self.corpus.add(st)
            self.assertEqual(self.corpus.snippets[0][0], "Download".lower())
            terms = self.corpus.snippets[1]
            self.assertIn("URL".lower(), terms)
            self.assertIn("String".lower(), terms)
            self.assertIn("gzip".lower(), terms)
            self.assertIn("getcontentencod", terms)

    def test_loc_5_limit(self):
        filepath = infer_path("../../test/classes/FTP_ApacheCommonsNet_Samples.java")
        with open(filepath) as f:
            src = f.read()
            st = self.corpus.parse(src)
            self.corpus.add(st)
            self.assertEqual(len(self.corpus.snippets), 3)


class Test_MethodStorage(unittest.TestCase):
    def setUp(self):
        self.JAVA8APIPATH_TEST = infer_path("../../data/APIMethodsListJava8.txt")
        self.storage = JavaCorpusStorage(self.JAVA8APIPATH_TEST, JavaMethodVisitor())
        self.storage.parse = java_parse

    def test_apiloaded(self):
        self.assertTrue(len(self.storage.publicapi) > 0)
        with open(self.JAVA8APIPATH_TEST) as apifile:
            for line in apifile:
                if len(line.strip()) == 0:
                    continue
                self.assertTrue(line.strip() in self.storage.publicapi)

    def test_classes_added(self):
        files = [infer_path("../../test/classes/DownloadWebpage.java")
            , infer_path("../../test/classes/FTP_ApacheCommonsNet_Samples.java")]
        for fn in files:
            with open(fn) as f:
                src = f.read()
                st = self.storage.parse(src)
                self.storage.add(st)

        self.assertEqual(len(self.storage.storage), 16)
        self.assertIn('getInputStream'.lower(), self.storage.storage)
        self.assertEqual(self.storage.storage['getInputStream'.lower()].count, 3)
        self.assertTrue(self.storage.storage['close'].publicapi)
        self.assertFalse(self.storage.storage['setDefaultPort'.lower()].publicapi)
