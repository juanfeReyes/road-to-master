package leetCode.medium;

import org.junit.jupiter.api.Test;

public class CombinationsTest {

    @Test
    public void base(){

        Combinations sut = new Combinations();
        int n = 4;
        int k = 2;

        sut.combine(n, k);
    }
}
