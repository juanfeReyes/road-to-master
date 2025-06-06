package leetCode.medium;

import org.junit.jupiter.api.Test;

public class RotateImageTest {

    @Test
    public void base(){
        RotateImage rotateImage = new RotateImage();
        int[][] input = new int[][]{
                {1,2,3},
                {4,5,6},
                {7,8,9},
        };
        rotateImage.rotate(input);
        String aux = "";
    }

    @Test
    public void larger(){
        RotateImage rotateImage = new RotateImage();
        int[][] input = new int[][]{
                {5,1,9,11},
                {2,4,8,10},
                {13,3,6,7},
                {15,14,12,16},
        };
        rotateImage.rotate(input);
        String aux = "";
    }
}
