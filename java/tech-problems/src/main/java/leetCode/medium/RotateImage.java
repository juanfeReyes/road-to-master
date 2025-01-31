package leetCode.medium;

public class RotateImage {

    public void rotate(int[][] matrix) {
        int max = matrix.length - 1;
        int mid = matrix.length / 2;


        for (int row = 0; row < mid; row++) {
            for (int j = row; j < max; j++) {
                int temp1 = matrix[row][j];
                int temp2 = matrix[j][max];
                int temp3 = matrix[max][max-j+row];
                int temp4 = matrix[max-j+row][row];
                matrix[row][j] = temp4;
                matrix[j][max] = temp1;
                matrix[max][max-j+row] = temp2;
                matrix[max-j+row][row] = temp3;
            }
            max--;
        }
    }
}

/**
 * 00 -> 02
 * 02 -> 22
 * 22 -> 20
 * 20 -> 00
 * <p>
 * <p>
 * 01 -> 12
 * 12 -> 21
 * 21 -> 10
 * 10 -> 01
 */