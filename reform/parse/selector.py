from ..constants import *
from ..parse.javaclassvisitor import JavaClassVisitor
from ..parse.javacorpus import JavaCorpusStorage, java_parse
from ..parse.javamethodvisitor import JavaMethodVisitor
from ..parse.javascriptcorpus import JavaScriptBaseVisitor
from ..parse.javascriptcorpus import JavaScriptCorpusStorage, js_parse
from ..parse.pythonclassvisitor import PythonClassVisitor
from ..parse.pythoncorpus import PythonCorpusStorage, python_parse
from ..parse.pythonmethodvisitor import PythonMethodVisitor


class CorpusSelector(object):
    """
    Selects corpus storage based on the language and options
    """
    PATH_MAP = {
        "JAVA_CLASSES": JAVA8CLASSESAPIPATH,
        "JAVA_METHODS": JAVA8METHODSAPIPATH,
        "PYTHON_CLASSES": PYTHON8CLASSESAPIPATH,
        "PYTHON_METHODS": PYTHON8METHODSAPIPATH,
        "JAVASCRIPT_METHODS": JAVASCRIPTMETHODSAPIPATH,
    }

    @staticmethod
    def select(language, methods, **kwargs):
        """
        Select corpus storage
        :param language: requested language
        :param methods: if method reformulation (default: classes)
        :param kwargs: additional options (for testing)
        :return: corpus storage or NotImplemented
        """
        if language.lower() == "java":
            apiname = "JAVA_CLASSES"
            visitor = JavaClassVisitor()
            if methods:
                apiname = "JAVA_METHODS"
                visitor = JavaMethodVisitor()
            api_path = kwargs.get(apiname, CorpusSelector.PATH_MAP[apiname])
            return JavaCorpusStorage(api_path, visitor), java_parse

        elif language.lower() == "python":
            apiname = "PYTHON_CLASSES"
            visitor = PythonClassVisitor()
            if methods:
                apiname = "PYTHON_METHODS"
                visitor = PythonMethodVisitor()
            api_path = kwargs.get(apiname, CorpusSelector.PATH_MAP[apiname])
            return PythonCorpusStorage(api_path, visitor), python_parse

        elif language.lower() == "javascript":
            if methods:
                apiname = "JAVASCRIPT_METHODS"
                visitor = JavaScriptBaseVisitor()
            else:
                return NotImplemented, NotImplemented
            api_path = kwargs.get(apiname, CorpusSelector.PATH_MAP[apiname])
            return JavaScriptCorpusStorage(api_path, visitor), js_parse
        return NotImplemented, NotImplemented
