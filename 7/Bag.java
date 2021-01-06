import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class BagContent {
  BagContent(int count, String name) {
    this.count = count;
    this.name = name;
  }

  public String name;
  public int count;
  public boolean opened = false;

  public static ArrayList<BagContent> fromString(String s) {
    var result = new ArrayList<BagContent>();

    Matcher m = Pattern.compile("([0-9])+ ([^ ]* [^ ]*) (bags|bag)").matcher(s);
    while (m.find()) {
      int count = Integer.parseInt(m.group(1));
      String name = m.group(2);
      result.add(new BagContent(count, name));
    }

    return result;
  }

  public BagContent copy() {
    return new BagContent(this.count, this.name);
  }
};

class BagType {
  public BagType() {
    this.content = new ArrayList<>();
    this.parents = new ArrayList<>();
  }

  public List<BagContent> content;
  public List<String> parents;

  public void addParent(String name) {
    parents.add(name);
  }
};