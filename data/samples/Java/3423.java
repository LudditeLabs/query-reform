/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Navarro & Tomio
 */
public class Serveur {

    private int numPort;

    private String nameFile = "test";

    private SVGWritter mySvgFile;

    public Serveur(int _numPort) throws IOException {
        numPort = _numPort;
        mySvgFile = new SVGWritter(nameFile);
    }

    public void lancer() {
        try {
            ServerSocket serverSocket = new ServerSocket(numPort);
            try {
                while (true) {
                    Socket socket = serverSocket.accept();
                    ObjectInputStream fluxDEntree = new ObjectInputStream(socket.getInputStream());
                    try {
                        traiteMessage((String) fluxDEntree.readObject());
                    } catch (ClassNotFoundException e) {
                        System.err.println(e.getMessage());
                    }
                    socket.close();
                }
            } catch (IOException e2) {
                System.err.println(e2.getMessage());
            }
        } catch (IOException e1) {
            System.err.println(e1.getMessage());
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        System.out.println("-- Création du Serveur :");
        Serveur serveur = new Serveur(8080);
        serveur.lancer();
    }

    public void traiteMessage(String message) throws IOException {
        System.out.println("Traitement du message");
        System.out.println(message);
        ArrayList<String> tempTab = new ArrayList();
        StringTokenizer st = new StringTokenizer(message, ",");
        while (st.hasMoreTokens()) {
            tempTab.add(st.nextToken());
        }
        if (tempTab.get(0).equals("PROC") || tempTab.get(0).equals("proc")) {
            if (tempTab.size() - 1 == 3) {
                mySvgFile.dessineProcessus(Integer.parseInt(tempTab.get(1)), String.valueOf(tempTab.get(2)), Integer.parseInt(tempTab.get(3)));
            } else {
                System.out.println("Le nombre d'arguments n'est pas bon");
            }
        } else if (tempTab.get(0).equals("SC") || tempTab.get(0).equals("sc")) {
            if (tempTab.size() - 1 == 3) {
                mySvgFile.dessineSC(Integer.parseInt(tempTab.get(1)), Integer.parseInt(tempTab.get(2)), Integer.parseInt(tempTab.get(3)));
            } else {
                System.out.println("Le nombre d'arguments n'est pas bon");
            }
        } else if (tempTab.get(0).equals("REQ") || tempTab.get(0).equals("req")) {
            if (tempTab.size() - 1 == 4) {
                mySvgFile.dessinerREQ(Integer.parseInt(tempTab.get(1)), Integer.parseInt(tempTab.get(2)), Integer.parseInt(tempTab.get(3)), Integer.parseInt(tempTab.get(4)));
            } else {
                System.out.println("Le nombre d'arguments n'est pas bon");
            }
        } else if (tempTab.get(0).equals("REP") || tempTab.get(0).equals("rep")) {
            if (tempTab.size() - 1 == 4) {
                mySvgFile.dessinerREP(Integer.parseInt(tempTab.get(1)), Integer.parseInt(tempTab.get(2)), Integer.parseInt(tempTab.get(3)), Integer.parseInt(tempTab.get(4)));
            } else {
                System.out.println("Le nombre d'arguments n'est pas bon");
            }
        } else if (tempTab.get(0).equals("REAL") || tempTab.get(0).equals("real")) {
            if (tempTab.size() - 1 == 4) {
                mySvgFile.dessinerREAL(Integer.parseInt(tempTab.get(1)), Integer.parseInt(tempTab.get(2)), Integer.parseInt(tempTab.get(3)), Integer.parseInt(tempTab.get(4)));
            } else {
                System.out.println("Le nombre d'arguments n'est pas bon");
            }
        } else if (tempTab.get(0).equals("Fin") || tempTab.get(0).equals("fin")) {
            if (tempTab.size() == 1) {
                mySvgFile.writeAllData();
            } else {
                System.out.println("Le message entré n'est pas compréhensible par le serveur");
            }
        } else {
            System.out.println("Le message entré n'est pas compréhensible par le serveur");
        }
    }
}
