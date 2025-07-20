package interviewPrep.streams;

import java.util.List;

public class ListOfStringToUpper {

    public List<String> solution(List<String> list){
        return list.stream()
                .map(String::toUpperCase)
                .toList();
    }
}
