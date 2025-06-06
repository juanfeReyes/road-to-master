package hackerrank.interviewTraining.arrays;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TwoDArraysTest {

  @Test
  public void test1(){
    List<List<Integer>> in = List.of(
        List.of(1, 1, 1, 0, 0, 0),
        List.of(0, 1, 0, 0, 0, 0),
        List.of(1, 1, 1, 0, 0, 0),
        List.of(0, 0, 2, 4, 4, 0),
        List.of(0, 0, 0, 2, 0, 0),
        List.of(0, 0, 1, 2, 4, 0)
    );
    int max = TwoDArrays.hourglassSum(in);

    assertThat(max).isEqualTo(19);
  }

}