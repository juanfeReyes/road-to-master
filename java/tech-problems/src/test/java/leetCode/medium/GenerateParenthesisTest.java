package leetCode.medium;

import org.junit.jupiter.api.Test;

public class GenerateParenthesisTest {

    @Test
    public void base(){
        GenerateParenthesis sut = new GenerateParenthesis();
        int n = 3;

        sut.generateParenthesis(n);
    }
}
