import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Part1 {
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

    var possibleAllergens = supply.allergenMap.values().stream().flatMap(x -> x.stream()).collect(Collectors.toSet());

    var result = supply.foods.stream().flatMapToInt(x -> Arrays.stream(x.ingredients));
    var res2 = result.filter(x -> !possibleAllergens.contains(x)).count();
    System.out.println(res2);
    System.out.println("Done");
  }
}