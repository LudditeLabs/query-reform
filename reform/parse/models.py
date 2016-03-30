class CorpusNode(object):
    """
    Stores raw corpus terms
    """

    def __init__(self, name, type_name, split_exclude_list=None):
        self.name = name
        self.type_name = type_name
        self.is_split = self._is_split(split_exclude_list or [])

    def __repr__(self):
        return "(%s, %s)" % (self.name, self.type_name)

    def __eq__(self, other):
        return self.name == other.name and self.type_name == other.type_name

    def _is_split(self, split_exclude_list):
        return self.type_name not in split_exclude_list


class APINode(object):
    """
    Holds base properties of an AST element for reformulation: name, count, public API inclusion
    """

    def __init__(self, name, count=1, publicapi=False):
        self.count = count
        self.name = name
        self.publicapi = publicapi

    def __repr__(self):
        return self.name


class AstNode(object):
    """
    Java AST Node holds parsed state and child elements
    """

    def __init__(self, st):
        self.st = st

    @property
    def name(self):
        name = ''
        if hasattr(self.st, 'name'):
            if isinstance(self.st.name, basestring):
                name = self.st.name
            if hasattr(self.st.name, 'value'):
                name = self.st.name.value
                if hasattr(self.st.name.value, 'name'):
                    name = self.st.name.value.name
                    if hasattr(self.st.name.value.name, 'value'):
                        name = self.st.name.value.name.value
            elif hasattr(self.st.name, 'name'):
                name = self.st.name.name
                if hasattr(self.st.name.name, 'value'):
                    name = self.st.name.name.value
        elif hasattr(self.st, 'attr'):
            if isinstance(self.st.attr, basestring):
                name = self.st.attr
        elif hasattr(self.st, 'value'):
            if isinstance(self.st.value, basestring):
                name = self.st.value
        elif hasattr(self.st, 'id'):
            if isinstance(self.st.id, basestring):
                name = self.st.id
        elif isinstance(self.st, basestring):
            name = self.st

        return name

    @property
    def type_name(self):
        return self.st.__class__.__name__

    def children(self):
        res = []
        if hasattr(self.st, "_fields"):
            for f in self.st._fields:
                field = getattr(self.st, f)
                if field:
                    if isinstance(field, list):
                        for elem in field:
                            res.append(AstNode(elem))
        return res

    def fields(self):
        res = []
        if hasattr(self.st, "_fields"):
            for f in self.st._fields:
                field = getattr(self.st, f)
                if field:
                    if not isinstance(field, list):
                        res.append(AstNode(field))

        return res

    def __repr__(self):
        return "{0}-{1}({2})".format(self.type_name, self.name, repr(self.st))
