import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

class Verifier {
  Verifier(int size) {
    values = new long[size];
    this.size = size;
  }

  public void add(long value) {
    values[end] = value;
    start = (start + 1) % size;
    if (!looped && (end + 1) >= size) {
      looped = true;
    }

    end = (end + 1) % size;
  }

  public long at(int index) {
    if (index >= size)
      throw new RuntimeException("Bug");

    return values[(start + index) % size];
  }

  public boolean verify(long value) {
    HashSet<Long> search = new HashSet<>();

    int endI = looped ? size : end;
    for (int i = 0; i < endI; ++i) {
      long s1 = values[i];

      if (search.contains(s1))
        return true;

      long s2 = value - s1;
      if (s2 >= 0)
        search.add(s2);
    }

    return false;
  }

  private int start = 0;
  private int end = 0;
  private final int size;

  private boolean looped = false;
  private long[] values;
}

class Part1 {

  static long getTarget(List<Long> data, int prevSize) {
    var verifier = new Verifier(prevSize);

    int i = 0;

    // Preamble
    for (; i < prevSize; ++i) {
      verifier.add(data.get(i));
    }

    long value = data.get(i);
    while (verifier.verify(value)) {
      verifier.add(value);
      value = data.get(++i);
    }

    return value;
  }

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").mapToLong(Long::parseLong).boxed().collect(Collectors.toList());

    long target = getTarget(data, 25);

    System.out.println("Target: " + target);
  }
}