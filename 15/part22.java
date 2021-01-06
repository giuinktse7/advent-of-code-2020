import java.io.IOException;
import java.util.stream.Collectors;

class Part22 {

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").collect(Collectors.toList()).get(0).split(",");

    var start = System.nanoTime();
    long values[] = new long[(int) Math.pow(2, 25)];

    final int rounds = 30000000;
    long round = 1;

    while (round <= data.length) {
      int value = Integer.parseInt(data[(int) round - 1]);
      values[value] |= round;
      ++round;
    }

    int spoken = 0;
    long mask = ((long) Integer.MAX_VALUE);

    while (round < rounds) {
      int prevSpoken = spoken;
      spoken = values[spoken] == 0 ? 0 : (int) (round - (values[spoken] & mask));

      values[prevSpoken] <<= 32;
      values[prevSpoken] |= round;
      ++round;
    }

    var end = System.nanoTime();
    System.out.println("Answer: " + spoken + " (" + Parser.showTime(start, end) + ").");
  }
}