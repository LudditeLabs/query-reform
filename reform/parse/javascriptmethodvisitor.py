from ..constants import *
from ..parse.javascriptcorpus import JavaScriptBaseVisitor
from ..parse.models import AstNode


class JavaScriptMethodVisitor(JavaScriptBaseVisitor):
    """
    Visits important nodes (ClassDelaration, MethodDefinition) to extract properties
    """

    def __init__(self):
        super(JavaScriptBaseVisitor, self).__init__()
        self.split_exclude_list = ["FunctionDef", "Call"]

    def visit(self, node):
        print node.__class__.__name__
        method = 'visit_%s' % node.__class__.__name__
        return getattr(self, method, self.generic_visit)(node)

    def generic_visit(self, node):
        for child in node:
            self.visit(child)

    def visit_FunctionDef(self, elem):
        # more then N LOC
        if hasattr(elem, "body") and len(elem.body) >= N_LOC:
            res = self._parse_method_body(elem)
            self.snippets.append(res)
        self.visit(elem)

    def visit_Call(self, elem):
        name = AstNode(elem.func).name
        self.add_term(name)
