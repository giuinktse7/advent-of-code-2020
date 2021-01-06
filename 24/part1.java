import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

class Part1 {
  static class Point {
    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int x;
    public int y;

    public Point copy() {
      return new Point(x, y);
    }

    public boolean equals(Object obj) {
      if (!(obj instanceof Point))
        return false;
      var p2 = (Point) obj;
      return x == p2.x && y == p2.y;
    }

    private static StringBuilder sb = new StringBuilder();

    @Override
    public String toString() {
      sb.delete(0, sb.length());
      sb.append("(");
      sb.append(x);
      sb.append(", ");
      sb.append(y);
      sb.append(")");

      return sb.toString();
    }

    public int hashCode() {
      return 31 * x + y;
    }
  }

  static class Tile {
    private static HashMap<Point, Tile> tiles = new HashMap<>();

    private static Point indexPoint = new Point(0, 0);

    private Tile getOrCreate(int x, int y) {
      indexPoint.x = x;
      indexPoint.y = y;

      var tile = tiles.get(indexPoint);
      if (tile == null) {
        tile = new Tile(x, y);
        tiles.put(indexPoint, tile);
        return tile;
      }

      return tile;
    }

    public Tile(int x, int y) {
      white = true;
      this.location = new Point(x, y);
    }

    public void flip() {
      white = !white;
    }

    public Tile east() {
      if (_east == null) {
        _east = getOrCreate(location.x + 2, location.y);
      }

      return _east;
    }

    public Tile southeast() {
      if (_southeast == null) {
        _southeast = getOrCreate(location.x + 1, location.y + 1);
      }

      return _southeast;
    }

    public Tile southwest() {
      if (_southwest == null) {
        _southwest = getOrCreate(location.x - 1, location.y + 1);
      }

      return _southwest;
    }

    public Tile west() {
      if (_west == null) {
        _west = getOrCreate(location.x - 2, location.y);
      }

      return _west;
    }

    public Tile northwest() {
      if (_northwest == null) {
        _northwest = getOrCreate(location.x - 1, location.y - 1);
      }

      return _northwest;
    }

    public Tile northeast() {
      if (_northeast == null) {
        _northeast = getOrCreate(location.x + 1, location.y - 1);
      }

      return _northeast;
    }

    // void setEast(Tile tile) {
    // this._east = tile;
    // }

    // void setSoutheast(Tile tile) {
    // this._southeast = tile;
    // }

    // void setSouthwest(Tile tile) {
    // this._southwest = tile;
    // }

    // void setWest(Tile tile) {
    // this._west = tile;
    // }

    // void setNorthwest(Tile tile) {
    // this._northwest = tile;
    // }

    // void setNortheast(Tile tile) {
    // this._northeast = tile;
    // }

    public Point location;

    public Tile _east;
    public Tile _southeast;
    public Tile _southwest;
    public Tile _west;
    public Tile _northwest;
    public Tile _northeast;

    public boolean white;
  }

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").collect(Collectors.toList());

    Tile ref = new Tile(0, 0);

    long blackTiles = 0;

    for (var line : data) {
      var currentTile = ref;
      char prev = 'x';
      // System.out.println(line);
      for (int i = 0; i < line.length(); ++i) {
        char curr = line.charAt(i);

        if (prev == 's' && curr == 'e') {
          currentTile = currentTile.southeast();
        } else if (prev == 'n' && curr == 'e') {
          currentTile = currentTile.northeast();
        } else if (prev == 's' && curr == 'w') {
          currentTile = currentTile.southwest();
        } else if (prev == 'n' && curr == 'w') {
          currentTile = currentTile.northwest();
        } else if (curr == 'e') {
          currentTile = currentTile.east();
        } else if (curr == 'w') {
          currentTile = currentTile.west();
        } else {
          prev = curr;
          continue;
        }

        // System.out.println(("" + prev) + curr + " -> " + currentTile.location);
        prev = curr;
      }

      currentTile.flip();
      if (currentTile.white)
        --blackTiles;
      else
        ++blackTiles;
    }

    System.out.println(blackTiles);
    System.out.println("Done");
  }
}