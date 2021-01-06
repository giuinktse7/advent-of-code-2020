import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;


class Part2 {
  public static void main(String[] args) throws IOException {
    Path path = FileSystems.getDefault().getPath("./input.txt");

    int valid = 0;
    List<Data> allData = Files
      .readAllLines(path)
      .stream()
      .map(Data::fromLine)
      .collect(Collectors.toList());

      for (Data data : allData) {
        boolean first = data.password.charAt(data.min - 1) == data.c;
        boolean second = data.password.charAt(data.max - 1) == data.c;
        if (first ^ second) {
          ++valid;
        }
      }

    System.out.println(valid);
    System.out.println("Done");
  }
}