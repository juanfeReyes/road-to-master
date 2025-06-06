package leetCode.medium;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LetterCasePermutation {

    List<String> permutations;
    Set<String> set;

    public List<String> letterCasePermutation(String s) {
        permutations = new ArrayList<>();
        set = new HashSet<>();
        permute(s, 0);

        return permutations;
    }

    private void permute(String s, int idx) {
        if(idx >= s.length()){
            return ;
        }

        
        StringBuilder low = new StringBuilder(s);
        low.setCharAt(idx, Character.toLowerCase(low.charAt(idx)));
        if (!set.contains(low.toString())) {
            permutations.add(low.toString());
            set.add(low.toString());
            for (int i = idx + 1; i < s.length(); i++) {
                permute(low.toString(), i);
            }
        }

        StringBuilder upp = new StringBuilder(s);
        upp.setCharAt(idx, Character.toUpperCase(upp.charAt(idx)));
        if (!set.contains(upp.toString())) {
            permutations.add(upp.toString());
            set.add(upp.toString());
            for (int i = idx + 1; i < s.length(); i++) {
                permute(upp.toString(), i);
            }
        }
    }
}
