import unittest

import os
from reform.index.load import Loader

from reform.tests import infer_path

class Test__Loader(unittest.TestCase):
    def setUp(self):
        self.JAVA8APIPATH_TEST = infer_path("../../data/APIClassesListJava8.txt")

    def test_build(self):
        loader = Loader(infer_path("../../test/classes"),
                        infer_path("../../test/storage"), "test_build",
                        "java")

        vectorizer = loader.build(JAVA_CLASSES=self.JAVA8APIPATH_TEST)

        res = vectorizer.reformulate(["get"], num_res=1)

        self.assertEqual(res, ["IOException"])

    def test_load(self):
        loader = Loader(infer_path("../../test/classes"),
                        infer_path("../../test/storage"), "test_load",
                        "java")
        vectorizer1 = loader.build(JAVA_CLASSES=self.JAVA8APIPATH_TEST)
        res = vectorizer1.reformulate(["get"], num_res=1)
        self.assertEqual(res, ["IOException"])

        loader1 = Loader(infer_path("../../test/classes"),
                         infer_path("../../test/storage"), "test_load",
                         "java")
        vectorizer2 = loader1.load()
        res = vectorizer2.reformulate(["get"], num_res=1)
        self.assertEqual(res, ["IOException"])

    def test_type_regression(self):
        loader = Loader(infer_path("../../test/regression/107245.java"),
                        infer_path("../../test/storage"),
                        "test_regression",
                        "java")
        vectorizer1 = loader.build(JAVA_CLASSES=self.JAVA8APIPATH_TEST)
        res = vectorizer1.reformulate(["get"], num_res=1)
        self.assertEqual(res, [])
