package leetCode.medium;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.com/problems/set-matrix-zeroes/
 */
public class SetMatrixZeroes {

    public void setZeroes(int[][] matrix) {
        List<Coordinate> zeros = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {
                    zeros.add(new Coordinate(i, j));
                }
            }
        }

        for (int w = 0; w < zeros.size(); w++) {
            Coordinate coord = zeros.get(w);
            int i = coord.row;
            int j = coord.col;

            for (int x = 0; x < matrix[i].length; x++) {
                matrix[i][x] = 0;
            }

            for (int x = 0; x < matrix.length; x++) {
                matrix[x][j] = 0;
            }
        }
    }

    class Coordinate {
        int row;
        int col;

        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}
