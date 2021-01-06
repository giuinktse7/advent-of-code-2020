import java.util.stream.Stream;

public class Board {
  public Board(int size) {
    board = new Tile[size * size];

    this.size = size;
    xMax = size - 1;
  }

  void set(Tile tile, int x, int y) {
    board[y * size + x] = tile;
  }

  Tile get(int x, int y) {
    return board[y * size + x];
  }

  String show() {
    StringBuilder sb = new StringBuilder();
    for (int y = 0; y < size; ++y) {
      for (int x = 0; x < size; ++x) {
        // int temp = x;
        // int x0 = xMax - y;
        // int y0 = xMax - temp;
        // sb.append(get(x0, y0).id);
        var tile = get(x, y);
        sb.append(tile != null ? tile.id : 0);
        if (x != xMax) {
          sb.append(" ");
        }
      }
      sb.append("\n");
    }

    return sb.toString();
  }

  long cornerIdProduct() {
    return Stream.of(get(0, 0), get(0, size - 1), get(size - 1, 0), get(size - 1, size - 1)).mapToLong(t -> t.id)
        .reduce(Math::multiplyExact).orElse(0L);
  }

  Tile[] board;
  public final int size;
  private final int xMax;
}