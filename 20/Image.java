public class Image {
  Image(Board board, SeaMonster seaMonster) {
    int tileSizeWithoutBorder = Tile.Rows * Tile.Columns - 2 * (Tile.Rows + Tile.Columns - 2);
    int pixelCount = board.size * board.size * tileSizeWithoutBorder;

    this.seaMonster = seaMonster;

    this.size = (int) Math.sqrt(pixelCount);
    this.xMax = size - 1;
    this.yMax = size - 1;
    data = new boolean[pixelCount];

    for (int y = 0; y < board.size; ++y) {
      int yOffset = y * (Tile.Rows - 2);
      for (int x = 0; x < board.size; ++x) {
        var tile = board.get(x, y);

        int xOffset = x * (Tile.Columns - 2);
        for (int tileY = 1; tileY < Tile.Rows - 1; ++tileY) {
          for (int tileX = 1; tileX < Tile.Columns - 1; ++tileX) {
            if (tile.get(tileX, tileY)) {
              ++totalRoughness;
              set(xOffset + tileX - 1, yOffset + tileY - 1, true);
            }
          }
        }
      }
    }
  }

  public long checkSeaMonsters() {
    int monsterSize = seaMonster.count();
    for (var orientation : Orientation.cachedValues) {
      long result = 0;
      for (int y = 0; y < size - seaMonster.height; ++y) {
        for (int x = 0; x < size - seaMonster.width; ++x) {
          if (hasSeaMonster(x, y, orientation))
            result += monsterSize;
        }
      }
      if (result != 0) {
        System.out.println("Found sea monsters at orientation: " + orientation);
        return totalRoughness - result;
      }
    }

    return -1;
  }

  private void set(int x, int y, boolean value) {
    data[y * size + x] = value;
  }

  public boolean get(int x, int y) {
    return data[y * size + x];
  }

  private boolean get(int x, int y, Orientation orientation) {
    int temp;
    switch (orientation) {
      case Identity:
        break;
      case Rotate90:
        temp = x;
        x = xMax - y;
        y = temp;
        break;
      case Rotate180:
        x = xMax - x;
        y = yMax - y;
        break;
      case Rotate270:
        temp = x;
        x = y;
        y = yMax - temp;
        break;
      case IdentityFlip:
        y = yMax - y;
        break;
      case Rotate90Flip:
        temp = x;
        x = xMax - y;
        y = yMax - temp;
        break;
      case Rotate180Flip:
        x = xMax - x;
        break;
      case Rotate270Flip:
        temp = x;
        x = y;
        y = temp;
        break;
    }

    return data[size * y + x];
  }

  public boolean hasSeaMonster(int startX, int startY, Orientation orientation) {
    for (var index : seaMonster.required) {
      if (!get(startX + index.x, startY + index.y, orientation))
        return false;
    }

    return true;
  }

  public String show() {
    return show(Orientation.Identity);
  }

  public String show(Orientation orientation) {
    StringBuilder sb = new StringBuilder();
    for (int y = 0; y < size; ++y) {
      for (int x = 0; x < size; ++x) {
        sb.append(get(x, y, orientation) ? '#' : '.');
        if (x != size - 1) {
          sb.append(" ");
        }
      }
      sb.append("\n");
    }

    return sb.toString();
  }

  boolean[] data;
  long totalRoughness = 0;

  private SeaMonster seaMonster;

  private final int size;
  private final int xMax;
  private final int yMax;
}
