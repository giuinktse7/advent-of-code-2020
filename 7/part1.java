import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import java.util.stream.Stream;

class Part1 {

  static BagType getOrCreateBag(HashMap<String, BagType> map, String name) {
    if (!map.containsKey(name)) {
      map.put(name, new BagType());
    }

    return map.get(name);
  }

  public static void main(String[] args) throws IOException {
    Stream<String> lines = Parser.lines("./input.txt");

    var zero = new HashMap<String, BagType>();
    var map = lines.reduce(zero, (acc, next) -> {
      var parts = next.split(" bags contain ");
      String name = parts[0];
      String reqs = parts[1];
      var bag = getOrCreateBag(acc, name);

      var content = BagContent.fromString(reqs);
      bag.content = content;
      for (var c : content) {
        var childBag = getOrCreateBag(acc, c.name);
        childBag.addParent(name);
      }

      return acc;
    }, (m1, m2) -> {
      m1.putAll(m2);
      return m1;
    });

    Stack<String> parents = new Stack<>();

    var shinyGold = map.get("shiny gold");
    parents.addAll(shinyGold.parents);

    int count = 0;

    while (!parents.isEmpty()) {
      var parent = parents.pop();
      if (!map.containsKey(parent))
        continue;

      parents.addAll(map.get(parent).parents);
      map.remove(parent);

      ++count;
    }

    System.out.println(count);
  }
}