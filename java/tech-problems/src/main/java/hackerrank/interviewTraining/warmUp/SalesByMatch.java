package hackerrank.interviewTraining.warmUp;

import java.util.HashMap;
import java.util.List;

public class SalesByMatch {

  public static int sockMerchant(int n, List<Integer> ar) {
    int pairNumbers = 0;

    HashMap<Integer, Integer> counters = new HashMap<>();
    ar.stream().forEach((sock) -> {
      if(counters.containsKey(sock)){
        counters.put(sock, counters.get(sock)+1);
      }
      else{
        counters.put(sock, 1);
      }
    });

    for(Integer val : counters.values()){
      pairNumbers += val/2;
    }

    return pairNumbers;
  }
}
