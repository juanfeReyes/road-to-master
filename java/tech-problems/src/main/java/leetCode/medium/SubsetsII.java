package leetCode.medium;

import java.util.*;

public class SubsetsII {

    List<List<Integer>> permutations;
    Set<String> set;


    public List<List<Integer>> subsetsWithDup(int[] nums) {
        permutations = new ArrayList<>();
        set = new HashSet<>();

        Arrays.sort(nums);
        permute(nums, 0, new ArrayList<>());

        return permutations;
    }

    // how to avoid duplicate
    private void permute(int[] nums, int idx, List<Integer> sub) {
        if(set.contains(sub.toString())){
            return;
        }

        set.add(sub.toString());
        permutations.add(new ArrayList<>(sub));

        for (int i = idx; i < nums.length; i++) {
            List<Integer> cloneSub = new ArrayList<>(sub);
            int current = nums[i];
            cloneSub.add(current);
            permute(nums, i + 1, cloneSub);
        }
    }
}
