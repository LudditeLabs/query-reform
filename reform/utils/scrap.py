import re


class Scrap(object):
    """
    Scraps method names from the Java doc
    """

    def __init__(self, file_path, out_name):
        self.file_path = file_path
        self.out_name = out_name
        self.java_method_re = re.compile('^([a-z]+.+)\(')
        self.js_method_re = re.compile('^([a-z]+): ')
        self.python_class_re = re.compile('^([A-z]+.+) \(class in')
        self.python_method_re = re.compile('^([A-z]+.+)\(\)')

    def scrap_java_methods(self):
        self._scrap(self.java_method_re)

    def scrap_js_methods(self):
        self._scrap(self.js_method_re)

    def scrap_python_classes(self):
        self._scrap(self.python_class_re)

    def scrap_python_methods(self):
        self._scrap(self.python_method_re)

    def _scrap(self, scrap_re):
        res = set()
        with open(self.file_path) as f:

            for line in f:
                match = scrap_re.findall(line.strip())
                if match:
                    res.add(match[0])
        print "Found %d methods" % len(res)
        with open(self.out_name, 'w') as o:
            for r in res:
                o.write(r + '\n')


if __name__ == '__main__':
    scrapper = Scrap('../../data/raw/js_methods.txt', 'jsapimethods.txt')
    scrapper.scrap_js_methods()
