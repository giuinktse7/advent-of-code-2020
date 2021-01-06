import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class Part2 {

  static <T> Set<T> intersect(Set<T> a, Set<T> b) {
    a.retainAll(b);
    return a;
  }

  public static void main(String[] args) throws IOException {
    Parser parser = Parser.create("./input.txt").groupOn(line -> line.isEmpty());

    int result = parser.reduce(0, lines -> {
      var sets = lines.stream().map(line -> line.chars().boxed().collect(Collectors.toSet()));

      return sets.reduce(new HashSet<>(), Part2::intersect).size();
    }, (a, b) -> a + b);

    System.out.println(result);
  }
}