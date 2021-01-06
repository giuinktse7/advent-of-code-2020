import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Parser {
  private Parser(Path path) throws IOException {
    lines = Files.lines(path);
  }

  public static Parser create(String path) throws IOException {
    Path filepath = FileSystems.getDefault().getPath(path);

    return new Parser(filepath);
  }

  public static Stream<String> lines(String path) throws IOException {
    Path filepath = FileSystems.getDefault().getPath(path);

    return Files.lines(filepath);
  }

  public Parser groupOn(Predicate<String> predicate) {
    groupPredicate = predicate;
    return this;
  }

  public <T> Stream<T> construct(Function<List<String>, T> constructor) {
    return getGroups(lines, constructor, groupPredicate);
  }

  public <T, R> R reduce(R zero, Function<List<String>, R> constructor, BinaryOperator<R> combiner) {
    return getGroups(lines, constructor, groupPredicate).reduce(zero, combiner);
  }

  private static <T> Stream<T> getGroups(Stream<String> lines, Function<List<String>, T> cons,
      Predicate<String> isSeparator) {
    BiFunction<ArrayList<List<String>>, ? super String, ArrayList<List<String>>> accumulator = (acc, next) -> {
      if (isSeparator.test(next)) {
        acc.add(new ArrayList<>());
      } else {
        acc.get(acc.size() - 1).add(next);
      }

      return acc;
    };

    BinaryOperator<ArrayList<List<String>>> combiner = (a, b) -> {
      a.addAll(b);
      return a;
    };

    var zero = new ArrayList<List<String>>();
    zero.add(new ArrayList<>());
    return lines.reduce(zero, accumulator, combiner).stream().map(cons);
  }

  public static String showTime(long from, long to) {
    return ((to - from) / (int) Math.pow(10, 6)) + " ms";
  }

  private Stream<String> lines;
  private Predicate<String> groupPredicate;
}
