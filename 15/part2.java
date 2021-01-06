import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

class LastSpoken2 {
  LastSpoken2(int round) {
    first = round;
  }

  void add(int round) {
    if (first == -1) {
      first = round;
    } else if (second == -1) {
      second = round;
    } else {
      first = second;
      second = round;
    }
  }

  public int first = -1;
  public int second = -1;
}

class State2 {

  public long add(long round, long value) {
    maxValue = Math.max(maxValue, value);
    if (!values.containsKey(value)) {
      values.put(value, new LastSpoken(round));
      return -1L;
    } else {
      var lastSpoken = values.get(value);
      lastSpoken.add(round);
      return lastSpoken.second - lastSpoken.first;
    }
  }

  public boolean has(long value) {
    return values.containsKey(value);
  }

  public long size() {
    return values.size();
  }

  private HashMap<Long, LastSpoken> values = new HashMap<>((int) Math.pow(10, 6));
  long maxValue = 0;
}

class Part2 {

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").collect(Collectors.toList());
    var startValues = data.get(0).split(",");

    var s1 = System.nanoTime();
    long k[] = new long[29421818];
    var s2 = System.nanoTime();
    System.out.println("sss in " + ((s2 - s1) / (int) Math.pow(10, 6)) + " ms.");

    var start = System.nanoTime();
    final int rounds = 30000000;
    int round = 1;

    var state = new State2();

    for (var value : startValues) {
      state.add(round++, Long.parseLong(value));
    }

    long prevValue = 0;

    while (round < rounds) {
      if (state.has(prevValue)) {
        long diff = state.add(round, prevValue);
        prevValue = diff;
      } else {
        state.add(round, prevValue);
        prevValue = 0;
      }

      ++round;
    }

    var end = System.nanoTime();
    System.out.println("Finished in " + ((end - start) / (int) Math.pow(10, 6)) + " ms.");
    System.out.println(round + ", " + prevValue);
    System.out.println(state.size());
    System.out.println("max: " + state.maxValue);

    System.out.println("Done");
  }
}