package leetCode.medium;

import org.junit.jupiter.api.Test;

public class LetterCasePermutationsTest {

    @Test
    public void base(){
        LetterCasePermutation sut = new LetterCasePermutation();
        String input = "a1b2";

        sut.letterCasePermutation(input);
    }

    @Test
    public void base2(){
        LetterCasePermutation sut = new LetterCasePermutation();
        String input = "3z4";

        sut.letterCasePermutation(input);
    }

    @Test
    public void base3(){
        LetterCasePermutation sut = new LetterCasePermutation();
        String input = "12345";

        sut.letterCasePermutation(input);
    }
}
