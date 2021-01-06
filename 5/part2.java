import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

class Part2 {

  static int getId(String rowLine) {
    final int rowSplits = 7;

    var rowString = rowLine.substring(0, rowSplits);
    var columnString = rowLine.substring(rowSplits);

    int row = 0;

    int d = 64;

    for (int i = 0; i < rowString.length(); ++i) {
      if (rowString.charAt(i) == 'B') {
        row += d;
      }

      d >>= 1;
    }

    int column = 0;
    d = 4;

    for (int i = 0; i < columnString.length(); ++i) {
      if (columnString.charAt(i) == 'R') {
        column += d;
      }
      d >>= 1;
    }

    int id = row * 8 + column;
    return id;
  }

  public static void main(String[] args) throws IOException {
    Path path = FileSystems.getDefault().getPath("./input.txt");

    HashSet<Integer> ids = new HashSet<>();

    List<String> rows = Files.readAllLines(path);

    for (String row : rows) {
      int id = getId(row);
      ids.add(id);
    }

    for (int id : ids) {
      if (!ids.contains(id + 1) && ids.contains(id + 2)) {
        System.out.println(id + 1);
        break;
      }
    }

    System.out.println("Done");
  }
}