import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

class Part2 {

  static class State {

    State(int inputWidth, int inputHeight, int simulationSteps) {
      int requiredWidth = simulationSteps * 2 + inputWidth;
      int requiredHeight = simulationSteps * 2 + inputHeight;

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

      this.cubes = new boolean[maxWidth * maxHeight * maxDepth * maxDepth];
      this.staging = new boolean[maxWidth * maxHeight * maxDepth * maxDepth];
    }

    void initialize(List<String> data) {
      for (int row = 0; row < data.size(); ++row) {
        var line = data.get(row);

        for (int column = 0; column < line.length(); ++column) {
          if (line.charAt(column) == '#')
            setActive(column - inputWidth / 2, row - inputHeight / 2, 0, 0, true);
        }
      }
    }

    private int posToIndex(int x, int y, int z, int h) {
      return maxWidth * (maxHeight * ((h + endZ) * maxDepth + (z + endZ)) + (y + endY)) + x + endX;
    }

    void setActive(int x, int y, int z, int h, boolean active) {
      var index = posToIndex(x, y, z, h);

      if (cubes[index] && !active) {
        --_activeCubes;
      } else if (!cubes[index] && active) {
        ++_activeCubes;
      }

      cubes[index] = active;
      staging[index] = active;
    }

    boolean active(int x, int y, int z, int h) {
      return cubes[posToIndex(x, y, z, h)];
    }

    boolean active(int index) {
      return cubes[index];
    }

    public void runStep() {
      for (int h = startZ; h < endZ; ++h) {
        for (int z = startZ; z < endZ; ++z) {
          for (int y = startY; y < endY; ++y) {
            var baseIndex = posToIndex(0, y, z, h);

            for (int x = startX; x < endX; ++x) {
              var index = baseIndex + x;
              var activeNeighbors = getActiveNeighbors(x, y, z, h);

              if (active(index)) {
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
      }

      System.arraycopy(staging, 0, cubes, 0, staging.length);
    }

    private int getActiveNeighbors(int x, int y, int z, int h) {
      int fromX = Math.max(startX, x - 1);
      int fromY = Math.max(startY, y - 1);
      int fromZ = Math.max(startZ, z - 1);
      int fromH = Math.max(startZ, h - 1);

      int toX = Math.min(endX - 1, x + 1);
      int toY = Math.min(endY - 1, y + 1);
      int toZ = Math.min(endZ - 1, z + 1);
      int toH = Math.min(endZ - 1, h + 1);

      int sum = 0;

      for (int hPos = fromH; hPos <= toH; ++hPos) {
        for (int zPos = fromZ; zPos <= toZ; ++zPos) {
          for (int yPos = fromY; yPos <= toY; ++yPos) {
            for (int xPos = fromX; xPos <= toX; ++xPos) {
              if (x == xPos && y == yPos && z == zPos && h == hPos)
                continue;
              else if (active(xPos, yPos, zPos, hPos))
                ++sum;
            }
          }
        }
      }

      return sum;
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
    var start = System.nanoTime();
    var data = Parser.lines("./input.txt").collect(Collectors.toList());

    final int simulationSteps = 6;

    int inputWidth = data.size();
    int inputHeight = data.get(0).length();

    var state = new State(inputWidth, inputHeight, simulationSteps);
    state.initialize(data);

    for (int i = 0; i < simulationSteps; ++i) {
      state.runStep();
    }

    var activeCubes = state.activeCubes();
    var end = System.nanoTime();

    System.out.println(activeCubes + " (" + Parser.showTime(start, end) + ").");
  }
}