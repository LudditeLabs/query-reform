# The benchmark is distributed under the Creative Commons,
# Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
# and its derivatives. For attribution, please cite this page, and our publications below.
# This data is provided free of charge for non-commercial and academic benchmarking and
# experimentation use. If you would like to contribute to the benchmark, please contact us.
# If you believe you intended usage may be restricted by the license,
# please contact us and we can discuss the possibilities.

from imaplib import IMAP4, IMAP4_SSL

import logging
logger = logging.getLogger(__name__)

class ImapTransport(object):

    def __init__(self, hostname, port=None, ssl=False):
        self.hostname = hostname
        self.port = port

        if ssl:
            self.transport = IMAP4_SSL
            if not self.port:
                self.port = 993
        else:
            self.transport = IMAP4
            if not self.port:
                self.port = 143

        self.server = self.transport(self.hostname, self.port)
        logger.debug("Created IMAP4 transport for {host}:{port}".format(host=self.hostname, port=self.port))

    def list_folders(self):
        logger.debug("List all folders in mailbox")
        return self.server.list()

    def connect(self, username, password):
        self.server.login(username, password)
        self.server.select()
        logger.debug("Logged into server {} and selected mailbox 'INBOX'".format(self.hostname))
        return self.server
        