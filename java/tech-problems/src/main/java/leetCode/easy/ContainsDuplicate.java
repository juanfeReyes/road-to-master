package leetCode.easy;

import java.util.HashMap;

public class ContainsDuplicate {

  public boolean containsDuplicate(int[] nums) {
    HashMap<Integer, Integer> count = new HashMap<>();
    for (int num: nums){
      if(count.containsKey(num)){
        count.replace(num, count.get(num)+1);
      }else{
        count.put(num, 1);
      }
      if(count.get(num)>1){
        return true;
      }
    }

    return false;
  }
}
