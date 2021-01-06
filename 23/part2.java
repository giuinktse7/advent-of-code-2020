import java.io.IOException;
import java.util.stream.Collectors;

class Part2 {
  public static void main(String[] args) throws IOException {
    var start = System.nanoTime();
    String data = Parser.lines("./input.txt").collect(Collectors.toList()).get(0);

    var solution = new Solution(data);

    for (int i = 0; i < Math.pow(10, 7); ++i) {
      solution.move();
    }
    long answer = solution.answer();
    var end = System.nanoTime();

    System.out.println(answer + " (" + Parser.showTime(start, end) + ")");
  }
}