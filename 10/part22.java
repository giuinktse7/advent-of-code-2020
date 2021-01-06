import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

class Part22 {

  static int getReach(List<Integer> data, int index) {
    int result = 0;

    int src = data.get(index);
    int k = index + 1;
    while (k < data.size() && data.get(k++) - src <= 3)
      ++result;

    return result;
  }

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
    data.add(0);
    data.sort((a, b) -> (a - b));
    data.add(data.get(data.size() - 1) + 3);

    long[] sums = new long[data.size()];

    for (int i = data.size() - 1; i >= 0; --i) {
      int reach = getReach(data, i);

      long sum = 0L;
      switch (reach) {
        case 0:
          sum = 1L;
          break;
        case 1:
          sum = sums[i + 1];
          break;
        default:
          for (int k = i + 1; k <= i + reach; ++k)
            sum += sums[k];
      }

      sums[i] = sum;
    }

    System.out.println(sums[0]);
  }
}