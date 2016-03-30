#!/usr/bin/python
import argparse
import cmd
import logging
import sys

import os
from reform.index.load import Loader

LANGUAGES = ["java", "python", "javascript"]
COMMANDS = ["index", "reform"]


class InteractiveReform(cmd.Cmd):
    '''
    Interactive reformulation with pre-loaded storage
    '''

    def do_reform(self, query):
        if query.startswith('"') and query.endswith('"'):
            query = query[1:-1]
        res = self.storage.normalize_and_reformulate(query, self.options.all)
        print_result(res, query)

    def do_exit(self, line):
        return True

    def help_reform(self):
        print '\n'.join(['To reformulate queries run',
                         'reform "query"',
                         'Example: reform "how to download file by URL"',
                         'It runs with the parameters '
                         '(index name, method reformulation, etc) '
                         'that were passed when entering into the interactive mode',
                         ])

    def help_exit(self):
        print '\n'.join(['exit',
                         'Exits the interactive mode',
                         ])


def print_result(res, query):
    if not res:
        logging.error("Cannot find candidates for the query: %s" % query)
    else:
        print "Reformulation result: %s" % ', '.join(res)


# ------------------------------------
# Main function
# ------------------------------------
def main():
    """
    The Main function.
    """
    # Parse options...
    parser = prepare_argparser()
    options = opt_validate(parser)
    # end of parsing commandline options


    info = options.info
    warn = options.warn
    debug = options.debug
    error = options.error

    # 0 output arguments
    info("\n" + options.argtxt)

    loader = Loader(options.path, options.index_folder, options.index_name, options.lang,
                    options.methods,
                    options.recursive, options.dump, options.skip)
    if options.command == "index":
        info("Building index...")
        loader.build()

    if options.command == "reform":
        info("Loading the storage...")
        storage = loader.load()
        if options.interactive:
            info("Entering interactive mode. Type 'help' to see available commands")
            interactive = InteractiveReform()
            interactive.storage = storage
            interactive.options = options
            interactive.cmdloop()
        else:
            info("Reformulating...")
            res = storage.normalize_and_reformulate(options.query, options.all)
            info("Done")
            print_result(res, options.query)


def prepare_argparser():
    """
    Prepares optparser object. New options will be added in this
    function first.

    """
    usage = """
    %(prog)s index path [options] To create pre-calculated storage for the path given
    %(prog)s reform query [options] To reformulate the query
    """
    description = "%(prog)s -- Reformulates search queries using code training set"

    optparser = argparse.ArgumentParser(prog="reformulation", description=description, usage=usage)

    group = optparser.add_argument_group("index",
                                         "index creates pre-calculated storage for futher reformulation.")
    group.add_argument("-r", "--recursive", dest="recursive", action="store_true",
                       help="Recursive scan of folders to index. DEFAULT: False",
                       default=False)

    group.add_argument("-d", "--dump", dest="dump", action="store_true",
                       help="Dumps corpus to a file and loads it by word2vec. DEFAULT: False",
                       default=False)

    group.add_argument("-s", "--skip", dest="skip", action="store_true",
                       help="Skips processing and takes existing corpus. DEFAULT: False",
                       default=False)

    groupReform = optparser.add_argument_group("reform",
                                               "reformulates te query using the index.")

    groupReform.add_argument("-a", "--all", dest="all", action="store_true", default=False,
                             help="Reformulate using all ASTs (not public API only). DEFAULT: false")

    optparser.add_argument("-i", "--interactive", dest="interactive", action="store_true",
                           help="Interactive mode for reformulation. DEFAULT: False",
                           default=False)
    optparser.add_argument("-f", "--folder", dest="index_folder",
                           help="Index folder. DEFAULT: index",
                           default="index")

    optparser.add_argument("-n", "--name", dest="index_name",
                           help="Index name. DEFAULT: index",
                           default="index")

    # optparser.add_argument("-h", "--help", action="help", help="show this help message and exit.")
    optparser.add_argument("command", help="command to run [index|reform]")
    optparser.add_argument("arg", help="Command argument [path|query]")
    optparser.add_argument("-l", "--lang", dest="lang",
                           help="Language. DEFAULT: Java",
                           default="Java")
    optparser.add_argument("-m", "--methods", dest="methods", action="store_true",
                           help="Use methods instead of classes for reformulation. DEFAULT: False",
                           default=False)
    optparser.add_argument("--verbose", dest="verbose", type=int, default=2,
                           help="Set verbose level. 0: only show critical message, 1: show additional warning message, 2: show process information, 3: show debug messages. DEFAULT:2")

    return optparser


def opt_validate(optparser):
    """Validate options from a OptParser object.

    Ret: Validated options object.
    """
    args = optparser.parse_args()

    if not args.command:
        logging.error("No command")
        logging.error(optparser.usage)
        sys.exit(1)

    if args.command not in COMMANDS:
        logging.error("Unknown command: %s" % args.command)
        logging.error("Available commands: %s" % COMMANDS)
        sys.exit(1)

    if args.command == "index":
        if not args.arg:
            logging.error("No path")
            logging.error(optparser.usage)
            sys.exit(1)
        args.path = args.arg

    if args.command == "reform":
        if args.interactive:
            args.query = ""
        else:
            if not args.arg:
                logging.error("No query")
                logging.error(optparser.usage)
                sys.exit(1)
            args.query = args.arg
        args.path = ''

    args.lang = args.lang.lower()

    if (not args.index_folder.endswith('\\')) or (not args.index_folder.endswith('/')):
        args.index_folder = args.index_folder + '/'

    if not os.path.exists(args.index_folder):
        os.mkdir(args.index_folder)
        if not os.path.exists(args.index_folder):
            logging.error("Cannot create path: %s!" % args.index_folder)
            sys.exit(1)
    args.index_folder = os.path.abspath(args.index_folder)

    if args.lang.lower() not in LANGUAGES:
        logging.error("Unknown language: %s" % args.lang)
        logging.error("Available languages: %s" % LANGUAGES)
        sys.exit(1)

    logging.basicConfig(level=(4 - args.verbose) * 10,
                        format='%(levelname)-5s @ %(asctime)s: %(message)s ',
                        datefmt='%a, %d %b %Y %H:%M:%S',
                        stream=sys.stderr,
                        filemode="w"
                        )

    args.error = logging.critical  # function alias
    args.warn = logging.warning
    args.debug = logging.debug
    args.info = logging.info

    arglist = ["# ARGUMENTS LIST:"]
    arglist.append("# command = %s" % args.command)
    arglist.append("# language = %s" % args.lang)
    arglist.append("# index folder = %s" % args.index_folder)
    arglist.append("# index name = %s" % args.index_name)
    if args.methods:
        arglist.append("# Using methods for reformulation")
    else:
        arglist.append("# Using classes for reformulation")

    if args.command == "reform":
        arglist.append("# query = %s" % args.query)
        arglist.append("# all ASTs = %s" % args.all)

    args.argtxt = "\n".join(arglist)

    return args


if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        sys.stderr.write("User interruption. Finished.\n")
        sys.exit(0)
