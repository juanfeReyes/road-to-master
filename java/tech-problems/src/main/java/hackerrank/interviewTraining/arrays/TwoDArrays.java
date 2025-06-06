package hackerrank.interviewTraining.arrays;

import java.util.List;

public class TwoDArrays {

  private static int sumFrame(int refi, int refj, List<List<Integer>> arr){
    int a = arr.get(refi-1).get(refj-1);
    int b = arr.get(refi-1).get(refj);
    int c = arr.get(refi-1).get(refj+1);
    int d = arr.get(refi).get(refj);
    int e = arr.get(refi+1).get(refj-1);
    int f = arr.get(refi+1).get(refj);
    int g = arr.get(refi+1).get(refj+1);
    return a+b+c+d+e+f+g;
  }

  public static int hourglassSum(List<List<Integer>> arr) {
    int max = Integer.MIN_VALUE;
    for(int i = 1; i < arr.size()-1; i++){
      for(int j = 1; j < arr.size()-1; j++){
        int hourGlass = TwoDArrays.sumFrame(i,j,arr);
        if(hourGlass>max){ max = hourGlass; }
      }
    }
    // Write your code here
    return max;
  }
}
