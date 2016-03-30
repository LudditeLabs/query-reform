import os

"""
Changeable internal constants
"""
# minimum number of term occurrence to include it to training model
MIN_TERM_COUNT = 1

# number of output reformulated terms
NUM_RESULTS = 5

# number of candidate reformulated terms
NUM_CANDIDATES = 10

"""
Unchangeable internal constants
"""
# path with Java API classes
JAVA8CLASSESAPIPATH = os.path.realpath("./data/APIClassesListJava8.txt")

# path with Java API methods
JAVA8METHODSAPIPATH = os.path.realpath("./data/APIMethodsListJava8.txt")

# path with Python API classes
PYTHON8CLASSESAPIPATH = os.path.realpath("./data/APIClassesListPython27.txt")

# path with Python API methods
PYTHON8METHODSAPIPATH = os.path.realpath("./data/APIMethodsListPython27.txt")

# path with JavaScript API methods
JAVASCRIPTMETHODSAPIPATH = os.path.realpath("./data/APIMethodsListJavaScript.txt")

# minimum number of lines of code to include method as a snippet to corpus
N_LOC = 5

# extentions of index files
INDEX_EXT = ".idx"
CLASSES_EXT = ".cls"
INFO_EXT = ".inf"
SNIPPETS_EXT = ".snp"
