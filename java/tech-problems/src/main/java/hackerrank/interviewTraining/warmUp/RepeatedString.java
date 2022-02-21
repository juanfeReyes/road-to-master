package hackerrank.interviewTraining.warmUp;

public class RepeatedString {

  public static long repeatedString(String s, long n) {
    long result = 0L;
    //Get ocurrences of a for substring
    long occurrences = s.chars().filter(ch -> ch == 'a').count();

    //Get whole factor (n / s.length)
    long wholeFactor = n / s.length();

    result += occurrences * wholeFactor;

    // if residual (n % s.length > 0)
    long residual =  n % s.length();
    if(residual > 0){
      // substring s(0,residual)
      String residualString = s.substring(0, (int) residual);

      //ocurrences of residual = oor
      long residualOcurrences = residualString.chars().filter(ch -> ch == 'a').count();

      //result += oor
      result += residualOcurrences;
    }

    // Write your code here
    return result;
  }
}
