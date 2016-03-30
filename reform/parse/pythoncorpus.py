import ast
import logging
from collections import defaultdict

from ..parse.models import APINode
from ..parse.models import AstNode
from ..parse.models import CorpusNode
from ..processing.normalize import Normalizer


def python_parse(src):
    """
    Parses snippets
    :param src: python source file
    :return:
    """
    st = None
    try:
        st = ast.parse(src)
        if not st:
            logging.error("Cannot parse a source file")
            return None

    except Exception as ex:
        logging.error(ex.message)
        return None

    return st


class PythonCorpusStorage(object):
    """
    The storage holds all parsed Python snippets
    The storage also holds all parsed Python classes
    The key of the class storage is the class name - in the lower case. Which is used for operation,
    while the real name is used for the output
    """

    def __init__(self, publicAPIPath, visitor):
        self.snippets = []
        self.normalizer = Normalizer()
        self.storage = {}
        self.publicapi = {}
        self._parse_api(publicAPIPath)
        self.visitor = visitor

    def add(self, st):
        """
        Adds to the storage
        """
        if not st:
            return
        self.visitor.clear()
        try:
            self.visitor.visit(st)
        except Exception as ex:
            logging.error(ex.message)
            return None
        for s in self.visitor.snippets:
            normalized_snippet = self.normalizer.process_snippet(s)
            if len(normalized_snippet) != 0:
                self.snippets.append(normalized_snippet)
        self._update_storage(self.visitor.reformulation_storage)

    def _parse_api(self, publicAPIPath):
        with open(publicAPIPath) as apifile:
            for line in apifile:
                if len(line.strip()) == 0:
                    continue
                self.publicapi[line.strip()] = True

    def _update_storage(self, to_update):
        for key in to_update:
            if key in self.storage:
                self.storage[key].count += to_update[key].count
            else:
                if to_update[key].name in self.publicapi:
                    to_update[key].publicapi = True
                self.storage[key] = to_update[key]


class PythonBaseVisitor(ast.NodeVisitor):
    """
    Visits important nodes (ClassDelaration, MethodDefinition) to extract properties
    """

    def __init__(self):
        super(PythonBaseVisitor, self).__init__()
        self.classes_methods = defaultdict(list)
        self.active_class_name = ''
        self.snippets = []
        self.reformulation_storage = {}
        self.split_exclude_list = []
        self.normalizer = Normalizer()

    def clear(self):
        self.classes_methods.clear()
        self.active_class_name = ''
        self.snippets = []
        self.reformulation_storage.clear()

    def normalized_name(self, api_term):
        return self.normalizer.process_api_term(api_term)

    def add_term(self, api_term):
        normalized_term = self.normalized_name(api_term)
        if not normalized_term:
            return
        if normalized_term in self.reformulation_storage:
            self.reformulation_storage[normalized_term].count += 1
        else:
            to_add = APINode(api_term)
            self.reformulation_storage[normalized_term] = to_add

    def generic_visit(self, node):
        ast.NodeVisitor.generic_visit(self, node)

    def _parse_method_body(self, elem):
        nodes = []
        self._get_corpus_nodes(AstNode(elem), nodes)
        return nodes

    def _get_corpus_nodes(self, node, res):
        children = node.children()
        fields = node.fields()

        for c in children:
            self._get_corpus_nodes(c, res)
            if c.name:
                res.append(CorpusNode(c.name, c.type_name, self.split_exclude_list))

        for f in fields:
            self._get_corpus_nodes(f, res)
            if f.name:
                res.append(CorpusNode(f.name, f.type_name, self.split_exclude_list))
