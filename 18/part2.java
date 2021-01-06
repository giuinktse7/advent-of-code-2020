import java.io.IOException;
import java.util.Stack;
import java.util.stream.Collectors;

class Part2 {
  enum Operator {
    Add, Multiply, GroupStart
  }

  private static final int MinPrecedence = 0;
  private static final int MaxPrecedence = 100;

  private static final int PrecedenceAdd = 10;
  private static final int PrecedenceMultiply = 5;
  private static final int PrecedenceGroupStart = MinPrecedence;

  static class PartialFunctionPool {
    int nextId = 0;
    PartialFunction[] partials;

    int recycleCursor = -1;
    int[] recycledIds;

    public PartialFunction partialFunction(Operator operator, long value) {
      if (recycleCursor != -1) {
        var partial = partials[recycledIds[recycleCursor]];
        partial.operator = operator;
        partial.value = value;

        --recycleCursor;

        return partial;
      } else {
        if (nextId >= partials.length) {
          throw new RuntimeException("Pool stack blown.");
        }
        int id = nextId;
        var partial = new PartialFunction(id, operator, value);
        partials[id] = partial;
        ++nextId;

        return partial;
      }
    }

    public PartialFunctionPool(int maxCount) {
      partials = new PartialFunction[maxCount];
      recycledIds = new int[maxCount];
    }

    void free(PartialFunction partial) {
      recycledIds[++recycleCursor] = partial.id;
    }

    private static class PartialFunction {
      protected PartialFunction(int id, Operator operator, long value) {
        this.id = id;
        this.value = value;
        this.operator = operator;
      }

      public long apply(long rhs) {
        switch (operator) {
          case Add:
            return value + rhs;
          case Multiply:
            return value * rhs;
          case GroupStart:
            return rhs;
          default:
            return rhs;
        }
      }

      int precedence() {
        switch (operator) {
          case Add:
            return PrecedenceAdd;
          case Multiply:
            return PrecedenceMultiply;
          case GroupStart:
            return PrecedenceGroupStart;
          default:
            return MaxPrecedence;
        }
      }

      private int id;
      public long value;
      public Operator operator;
    }
  }

  static class Interpreter {
    public Interpreter(int poolSize) {
      pool = new PartialFunctionPool(poolSize);
    }

    long interpret(String expression) {
      builder.delete(0, builder.length());
      char c;
      for (int i = 0; i < expression.length(); ++i) {
        c = expression.charAt(i);
        if (c == ' ')
          continue;

        builder.append(c);
      }
      this.code = builder.toString();

      partial = null;
      from = 0;
      to = 0;
      value = 0;
      callStack.clear();

      while (to < code.length()) {
        switch (code.charAt(to)) {
          case '(':
            applyUntil(MaxPrecedence);
            callStack.add(pool.partialFunction(Operator.GroupStart, 0));
            break;

          case ')':
            applyUntil(MinPrecedence);
            pool.free(callStack.pop());
            break;

          case '+':
            applyUntil(PrecedenceAdd);
            partial = pool.partialFunction(Operator.Add, value);
            break;

          case '*':
            applyUntil(PrecedenceMultiply);
            partial = pool.partialFunction(Operator.Multiply, value);
            break;

          default:
            ++to;
        }
      }

      applyUntil(MinPrecedence);

      return value;
    }

    private void applyUntil(int minPrecedence) {
      if (from != to) {
        value = parseInt(from, to);
      }

      if (partial != null) {
        if (partial.precedence() >= minPrecedence) {
          value = partial.apply(value);
          pool.free(partial);
          partial = null;

        } else {
          callStack.add(partial);
          partial = null;
          next();
          return;
        }
      }

      if (!callStack.empty()) {
        var top = callStack.peek();
        if (top.precedence() >= minPrecedence && top.operator != Operator.GroupStart) {
          do {
            top = callStack.pop();
            pool.free(top);
            value = top.apply(value);

            if (callStack.empty())
              break;

            top = callStack.peek();
          } while (!callStack.empty() && top.precedence() >= minPrecedence && top.operator != Operator.GroupStart);
        }
      }

      next();
    }

    private void next() {
      ++to;
      from = to;
    }

    private String code;

    private Stack<PartialFunctionPool.PartialFunction> callStack = new Stack<>();

    private long value;

    private int from;
    private int to;

    private PartialFunctionPool.PartialFunction partial;
    private PartialFunctionPool pool;

    private StringBuilder builder = new StringBuilder(200);

    private int parseInt(int from, int to) {
      int value = 0;
      for (;; value *= 10) {
        value += '0' - code.charAt(from);
        if (++from == to)
          return -value;

      }
    }

  }

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").collect(Collectors.toList());

    var start = System.nanoTime();
    final int poolSize = 10;
    var interpreter = new Interpreter(poolSize);

    long sum = 0;
    for (int i = 0; i < data.size(); ++i) {
      sum += interpreter.interpret(data.get(i));
    }

    var end = System.nanoTime();
    System.out.println(sum + " (" + Parser.showTimeMicro(start, end) + ").");
  }
}