package leetCode.easy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClimbingStairsTest {
  ClimbingStairs climbingStairs = new ClimbingStairs();

  @Test
  public void test1(){
    int input = 2;
    assertThat(climbingStairs.climbStairs(input)).isEqualTo(2);
  }

  @Test
  public void test2(){
    int input = 3;
    assertThat(climbingStairs.climbStairs(input)).isEqualTo(3);
  }
}