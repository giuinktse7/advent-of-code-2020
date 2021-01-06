import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

class Index {
  public Index(int x, int y) {
    this.x = x;
    this.y = y;
  }

  int x;
  int y;
}

public class SeaMonster {

  public SeaMonster(String file) throws IOException {
    var lines = Parser.lines(file).collect(Collectors.toList());

    for (int y = 0; y < lines.size(); ++y) {
      var line = lines.get(y);
      for (int x = 0; x < line.length(); ++x) {
        if (line.charAt(x) == '#') {
          required.add(new Index(x, y));
          width = Math.max(width, x + 1);
          height = Math.max(height, y + 1);
        }
      }
    }
  }

  public int count() {
    return required.size();
  }

  public int width = 0;
  public int height = 0;

  public ArrayList<Index> required = new ArrayList<>(16);
}
