package interviewPrep.streams;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FirstNonRepetitiveLetterTest {

    @Test
    public void getLetter(){
        FirstNonRepetitiveLetter sut = new FirstNonRepetitiveLetter();
        String input = "abcab";
        String output = sut.solution(input);

        assertEquals("c", output);
    }

    @Test
    public void getOnlyFirst(){
        FirstNonRepetitiveLetter sut = new FirstNonRepetitiveLetter();
        String input = "abcabd";
        String output = sut.solution(input);

        assertEquals("c", output);
    }

    @Test
    public void getOnlyFirstUnordered(){
        FirstNonRepetitiveLetter sut = new FirstNonRepetitiveLetter();
        String input = "abdabc";
        String output = sut.solution(input);

        assertEquals("c", output);
    }
}