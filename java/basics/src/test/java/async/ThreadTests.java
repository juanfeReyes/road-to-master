package async;

import org.junit.jupiter.api.Test;
import r2m.cloud.async.AsyncCounter;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreadTests {


    @Test
    public void threadShouldStart() {
        Thread t = new Thread(() -> System.out.println("Creating a thread"));
        t.start();
    }

    @Test
    public void threadShouldBeTrackedByStates() throws InterruptedException {
        Thread tStates = new Thread(() -> {

        });
        assertEquals(Thread.State.NEW, tStates.getState());
        tStates.start();
        assertEquals(Thread.State.RUNNABLE, tStates.getState());
        // check for BLOCKED
        // check for WAITING
        // check for TIMED_WAITING
        tStates.join(); // wait for thread to complete
        assertEquals(Thread.State.TERMINATED, tStates.getState());
    }

    @Test
    public void threadShouldBeSync() throws InterruptedException {
        AsyncCounter aCounter = new AsyncCounter();
        Thread t1 = new Thread(() -> {
            aCounter.incrementSyncMethod();
            aCounter.incrementSyncMethod();
            aCounter.incrementSyncMethod();
        });
        Thread t2 = new Thread(() -> {
            aCounter.incrementSyncBlock();
            aCounter.incrementSyncBlock();
            aCounter.incrementSyncBlock();
        });
        Thread t3 = new Thread(() -> {
            aCounter.incrementSyncMethod();
            aCounter.incrementSyncBlock();
            aCounter.incrementSyncMethod();
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        assertEquals(9, aCounter.getCounter());
    }

    @Test
    public void multipleThreadsShouldStartAtOnce() throws BrokenBarrierException, InterruptedException {
        CyclicBarrier gate = new CyclicBarrier(3); // create a blocking gate to sync thread exec
        AsyncCounter aCounter = new AsyncCounter();
        Thread t1 = new Thread(() -> {
            try {
                gate.await();
                aCounter.incrementSyncMethod();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                gate.await();
                aCounter.incrementSyncMethod();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        t2.start();

        gate.await(); // now all 3 gates has been reached and execution is unblocked in all threads

        t1.join();
        t2.join();

        assertEquals(2, aCounter.getCounter());
    }
}
