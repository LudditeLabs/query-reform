import logging
from collections import defaultdict

from slimit.parser import Parser
from ..constants import *
from ..parse.models import APINode
from ..parse.models import AstNode
from ..parse.models import CorpusNode
from ..processing.normalize import Normalizer


def js_parse(src):
    """
    Parses snippets
    :param src: JavaScript source file
    :return:
    """
    parser = Parser()

    try:
        st = parser.parse(src)
        if not st:
            logging.error("Cannot parse a source file")
            return None

    except Exception as ex:
        logging.error(ex.message)
        return None

    return st


class JavaScriptCorpusStorage(object):
    """
    The storage holds all parsed JavaScript snippets
    The storage also holds all parsed JavaScript classes
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
        self.split_exclude_list = ["FunctionDef", "Call"]

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


class JavaScriptBaseVisitor(object):
    """
    Visits important nodes (ClassDelaration, MethodDefinition) to extract properties
    """

    def __init__(self):
        super(JavaScriptBaseVisitor, self).__init__()
        self.classes_methods = defaultdict(list)
        self.active_class_name = ''
        self.snippets = []
        self.reformulation_storage = {}
        self.split_exclude_list = []
        self.normalizer = Normalizer()

    def clear(self):
        self.active_class_name = ''
        self.snippets = []
        self.reformulation_storage.clear()

    def visit(self, node):
        method = 'visit_%s' % node.__class__.__name__
        return getattr(self, method, self.generic_visit)(node)

    def visit_FunctionCall(self, elem):
        name = AstNode(elem.identifier).name
        self.add_term(name)
        for prop in elem:
            self.visit(prop)

    def visit_FuncDecl(self, elem):
        # more then N LOC
        if hasattr(elem, "lineno") and elem.lineno >= N_LOC:
            res = self._parse_method_body(elem)
            self.snippets.append(res)
        for prop in elem:
            self.visit(prop)

    def generic_visit(self, node):
        for child in node:
            self.visit(child)

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

    def _parse_method_body(self, elem):
        nodes = []
        self._get_corpus_nodes(AstNode(elem), nodes)
        return nodes

    def _get_corpus_nodes(self, node, res):
        if not node.st:
            return

        children = node.st.children()

        for c in children:
            child_node = AstNode(c)
            self._get_corpus_nodes(child_node, res)
            if child_node.name:
                res.append(
                    CorpusNode(child_node.name, child_node.type_name, self.split_exclude_list))
