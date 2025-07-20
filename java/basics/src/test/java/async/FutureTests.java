package async;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTests {


    @Test
    public void futureShouldRun() throws ExecutionException, InterruptedException {
        // Future is a result of an async computation
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> f = es.submit(() -> "Future test");

        // get is blocking by waiting until async call completes
        assertEquals("Future test", f.get());

        es.shutdown();
    }

    @Test
    public void completableFutureShouldRun() throws ExecutionException, InterruptedException {
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> "CompletableFuture");

        // Non-blocking methods to chain other commands
        CompletableFuture<String> cfChain = cf.thenApply((s) -> s+" test");

        assertEquals("CompletableFuture test", cfChain.get());
    }

    @Test
    public void completableFutureCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> "CompletableFuture");
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> " test");

        CompletableFuture<String> cf12 = cf1.thenCombine(cf2, (s1, s2) -> s1+s2);
        assertEquals("CompletableFuture test", cf12.get());
    }

    @Test
    public void completableFutureParallelExecution() throws ExecutionException, InterruptedException {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> "CompletableFuture");
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> " test");

        CompletableFuture<Void> allCfs = CompletableFuture.allOf(cf1, cf2);

        allCfs.get();
        assertTrue(cf1.isDone());
        assertTrue(cf2.isDone());
        assertEquals("CompletableFuture test", cf1.get() + cf2.get());
    }

    @Test
    public void completableFutureExceptionHandling() {
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Exception inside Completable future");
        });

        ExecutionException ex =  assertThrows(ExecutionException.class, cf::get);
        assertEquals("Exception inside Completable future", ex.getCause().getMessage());
    }
}
