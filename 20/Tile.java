import java.util.HashSet;
import java.util.List;

enum Orientation {
  Identity(0), Rotate90(1), Rotate180(2), Rotate270(3), IdentityFlip(4), Rotate90Flip(5), Rotate180Flip(6),
  Rotate270Flip(7);

  public static Orientation[] cachedValues = values();

  public final int value;

  private Orientation(int value) {
    this.value = value;
  }

  public static Orientation fromInt(int i) {
    if (cachedValues == null) {
      cachedValues = Orientation.values();
    }

    return cachedValues[i];
  }
}

class Border {
  public Border(int top, int right, int bottom, int left) {
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.left = left;
  }

  public final int top;
  public final int right;
  public final int bottom;
  public final int left;

  public int get(BorderSide side) {
    switch (side) {
      case Top:
        return top;
      case Right:
        return right;
      case Bottom:
        return bottom;
      case Left:
        return left;
      default:
        throw new RuntimeException("we are kabomm");
    }
  }
}

enum BorderSide {
  Top, Right, Bottom, Left
}

public class Tile {
  public static final int Rows = 10;
  public static final int Columns = 10;

  private static final int xMax = Columns - 1;
  private static final int yMax = Rows - 1;

  int getBorder(BorderSide side) {
    var border = borders[_orientation.value];
    switch (side) {
      case Top:
        return border.top;
      case Right:
        return border.right;
      case Bottom:
        return border.bottom;
      case Left:
        return border.left;
      default:
        throw new RuntimeException("we are kabomm");
    }
  }

  public void align(int border, BorderSide side) {
    int i = 0;
    while (border != borders[i].get(side))
      ++i;

    setOrientation(Orientation.fromInt(i));
  }

  public Tile(List<String> tileRows) {
    int cursor = 0;
    var lineWithId = tileRows.get(cursor++);
    var stringId = lineWithId.substring(0, lineWithId.length() - 1).split(" ")[1];
    this.id = Integer.parseInt(stringId);

    for (; cursor < tileRows.size(); ++cursor) {
      var row = tileRows.get(cursor);
      for (int colIndex = 0; colIndex < row.length(); ++colIndex) {
        data[Columns * (cursor - 1) + colIndex] = row.charAt(colIndex) == '#';
      }
    }

    var orientations = Orientation.cachedValues;
    for (int i = 0; i < orientations.length; ++i) {
      var orientation = orientations[i];

      int top = 0;
      int bottom = 0;
      for (int column = 0; column < Columns; ++column) {
        if (get(column, 0, orientation))
          top |= 1 << (xMax - column);

        if (get(column, yMax, orientation))
          bottom |= 1 << (xMax - column);
      }

      int left = 0;
      int right = 0;
      for (int row = 0; row < Columns; ++row) {
        if (get(0, row, orientation))
          left |= 1 << (yMax - row);

        if (get(xMax, row, orientation))
          right |= 1 << (yMax - row);
      }

      borderValues.add(top);
      borders[i] = new Border(top, right, bottom, left);
    }
  }

  Border[] borders = new Border[8];
  HashSet<Integer> borderValues = new HashSet<>();

  public boolean get(int x, int y) {
    return get(x, y, _orientation);
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

    return data[Columns * y + x];
  }

  public void setOrientation(Orientation orientation) {
    this._orientation = orientation;
  }

  public Orientation orientation() {
    return this._orientation;
  }

  boolean[] data = new boolean[Rows * Columns];
  public final int id;
  private Orientation _orientation;
}