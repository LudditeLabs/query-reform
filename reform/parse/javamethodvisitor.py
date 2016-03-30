from ..constants import *
from ..parse.javacorpus import JavaBaseVisitor
from ..parse.models import AstNode
from ..parse.models import CorpusNode


class JavaMethodVisitor(JavaBaseVisitor):
    """
    Visits important nodes (ClassDelaration, MethodDeclaration) to extract properties
    """

    def __init__(self):
        super(JavaMethodVisitor, self).__init__()
        self.split_exclude_list = ["MethodInvocation", "MethodDeclaration"]

    def visit_ClassDeclaration(self, elem):
        if elem.name not in self.classes_methods:
            self.classes_methods[elem.name] = []

        self.active_class_name = elem.name
        return True

    def leave_ClassDeclaration(self, elem):
        methods = self.classes_methods.pop(self.active_class_name, [])

        for method in methods:
            res = self._parse_method_body(method)
            res.insert(0, CorpusNode(self.active_class_name, "ClassDeclaration",
                                     self.split_exclude_list))
            self.snippets.append(res)

        self.active_class_name = ''
        return True

    def visit_MethodDeclaration(self, elem):
        # more then N LOC
        if hasattr(elem, "body") and len(elem.body) >= N_LOC:
            self.classes_methods[self.active_class_name].append(elem)
        return True

    def visit_MethodInvocation(self, elem):
        name = AstNode(elem).name
        self.add_term(name)
        return True
