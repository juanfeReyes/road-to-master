package interviewPrep.streams;

import java.util.List;

public class RemoveDuplicates {

    public List<Integer> solution(List<Integer> nums){
        return nums.stream().distinct().toList();
    }
}
