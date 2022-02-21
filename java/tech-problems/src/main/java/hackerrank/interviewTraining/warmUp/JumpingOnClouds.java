package hackerrank.interviewTraining.warmUp;

import java.util.List;

public class JumpingOnClouds {

  public static int jumpingOnClouds(List<Integer> c) {
    int jumps = 0;
    // Write your code here
    for(int i = 0; i < c.size()-1; ){
      if(i<c.size()-2 && c.get(i+2)==0){
        i += 2;
        jumps++;
        continue;
      }
      i++;
      jumps++;
    }
    return jumps;
  }
}
