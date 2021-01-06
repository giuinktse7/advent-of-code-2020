import java.io.IOException;
import java.util.stream.Collectors;

class Part1 {
  public static final long mod = 20201227;

  public static long transform(long subject, long value) {
    value *= subject;
    return value < mod ? value : value % mod;
  }

  public static long loopSize(long publicKey) {
    long loops = 0;
    long value = 1;
    while (value != publicKey) {
      value = transform(7, value);
      ++loops;
    }

    return loops;
  }

  public static long transformN(long subject, long loops) {
    long value = 1;
    for (int i = 0; i < loops; ++i) {
      value = transform(subject, value);
    }

    return value;
  }

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").collect(Collectors.toList());

    long publicKey1 = Long.parseLong(data.get(0));
    long publicKey2 = Long.parseLong(data.get(1));

    var loop1 = loopSize(publicKey1);

    var t1 = transformN(publicKey2, loop1);

    System.out.println("Encryption key: " + t1);
    System.out.println("Done");
  }
}