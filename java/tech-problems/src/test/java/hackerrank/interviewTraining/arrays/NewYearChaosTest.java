package hackerrank.interviewTraining.arrays;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NewYearChaosTest {

  @Test
  public void test1() throws IOException {
    List<Integer> in = List.of(2, 1, 5, 3, 4);
    NewYearChaos.minimumBribes(in);
    //Ans: 3
  }

  @Test
  public void test2() throws IOException {
    List<Integer> in = List.of(2, 5, 1, 3, 4);
    NewYearChaos.minimumBribes(in);
    //Ans: Too chaotic
  }

  @Test
  public void test3() throws IOException {
    List<Integer> in = List.of(1, 2, 5, 3, 7, 8, 6, 4);
    NewYearChaos.minimumBribes(in);
    //Ans: 7
  }

}