package leetCode.medium;

import org.junit.jupiter.api.Test;

public class SpiralMatrixTest {

    @Test
    public void base(){
        SpiralMatrix spiralMatrix = new SpiralMatrix();
        int[][] input = new int[][]{
                {1,2,3},
                {4,5,6},
                {7,8,9},
        };

        spiralMatrix.spiralOrder(input);
    }

    @Test
    public void base1(){
        SpiralMatrix spiralMatrix = new SpiralMatrix();
        int[][] input = new int[][]{
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12},
        };

        spiralMatrix.spiralOrder(input);
    }
}
