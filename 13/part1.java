import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

class Part1 {

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").collect(Collectors.toList());

    var target = Integer.parseInt(data.get(0));
    var stops = Arrays.stream(data.get(1).split(",")).filter(x -> !x.equals("x")).map(x -> Integer.parseInt(x))
        .collect(Collectors.toList());

    int best = -1;
    int delta = Integer.MAX_VALUE;

    for (var stop : stops) {
      var departure = target + stop - target % stop;
      int stopDelta = departure - target;

      if (stopDelta < delta) {
        delta = stopDelta;
        best = stop;
      }
    }

    System.out.println(best * delta);
  }
}