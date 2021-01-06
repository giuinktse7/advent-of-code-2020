import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class Part1 {

  static int seqToInt(String seq, char inc) {
    return seq.chars().reduce(0, (acc, next) -> (acc << 1) + (next == (int) inc ? 1 : 0));
  }

  static int getId(String rowLine) {
    final int rowSplits = 7;

    var rowString = rowLine.substring(0, rowSplits);
    var columnString = rowLine.substring(rowSplits);

    int row = seqToInt(rowString, 'B');
    int column = seqToInt(columnString, 'R');

    return row * 8 + column;
  }

  public static void main(String[] args) throws IOException {
    Path path = FileSystems.getDefault().getPath("./input.txt");
    List<String> rows = Files.readAllLines(path);

    int maxId = 0;

    for (String row : rows)
      maxId = Math.max(maxId, getId(row));

    System.out.println(maxId);
  }
}