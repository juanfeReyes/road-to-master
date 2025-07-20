package interviewPrep.streams;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FindNSmallestAndLargestTest {

    @Test
    void solution() {
        FindNSmallestAndLargest sut = new FindNSmallestAndLargest();
        Map<String, Integer> output = sut.solution(List.of(1, 2, 3, 4, 5), 2);

        assertEquals(2, output.get("smallest"));
        assertEquals(4, output.get("largest"));

    }
}