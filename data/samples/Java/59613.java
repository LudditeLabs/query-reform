/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.*;

public class MSP {

    private ExecutorService tasks_executor;

    private FileInputStream fin;

    private FileOutputStream fout;

    public MSP(FileInputStream fin_, FileOutputStream fout_) {
        tasks_executor = CommonTools.tasks_executor;
        fin = fin_;
        fout = fout_;
    }

    public void execute() {
        int n;
        Scanner scanner = new Scanner(fin);
        n = scanner.nextInt();
        Vector<Future<MatrixResult>> futures = new Vector<Future<MatrixResult>>(n);
        for (int i = 0; i < n; ++i) {
            int M = scanner.nextInt();
            int N = scanner.nextInt();
            int seed = scanner.nextInt();
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            int m = scanner.nextInt();
            MatrixSumTask task = new MatrixSumTask(M, N, seed, a, b, m);
            futures.add(tasks_executor.submit(task));
        }
        MatrixResult result;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fout));
        for (int i = 0; i < n; ++i) {
            try {
                result = futures.get(i).get();
                bw.write("Case #" + String.valueOf(i) + ": " + String.valueOf(result.x1) + " " + String.valueOf(result.y1) + " " + String.valueOf(result.x2) + " " + String.valueOf(result.y2) + " " + String.valueOf(result.sum) + " " + String.valueOf(result.size) + "\n");
            } catch (InterruptedException e) {
                System.err.println(e.toString());
                e.printStackTrace();
            } catch (ExecutionException e) {
                System.err.println(e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println(e.toString());
                e.printStackTrace();
            }
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        long start = System.currentTimeMillis();
        if (args.length != 3) {
            System.err.println("Wrong number of arguments.");
            return;
        }
        int threads_number = Integer.valueOf(args[0]);
        CommonTools.tasks_executor = Executors.newSingleThreadExecutor();
        CommonTools.subtasks_executor = Executors.newFixedThreadPool(threads_number);
        try {
            FileInputStream fin = new FileInputStream(args[1]);
            FileOutputStream fout = new FileOutputStream(args[2]);
            MSP msp = new MSP(fin, fout);
            msp.execute();
        } catch (FileNotFoundException e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }
        CommonTools.tasks_executor.shutdown();
        CommonTools.subtasks_executor.shutdown();
        long stop = System.currentTimeMillis();
        System.out.println("Elapsed time: " + String.valueOf(stop - start) + " milliseconds");
        return;
    }
}
