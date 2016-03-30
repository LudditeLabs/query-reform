/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.lang.*;
import java.lang.reflect.UndeclaredThrowableException;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author <A HREF="mailto:mike@opennms.org">Mike</A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS</A>
 *
 */
public final class SocketTest extends Thread {

    static final int MAX_CLIENTS_DEFAULT = 200;

    private static int m_maxClients;

    static final int NEW_CLIENT_INTERVAL_DEFAULT = 300000;

    private static int m_newClientInterval;

    /**
	 * TCP port on which the daemon listens for incoming requests.
	 */
    static final int DAEMON_TCP_PORT = 15000;

    /**
	 * List of clients currently connected to the daemon
	 */
    private static List m_clients;

    /**
	 * Client thread.
	 */
    private final class Client extends Thread {

        int m_count;

        int m_lastCheckedCount;

        Client(int id) {
            super("Client-" + id);
            m_count = 0;
            m_lastCheckedCount = 0;
        }

        public boolean statusOk() {
            if (m_count != m_lastCheckedCount) {
                m_lastCheckedCount = m_count;
                return true;
            } else return false;
        }

        public void run() {
            Socket connection = null;
            DataInputStream dis = null;
            try {
                connection = new Socket(InetAddress.getLocalHost(), DAEMON_TCP_PORT);
                BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
                dis = new DataInputStream(bis);
            } catch (IOException ex) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Throwable t) {
                    }
                }
                throw new UndeclaredThrowableException(ex);
            } catch (Throwable t) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Throwable tx) {
                    }
                }
                throw new UndeclaredThrowableException(t);
            }
            for (; ; ) {
                byte[] data = new byte[2048];
                int len = -1;
                try {
                    len = dis.readInt();
                } catch (IOException ex) {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (Throwable t) {
                        }
                    }
                    throw new UndeclaredThrowableException(ex);
                }
                try {
                    dis.readFully(data, 0, len);
                } catch (IOException ex) {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (Throwable t) {
                        }
                    }
                    throw new UndeclaredThrowableException(ex);
                }
                m_count++;
            }
        }
    }

    /**
	 * Server thread.
	 */
    private final class Server extends Thread {

        List m_clients;

        Server(List clients) {
            super("Server");
            m_clients = clients;
        }

        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(DAEMON_TCP_PORT);
            } catch (IOException ex) {
                throw new UndeclaredThrowableException(ex);
            }
            long lastClientStarted = 0;
            boolean firstTime = true;
            try {
                serverSocket.setSoTimeout(1000);
                int client_id = 1;
                for (; ; ) {
                    if (!firstTime && ((System.currentTimeMillis() - lastClientStarted) >= m_newClientInterval) && m_clients.size() < m_maxClients) {
                        Client clnt = new Client(client_id);
                        clnt.start();
                        lastClientStarted = System.currentTimeMillis();
                        m_clients.add(clnt);
                    }
                    Socket sock;
                    try {
                        sock = serverSocket.accept();
                    } catch (InterruptedIOException iE) {
                        if (firstTime) {
                            Client clnt = new Client(client_id);
                            clnt.start();
                            firstTime = false;
                            lastClientStarted = System.currentTimeMillis();
                            m_clients.add(clnt);
                        }
                        continue;
                    }
                    ClientHandler handler = new ClientHandler(client_id, sock);
                    handler.start();
                    System.out.println("SocketTest:  finished starting client & handler pair number " + client_id);
                    client_id++;
                }
            } catch (IOException ioE) {
                System.out.println("I/O exception occured processing incomming request " + ioE);
            } catch (Throwable t) {
                System.out.println("An undeclared throwable was caught " + t);
            } finally {
                System.exit(0);
            }
        }
    }

    /**
	 * Client handler thread.
	 */
    private final class ClientHandler extends Thread {

        Socket m_connection;

        ClientHandler(int id, Socket sock) {
            super("ClientHandler-" + id);
            m_connection = sock;
        }

        public void run() {
            DataOutputStream dos = null;
            try {
                BufferedOutputStream bos = new BufferedOutputStream(m_connection.getOutputStream());
                dos = new DataOutputStream(bos);
            } catch (IOException ex) {
                if (m_connection != null) {
                    try {
                        m_connection.close();
                    } catch (Throwable t) {
                    }
                }
                throw new UndeclaredThrowableException(ex);
            }
            int written = 0;
            byte[] sendBuf = new byte[2048];
            Random generator = new Random();
            for (; ; ) {
                int len = -1;
                try {
                    len = generator.nextInt(2048);
                    if (len == 0) len = 2048;
                    dos.writeInt(len);
                    dos.flush();
                    dos.write(sendBuf, 0, len);
                    dos.flush();
                } catch (IOException ex) {
                    if (m_connection != null) {
                        try {
                            m_connection.close();
                        } catch (Throwable t) {
                        }
                    }
                    throw new UndeclaredThrowableException(ex);
                }
                written++;
                try {
                    sleep(1);
                } catch (InterruptedException ie) {
                    throw new UndeclaredThrowableException(ie);
                }
            }
        }
    }

    /**
	 *
	 */
    public SocketTest() {
        m_clients = null;
        m_maxClients = MAX_CLIENTS_DEFAULT;
        m_newClientInterval = NEW_CLIENT_INTERVAL_DEFAULT;
    }

    /**
	 *
	 */
    public synchronized void init() {
        m_clients = Collections.synchronizedList(new LinkedList());
        Server server = new Server(m_clients);
        server.start();
    }

    /**
	 * The main routine. Basically a watchdog thread which is responsible
	 * for verifying that none of the server/client sockets are hung.
	 */
    public void run() {
        int count = 0;
        for (; ; ) {
            try {
                sleep(2000);
            } catch (InterruptedException ie) {
                throw new UndeclaredThrowableException(ie);
            }
            boolean hungClient = false;
            synchronized (m_clients) {
                Iterator iter = m_clients.iterator();
                while (iter.hasNext()) {
                    Client clnt = (Client) iter.next();
                    if (!clnt.statusOk()) {
                        System.out.println("SocketTest: client '" + clnt.getName() + "' appears to be hung!");
                        hungClient = true;
                        break;
                    }
                }
            }
            count++;
            if ((count % 10 == 0) && !hungClient) {
                System.out.println("SocketTest: " + m_clients.size() + "  clients...status: ok.");
            }
        }
    }

    public static void main(String[] args) {
        SocketTest tester = new SocketTest();
        int index = 0;
        System.out.println("SocketTest: parsing args...");
        while (index < args.length) {
            if (args[index].equals("-c")) {
                String str = args[index + 1];
                try {
                    m_maxClients = Integer.parseInt(str);
                } catch (NumberFormatException nfE) {
                    System.out.println("SocketTest: number format exception parsing max clients argument...defaulting to " + MAX_CLIENTS_DEFAULT);
                    m_maxClients = MAX_CLIENTS_DEFAULT;
                }
            }
            if (args[index].equals("-i")) {
                String str = args[index + 1];
                try {
                    m_newClientInterval = Integer.parseInt(str);
                } catch (NumberFormatException nfE) {
                    System.out.println("SocketTest: number format exception parsing new client interval argument...defaulting to " + NEW_CLIENT_INTERVAL_DEFAULT + "ms");
                    m_newClientInterval = NEW_CLIENT_INTERVAL_DEFAULT;
                }
            }
            index = index + 2;
        }
        System.out.println("Initializing tester...maxClients: " + m_maxClients + " newClientInterval: " + m_newClientInterval);
        tester.init();
        System.out.println("Starting tester...");
        tester.start();
        System.out.println("Finished main...");
    }
}
