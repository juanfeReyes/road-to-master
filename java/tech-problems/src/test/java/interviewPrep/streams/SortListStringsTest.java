package interviewPrep.streams;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SortListStringsTest {

    @Test
    public void solution1(){
        SortListStrings sut = new SortListStrings();
        Map<String, List<String>> output = sut.solution(List.of("abc", "xyz"));
        assertEquals(2, output.get("asc").size());
    }

}