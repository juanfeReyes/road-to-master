package leetCode.easy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BestTimeToBuyAndSellStockTest {
  BestTimeToBuyAndSellStock bestTimeToBuyAndSellStock = new BestTimeToBuyAndSellStock();

  @Test
  public void Test1(){
    int[] input = {7,1,5,3,6,4};
    assertThat(bestTimeToBuyAndSellStock.maxProfit(input)).isEqualTo(5);
  }

  @Test
  public void Test2(){
    int[] input = {7,6,4,3,1};
    assertThat(bestTimeToBuyAndSellStock.maxProfit(input)).isEqualTo(0);
  }

  @Test
  public void Test3(){
    int[] input = {3,3,5,0,0,3,1,4};
    assertThat(bestTimeToBuyAndSellStock.maxProfit(input)).isEqualTo(4);
  }

  @Test
  public void Test4(){
    int[] input = {2,4,1};
    assertThat(bestTimeToBuyAndSellStock.maxProfit(input)).isEqualTo(2);
  }

  @Test
  public void Test5(){
    int[] input = {3,2,6,5,0,3};
    assertThat(bestTimeToBuyAndSellStock.maxProfit(input)).isEqualTo(4);
    //
  }
}