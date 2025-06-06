package hackerrank.interviewTraining.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MinimumSwaps {

  static int minimumSwaps(int[] array) {
    int n = array.length - 1;
    int minSwaps = 0;
    for (int i = 0; i < n; i++) {
      if (i < array[i] - 1) {
        swap(array, i, Math.min(n, array[i] - 1));
        minSwaps++;
        i--;
      }
    }
    return minSwaps;
  }

  private static void swap(int[] array, int i, int j) {
    int temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }
}
