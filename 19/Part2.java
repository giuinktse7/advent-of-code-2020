import java.io.IOException;
import java.util.stream.Collectors;

class Part2 {

  public static void main(String[] args) throws IOException {
    var start = System.nanoTime();
    String input = "./input.txt";
    var data = Parser.lines(input).collect(Collectors.toList());

    RuleNode.rules = new Rule[256];

    int cursor = 0;
    String line = data.get(cursor);
    while (!line.isEmpty()) {
      var rule = Rule.parseRule(line);
      RuleNode.rules[rule.id] = rule;

      line = data.get(++cursor);
    }

    // Skip empty line
    ++cursor;

    int matches = 0;
    State state = new State();
    for (; cursor < data.size(); ++cursor) {
      line = data.get(cursor);
      state.cursor = 0;
      state.expression = line;
      var node = RuleNode.create(0, 0, null, null, 0);

      var result = node.parse(state);

      switch (result) {
        case Success:
          ++matches;
          break;
        default:
          break;
      }

      state.reset();
    }
    var end = System.nanoTime();
    System.out.println(matches + " (" + Parser.showTime(start, end) + ")");
  }
}