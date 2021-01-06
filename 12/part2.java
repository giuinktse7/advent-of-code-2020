import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

class Waypoint {
  public Waypoint(int x, int y) {
    this.x = x;
    this.y = y;
  }

  void rotate(int degrees) {
    if (degrees == 90) {
      int tempX = x;
      x = -y;
      y = tempX;
    } else if (degrees == 180) {
      x = -x;
      y = -y;
    } else if (degrees == 270) {
      int tempX = x;
      x = y;
      y = -tempX;
    }
  }

  public int x;
  public int y;
}

class Ship2 {
  public Ship2() {
    waypoint = new Waypoint(10, 1);
  }

  void doAction(Action action) {
    switch (action.type) {
      case North:
        waypoint.y += action.value;
        break;

      case South:
        waypoint.y -= action.value;
        break;

      case East:
        waypoint.x += action.value;
        break;

      case West:
        waypoint.x -= action.value;
        break;

      case Left:
        waypoint.rotate(action.value);
        break;

      case Right:
        waypoint.rotate(360 - action.value);
        break;

      case Forward: {
        _x += waypoint.x * action.value;
        _y += waypoint.y * action.value;
        break;
      }
    }
  }

  public final int x() {
    return _x;
  }

  public final int y() {
    return _y;
  }

  public Waypoint waypoint;

  private int _x = 0;
  private int _y = 0;
}

class Part2 {

  public static void main(String[] args) throws IOException {
    List<Action> data = Parser.lines("./input.txt").map(Action::fromString).collect(Collectors.toList());

    var start = System.nanoTime();
    var ship = new Ship2();

    for (int i = 0; i < data.size(); ++i)
      ship.doAction(data.get(i));

    var answer = Math.abs(ship.x()) + Math.abs(ship.y());

    var end = System.nanoTime();

    System.out.println(answer + " in " + ((end - start) / 1000) + " us");
  }
}