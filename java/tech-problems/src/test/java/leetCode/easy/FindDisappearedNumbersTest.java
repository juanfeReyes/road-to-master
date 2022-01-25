package leetCode.easy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FindDisappearedNumbersTest {
  FindDisappearedNumbers findDisappearedNumbers = new FindDisappearedNumbers();

  @Test
  public void test1(){
    int[] input = {4,3,2,7,8,2,3,1};
    assertThat(findDisappearedNumbers.findDisappearedNumbers(input)).containsExactly(5,6);
  }

  @Test
  public void test2(){
    int[] input = {1,1};
    assertThat(findDisappearedNumbers.findDisappearedNumbers(input)).containsExactly(2);
  }
}