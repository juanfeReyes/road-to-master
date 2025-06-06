package leetCode.medium;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class WordSearch {

    /**
     * Slow but passed.
     * Backtracking is better (recursion)
     *
     * @param board
     * @param word
     * @return
     */
    public boolean exist(char[][] board, String word) {
        Stack<Node> nodes = new Stack<>();
        Set<Character> boardChars = new HashSet<>();
        boolean result = false;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(word.charAt(0) == board[i][j]){
                    nodes.push(new Node(i, j, board[i][j], 0, new HashSet<>()));
                }
                boardChars.add(board[i][j]);
            }
        }

        for (int i = 0; i < word.length(); i++) {
            if(!boardChars.contains(word.charAt(i))){
                return false;
            }
        }

        while(!nodes.isEmpty()){
            Node node = nodes.pop();
            if(node.wordIdx+1 == word.length()){
                return true;
            }

            char nextLetter = word.charAt(node.wordIdx+1);
            int up = node.i - 1;
            int down = node.i + 1;
            int left = node.j - 1;
            int right = node.j + 1;
            node.path.add(node.i+"-"+node.j);

            // check up
            if(up >= 0 && !node.path.contains(up+"-"+node.j)) {
                if(board[up][node.j] == nextLetter){

                    nodes.push(new Node(up, node.j, board[up][node.j], node.wordIdx+1, new HashSet<>(node.path)));
                }
            }

            // check down
            if(down < board.length && !node.path.contains(down+"-"+node.j) ) {
                if(board[down][node.j] == nextLetter){
                    nodes.push(new Node(down, node.j, board[down][node.j], node.wordIdx+1, new HashSet<>(node.path)));
                }
            }

            // check left
            if(left >= 0  && !node.path.contains(node.i+"-"+left)) {
                if(board[node.i][left] == nextLetter){
                    nodes.push(new Node(node.i, left, board[node.i][left], node.wordIdx+1, new HashSet<>(node.path)));
                }
            }

            // check right
            if(right < board[node.i].length && !node.path.contains(node.i+"-"+right)) {
                if (board[node.i][right] == nextLetter) {
                    nodes.push(new Node(node.i, right, board[node.i][right], node.wordIdx + 1, new HashSet<>(node.path)));
                }
            }
        }


        return result;
    }

    class Node {
        public int i;
        public int j;
        public char content;
        public int wordIdx;
        Set<String> path;

        public Node(int i, int j, char content, int wordIdx, Set<String> path){
            this.i = i;
            this.j = j;
            this.content = content;
            this.wordIdx = wordIdx;
            this.path = path;
        }
    }
}
