/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.io.PrintStream;
import org.jikesrvm.mm.mminterface.MemoryManager;
import org.vmmagic.pragma.NoInline;

class Exhaust {

    private static final PrintStream o = System.out;

    static Object[] first;

    static Object[] last;

    static final int itemSize = 64;

    static double growthFactor = 10.0;

    static int rounds = 5;

    static long tot = 0;

    static long wasAllocating;

    public static void main(String[] args) {
        long mhs = MemoryManager.getMaxHeapSize().toLong();
        o.println("Max heap size: " + mhs + " bytes");
        if (mhs > 1024 * 1024) o.println("  that's " + mhs / (1024.0 * 1024.0) + " megabytes");
        if (mhs > 1024 * 1024 * 1024) o.println("  that's " + mhs / (1024.0 * 1024 * 1024) + " gigabytes");
        runTest();
        System.exit(0);
    }

    @NoInline
    public static int doInner(int size) {
        while (true) {
            wasAllocating = size;
            Object[] next = new Object[size / 4];
            last[0] = next;
            last = next;
            tot += size;
            wasAllocating = 0;
        }
    }

    @NoInline
    public static void runTest() {
        int size = itemSize;
        for (int i = 1; i <= rounds; i++) {
            o.println("Starting round " + i + " with size = " + size);
            first = new Object[1];
            last = first;
            tot = 0;
            o.println("  Allocating until exception thrown");
            try {
                doInner(size);
            } catch (OutOfMemoryError e) {
                first = last = null;
                o.println("  Caught OutOfMemory - freeing now");
                o.println("  Had " + tot + " bytes allocated; failed trying to allocate " + wasAllocating + " bytes");
            }
            size *= growthFactor;
        }
        System.out.println("Overall: SUCCESS");
    }
}
