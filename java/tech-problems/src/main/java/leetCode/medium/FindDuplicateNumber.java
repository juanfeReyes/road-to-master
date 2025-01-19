package leetCode.medium;

/**
 * https://leetcode.com/problems/find-the-duplicate-number/
 */
public class FindDuplicateNumber {

    public int findDuplicate(int[] nums) {
        int[] counter = new int[nums.length+1];

        for (int i = 0; i < nums.length; i++) {
            counter[nums[i]]++;
            if(counter[nums[i]] == 2) {
                return nums[i];
            }
        }

        return -1;
    }
}
