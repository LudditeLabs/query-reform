from ..constants import *
from ..parse.models import CorpusNode
from ..parse.pythoncorpus import PythonBaseVisitor


class PythonClassVisitor(PythonBaseVisitor):
    """
    Visits important nodes (ClassDelaration, MethodDefinition) to extract properties
    """

    def __init__(self):
        super(PythonClassVisitor, self).__init__()
        self.split_exclude_list = ["ClassDef"]

    def visit_Import(self, elem):
        for n in elem.names:
            name = n.name
            if name != '*':
                self.add_term(name)

    def visit_ImportFrom(self, elem):
        for n in elem.names:
            name = n.name
            if name != '*':
                self.add_term(name)

    def visit_ClassDef(self, elem):
        if elem.name not in self.classes_methods:
            self.classes_methods[elem.name] = []

        self.active_class_name = elem.name
        self.generic_visit(elem)

    def visit_FunctionDef(self, elem):
        # more then N LOC
        if hasattr(elem, "body") and len(elem.body) >= N_LOC:
            res = self._parse_method_body(elem)
            res.insert(0, CorpusNode(self.active_class_name, "ClassDef", self.split_exclude_list))
            self.snippets.append(res)
