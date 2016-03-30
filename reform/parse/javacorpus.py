from collections import defaultdict

import ply.lex as lex
import ply.yacc as yacc
import plyj.model as m
import plyj.parser as plyj
from ply.lex import NullLogger
from plyj.parser import MyLexer, MyParser
from ..parse.models import APINode
from ..parse.models import AstNode
from ..parse.models import CorpusNode
from ..processing.normalize import Normalizer


class NoErrorLexer(MyLexer):
    def __init__(self):
        super(NoErrorLexer, self).__init__()

    def t_error(self, t):
        t.lexer.skip(1)


class NoLogParser(plyj.Parser):
    def __init__(self):
        super(NoLogParser, self).__init__()
        self.lexer = lex.lex(module=NoErrorLexer(), optimize=1, errorlog=NullLogger())
        p = MyParser()
        p.p_error = p.p_empty
        self.parser = yacc.yacc(module=p, start='goal', optimize=1, errorlog=NullLogger())


def java_parse(src):
    """
    Parses snippets
    :param src: java source file
    :return:
    """
    st = None
    parser = NoLogParser()

    try:
        st = parser.parse_string(src)
        if not st:
            return None

    except Exception as ex:
        return None

    return st


class JavaCorpusStorage(object):
    """
    The storage holds all parsed Java snippets
    The storage also holds all parsed Java classes
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
        Adds data to the storage
        """

        if not st:
            return
        self.visitor.clear()
        try:
            st.accept(self.visitor)
        except Exception as ex:
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


class JavaBaseVisitor(m.Visitor):
    """
    Visits important nodes (ClassDelaration, MethodDefinition) to extract properties
    """

    def __init__(self):
        super(JavaBaseVisitor, self).__init__()
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
