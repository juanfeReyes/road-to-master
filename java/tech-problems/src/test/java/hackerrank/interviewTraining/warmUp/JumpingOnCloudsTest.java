package hackerrank.interviewTraining.warmUp;

import hackerrank.interviewTraining.warmUp.JumpingOnClouds;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JumpingOnCloudsTest {

  @Test
  public void test1(){
    List<Integer> in = List.of(0,1,0,0,0,1,0);
    int result = JumpingOnClouds.jumpingOnClouds(in);

    assertThat(result).isEqualTo(3);
  }

  @Test
  public void test2(){
    List<Integer> in = List.of(0,0,1,0,0,1,0);
    int result = JumpingOnClouds.jumpingOnClouds(in);

    assertThat(result).isEqualTo(4);
  }

}