import java.io.IOException;
import java.util.stream.Collectors;

class Part1 {

  public static void main(String[] args) throws IOException {
    var lines = Parser.lines("./input.txt").collect(Collectors.toList());

    boolean[] executedLines = new boolean[lines.size()];

    int cursor = 0;
    long accumulated = 0;

    while (!executedLines[cursor]) {
      executedLines[cursor] = true;
      var parts = lines.get(cursor).split(" ");

      String op = parts[0];
      char sign = parts[1].charAt(0);
      long value = Integer.parseInt(parts[1].substring(1)) * (sign == '+' ? 1 : -1);

      switch (op) {
        case "acc":
          accumulated += value;
          break;
        case "jmp":
          cursor += value;
          continue;
        default:
          break;
      }

      ++cursor;
    }

    System.out.println(accumulated);
    System.out.println("Done");
  }
}