import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

class Part2 {

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
    long target = Part1.getTarget(data, 25);

    int start = 0;
    int end = 0;
    long contiguousSum = 0;

    while (!(contiguousSum == target && start != end)) {
      contiguousSum += data.get(end++);

      while (contiguousSum > target)
        contiguousSum -= data.get(start++);
    }

    long min = data.get(start);
    long max = min;

    for (int i = start; i <= end; ++i) {
      long value = data.get(i);
      min = Math.min(min, value);
      max = Math.max(max, value);
    }

    System.out.println("Encryption weakness: " + (min + max));
  }
}