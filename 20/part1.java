import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

class Part1 {
  static class Puzzle {
    Puzzle(List<Tile> tiles) {
      this.tiles = tiles;

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

    public List<Tile> tiles;
    public HashMap<Integer, ArrayList<Tile>> borders = new HashMap<>();
  }

  public static void main(String[] args) throws IOException {
    var tiles = Parser.create("./input.txt").groupOn(String::isEmpty).construct(Tile::new).collect(Collectors.toList());

    Puzzle puzzle = new Puzzle(tiles);

    ArrayList<Tile> cornerTiles = new ArrayList<>();

    for (var tile : tiles) {
      int uniques = 0;
      for (int border : tile.borderValues) {
        if (puzzle.borders.get(border).size() == 1) {
          ++uniques;
          if (uniques == 4) {
            cornerTiles.add(tile);
            break;
          }
        }
      }
    }

    var result = cornerTiles.stream().mapToLong(x -> x.id).reduce(1, Math::multiplyExact);

    System.out.println(result);
  }
}