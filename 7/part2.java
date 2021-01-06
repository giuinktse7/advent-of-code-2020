import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.stream.Stream;

class Part2 {

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

      return acc;
    }, (m1, m2) -> {
      m1.putAll(m2);
      return m1;
    });

    Stack<BagContent> bags = new Stack<>();

    bags.addAll(map.get("shiny gold").content);

    int count = 0;
    int multiplier = 1;

    while (!bags.isEmpty()) {
      var bagContent = bags.peek();
      if (bagContent.opened) {
        multiplier /= bagContent.count;
        bags.pop();
        continue;
      }

      bagContent.opened = true;
      var bag = map.get(bagContent.name);

      count += bagContent.count * multiplier;

      if (bag.content.isEmpty()) {
        bags.pop();
        continue;
      }

      for (var c : bag.content) {
        bags.add(c.copy());
      }

      multiplier *= bagContent.count;
    }

    System.out.println(count);
  }
}