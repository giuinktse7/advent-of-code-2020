import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

class Data {
  public int min;
  public int max;

  public char c;
  public String password;

  public static Data fromLine(String s) {
    Data data = new Data();
    String[] vars = s.split("-", 2);
    String[] vars2 = vars[1].split(" ", 2);

    data.min = Integer.parseInt(vars[0]);
    data.max = Integer.parseInt(vars2[0]);

    String rest = vars2[1];

    data.c = rest.charAt(0);
    data.password = rest.substring(3);

    return data;
  }
}

class Part1 {
  public static void main(String[] args) throws IOException {
    Path path = FileSystems.getDefault().getPath("./input.txt");

    int valid = 0;
    List<Data> allData = Files.readAllLines(path).stream().map(Data::fromLine).collect(Collectors.toList());

    for (Data data : allData) {
      int count = data.password.length() - data.password.replaceAll(data.c + "", "").length();
      if (count >= data.min && count <= data.max) {
        ++valid;
      }
    }

    System.out.println(valid);
    System.out.println("Done");
  }
}