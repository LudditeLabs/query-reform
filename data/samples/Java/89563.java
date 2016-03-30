/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 *
 * @author Arjen
 */
public class RaceDataReceiver {

    private byte[] data = new byte[1024];

    private DatagramSocket socket;

    private DatagramPacket packet;

    private ByteBuffer byteBuffer;

    private int bufferSize;

    private int pixelsInBuffer;

    /** Creates a new instance of RaceDataReceiver */
    public RaceDataReceiver() {
        try {
            socket = new DatagramSocket(2001);
            packet = new DatagramPacket(data, data.length);
            byteBuffer = ByteBuffer.wrap(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveData() {
        try {
            socket.receive(packet);
            System.out.println("Timestamp: " + byteBuffer.getInt(0));
            System.out.println("Location horse 1: " + byteBuffer.getInt(4));
            System.out.println("Speed horse 1: " + byteBuffer.getInt(8));
            System.out.println("Location horse 2: " + byteBuffer.getInt(12));
            System.out.println("Speed horse 2: " + byteBuffer.getInt(16));
            System.out.println("Location horse 3: " + byteBuffer.getInt(20));
            System.out.println("Speed horse 3: " + byteBuffer.getInt(24));
            System.out.println("Location horse 4: " + byteBuffer.getInt(28));
            System.out.println("Speed horse 4: " + byteBuffer.getInt(32) + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RaceDataReceiver rdr = new RaceDataReceiver();
        long timer = System.currentTimeMillis();
        while (true) {
            rdr.receiveData();
        }
    }
}
