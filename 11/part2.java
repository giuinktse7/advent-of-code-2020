import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

class State2 {
  State2(List<String> data) {
    rows = data.size();
    columns = data.get(0).length();

    var idToState = new LocationType[] { LocationType.Floor, LocationType.EmptySeat };

    locations = new LocationType[rows * columns];
    nextLocations = new LocationType[rows * columns];

    for (int row = 0; row < rows; ++row) {
      String line = data.get(row);
      for (int column = 0; column < columns; ++column) {
        int index = posToIndex(row, column);
        locations[index] = idToState[line.charAt(column) - 48];
      }
    }

    System.arraycopy(locations, 0, nextLocations, 0, locations.length);
  }

  int posToIndex(int row, int column) {
    return row * columns + column;
  }

  LocationType location(int row, int column) {
    return locations[posToIndex(row, column)];
  }

  void setLocation(int row, int column, LocationType type) {
    nextLocations[posToIndex(row, column)] = type;
  }

  boolean run() {
    var changed = false;

    // System.out.println("Before: \n");
    // showLocations();

    for (int row = 0; row < rows; ++row) {
      for (int column = 0; column < columns; ++column) {
        var type = location(row, column);
        if (type == null || type == LocationType.Floor)
          continue;

        int occupiedAdjacent = occupiedAdjacentSeats(row, column);

        if (location(row, column) == LocationType.EmptySeat && occupiedAdjacent == 0) {
          setLocation(row, column, LocationType.OccupiedSeat);
          changed = true;
        } else if (location(row, column) == LocationType.OccupiedSeat && occupiedAdjacent >= 5) {
          setLocation(row, column, LocationType.EmptySeat);
          changed = true;
        }
      }
    }

    System.arraycopy(nextLocations, 0, locations, 0, nextLocations.length);

    // System.out.println("After: \n");
    // showLocations();
    // System.out.println("\n----------------\n");

    return changed;
  }

  private boolean validPos(int row, int column) {
    return 0 <= row && row < rows && 0 <= column && column < columns;
  }

  public int occupiedAdjacentSeats(int row, int column) {
    int occupied = 0;

    int fromRow = Math.max(0, row - 1);
    int toRow = Math.min(rows - 1, row + 1);

    int fromColumn = Math.max(0, column - 1);
    int toColumn = Math.min(columns - 1, column + 1);

    for (int r = fromRow; r <= toRow; ++r) {
      for (int c = fromColumn; c <= toColumn; ++c) {
        if (r == row && c == column)
          continue;

        int deltaRow = r - row;
        int deltaColumn = c - column;

        int currentRow = r;
        int currentColumn = c;

        do {
          var type = locations[posToIndex(currentRow, currentColumn)];
          if (type == LocationType.OccupiedSeat) {
            ++occupied;
            break;
          } else if (type == LocationType.EmptySeat) {
            break;
          }

          currentRow += deltaRow;
          currentColumn += deltaColumn;
        } while (validPos(currentRow, currentColumn));

      }
    }

    return occupied;
  }

  long totalOccupiedSeats() {
    long result = 0;
    for (int i = 0; i < locations.length; ++i) {
      if (locations[i] == LocationType.OccupiedSeat)
        ++result;
    }

    return result;
  }

  void showLocations() {
    for (int row = 0; row < rows; ++row) {
      for (int column = 0; column < columns; ++column) {
        switch (location(row, column)) {
          case Floor:
            System.out.print(".");
            break;
          case EmptySeat:
            System.out.print("L");
            break;
          case OccupiedSeat:
            System.out.print("#");
            break;
        }
      }
      System.out.print("\n");
    }
  }

  public int rows;
  public int columns;
  public LocationType[] locations;
  public LocationType[] nextLocations;
}

class Part2 {

  public static void main(String[] args) throws IOException {
    List<String> data = Parser.lines("./input.txt").map(x -> x.replace(".", "0").replace("L", "1"))
        .collect(Collectors.toList());

    var start = System.nanoTime();
    var state = new State2(data);
    while (state.run())
      ;

    System.out.println(state.totalOccupiedSeats());
    var end = System.nanoTime();

    System.out.println(((end - start) / 1000000) + "ms");
    System.out.println("Done");
  }
}