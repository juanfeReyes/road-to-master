package interviewPrep.streams;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountStringsThatStartWithTest {

    @Test
    void solution() {
        CountStringsThatStartWith sut = new CountStringsThatStartWith();
        Long count = sut.solution(List.of("ab234", "a345"), "ab");
        assertEquals(1, count);
    }

    @Test
    void solution1() {
        CountStringsThatStartWith sut = new CountStringsThatStartWith();
        Long count = sut.solution(List.of("ab234", "a345"), "a");
        assertEquals(2, count);
    }
}