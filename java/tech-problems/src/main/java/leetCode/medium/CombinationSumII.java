package leetCode.medium;

import java.util.*;

public class CombinationSumII {
    List<List<Integer>> combinations;
    Set<String> set;

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        combinations = new ArrayList<>();
        set = new HashSet<>();

        Arrays.sort(candidates);
        genCombination(candidates, target, 0, new ArrayList<>(), 0);

        return combinations;
    }

    public void genCombination(int[] candidates, int target, int idx, List<Integer> sub, int sum){

        if(sum > target){
            return;
        }

        if(sum == target && !set.contains(sub.toString())){
            set.add(sub.toString());
            combinations.add(sub);
        }

        set.add(sub.toString());
        for (int i = idx; i < candidates.length; i++) {
            int current = candidates[i];
            int s = sum + current;
            List<Integer> aux = new ArrayList<>(sub);
            aux.add(current);
            if(!set.contains(aux.toString())) {
                genCombination(candidates, target, i + 1, aux, s);
            }
        }
    }
}
