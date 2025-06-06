package leetCode.easy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class SingleNumberTest {
  SingleNumber singleNumber = new SingleNumber();

  @Test
  public void test1(){
    int[] input = {2,2,1};
    assertThat(singleNumber.singleNumber(input)).isEqualTo(1);
  }

  @Test
  public void test2(){
    int[] input = {4,1,2,1,2};
    assertThat(singleNumber.singleNumber(input)).isEqualTo(4);
  }

  @Test
  public void test3(){
    int[] input = {1};
    assertThat(singleNumber.singleNumber(input)).isEqualTo(1);
  }
}