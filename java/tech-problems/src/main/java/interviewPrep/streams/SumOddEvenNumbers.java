package interviewPrep.streams;

import java.util.List;
import java.util.Map;

public class SumOddEvenNumbers {

    public Map<String, Integer> solution(List<Integer> nums) {

        Integer even = nums.stream().filter((num) -> num % 2 == 0).reduce(0, Integer::sum);
        Integer odd = nums.stream().filter((num) -> num % 2 != 0).reduce(0, Integer::sum);


        return Map.of(
                "even", even,
                "odd", odd
        );
    }
}
