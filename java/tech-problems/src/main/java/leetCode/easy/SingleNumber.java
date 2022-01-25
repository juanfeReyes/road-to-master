package leetCode.easy;

import java.util.Arrays;

public class SingleNumber {
  public int singleNumber(int[] nums) {
    Arrays.sort(nums);
    int ref = nums[0];
    boolean duplicatedTest = true;
    for(int i = 1; i < nums.length; i++){
      int actual = nums[i];
      // if not duplicatedTest then ref = actual
      if(!duplicatedTest){
        ref = actual;
      }
      // if duplicatedTest and is different then it should return ref
      if(duplicatedTest && ref != actual){
        return  ref;
      }
      duplicatedTest = !duplicatedTest;
    }
    return ref;
  }
}

// ref = 1
// 1 2 2


// 1 1 2 2 4