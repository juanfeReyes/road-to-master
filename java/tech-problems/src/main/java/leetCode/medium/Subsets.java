package leetCode.medium;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Subsets {

    List<List<Integer>> permutations;

    public List<List<Integer>> subsets(int[] nums) {
        permutations = new ArrayList<>();

        permute(nums, 0, new ArrayList<>());

        return permutations;
    }

    private void permute(int[] nums, int idx, List<Integer> sub){
        permutations.add(new ArrayList<>(sub));

        for (int i = idx; i < nums.length; i++) {
            List<Integer> cloneSub = new ArrayList<>(sub);
            int current = nums[i];
            cloneSub.add(current);
            permute(nums, i+1, cloneSub);
        }
    }
}
