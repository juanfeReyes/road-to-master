package interviewPrep.streams;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AverageIntValueTest {

    @Test
    void solution() {
        AverageIntValue sut = new AverageIntValue();
        double output = sut.solution(List.of(1, 2, 3, 4));
        assertEquals(2.5, output);
    }
}