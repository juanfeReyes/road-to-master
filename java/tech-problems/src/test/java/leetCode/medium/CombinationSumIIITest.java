package leetCode.medium;

import org.junit.jupiter.api.Test;

public class CombinationSumIIITest {

    @Test
    public void base(){
        CombinationSumIII sut = new CombinationSumIII();
        int k = 3;
        int n = 7;

        sut.combinationSum3(k, n);
    }

    @Test
    public void base2(){
        CombinationSumIII sut = new CombinationSumIII();
        int k = 3;
        int n = 9;

        sut.combinationSum3(k, n);
    }
}
