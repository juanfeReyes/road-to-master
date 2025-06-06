package hackerrank.interviewTraining.warmUp;

import java.util.Arrays;

public class CountingValleys {

  public static int countingValleys(int steps, String path) {
    // Write your code here
    int valleyCounter = 0;
    int seaLevel = 0;
    boolean inValley = false;

    String[] arrPath = path.split("");
    for(String step: arrPath){
      if(step.equals("U")){
        seaLevel++;
      }else{
        seaLevel--;
      }
      if(!inValley && seaLevel == -1){
        valleyCounter++;
        inValley = true;
      }
      if(inValley && seaLevel==0){
        inValley=false;
      }
    }

    return valleyCounter;
  }
}
