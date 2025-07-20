package interviewPrep.streams;

import java.util.List;
import java.util.Map;

public class FindNSmallestAndLargest {

    public Map<String, Integer> solution(List<Integer> nums, int n){

        List<Integer> sorted = nums.stream().sorted(Integer::compareTo).toList();

        return Map.of(
          "smallest", sorted.get(n-1),
          "largest",  sorted.get(sorted.size()-n)
        );
    }
}
