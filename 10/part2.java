import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

class Part2 {

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
    data.add(0);
    data.sort((a, b) -> (a - b));
    data.add(data.get(data.size() - 1) + 3);

    long multiplier = 1;
    BigInteger answer = BigInteger.valueOf(1);

    HashMap<Integer, ArrayList<Integer>> branches = new HashMap<>();
    HashMap<Integer, Boolean> canDrop = new HashMap<>();

    for (int i = 0; i < data.size(); ++i) {
      int value = data.get(i);

      if (i == 0 || i == data.size() - 1) {
        canDrop.put(value, false);
      } else {
        canDrop.put(value, value - data.get(i - 1) < 3 && data.get(i - 1) - value < 3);
      }

      branches.put(value, new ArrayList<>());

      int k = i + 1;
      while (k < data.size()) {
        int diff = data.get(k) - value;
        if (diff > 3) {
          break;
        }

        branches.get(value).add(data.get(k));

        ++k;
      }
    }

    HashMap<Integer, Long> sums = new HashMap<>();

    for (int i = data.size() - 1; i >= 0; --i) {
      int key = data.get(i);
      var list = branches.get(key);
      if (list.isEmpty()) {
        sums.put(key, 1L);
      } else if (list.size() == 1) {
        sums.put(key, sums.get(list.get(0)));
      } else {
        long sum = 0;
        for (int value : list) {
          sum += sums.get(value);
          if (!canDrop.containsKey(value)) {
            break;
          }
        }

        sums.put(key, sum);
      }
    }

    System.out.println(sums.get(0));

    // ArrayList<Integer> branches = new ArrayList<>();

    // May not branch on min-diff

    // int prevChoices = 0;
    // int i = 0;
    // while (i < data.size()) {
    // int curr = data.get(i);
    // int minDiff = (int) (curr - prev);
    // int diff = minDiff;

    // int choices = 0;
    // int k = i;
    // while (diff <= 3) {
    // int pattern = 1 << (diff - 1);

    // choices |= pattern;
    // ++k;
    // diff = (int) (data.get(k) - prev);
    // }

    // branches.add(Integer.bitCount(choices));

    // i += minDiff;
    // prevChoices = choices;
    // prev = curr;
    // }

    // System.out.println(differences.get(1) * (differences.get(3) + 1));
    System.out.println("Done");
  }
}