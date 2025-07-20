package async;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConcurrentCollectionsTests {

    // TODO: check read syncs

    @Test
    public void concurrentList() throws InterruptedException {
        CopyOnWriteArrayList<Integer> set = new CopyOnWriteArrayList<>();
        ExecutorService es = Executors.newFixedThreadPool(4);
        es.submit(() -> set.add(1));
        es.submit(() -> set.add(2));
        es.submit(() -> set.add(1));

        es.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(3, set.size());
    }

    @Test
    public void concurrentSet() throws InterruptedException {
        CopyOnWriteArraySet<Integer> set = new CopyOnWriteArraySet<>();
        ExecutorService es = Executors.newFixedThreadPool(4);
        es.submit(() -> set.add(1));
        es.submit(() -> set.add(2));
        es.submit(() -> set.add(1));

        es.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(2, set.size());
    }

    @Test
    public void concurrentMap() throws InterruptedException {
        ConcurrentMap<String, Integer> wordCounter = new ConcurrentHashMap<>();
        ExecutorService es = Executors.newFixedThreadPool(4);
        es.submit(() -> wordCounter.merge("hi", 1, Integer::sum));
        es.submit(() -> wordCounter.merge("hi", 1, Integer::sum));
        es.submit(() -> wordCounter.merge("test", 1, Integer::sum));

        es.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(2, wordCounter.size());
        assertEquals(2, wordCounter.get("hi"));
        assertEquals(1, wordCounter.get("test"));
    }
}
