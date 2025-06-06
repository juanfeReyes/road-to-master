package hackerrank.interviewTraining.warmUp;

import hackerrank.interviewTraining.warmUp.CountingValleys;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CountingValleysTest {

  @Test
  public void test1(){
    String in = "UDDDUDUU";
    int result = CountingValleys.countingValleys(in.length(), in);

    assertThat(result).isEqualTo(1);
  }

}