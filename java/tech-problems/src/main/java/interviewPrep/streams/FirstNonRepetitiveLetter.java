package interviewPrep.streams;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FirstNonRepetitiveLetter {

    public String solution(String word){

        Stream<String> ss = word.chars()
                .mapToObj((c) -> String.valueOf((char) c));
        Map<String, Long> sSum = ss.collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        return sSum.entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .sorted()
                .toList()
                .get(0);
    }
}
