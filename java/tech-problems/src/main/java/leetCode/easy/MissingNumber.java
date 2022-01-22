package leetCode.easy;

import java.util.Arrays;

public class MissingNumber {
  public int missingNumber(int[] nums) {
    Arrays.sort(nums);
    for(int i = 0; i < nums.length-1; i++){
      if(nums[i+1]-nums[i]>1){
        return nums[i]+1;
      }
    }
    if(nums[0]>0){
      return nums[0]-1;
    }
    return nums[nums.length-1]+1;
  }
}
