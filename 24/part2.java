
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

enum Color {
  Black, White
}

enum Adjacent {
  West, East, SouthWest, NorthWest, SouthEast, NorthEast;

  public static Adjacent[] cachedValues = values();

}

class Part2 {
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

  static class TileFloor {
    private static HashMap<Point, Tile> tiles = new HashMap<>();

    public TileFloor() {
      var zeroTile = new Tile(0, 0);
      tiles.put(zeroTile.location, zeroTile);
      zeroTile.createAllAdjacent();
    }

    public int tileCount() {
      return tiles.size();
    }

    public void initialize(List<String> data) {
      var zeroTile = ref();

      for (var line : data) {
        var currentTile = zeroTile;
        char prev = 'x';
        for (int i = 0; i < line.length(); ++i) {
          char curr = line.charAt(i);

          Adjacent adjacentType;

          if (prev == 's' && curr == 'e') {
            adjacentType = Adjacent.SouthEast;
          } else if (prev == 'n' && curr == 'e') {
            adjacentType = Adjacent.NorthEast;
          } else if (prev == 's' && curr == 'w') {
            adjacentType = Adjacent.SouthWest;
          } else if (prev == 'n' && curr == 'w') {
            adjacentType = Adjacent.NorthWest;
          } else if (curr == 'e') {
            adjacentType = Adjacent.East;
          } else if (curr == 'w') {
            adjacentType = Adjacent.West;
          } else {
            prev = curr;
            continue;
          }

          currentTile = currentTile.adjacent(adjacentType, true);

          prev = curr;
        }

        currentTile.flip();
      }
    }

    public void nextDay() {
      changedTiles.clear();
      blackTiles = 0;

      for (var tile : tiles.values()) {
        var c = tile.stageFlip();
        if (tile.color != tile.nextColor) {
          changedTiles.add(tile);
        }
        if (c == Color.Black) {
          ++blackTiles;
        }
      }

      for (var tile : changedTiles) {
        tile.commit();
        if (tile.black())
          tile.createAllAdjacent();
      }
    }

    public int blackTiles = 0;

    public Tile ref() {
      return tiles.get(new Point(0, 0));
    }

    static class Tile {
      public Tile(int x, int y) {
        color = Color.White;
        nextColor = Color.White;
        this.location = new Point(x, y);
      }

      private Tile getOrCreate(int x, int y) {
        var tile = tiles.get(new Point(x, y));
        if (tile == null) {
          tile = new Tile(x, y);
          tiles.put(tile.location, tile);
          return tile;
        }

        return tile;
      }

      public static long maxX = 0;
      public static long minX = Long.MAX_VALUE;

      private Tile get(int x, int y) {
        maxX = Math.max(maxX, x);
        minX = Math.min(minX, x);
        return tiles.get(new Point(x, y));
      }

      boolean white() {
        return color == Color.White;
      }

      boolean black() {
        return color == Color.Black;
      }

      public void flip() {
        if (color == Color.Black) {
          color = Color.White;
          nextColor = Color.White;
        } else {
          color = Color.Black;
          nextColor = Color.Black;
        }
        createAllAdjacent();
      }

      public void createAllAdjacent() {
        if (hasAllAdjacents)
          return;

        for (var adjacentType : Adjacent.cachedValues) {
          adjacent(adjacentType, true);
        }

        hasAllAdjacents = true;
      }

      public Color stageFlip() {
        int nearbyBlack = adjacentBlack();
        switch (color) {
          case White:
            if (nearbyBlack == 2) {
              nextColor = Color.Black;
              return Color.Black;
            }
            break;
          case Black:
            if (nearbyBlack == 0 || nearbyBlack > 2) {
              nextColor = Color.White;
              return Color.White;

            }
            break;
        }

        nextColor = color;
        return nextColor;
      }

      public void commit() {
        color = nextColor;
        createAllAdjacent();
      }

      private int adjacentBlack() {
        int result = 0;
        Tile tile;

        for (var adjacentType : Adjacent.cachedValues) {
          tile = adjacent(adjacentType);
          if (tile != null && tile.black())
            ++result;
        }

        return result;
      }

      public Tile adjacent(Adjacent adj) {
        return adjacent(adj, false);
      }

      public Tile getOrCreateAdjacent(Adjacent adj) {
        return adjacent(adj, true);
      }

      public Tile adjacent(Adjacent adj, boolean create) {
        int dx = 0;
        int dy = 0;
        switch (adj) {
          case West:
            dx = -2;
            break;
          case East:
            dx = 2;
            break;
          case SouthWest:
            dx = -1;
            dy = 1;
            break;
          case NorthWest:
            dx = -1;
            dy = -1;
            break;
          case SouthEast:
            dx = 1;
            dy = 1;
            break;
          case NorthEast:
            dx = 1;
            dy = -1;
            break;
        }

        return create ? getOrCreate(location.x + dx, location.y + dy) : get(location.x + dx, location.y + dy);
      }

      public Point location;
      private boolean hasAllAdjacents = false;

      public Color color;
      public Color nextColor;

    }

    private ArrayList<Tile> changedTiles = new ArrayList<>(4096);
  }

  public static void main(String[] args) throws IOException {
    var start = System.nanoTime();
    var data = Parser.lines("./input.txt").collect(Collectors.toList());

    var floor = new TileFloor();
    floor.initialize(data);

    for (int i = 0; i < 100; ++i) {
      floor.nextDay();
    }

    var end = System.nanoTime();

    System.out.println("Black tiles: " + floor.blackTiles + " (" + Parser.showTime(start, end) + ").");
  }
}