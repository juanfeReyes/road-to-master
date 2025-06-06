package leetCode.medium;

import org.junit.jupiter.api.Test;

public class CombinationSumTest {

    @Test
    public void base(){
        CombinationSum sut = new CombinationSum();
        int[] candidates = new int[]{2,3,6,7};
        int target = 7;

        sut.combinationSum(candidates, target);
    }

}
