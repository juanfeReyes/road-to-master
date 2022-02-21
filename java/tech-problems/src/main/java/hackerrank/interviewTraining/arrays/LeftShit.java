package hackerrank.interviewTraining.arrays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeftShit {

  public static List<Integer> rotLeft(List<Integer> a, int d) {
    List<Integer> rotated = new ArrayList<>(a);
    Collections.rotate(rotated, a.size()-d);
    // Write your code here
    return rotated;
  }
}
