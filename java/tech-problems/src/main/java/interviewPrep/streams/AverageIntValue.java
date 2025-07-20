package interviewPrep.streams;

import java.util.List;
import java.util.stream.Collectors;

public class AverageIntValue {

    public double solution(List<Integer> nums) {
        return nums.stream().mapToDouble(i -> i).average().orElse(0.0);
    }
}
