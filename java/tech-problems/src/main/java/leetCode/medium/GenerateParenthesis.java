package leetCode.medium;

import java.util.ArrayList;
import java.util.List;

public class GenerateParenthesis {

    List<String> parenthesis;

    public List<String> generateParenthesis(int n) {
        parenthesis = new ArrayList<>();
        backtrack(n, new StringBuilder(""), 0, 0);
        return parenthesis;
    }

    private void backtrack(int n, StringBuilder s, int open, int close){

        if(s.length() == n*2){
            parenthesis.add(s.toString());
            return;
        }

        if(open < n){
            StringBuilder aux = new StringBuilder(s);
            aux.append("(");
            backtrack(n, aux, open+1, close);
        }

        if(close < open){
            StringBuilder aux = new StringBuilder(s);
            aux.append(")");
            backtrack(n, aux, open, close+1);
        }
    }
}
