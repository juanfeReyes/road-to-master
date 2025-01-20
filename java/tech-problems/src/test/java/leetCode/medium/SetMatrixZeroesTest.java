package leetCode.medium;

import org.junit.jupiter.api.Test;

public class SetMatrixZeroesTest {

    @Test
    public void baseTest (){

        SetMatrixZeroes sol = new SetMatrixZeroes();
        int[][] input = new int[][]{
                {1,1,1},
                {1,0,1},
                {1,1,1},
                {1,1,1},
        };
        sol.setZeroes(input);
    }

    @Test
    public void zeroSameRowTest (){

        SetMatrixZeroes sol = new SetMatrixZeroes();
        int[][] input = new int[][]{
                {0,1,2,0},
                {3,4,5,2},
                {1,3,1,5}
        };
        sol.setZeroes(input);
    }
}
