import java.util.ArrayList;

enum NodeType {
  Leaf, Sequential
}

enum ParseResult {
  Success, Fail
}

public abstract class RuleNode {
  public static Rule[] rules;

  public RuleNode(Rule rule, RuleNode previous) {
    this.rule = rule;
    this.previous = previous;
  }

  public void setRootIndentation() {
    this.indentation = 0;
  }

  SequentialNode nearestNonTokenNode() {
    if (nodeType() == NodeType.Sequential)
      return (SequentialNode) this;

    var result = previous;
    while (result != null && result.nodeType() != NodeType.Sequential)
      result = result.previous;

    if (result == null) {
      throw new RuntimeException("Should never happen.");
    }

    return (SequentialNode) result;
  }

  abstract ParseResult parse(State state);

  abstract NodeType nodeType();

  public abstract String showState();

  public String showFullPath() {
    StringBuilder builder = new StringBuilder();
    ArrayList<RuleNode> nodes = new ArrayList<>();
    RuleNode node = this;
    while (node != null) {
      if (node.nodeType() != NodeType.Leaf)
        nodes.add(node);
      node = node.previous;
    }

    for (int i = nodes.size() - 1; i >= 0; --i) {
      node = nodes.get(i);
      builder.append(node.showState());
      if (i > 0) {
        builder.append(" -> ");
      }
    }

    return builder.toString();
  }

  static RuleNode create(int cursor, int ruleId, SequentialNode groupNode, RuleNode previous, int indentation) {
    var rule = rules[ruleId];
    var node = rule.type == RuleType.Token ? new LeafNode(rule, previous) : new SequentialNode(rule, previous);
    node.indentation = indentation;
    node.groupNode = groupNode;
    node.cursor = cursor;
    return node;
  }

  boolean hasDanglingTokens(State state) {
    var node = nearestNonTokenNode();
    while (node.previous != null && !node.hasRemainingTokens())
      node = node.previous.nearestNonTokenNode();

    return (node.previous == null && !node.hasRemainingTokens()) && state.hasNextToken();
  }

  protected final Rule rule;
  RuleNode previous;
  protected int cursor;

  SequentialNode groupNode;

  protected int indentation;
}

class SequentialNode extends RuleNode {
  public SequentialNode(Rule rule, RuleNode previous) {
    super(rule, previous);
  }

  public String showState() {
    return rule.id + " (" + subrule + ": " + rule.ruleText.split("\\|")[subrule].trim() + ")";
  }

  public String showSubRule() {
    return rule.ruleText.split("\\|")[subrule].trim();
  }

  NodeType nodeType() {
    return NodeType.Sequential;
  }

  RuleNode getOrCreateNode(State state, int index) {
    if (index > nodes.size()) {
      throw new RuntimeException("Should never happen.");
    }

    if (index < nodes.size())
      return nodes.get(index);

    int ruleId = rule.subrules.get(subrule).get(index);
    var previous = index == 0 ? this : state.previous;
    var node = RuleNode.create(state.cursor, ruleId, this, previous, this.indentation + 1);
    nodes.add(node);
    return node;
  }

  private int currentRuleCount() {
    return rule.subrules.get(subrule).size();
  }

  private ParseResult nextBranch(State state, RuleNode failedNode) {
    var node = failedNode.previous;
    while (node != this) {
      if (node.nodeType() == NodeType.Leaf) {
        node = node.previous;
        continue;
      }

      var pathChanged = ((SequentialNode) node).nextRuleset(state);
      if (pathChanged) {
        state.cursor = node.cursor;
        var newResult = node.parse(state);
        if (newResult == ParseResult.Success) {
          return newResult;
        } else {
          // Must make a different change
        }
      }

      node = node.previous;
    }

    return ParseResult.Fail;
  }

  ParseResult parse(State state) {
    if (!state.hasNextToken()) {
      return ParseResult.Fail;
    }

    for (int i = 0; i < currentRuleCount(); ++i) {
      nodeIndex = i;
      var node = getOrCreateNode(state, i);
      var result = node.parse(state);
      switch (result) {
        case Success:
          break;
        case Fail:
          --nodeIndex;
          if (nextBranch(state, node) == ParseResult.Success) {
            // A failed node must always be removed because something before it will change
            // (or we fail entirely)
            nodes.remove(i);
            --i;
          } else {
            // We got to "this". Resolve here.
            state.cursor = cursor;
            if (!nextRuleset(state)) {
              nodeIndex = -1;
              return ParseResult.Fail;
            } else {
              // -1 because the for loop will bump it to 0
              i = -1;
            }
          }
          break;
      }
    }

    return ParseResult.Success;
  }

  private boolean nextRuleset(State state) {
    if (subrule == rule.subrules.size() - 1) {
      return false;
    }

    ++subrule;

    nodes.clear();

    return true;
  }

  ArrayList<RuleNode> nodes = new ArrayList<>();
  public int subrule = 0;

  private int nodeIndex = -1;

  boolean hasRemainingTokens() {
    return (nodeIndex == -1) || (nodeIndex != currentRuleCount() - 1);
  }
}

class LeafNode extends RuleNode {
  public LeafNode(Rule rule, RuleNode previous) {
    super(rule, previous);
  }

  ParseResult parse(State state) {
    if (!state.hasNextToken() || rule.token != state.peek()) {
      return ParseResult.Fail;
    }

    state.consumeToken();
    if (hasDanglingTokens(state)) {
      return ParseResult.Fail;
    }

    state.previous = this;

    return ParseResult.Success;
  }

  public String showState() {
    return rule.token + "";
  }

  NodeType nodeType() {
    return NodeType.Leaf;
  }
}