package leetCode.medium;

import org.junit.jupiter.api.Test;

public class SubsetsTest {

    @Test
    public void base(){
        Subsets sut = new Subsets();
        int[] input = new int[]{1,2,3};

        sut.subsets(input);
    }

    @Test
    public void base2(){
        Subsets sut = new Subsets();
        int[] input = new int[]{0};

        sut.subsets(input);
    }
}
