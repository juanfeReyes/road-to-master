package interviewPrep.streams;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListOfStringToUpperTest {

    @Test
    void solution() {
        ListOfStringToUpper sut = new ListOfStringToUpper();
        List<String> output = sut.solution(List.of("abc", "wer"));
        assertEquals("ABC", output.get(0));
        assertEquals("WER", output.get(1));
    }
}