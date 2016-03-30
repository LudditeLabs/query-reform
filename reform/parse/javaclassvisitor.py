from ..constants import *
from ..parse.javacorpus import JavaBaseVisitor
from ..parse.models import AstNode
from ..parse.models import CorpusNode


class JavaClassVisitor(JavaBaseVisitor):
    """
    Visits important nodes (ClassDelaration, MethodDefinition) to extract properties
    """

    def __init__(self):
        super(JavaClassVisitor, self).__init__()
        self.split_exclude_list = ["Type", "ClassDeclaration"]

    def visit_ImportDeclaration(self, elem):
        name = AstNode(elem).name
        class_name = name.split('.')[-1]
        if class_name == '*':
            return True
        self.add_term(class_name)
        return True

    def visit_ClassDeclaration(self, elem):
        self.active_class_name = elem.name
        return True

    def leave_ClassDeclaration(self, elem):
        self.active_class_name = ''
        return True

    def visit_MethodDeclaration(self, elem):
        # more then N LOC
        if hasattr(elem, "body") and len(elem.body) >= N_LOC:
            res = self._parse_method_body(elem)
            res.insert(0, CorpusNode(self.active_class_name, "ClassDeclaration",
                                     self.split_exclude_list))
            self.snippets.append(res)
        return True
