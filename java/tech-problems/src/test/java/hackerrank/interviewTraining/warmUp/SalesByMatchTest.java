package hackerrank.interviewTraining.warmUp;

import hackerrank.interviewTraining.warmUp.SalesByMatch;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SalesByMatchTest {

  @Test
  public void test1(){
    List<Integer> in = List.of(1,2,1,2,1,3,2);
    var result = SalesByMatch.sockMerchant(in.size(), in);

    assertThat(result).isEqualTo(2);
  }

}