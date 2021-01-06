import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

class Part2 {
  public static void main(String[] args) throws IOException {
    int target = 2020;

    Path path = FileSystems.getDefault().getPath("./input.txt");
    
    List<Integer> lines = Files
      .readAllLines(path)
      .stream()
      .map(x -> Integer.parseInt(x))
      .collect(Collectors.toList());

    for (int l1 : lines) {
      for (int l2 : lines) {
        for (int l3 : lines){
          if (l1 + l2 + l3 == target) {
            System.out.println(l1*l2*l3);
            return;
          }
        }
      }
    }

    System.out.println("Done");
  }
}