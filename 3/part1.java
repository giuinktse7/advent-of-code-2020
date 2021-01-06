import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Part1 {
  public static int rowCount;
  public static int columnCount;
  public static String forest;

  public static long check(int[] forest, int rowStep, int colStep) {
    long trees = 0;
    int row = 0;
    int column = 0;
    while (row < rowCount) {
      if (forest[row * columnCount + column] == 1) {
        ++trees;
      }

      row += rowStep;
      column = (column + colStep) % columnCount;

    }

    return trees;
  }

  public static long check(int rowStep, int colStep) {
    long trees = 0;
    int row = 0;
    int column = 0;
    while (row < rowCount) {
      if (forest.charAt(row * columnCount + column) == '#') {
        ++trees;
      }

      row += rowStep;
      column = (column + colStep) % columnCount;

    }

    return trees;
  }

  public static void main(String[] args) throws IOException {
    long start = System.nanoTime();
    StringBuilder builder = new StringBuilder();
    BufferedReader reader = new BufferedReader(new FileReader("./input.txt"));

    String line = reader.readLine();
    columnCount = line.length();
    builder.append(line);

    while ((line = reader.readLine()) != null) {
      ++rowCount;

      builder.append(line);
    }

    reader.close();

    forest = builder.toString();

    // List<String> rows = Files.readAllLines(path);

    // int[] forest = new int[columnCount * rows.size()];

    // for (int rowIndex = 0; rowIndex < rows.size(); ++rowIndex) {
    // String row = rows.get(rowIndex);
    // for (int column = 0; column < row.length(); ++column) {
    // forest[rowIndex * columnCount + column] = row.charAt(column) == '.' ? 0 : 1;
    // }
    // }

    /*
     * Right 1, down 1. Right 3, down 1. (This is the slope you already checked.)
     * Right 5, down 1. Right 7, down 1. Right 1, down 2.
     */
    long r1 = check(1, 1);
    long r2 = check(1, 3);
    long r3 = check(1, 5);
    long r4 = check(1, 7);
    long r5 = check(2, 1);
    long stop = System.nanoTime();

    System.out.println(r1 * r2 * r3 * r4 * r5);
    System.out.println("Done in " + ((stop - start) / (1000)) + "us.");

  }
}