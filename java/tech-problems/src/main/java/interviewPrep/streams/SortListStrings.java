package interviewPrep.streams;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SortListStrings {

    public Map<String, List<String>> solution(List<String> list) {
        List<String> sortedAsc = list.stream().sorted(String::compareTo).toList();
        List<String> sortedDesc = list.stream().sorted(String::compareTo).collect(Collectors.collectingAndThen(
                Collectors.toList(),
                (l) -> {
                    Collections.reverse(l);
                    return l;
                }
        ));
        return Map.of(
                "asc", sortedAsc,
                "desc", sortedDesc
        );
    }
}
