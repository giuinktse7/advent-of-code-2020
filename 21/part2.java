import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Part2 {
  static class Food {
    public Food(int[] ingredients, String[] allergens) {
      this.ingredients = ingredients;
      this.allergens = allergens;
    }

    public int[] ingredients;
    public String[] allergens;
  }

  static class Supply {
    private static int nextId = 0;

    public void addFood(String[] ingredients, String[] allergens) {
      int[] ingredientIds = new int[ingredients.length];

      for (int i = 0; i < ingredients.length; ++i) {
        var ingredient = ingredients[i];
        if (!ingredientMap.containsKey(ingredient)) {
          ingredientMap.put(ingredient, nextId);
          reverseIngredientMap.put(nextId, ingredient);
          ++nextId;
        }

        ingredientIds[i] = ingredientMap.get(ingredient);
      }

      foods.add(new Food(ingredientIds, allergens));

      var set = Arrays.stream(ingredients).map(ingredientMap::get).collect(Collectors.toSet());

      for (var allergen : allergens) {
        if (!allergenMap.containsKey(allergen)) {
          allergenMap.put(allergen, new HashSet<>(set));
        } else {
          allergenMap.merge(allergen, set, (a, b) -> {
            a.retainAll(b);
            return a;
          });
        }
      }
    }

    public ArrayList<Food> foods = new ArrayList<>(64);

    public HashMap<String, Set<Integer>> allergenMap = new HashMap<>();

    public HashMap<String, Integer> ingredientMap = new HashMap<>();
    public HashMap<Integer, String> reverseIngredientMap = new HashMap<>();
  }

  public static void main(String[] args) throws IOException {
    var start = System.nanoTime();
    var data = Parser.lines("./input.txt").collect(Collectors.toList());
    var pattern = Pattern.compile("(.*)\\(contains(.*)\\)");

    var supply = new Supply();
    for (var line : data) {
      var m = pattern.matcher(line);
      m.find();
      var ingredients = m.group(1).trim().split(" ");
      var allergens = m.group(2).trim().split(", ");
      supply.addFood(ingredients, allergens);
    }

    HashMap<Integer, Set<String>> allergenReverseMap = new HashMap<>();
    for (var entry : supply.allergenMap.entrySet()) {
      var allergen = entry.getKey();
      for (int i : entry.getValue()) {
        if (!allergenReverseMap.containsKey(i)) {
          HashSet<String> set = new HashSet<>();
          set.add(allergen);
          allergenReverseMap.put(i, set);
        } else {
          allergenReverseMap.get(i).add(allergen);
        }
      }
    }

    PriorityQueue<String> queue = new PriorityQueue<>(
        (a, b) -> supply.allergenMap.get(a).size() - supply.allergenMap.get(b).size());

    supply.allergenMap.keySet().forEach(queue::add);

    int i = supply.allergenMap.size();

    while (i > 0) {
      var currentAllergen = queue.poll();
      var ingredientId = supply.allergenMap.get(currentAllergen).iterator().next();

      allergenReverseMap.get(ingredientId).stream().filter(x -> x != currentAllergen).forEach(allergen -> {
        supply.allergenMap.get(allergen).remove(ingredientId);
        queue.remove(allergen);
        queue.add(allergen);
      });
      --i;
    }

    var allAllergens = supply.allergenMap.entrySet().stream().collect(Collectors.toList());
    allAllergens.sort((a, b) -> a.getKey().compareTo(b.getKey()));
    var k = allAllergens.stream().map(entry -> {
      var id = entry.getValue().iterator().next();
      return supply.reverseIngredientMap.get(id);
    }).collect(Collectors.joining(","));

    var end = System.nanoTime();
    System.out.println(k + " (" + Parser.showTime(start, end) + ")");

    System.out.println("Done");
  }
}