import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

class Part2 {

  static void logd(String s) {
    System.out.println(s);
  }

  static void log(String s) {
    System.out.println(s);
  }

  static class Puzzle {
    Puzzle(Set<Tile> tiles) {
      this.tiles = tiles;

      var boardSize = (int) Math.sqrt(tiles.size());
      board = new Board(boardSize);

      initializeBorders();

      findCorners();

      log(tiles.size() + " tiles, " + boardSize + "x" + boardSize + " board.");
    }

    private void initializeBorders() {
      for (var tile : tiles) {
        for (int border : tile.borderValues)
          if (borders.containsKey(border)) {
            borders.get(border).add(tile);
          } else {
            ArrayList<Tile> list = new ArrayList<>();
            list.add(tile);
            borders.put(border, list);
          }
      }
    }

    boolean isUnique(int border) {
      return borders.get(border).size() == 1;
    }

    private void findCorners() {
      for (var tile : tiles) {
        int uniques = 0;
        for (int border : tile.borderValues) {
          if (borders.get(border).size() == 1) {
            ++uniques;
            if (uniques == 4) {
              cornerTiles.add(tile);
              break;
            }
          }
        }
      }
    }

    public void solve() {
      placeFirstCorner();

      // Skip the first corner
      int x0 = 1;

      for (int y = 0; y < board.size; ++y) {
        for (int x = x0; x < board.size; ++x) {
          Tile placedTile;
          BorderSide oldSide;
          BorderSide newSide;

          if (x == 0) {
            placedTile = board.get(x, y - 1);
            oldSide = BorderSide.Bottom;
            newSide = BorderSide.Top;
          } else {
            placedTile = board.get(x - 1, y);
            oldSide = BorderSide.Right;
            newSide = BorderSide.Left;
          }

          int border = placedTile.getBorder(oldSide);
          Tile tile = getAdjacent(placedTile, border);
          tile.align(border, newSide);

          board.set(tile, x, y);
        }
        // Use x = 0 after the first row
        x0 = 0;
      }
    }

    private Tile getAdjacent(Tile tile, int border) {
      return borders.get(border).stream().filter(t -> t != tile).findAny().get();
    }

    private void placeFirstCorner() {
      // 1 is arbitrary
      var cornerTile = cornerTiles.get(1);
      int i = 0;

      while (!(isUnique(cornerTile.borders[i].top) && isUnique(cornerTile.borders[i].left)))
        ++i;

      cornerTile.setOrientation(Orientation.fromInt(i));

      board.set(cornerTile, 0, 0);
    }

    public Set<Tile> tiles;
    public HashMap<Integer, ArrayList<Tile>> borders = new HashMap<>();
    public Board board;
    public ArrayList<Tile> cornerTiles = new ArrayList<>();

  }

  public static void main(String[] args) throws IOException {
    var start = System.nanoTime();
    var tiles = Parser.create("./input-test.txt").groupOn(String::isEmpty).construct(Tile::new)
        .collect(Collectors.toSet());

    Puzzle puzzle = new Puzzle(tiles);
    puzzle.solve();

    var image = new Image(puzzle.board, new SeaMonster("sea-monster.txt"));
    System.out.println(image.show(Orientation.Rotate270Flip));

    var answer = image.checkSeaMonsters();
    var end = System.nanoTime();

    System.out.println(answer + " in " + Parser.showTime(start, end));
  }
}