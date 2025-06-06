package hackerrank.interviewTraining.arrays;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MinimumSwapsTest {

  @Test
  public void test1(){
    int[] in = {4, 3, 1, 2};
    int result = MinimumSwaps.minimumSwaps(in);

    assertThat(result).isEqualTo(3);
  }

}