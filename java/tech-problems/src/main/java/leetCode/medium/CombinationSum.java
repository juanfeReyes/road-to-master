package leetCode.medium;

import java.util.ArrayList;
import java.util.List;

public class CombinationSum {
    List<List<Integer>> combinations;

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        combinations = new ArrayList<>();

        genCombination(candidates, target, 0, new ArrayList<>(), 0);

        return combinations;
    }

    public void genCombination(int[] candidates, int target, int idx, List<Integer> sub, int sum){

        if(sum > target){
            return;
        }

        if(sum == target){
            combinations.add(sub);
        }

        for (int i = idx; i < candidates.length; i++) {
            int current = candidates[i];
            int s = sum + current;
            List<Integer> aux = new ArrayList<>(sub);
            aux.add(current);
            genCombination(candidates, target, i, aux, s);
        }
    }
}
