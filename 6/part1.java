import java.io.IOException;
import java.util.stream.Collectors;

class Part1 {
  public static void main(String[] args) throws IOException {
    Parser parser = Parser.create("./input.txt").groupOn(line -> line.isEmpty());

    int result = parser.reduce(0, lines -> {
      var chars = lines.stream().collect(Collectors.joining()).chars().boxed();

      return chars.collect(Collectors.toSet()).size();
    }, (a, b) -> a + b);

    System.out.println(result);
  }
}