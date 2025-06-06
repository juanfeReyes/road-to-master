package leetCode.easy;

import java.util.HashMap;
import java.util.Map;

public class ClimbingStairs {

  private Map<Integer, Integer> results = new HashMap();
  public int climbStairs(int n) {
    makeStep(n);
    return makeStep(n);
  }

  private int makeStep(int n){
    if(n <= 2 ){
      return n;
    }
    if(!results.containsKey(n)){
      results.put(n, makeStep(n - 1) + makeStep(n- 2));
    }

    return results.get(n).intValue();
  }
}
