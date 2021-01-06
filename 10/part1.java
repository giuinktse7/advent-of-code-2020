import java.io.IOException;
import java.util.stream.Collectors;

class Part1 {

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
    data.sort((a, b) -> (int) (a - b));

    long prev = 0;

    long diff1 = 0;
    long diff3 = 1;

    for (int i = 0; i < data.size(); ++i) {
      long curr = data.get(i);
      long diff = curr - prev;

      if (diff == 1)
        ++diff1;
      else if (diff == 3)
        ++diff3;

      prev = curr;
    }

    System.out.println(diff1 * diff3);
  }
}