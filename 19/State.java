public class State {
  char peek() {
    return expression.charAt(cursor);
  }

  boolean hasNextToken() {
    return cursor < expression.length();
  }

  void consumeToken() {
    ++cursor;
    maxCursor = Math.max(maxCursor, cursor);
  }

  void reset() {
    cursor = 0;
    maxCursor = 0;
    previous = null;
    previousCursor = -1;
  }

  public int cursor;
  public int maxCursor = 0;
  public String expression;

  public LeafNode previous = null;
  public int previousCursor = -1;
}

class PathChoice {
  public PathChoice(int rule, int subrule) {
    this.rule = rule;
    this.subrule = subrule;
  }

  int rule;
  int subrule;

  public boolean equals(Object obj) {
    if (!(obj instanceof PathChoice))
      return false;

    var that = (PathChoice) obj;
    return this.rule == that.rule && this.subrule == that.subrule;
  }
}