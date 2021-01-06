import java.io.IOException;
import java.util.stream.Collectors;

enum ActionType {
  North, East, West, South, Left, Right, Forward
}

class Action {
  public ActionType type;
  public int value;

  public static Action fromString(String s) {
    var action = new Action();
    action.value = Integer.parseInt(s.substring(1));

    switch (s.charAt(0)) {
      case 'N':
        action.type = ActionType.North;
        break;
      case 'S':
        action.type = ActionType.South;
        break;
      case 'E':
        action.type = ActionType.East;
        break;
      case 'W':
        action.type = ActionType.West;
        break;
      case 'L':
        action.type = ActionType.Left;
        break;
      case 'R':
        action.type = ActionType.Right;
        break;
      case 'F':
        action.type = ActionType.Forward;
        break;
    }

    return action;
  }
}

class Ship {
  public Ship() {
    _directionDegrees = 0;
    _x = 0;
    _y = 0;
  }

  void doAction(Action action) {
    switch (action.type) {
      case North:
        _y -= action.value;
        break;

      case South:
        _y += action.value;
        break;

      case East:
        _x += action.value;
        break;

      case West:
        _x -= action.value;
        break;

      case Left:
        _directionDegrees += action.value;
        break;

      case Right:
        _directionDegrees -= action.value;
        break;

      case Forward: {
        var rad = _directionDegrees * Math.PI / 180;
        var dx = (int) Math.round(action.value * Math.cos(rad));
        var dy = -(int) Math.round(action.value * Math.sin(rad));
        _x += dx;
        _y += dy;
        break;
      }
    }
  }

  public int x() {
    return _x;
  }

  public int y() {
    return _y;
  }

  private int _directionDegrees;
  private int _x;
  private int _y;

  public String toString() {
    return "x: " + _x + ", y: " + _y + ", rotation: " + _directionDegrees;
  }
}

class Part1 {

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").map(Action::fromString).collect(Collectors.toList());

    var ship = new Ship();

    for (var action : data)
      ship.doAction(action);

    var answer = Math.abs(ship.x()) + Math.abs(ship.y());
    System.out.println(answer);
  }
}