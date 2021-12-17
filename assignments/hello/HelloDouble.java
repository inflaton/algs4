public class HelloDouble {

  private static double NaN = Double.NaN;

  public static void main(String[] args) {

    System.out.println("NaN == 1 = " + (NaN == 1));
    System.out.println("NaN > 1 = " + (NaN > 1));
    System.out.println("NaN < 1 = " + (NaN < 1));
    System.out.println("NaN != 1 = " + (NaN != 1));
    System.out.println("NaN == NaN = " + (NaN == NaN));
    System.out.println("NaN > NaN = " + (NaN > NaN));
    System.out.println("NaN < NaN = " + (NaN < NaN));
    System.out.println("NaN != NaN = " + (NaN != NaN));

    testValue((double) 100);
    testValue(Double.POSITIVE_INFINITY);
    testValue(Double.NEGATIVE_INFINITY);
    testValues(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

    testValues(0.0, -0.0);
    testValue(NaN);
  }

  private static void testValue(double v) {
    testValues(v, v);
  }

  private static void testValues(double a, double b) {
    System.out.println("a = " + a);
    System.out.println("b = " + b);
    Double x = Double.valueOf(a);
    Double y = Double.valueOf(b);
    System.out.println("a == b = " + (a == b));
    System.out.println("x.equals(y) = " + (x.equals(y)));
  }
}
