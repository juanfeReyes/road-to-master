package interviewPrep.streams;

import java.util.List;

public class CountStringsThatStartWith {

    public Long solution(List<String> list, String prefix){

        return list.stream()
                .filter(s -> s.startsWith(prefix))
                .count();
    }
}
