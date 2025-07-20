package interviewPrep.streams;

import java.util.List;
import java.util.Map;

public class FindMinAndMax {

    public Map<String, Integer> solution(List<Integer> nums) {

        Integer min = nums.stream().min(Integer::compareTo).orElse(null);
        Integer max = nums.stream().max(Integer::compareTo).orElse(null);


        return Map.of(
                "min", min,
                "max", max
        );
    }
}
