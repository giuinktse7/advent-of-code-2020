import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Part1 {
  static class Range {
    public int l1;
    public int r1;

    public int l2;
    public int r2;

    boolean includes(int value) {
      return (l1 <= value && value <= r1) || (l2 <= value && value <= r2);
    }
  }

  static int computeInvalidAmount(ArrayList<Range> ranges, int[] data) {
    int sum = 0;
    for (int i = 0; i < data.length; ++i) {
      int value = data[i];
      boolean ok = false;

      for (var range : ranges) {
        if (range.includes(value)) {
          ok = true;
          break;
        }
      }

      if (!ok) {
        sum += value;
      }
    }
    return sum;
  }

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").collect(Collectors.toList());

    int cursor = 0;

    Pattern pattern = Pattern.compile(".*: (\\d+)-(\\d+) or (\\d+)-(\\d+)");

    ArrayList<Range> ranges = new ArrayList<>();

    while (true) {
      var line = data.get(cursor);
      if (line.isEmpty()) {
        ++cursor;
        break;
      }

      var range = new Range();
      var match = pattern.matcher(line);
      match.find();

      range.l1 = Integer.parseInt(match.group(1));
      range.r1 = Integer.parseInt(match.group(2));
      range.l2 = Integer.parseInt(match.group(3));
      range.r2 = Integer.parseInt(match.group(4));

      ranges.add(range);

      ++cursor;
    }

    // Skip "your ticket:"
    ++cursor;

    // Skip own ticket data
    ++cursor;

    // Skip empty line and "nearby tickets"
    cursor += 2;

    int[] values;

    // Use the first one to determine size of values array
    var firstRaw = data.get(cursor).split(",");
    ++cursor;
    values = new int[firstRaw.length];
    for (int i = 0; i < firstRaw.length; ++i) {
      values[i] = Integer.parseInt(firstRaw[i]);
    }

    long invalidCount = computeInvalidAmount(ranges, values);

    while (cursor < data.size()) {
      var rawValues = data.get(cursor).split(",");
      for (int i = 0; i < firstRaw.length; ++i) {
        values[i] = Integer.parseInt(rawValues[i]);
      }

      invalidCount += computeInvalidAmount(ranges, values);

      ++cursor;
    }

    System.out.println(invalidCount);
    System.out.println("Done");
  }
}