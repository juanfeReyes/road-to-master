package leetCode.medium;

import java.util.*;

public class LongestConsecutiveSequence {

    public int longestConsecutive(int[] nums) {
        int n = nums.length;

        if (n == 0) {
            return 0;
        }

        // Better just to sort
        Arrays.sort(nums);

        int cnt = 1;
        int maxi = 0;

        for (int i = 1; i < n; i++) {
            if (nums[i] != nums[i - 1]) {
                if (nums[i] == nums[i - 1] + 1) {
                    cnt++;
                } else {
                    maxi = Math.max(maxi, cnt);
                    cnt = 1;
                }
            }
        }

        return Math.max(maxi, cnt);
    }
}
