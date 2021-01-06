import java.io.IOException;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Part1 {

  enum Operator {
    Add, Multiply, None
  }

  static class PartiallyApplied {
    PartiallyApplied() {
      this.operator = Operator.None;
    }

    PartiallyApplied(long value, Operator operator) {
      this.value = value;
      this.operator = operator;
    }

    public long apply(long rhs) {
      switch (operator) {
        case Add:
          return value + rhs;
        case Multiply:
          return value * rhs;
        default:
          return rhs;
      }
    }

    public long value;
    public Operator operator;
  }

  static class Interpreter {
    private static final Pattern valuePattern = Pattern.compile("^\\d+$");

    long interpret(String expression) {
      this.code = expression.replace(" ", "");

      partial = new PartiallyApplied();
      from = 0;
      to = 0;

      while (to < code.length()) {
        var c = code.charAt(to);
        switch (c) {
          case '(':
            consume();
            lhsStack.add(partial);
            partial = new PartiallyApplied();
            value = 0;
            break;
          case ')':
            consume();
            value = lhsStack.pop().apply(value);
            break;
          case '+':
            consumeOperator(Operator.Add);
            break;
          case '*':
            consumeOperator(Operator.Multiply);
            break;
          default:
            ++to;
        }
      }

      consume();

      return value;
    }

    private void consumeOperator(Operator op) {
      consume();
      partial.value = value;
      partial.operator = op;

      value = 0;
    }

    private void consume() {
      if (from == to) {
        next();
        return;
      }
      String token = code.substring(from, to);

      if (valuePattern.matcher(token).find()) {
        value = partial.apply(Integer.parseInt(token));
        partial.value = 0;
        partial.operator = Operator.None;
      }

      next();
    }

    private void next() {
      ++to;
      from = to;
    }

    private String code;

    private Stack<PartiallyApplied> lhsStack = new Stack<>();

    private long value;

    private int from;
    private int to;

    private PartiallyApplied partial;
  }

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").collect(Collectors.toList());

    var interpreter = new Interpreter();

    long sum = 0;
    for (var line : data) {
      var value = interpreter.interpret(line);
      // System.out.println(line + " ==> " + value);
      sum += value;
    }

    System.out.println("Sum: " + sum);
  }
}