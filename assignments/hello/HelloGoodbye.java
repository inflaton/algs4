public class HelloGoodbye {

  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("must have 2 command-line arguments");
      return;
    }

    System.out.println(String.format("Hello %1s and %2s.", args[0], args[1]));
    System.out.println(String.format("Goodbye %1s and %2s.", args[1], args[0]));
  }
}
