package leetCode.easy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ContainsDuplicateTest {
  ContainsDuplicate containsDuplicate = new ContainsDuplicate();

  @Test
  public void test1(){
    int[] input = {1,2,3,1};
    assertThat(containsDuplicate.containsDuplicate(input)).isFalse();
  }

  @Test
  public void test2(){
    int[] input = {1,2,3,4};
    assertThat(containsDuplicate.containsDuplicate(input)).isFalse();
  }

  @Test
  public void test3(){
    int[] input = {1,1,1,3,3,4,3,2,4,2};
    assertThat(containsDuplicate.containsDuplicate(input)).isTrue();
  }

  @Test
  public void test4(){
    int[] input = {1,2,3,1};
    assertThat(containsDuplicate.containsDuplicate(input)).isTrue();
  }
}
