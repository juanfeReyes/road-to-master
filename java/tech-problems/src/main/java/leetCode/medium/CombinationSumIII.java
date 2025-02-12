package leetCode.medium;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CombinationSumIII {

    List<List<Integer>> combinations;

    public List<List<Integer>> combinationSum3(int k, int n) {
        combinations = new ArrayList<>();
        int[] nums = IntStream.range(1, 10).toArray();

        genCombination(nums, k, n, 0, new ArrayList<>(), 0);
        return combinations;
    }

    public void genCombination(int[] candidates, int maxSize, int target, int idx, List<Integer> sub, int sum) {
        if (sum > target) {
            return;
        }

        if (sum == target && sub.size() == maxSize) {
            combinations.add(sub);
        }

        for (int i = idx; i < candidates.length; i++) {
            int current = candidates[i];
            int s = sum + current;
            List<Integer> aux = new ArrayList<>(sub);
            aux.add(current);
            genCombination(candidates, maxSize, target, i + 1, aux, s);

        }
    }
}
