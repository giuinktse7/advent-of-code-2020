import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

class LastSpoken {
  LastSpoken(long round) {
    first = round;
  }

  void add(long round) {
    if (first == -1) {
      first = round;
    } else if (second == -1) {
      second = round;
    } else {
      first = second;
      second = round;
    }
  }

  public long first = -1;
  public long second = -1;
}

class State {
  public long add(long round, long value) {
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

  private HashMap<Long, LastSpoken> values = new HashMap<>();

}

class Part1 {

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").collect(Collectors.toList());
    var startValues = data.get(0).split(",");

    final int rounds = 2020;
    long round = 1;

    var state = new State();

    for (var value : startValues)
      state.add(round++, Long.parseLong(value));

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
    System.out.println(round + ", " + prevValue);

    System.out.println("Done");
  }
}