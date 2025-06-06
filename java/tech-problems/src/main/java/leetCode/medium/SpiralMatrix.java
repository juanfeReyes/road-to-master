package leetCode.medium;

import java.util.ArrayList;
import java.util.List;

public class SpiralMatrix {

    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        Direction direction = Direction.RIGHT;
        int maxItems = matrix.length*matrix[0].length;
        int limitUp = 0;
        int limitDown = matrix.length-1;
        int limitLeft = 0;
        int limitRight = matrix[0].length-1;
        int i = 0;
        int j = 0;
        for(int n = 0; n < maxItems; n++){
            if(j == limitRight && direction == Direction.RIGHT) {
                direction = Direction.DOWN;
                limitUp++;
            } else if (i == limitDown && direction == Direction.DOWN) {
                direction = Direction.LEFT;
                limitRight--;
            } else if (j == limitLeft && direction == Direction.LEFT) {
                direction = Direction.UP;
                limitDown--;
            }else if(i == limitUp && direction == Direction.UP) {
                direction = Direction.RIGHT;
                limitLeft++;
            }

            result.add(matrix[i][j]);
            if(direction == Direction.RIGHT) {
                j++;
            } else if (direction == Direction.DOWN) {
                i++;
            } else if (direction == Direction.LEFT) {
                j--;
            } else {
                i--;
            }


        }

        return result;
    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}
