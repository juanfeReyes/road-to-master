package leetCode.easy;

import java.util.*;

import java.util.stream.Collectors;

public class FindDisappearedNumbers {
  public List<Integer> findDisappearedNumbers(int[] nums) {
    int n = nums.length;
    List<Integer> missing = new ArrayList<>();
    Set<Integer> set = new HashSet<>(Arrays.stream(nums).boxed().collect(Collectors.toList()));
    for (int i = 1; i <= n; i++){
      if(!set.contains(i)){
        missing.add(i);
      }
    }
    return missing;
  }
}
