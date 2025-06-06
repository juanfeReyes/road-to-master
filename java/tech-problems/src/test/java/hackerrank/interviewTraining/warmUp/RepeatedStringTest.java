package hackerrank.interviewTraining.warmUp;

import hackerrank.interviewTraining.warmUp.RepeatedString;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RepeatedStringTest {

  @Test
  public void test1(){
    String in = "abcac";
    long n = 10;
    long result = RepeatedString.repeatedString(in, n);

    assertThat(result).isEqualTo(4);
  }

  @Test
  public void test2(){
    String in = "aba";
    long n = 10;
    long result = RepeatedString.repeatedString(in, n);

    assertThat(result).isEqualTo(7);
  }

  @Test
  public void test3(){
    String in = "a";
    long n = 1000000000000L;
    long result = RepeatedString.repeatedString(in, n);

    assertThat(result).isEqualTo(1000000000000L);
  }

  @Test
  public void test4(){
    String in = "epsxyyflvrrrxzvnoenvpegvuonodjoxfwdmcvwctmekpsnamchznsoxaklzjgrqruyzavshfbmuhdwwmpbkwcuomqhiyvuztwvq";
    long n = 549382313570L;
    long result = RepeatedString.repeatedString(in, n);

    assertThat(result).isEqualTo(16481469408L);
  }

}