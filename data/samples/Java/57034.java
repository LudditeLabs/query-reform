/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.util.Random;

public class BarrierTest {

    private String[] testStringArray = new String[128 * 3];

    private int midpoint = 128 * 3 / 2;

    public static final int CARD_DIRTY_BYTE = 0;

    public static final int CARD_CLEAN_BYTE = 1;

    public static final int CARD_SUMMARIZED_BYTE = 2;

    public BarrierTest() {
    }

    public native int checkCardMarking();

    public static native void doYoungGC();

    private void setString() {
        testStringArray[midpoint] = null;
    }

    private void setString(String str) {
        testStringArray[midpoint] = str;
    }

    public String getString() {
        String str = testStringArray[midpoint];
        return str;
    }

    public static void main(String args[]) {
        Random rd = new Random();
        BarrierTest test = new BarrierTest();
        String str = String.valueOf(rd.nextInt());
        System.out.println();
        doYoungGC();
        doYoungGC();
        doYoungGC();
        doYoungGC();
        if (test.checkCardMarking() == CARD_CLEAN_BYTE) System.out.println("PASS: Card is clean before setting the object field"); else System.out.println("FAIL: Card is not clean before setting the object field");
        test.setString(str);
        if (test.checkCardMarking() == CARD_DIRTY_BYTE) System.out.println("PASS: Card is dirty after setting the object field"); else System.out.println("FAIL: Card is not dirty after setting the object field");
        test.setString(new String("test"));
        doYoungGC();
        if (test.checkCardMarking() == CARD_SUMMARIZED_BYTE) System.out.println("PASS: Card is summarized after GC"); else System.out.println("FAIL: Card is not summarized after GC");
        doYoungGC();
        doYoungGC();
        if (test.checkCardMarking() == CARD_CLEAN_BYTE) System.out.println("PASS: Card is clean after GC"); else System.out.println("FAIL: Card is not clean after GC");
        test.setString();
        doYoungGC();
        if (test.checkCardMarking() == CARD_CLEAN_BYTE) System.out.println("PASS: Card is clean with GC after reference to the object is removed"); else System.out.println("FAIL: Card is not clean with GC after reference to the object is removed");
    }
}
