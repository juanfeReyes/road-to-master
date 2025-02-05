package leetCode.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LongestConsecutiveSequenceTest {

    @Test
    public void base(){
        LongestConsecutiveSequence lc = new LongestConsecutiveSequence();

        int[] input = new int[]{100,4,200,1,3,2};
        int sequence = lc.longestConsecutive(input);
        assertEquals(sequence, 4);
    }

    @Test
    public void base1(){
        LongestConsecutiveSequence lc = new LongestConsecutiveSequence();

        int[] input = new int[]{0,3,7,2,5,8,4,6,0,1};
        int sequence = lc.longestConsecutive(input);
        assertEquals(sequence, 9);
    }
}
