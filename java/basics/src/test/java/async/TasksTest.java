package async;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TasksTest {

    @Test
    public void runnableShouldRun(){
        // Runnable is a interface to define a task to be executed by a Thread
        Runnable runnable = () -> {

        };
        Thread tRunnable = new Thread(runnable);
        tRunnable.start();
    }
    
    @Test
    public void callableShouldRun() throws ExecutionException, InterruptedException {
        // Callable is an interface to define a task to be executed by a thread and returns a result
        Callable<String> callable = () -> {
            return "Callable task executed";
        };
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(callable);
        assertEquals("Callable task executed", future.get());
        es.shutdown();
    }

    @Test
    public void callableShouldPropagateException(){
        // Callable is able to propagate exceptions to the parent thread
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> f = es.submit(() -> {
            throw new RuntimeException("Exception inside callable");
        });

        ExecutionException ex = assertThrows(ExecutionException.class, f::get);
        assertEquals("Exception inside callable", ex.getCause().getMessage());
    }
}
