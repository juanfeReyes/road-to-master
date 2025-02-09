package leetCode.medium;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Combinations {

    List<List<Integer>> combinations;

    public List<List<Integer>> combine(int n, int k) {
        combinations = new ArrayList<>();
        int[] nums = IntStream.range(1, n+1).toArray();
        genCombine(nums, 0, k, new ArrayList<>());

        return combinations;
    }

    public void genCombine(int[] nums, int idx, int k, List<Integer> sub){

        if(sub.size() == k){
            combinations.add(sub);
            return;
        }

        for (int i = idx; i < nums.length; i++) {
            List<Integer> clone = new ArrayList<>(sub);
            clone.add(nums[i]);
            genCombine(nums, i+1, k, clone);
        }
    }
}
