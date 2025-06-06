package leetCode.easy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MissingNumberTest {
  MissingNumber missingNumber = new MissingNumber();

  @Test
  public void test1(){
    int[] input = {3,0,1};
    assertThat(missingNumber.missingNumber(input)).isEqualTo(2);
  }

  @Test
  public void test2(){
    int[] input = {0,1};
    assertThat(missingNumber.missingNumber(input)).isEqualTo(2);
  }

  @Test
  public void test3(){
    int[] input = {9,6,4,2,3,5,7,0,1};
    assertThat(missingNumber.missingNumber(input)).isEqualTo(8);
  }

  @Test
  public void test4(){
    int[] input = {1};
    assertThat(missingNumber.missingNumber(input)).isEqualTo(0);
  }
}
