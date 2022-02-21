package hackerrank.interviewTraining.arrays;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LeftShitTest {


  @Test
  public void test1(){
    List<Integer> a = List.of(1, 2, 3, 4, 5);
    int shift = 4;
    List<Integer> result = LeftShit.rotLeft(a, shift);

    assertThat(result).isEqualTo(List.of(5, 1, 2, 3, 4));
  }
}