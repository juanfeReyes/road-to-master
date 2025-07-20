package interviewPrep.streams;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FindMinAndMaxTest {

    @Test
    void solution() {
        FindMinAndMax sut = new FindMinAndMax();
        Map<String, Integer> output = sut.solution(List.of(1, 2, 3, 10, 5));

        assertEquals(1, output.get("min"));
        assertEquals(10, output.get("max"));
    }
}