/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import com.teletalk.jserver.SubSystem;
import com.teletalk.jserver.queue.FileDBQueueStorage;
import com.teletalk.jserver.queue.InQueueControllerSystem;
import com.teletalk.jserver.queue.QueueItem;
import com.teletalk.jserver.queue.messaging.QueueMessagingManager;

/**
 * @author Tobias L�fstrand
 */
public class ReceiverController extends InQueueControllerSystem {

    private int i = 0;

    protected void doShutDown() {
        super.doShutDown();
    }

    /**
    */
    public ReceiverController(SubSystem parent) {
        super(parent, "ReceiverController");
        super.queueManager.setQueueCollaborationManager(new QueueMessagingManager(super.queueManager, "QueueMessagingManager", "queueTestReceiver"));
        super.queueManager.getInQueue().setQueueStorage(new FileDBQueueStorage(queueManager.getInQueue()));
    }

    /**
    */
    public void run() {
        try {
            System.out.println("ReceiverController: waiting for incoming items...");
            super.queueManager.waitForEnabled(10000);
            QueueItem item = null;
            int i = 0;
            Thread.sleep(5000);
            System.out.println("ReceiverController: processing incoming items...");
            long startTime = -1;
            for (; canRun; i++) {
                try {
                    item = queueManager.getInQueue().checkOutFirst();
                    if (startTime < 0) startTime = System.currentTimeMillis();
                } catch (InterruptedException ie) {
                    if (!canRun) break;
                }
                if (((i + 1) % 100) == 0) {
                    System.out.println("T+" + (long) ((System.currentTimeMillis() - startTime) / 1000L) + "s - " + i + " items completed.");
                }
                if (Math.random() >= 0.8) queueManager.inItemDoneFailure(item); else queueManager.inItemDoneSuccess(item);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
