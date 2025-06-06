package leetCode.medium;

import org.junit.jupiter.api.Test;

public class CombinationSumIITest {


    @Test
    public void base(){
        CombinationSumII sut = new CombinationSumII();

        int[] candidates = new int[]{10,1,2,7,6,1,5};
        int target = 8;

        sut.combinationSum2(candidates, target);
    }

    @Test
    public void submit1(){
        // Slow scenario
        CombinationSumII sut = new CombinationSumII();

        int[] candidates = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
        int target = 30;

        sut.combinationSum2(candidates, target);
    }
}
