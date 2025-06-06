package leetCode.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordSearchTest {

    @Test
    public void base(){
        WordSearch ws = new WordSearch();
        char[][] board = new char[][]{
                {'A','B','C','E'},
                {'S','F','C','S'},
                {'A','D','E','E'},
        };
        String word = "ABCCED";
        boolean exist = ws.exist(board, word);
        assertEquals(exist, true);
    }

    @Test
    public void baseShort(){
        WordSearch ws = new WordSearch();
        char[][] board = new char[][]{
                {'A','B','C','E'},
                {'S','F','C','S'},
                {'A','D','E','E'},
        };
        String word = "SEE";
        boolean exist = ws.exist(board, word);
        assertEquals(exist, true);
    }

    @Test
    public void baseFalse(){
        WordSearch ws = new WordSearch();
        char[][] board = new char[][]{
                {'A','B','C','E'},
                {'S','F','C','S'},
                {'A','D','E','E'},
        };
        String word = "ABCB";
        boolean exist = ws.exist(board, word);
        assertEquals(exist, false);
    }

    @Test
    public void submitWrong(){
        WordSearch ws = new WordSearch();
        char[][] board = new char[][]{
                {'A','B','C','E'},
                {'S','F','E','S'},
                {'A','D','E','E'},
        };
        String word = "ABCESEEEFS";
        boolean exist = ws.exist(board, word);
        assertEquals(true, exist);
    }
}
