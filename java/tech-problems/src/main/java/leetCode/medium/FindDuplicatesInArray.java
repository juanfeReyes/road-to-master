package leetCode.medium;

import java.util.ArrayList;
import java.util.List;

public class FindDuplicatesInArray {
    public List<Integer> findDuplicates(int[] nums) {
        int[] counter = new int[nums.length+1];
        List<Integer> result = new ArrayList<>();

        for ( int i = 0; i < nums.length; i++) {
            int index = nums[i];
            counter[index]++;
            if(counter[index] == 2){
                result.add(index);
            }
        }

        return result;
    }
}
