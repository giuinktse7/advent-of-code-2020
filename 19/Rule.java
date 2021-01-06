import java.util.ArrayList;
import java.util.regex.Pattern;

enum RuleType {
  Composite, Token
}

public class Rule {

  private static final Pattern pattern = Pattern.compile("(\\d+(?: \\d+)*)");

  public static Rule parseRule(String ruleString) {
    if (ruleString.equals("8: 42")) {
      ruleString = "8: 42 | 42 8";
    } else if (ruleString.equals("11: 42 31")) {
      ruleString = "11: 42 31 | 42 11 31";
    }

    ArrayList<ArrayList<Integer>> components = new ArrayList<>();

    var parts = ruleString.split(":");
    int id = Integer.parseInt(parts[0]);
    boolean isTokenRule = parts[1].charAt(1) == '"';

    if (isTokenRule) {
      var token = parts[1];

      return new Rule(id, ruleString, token);
    }

    // Component rule

    var match = pattern.matcher(parts[1]);
    while (match.find()) {
      var ids = match.group(0).split(" ");
      ArrayList<Integer> component = new ArrayList<>();
      components.add(component);

      for (int i = 0; i < ids.length; ++i)
        component.add(Integer.parseInt(ids[i]));
    }

    return new Rule(id, ruleString, components);
  }

  private String translateTokens(String s) {
    int size = s.length();
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < size; ++i) {
      char c = s.charAt(i);

      // 14
      if (i == size - 2 && c == '1' && s.charAt(i + 1) == '4') {
        builder.append("b");
        ++i;
      } else if (i > 0 && i < size - 2 && c == '1' && s.charAt(i + 1) == '4' && s.charAt(i - 1) == ' '
          && s.charAt(i + 2) == ' ') {
        builder.append("b");
        ++i;
      } else if (i == size - 1 && c == '1' && s.charAt(i - 1) == ' ') {
        builder.append("a");
      } else if (i > 0 && i < size - 1 && c == '1' && s.charAt(i - 1) == ' ' && s.charAt(i + 1) == ' ') {
        builder.append("a");
      } else {
        builder.append(c);
      }
    }

    return builder.toString();
  }

  private Rule(int id, String ruleText, ArrayList<ArrayList<Integer>> components) {
    this.id = id;
    this.type = RuleType.Composite;
    this.ruleText = translateTokens(ruleText.split(":")[1]);
    this.subrules = components;
  }

  private Rule(int id, String ruleText, String token) {
    this.token = token.charAt(2);

    this.id = id;
    this.type = RuleType.Token;
    this.ruleText = ruleText;
  }

  public String toString() {
    return id + " (" + ruleText + ")";
  }

  public int id;
  public String ruleText;
  public RuleType type;

  public char token;
  public ArrayList<ArrayList<Integer>> subrules;
}
