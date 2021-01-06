import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Part2 {
  static class Range {
    public int l1;
    public int r1;

    public int l2;
    public int r2;

    boolean includes(int value) {
      return (l1 <= value && value <= r1) || (l2 <= value && value <= r2);
    }
  }

  static boolean validate(ArrayList<Range> ranges, int[] data) {
    for (int i = 0; i < data.length; ++i) {
      boolean ok = false;

      for (var range : ranges) {
        if (range.includes(data[i])) {
          ok = true;
          break;
        }
      }

      if (!ok) {
        return false;
      }
    }

    return true;
  }

  static void cascadeRemove(int valueIndex, HashSet<Integer>[] candidates) {
    ArrayDeque<Integer> valueIndices = new ArrayDeque<>();
    valueIndices.add(valueIndex);

    while (!valueIndices.isEmpty()) {
      int index = valueIndices.pop();

      // Get the single range index from the candidate index
      int rangeIndex = -1;
      for (int determinedRangeIndex : candidates[index]) {
        rangeIndex = determinedRangeIndex;
      }

      for (int c = 0; c < candidates.length; ++c) {
        if (c == index)
          continue;

        if (candidates[c].remove(rangeIndex) && candidates[c].size() == 1)
          valueIndices.add(c);
      }
    }
  }

  static void excludeRanges(ArrayList<Range> ranges, HashSet<Integer>[] candidates, int[] data) {
    for (int i = 0; i < data.length; ++i) {
      for (int r = 0; r < ranges.size(); ++r) {
        if (!ranges.get(r).includes(data[i])) {
          candidates[i].remove(r);

          if (candidates[i].size() == 1)
            cascadeRemove(i, candidates);
        }
      }
    }
  }

  static HashSet<Integer> createCandidateSet(int size) {
    HashSet<Integer> set = new HashSet<>();
    for (int i = 0; i < size; ++i) {
      set.add(i);
    }

    return set;
  }

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").collect(Collectors.toList());
    var start = System.nanoTime();

    Pattern pattern = Pattern.compile(".*: (\\d+)-(\\d+) or (\\d+)-(\\d+)");

    ArrayList<Range> ranges = new ArrayList<>();
    ArrayList<Integer> departureRangeIndices = new ArrayList<>();

    int cursor = 0;

    for (String line = data.get(cursor); !line.isEmpty(); line = data.get(++cursor)) {
      if (line.startsWith("departure"))
        departureRangeIndices.add(cursor);

      var range = new Range();
      var match = pattern.matcher(line);
      match.find();

      range.l1 = Integer.parseInt(match.group(1));
      range.r1 = Integer.parseInt(match.group(2));
      range.l2 = Integer.parseInt(match.group(3));
      range.r2 = Integer.parseInt(match.group(4));

      ranges.add(range);
    }

    // Skip the empty line and "your ticket:"
    cursor += 2;

    // Add own ticket to data
    var ourTicket = data.get(cursor);
    int valueCount = ourTicket.split(",").length;
    data.add(ourTicket);
    ++cursor;

    // Skip empty line and "nearby tickets"
    cursor += 2;

    int[] values;

    values = new int[valueCount];
    HashSet<Integer>[] candidates = new HashSet[valueCount];

    for (int i = 0; i < valueCount; ++i) {
      candidates[i] = createCandidateSet(ranges.size());
    }

    for (; cursor < data.size(); ++cursor) {
      var rawValues = data.get(cursor).split(",");
      for (int i = 0; i < valueCount; ++i) {
        values[i] = Integer.parseInt(rawValues[i]);
      }

      if (!validate(ranges, values))
        continue;

      excludeRanges(ranges, candidates, values);
    }

    long product = 1;
    for (int valueIndex = 0; valueIndex < valueCount; ++valueIndex) {
      for (int rangeIndex : candidates[valueIndex]) {
        if (departureRangeIndices.contains(rangeIndex)) {
          product *= values[valueIndex];
        }
      }
    }

    var end = System.nanoTime();

    System.out.println(product + " (in " + Parser.showTime(start, end) + ")");
    System.out.println("Done");
  }
}