import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

class Part1 {
  static class State {
    State(int inputWidth, int inputHeight, int simulationSteps) {
      int requiredWidth = simulationSteps * 2 + inputWidth + 1;
      int requiredHeight = simulationSteps * 2 + inputHeight + 1;

      this.inputWidth = inputWidth;
      this.inputHeight = inputHeight;

      this.maxWidth = requiredWidth;
      this.maxHeight = requiredHeight;
      this.maxDepth = (simulationSteps + 1) * 2;

      this.endX = maxWidth / 2;
      this.endY = maxHeight / 2;
      this.endZ = maxDepth / 2;

      this.startX = -this.endX;
      this.startY = -this.endY;
      this.startZ = -this.endZ;

      this.cubes = new boolean[maxWidth * maxHeight * maxDepth];
      this.staging = new boolean[maxWidth * maxHeight * maxDepth];
    }

    void initialize(List<String> data) {
      for (int row = 0; row < data.size(); ++row) {
        var line = data.get(row);

        for (int column = 0; column < line.length(); ++column) {
          if (line.charAt(column) == '#')
            setActive(column - inputWidth / 2, row - inputHeight / 2, 0, true);
        }
      }
    }

    private int posToIndex(int x, int y, int z) {
      return maxWidth * ((z + endZ) * maxHeight + (y + endY)) + x + endX;
    }

    void setActive(int x, int y, int z, boolean active) {
      var index = posToIndex(x, y, z);

      if (cubes[index] && !active) {
        --_activeCubes;
      } else if (!cubes[index] && active) {
        ++_activeCubes;
      }

      cubes[index] = active;
      staging[index] = active;
    }

    boolean active(int x, int y, int z) {
      return cubes[posToIndex(x, y, z)];
    }

    boolean active(int index) {
      return cubes[index];
    }

    public void runStep() {
      for (int z = startZ; z < endZ; ++z) {
        for (int y = startY; y < endY; ++y) {
          for (int x = startX; x < endX; ++x) {
            var activeNeighbors = getActiveNeighbors(x, y, z);

            var index = posToIndex(x, y, z);
            boolean isActive = active(index);

            if (isActive) {
              if (activeNeighbors != 2 && activeNeighbors != 3) {
                staging[index] = false;
                --_activeCubes;
              }
            } else {
              if (activeNeighbors == 3) {
                staging[index] = true;
                ++_activeCubes;
              }
            }
          }
        }
      }

      System.arraycopy(staging, 0, cubes, 0, staging.length);
    }

    private int getActiveNeighbors(int x, int y, int z) {
      int fromX = Math.max(startX, x - 1);
      int fromY = Math.max(startY, y - 1);
      int fromZ = Math.max(startZ, z - 1);

      int toX = Math.min(endX - 1, x + 1);
      int toY = Math.min(endY - 1, y + 1);
      int toZ = Math.min(endZ - 1, z + 1);

      int sum = 0;

      for (int zPos = fromZ; zPos <= toZ; ++zPos) {
        for (int yPos = fromY; yPos <= toY; ++yPos) {
          for (int xPos = fromX; xPos <= toX; ++xPos) {
            if (x == xPos && y == yPos && z == zPos)
              continue;
            else if (active(xPos, yPos, zPos))
              ++sum;
          }
        }
      }

      return sum;
    }

    public void printCubes() {
      for (int z = startZ; z < endZ; ++z) {
        System.out.println("z=" + z + ":");
        for (int y = startY; y < endY; ++y) {
          for (int x = startX; x < endX; ++x) {
            char c = active(x, y, z) ? '#' : '.';
            System.out.print(c);
          }
          System.out.print("\n");
        }
      }
    }

    public long activeCubes() {
      return _activeCubes;
    }

    long _activeCubes = 0;

    private boolean[] staging;
    private boolean[] cubes;

    private int inputWidth;
    private int inputHeight;

    private int maxWidth;
    private int maxHeight;
    private int maxDepth;

    private int startX;
    private int startY;
    private int startZ;

    private int endX;
    private int endY;
    private int endZ;

  }

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").collect(Collectors.toList());

    final int simulationSteps = 6;

    int inputWidth = data.size();
    int inputHeight = data.get(0).length();

    var state = new State(inputWidth, inputHeight, simulationSteps);
    state.initialize(data);

    System.out.println("Starting with " + state.activeCubes() + " active cubes.");

    for (int i = 0; i < simulationSteps; ++i) {
      state.runStep();
    }

    System.out.println(state.activeCubes());
    System.out.println("Done");
  }
}