import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;

class Part1 {
  public static void main(String[] args) throws IOException {
    HashSet<Integer> set = new HashSet<>();

    int target = 2020;

    Path path = FileSystems.getDefault().getPath("./input.txt");

    Files.lines(path).takeWhile(line -> {
      int value = Integer.parseInt(line);

      if (set.contains(value)) {
        System.out.println(value * (target - value));
        return false;
      }

      set.add(target - value);
      return true;
    }).forEach(x -> {});

    System.out.println("Done");
  }
}