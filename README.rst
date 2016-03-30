About
=====
When searching for useful code snippets, it helps to reformulate search queries in terms of code classes or methods. For example, a query: *"How to download a file by URL in Java"*, can be expanded to include *"InputStream, Vector, DataOutputStream"*. These terms are extracted from a pre-indexed storage that is built using example Java codes. 

Code search are formulated as a natural-language query, but actual code may contain esoteric terms and phrases which can be difficult to retrieve. This tool translates and expands natural-language queries to a format that can be fed to a code search system for more relevant search results.  
See `details <https://qspace.library.queensu.ca/bitstream/1974/13431/1/Niu_Haoran_201507_MASC.pdf>`_.


Install
=======

pip and virualenv
-----------------
To setup the application, it requires `pip <http://pip.readthedocs.org/en/stable/installing/>`_. and you might want to use a `python virtual environment <http://docs.python-guide.org/en/latest/dev/virtualenvs/>`_.
::
 # activate a virtualenv

 # install requirements
 pip install -r requirements.txt


Conda
-----
To setup the application with Conda package management system, it requires `conda <http://conda.pydata.org/docs/>`_.
::
 # Install conda

 # Create a conda environment from conda.yml
 conda env create -f conda.yml

 # Activate the environment
 source activate reformulate


Check installation
------------------
To check if the package properly installed you may run tests and perform a simple refomulation
::
 # (optional) run tests
 python -m unittest discover

 # build a sample index
 python reformulation.py index ./data/samples/Java/

 # run a sample reformulation
 python reformulation.py reform "how to write a file"

Usage
=====
Before reformulation can be performed, the index has to be created. The index is based on training data - code snippets.
It contains frequencies of each normalized term in the corpus and ASTs that will be used for reformulation.
The index can be created once for each corpus and then reused for every query.
 
Index
-----
To create index run:
::
 python reformulation.py index /path/to/code/snippets

The specified path is scanned (non-recursively) to find code files. Code files are parsed and snippets 
are extracted from each file. The index is stored in the *./index* folder with *index* base name. The default path 
can be changed. *python reformulation.py --help* shows available parameters. 
Note, that the index operation is a slow operation if there are many code files involved. 
Once the index is created it can used for reformulation.

Reformulation
-------------
To reformulate queries run:
::
 python reformulation.py reform "how to download file by URL"

The command reformulates the input query using the index in the *./index* folder. The output is printed to the console.
By default, the output contains only public API methods. *--all* parameters can be used to loosen the limit.

Using different languages
-------------------------
The default language for reformulation is Java. You can change the setting with the *--lang="Python"* parameter. The parameter 
must be specified both at the index and reformulation stage.

Interactive mode
----------------
In the interactive mode the application accepts queries and outputs the reformulation result in the terminal.
To enter the interactive mode use *-i* key of the *reform* command.

Additional parameters
=====================
To find out all available parameters, please, run:
::
 python reformulation.py --help

With additional parameters you can change index path to build multiple index files, change language, logging output, etc.


Useful snippets
===============
Try out sample data:
::
 python reformulation.py index data/samples/Java/ --name="samples"

 python reformulation.py reform "how to download file by URL" --name="samples"
 python reformulation.py reform "output random numbers" --name="samples"
 python reformulation.py reform "connect to a server" --name="samples"
 python reformulation.py reform "read input lines" --name="samples" --all


Use multiple index files at the same time:
::
 python reformulation.py index PATH_TO_SAMPLES --name="samples"
 python reformulation.py index PATH_TO_DEFAULT --name="default"

 python reformulation.py reform "how to download file by URL" --name="samples"
 python reformulation.py reform "output random numbers" --name="default"


Loosen the public API limit:
::
 python reformulation.py reform "how to download file by URL" --name="samples" --all


Build index and reformulate terms using Java methods for reformulation:
::
 python reformulation.py index ./data/samples/Java --name="samples" --methods
 python reformulation.py reform "how to download file by URL" --name="samples" --methods


Build index and reformulate terms using Python methods for reformulation:
::
 python reformulation.py index ./data/samples/Python --name="samples_python" --methods --lang="Python"
 python reformulation.py reform "segment length to remove" --name="samples_python" --methods --lang="Python"


Build index for all files in a repository recursively
::
 python reformulation.py index PATH -r



Downloading a big dataset
-------------------------
The chain of the links for the dataset (https://github.com/clonebench/BigCloneBench -> https://drive.google.com/file/d/0B70GNOiQD-X7ZDVBMzRUWktDUWs/view -> Download file ERA_BigCloneBench_IJaDataset.tar.gz).
The archive (when unzipped) has 3 folders, they are actually subsets of one another. So it's better to use one folder only for one index command


Running indexing on a big dataset
---------------------------------
Indexing of big datasets with 200k+ files takes RAM so to it is possible to dump intermediate data from RAM to starage with --dump option.
::
 python reformulation.py index data/samples/Java/ --name="samples" -d


API usage examples
==================

Parts of the code can be reused for *ad hoc* reformulation-related tasks.
   
Normalization
-------------
Normalization is the process of removal of common words and stemming of a query.
::
 from reform.processing.normalize import Normalizer

 normalizer = Normalizer()

 # split on query terms, removes stop-words, stemms the terms
 normalizer.process_query("how to play sound using java")

 # ['play', 'sound', 'use', 'java']


Vectorization
-------------
Vectorization is the process counting the most common words that can be followed by reformulation.
::
 from reform.parse.models import APINode
 from reform.processing.vectorize import Vectorizer
 
 # input snippets
 corpus = [
             ['downloadwebpagesampl', 'address', 'string', 'string', 'client', 'http', 'client',
              'build', 'http', 'client', 'httpclient', 'request', 'address', 'http', 'get',
              'httpget', 'http', 'get',
              'httpget', 'respons', 'request', 'client', 'execut', 'http', 'respons', 'httprespons',
              'string', 'line', 'br', 'read', 'line', 'page', 'line', '"\\n"', 'br', 'close',
              'client', 'protocol', 'except', 'clientprotocolexcept', 'io', 'except', 'ioexcept']
         ]

 # terms storage to output
 ast_classes_storage = {"httpclient": APINode("HttpClient", count=1)}

 # trains the model 
 vectorizer = Vectorizer()
 vectorizer.train(corpus, ast_classes_storage, min_count=0)

 # performes reformulation
 res = vectorizer.reformulate(["request"], num_res=1)

 # ['HttpClient']


Language-specific reformulations
--------------------------------
Find a language using the input parameters
::
 from reform.parse.selector import CorpusSelector

 corpus_storage, parse = CorpusSelector.select(self.lang, self.methods, **kwargs)

 if corpus_storage == NotImplemented:
     return