package leetCode.easy;

public class ProductofArrayExceptSelf {
    public int[] productExceptSelf(int[] nums) {
        int[] prefixes = new int[nums.length];
        int[] suffixes = new int[nums.length];
        int[] result = new int[nums.length];

        prefixes[0] = nums[0];
        suffixes[nums.length-1] = nums[nums.length-1];

        for (int i = 1, j = nums.length-2; i < nums.length-1; i++, j--) {
            prefixes[i] = prefixes[i-1]*nums[i];
            suffixes[j] = suffixes[j+1]*nums[j];
        }

        for ( int i = 0; i < nums.length; i++) {
            if(i == 0) {
                result[i]=suffixes[i+1];
            }
            else if(i == nums.length -1) {
                result[i]=prefixes[i-1];
            }
            else {
                result[i] = prefixes[i-1]*suffixes[i+1];
            }
        }

        return result;
    }
}
