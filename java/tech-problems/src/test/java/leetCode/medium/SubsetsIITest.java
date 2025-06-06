package leetCode.medium;

import org.junit.jupiter.api.Test;

public class SubsetsIITest {

    @Test
    public void base(){
        SubsetsII sut = new SubsetsII();
        int[] input = new int[]{1,2,2};

        sut.subsetsWithDup(input);
    }

    @Test
    public void submit1(){
        SubsetsII sut = new SubsetsII();
        int[] input = new int[]{4,4,4,1,4};

        sut.subsetsWithDup(input);

        /**
         * Expected:
         * [[],[1],[1,4],[1,4,4],[1,4,4,4],[1,4,4,4,4],[4],[4,4],[4,4,4],[4,4,4,4]]
         */
    }
}
