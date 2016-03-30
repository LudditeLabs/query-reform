from ..constants import *
from ..parse.models import AstNode
from ..parse.models import CorpusNode
from ..parse.pythoncorpus import PythonBaseVisitor


class PythonMethodVisitor(PythonBaseVisitor):
    """
    Visits important nodes (ClassDelaration, MethodDefinition) to extract properties
    """

    def __init__(self):
        super(PythonMethodVisitor, self).__init__()
        self.split_exclude_list = ["FunctionDef", "Call"]

    def visit_ClassDef(self, elem):
        self.active_class_name = elem.name
        self.generic_visit(elem)

    def visit_FunctionDef(self, elem):
        # more then N LOC
        if hasattr(elem, "body") and len(elem.body) >= N_LOC:
            res = self._parse_method_body(elem)
            res.insert(0, CorpusNode(self.active_class_name, "ClassDef", self.split_exclude_list))
            self.snippets.append(res)
        self.generic_visit(elem)

    def visit_Call(self, elem):
        name = AstNode(elem.func).name
        self.add_term(name)
