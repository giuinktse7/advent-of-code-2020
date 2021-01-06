import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

class Part1 {
  static class CupCircle {
    public CupCircle(String input) {
      Arrays.fill(liftedCups, -1);
      for (int i = 0; i < input.length(); ++i) {
        int label = Integer.parseInt(input.charAt(i) + "");
        minLabel = Math.min(minLabel, label);
        maxLabel = Math.max(maxLabel, label);
        cups.add(label);
      }
    }

    private int nextIndex(int index) {
      return index < cups.size() - 1 ? index + 1 : 0;
    }

    boolean pickedUp(int label) {
      return liftedCups[0] == label || liftedCups[1] == label || liftedCups[2] == label;
    }

    private int destinationFromLabel(int label) {
      while (label < minLabel || pickedUp(label)) {
        if (label < minLabel)
          label = maxLabel;

        if (pickedUp(label)) {
          --label;
        }
      }

      return label;
    }

    public String showCups() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < cups.size(); ++i) {
        if (cursor == i)
          sb.append("(" + cups.get(i) + ")");
        else
          sb.append(cups.get(i));

        if (i < cups.size() - 1)
          sb.append(" ");
      }

      return sb.toString();
    }

    public void move(int step) {
      int currentLabel = cups.get(cursor);

      for (int i = 0; i < 3; ++i) {
        liftedCups[i] = cups.remove(nextIndex(cursor));
      }

      int dest = destinationFromLabel(currentLabel - 1);
      int destIndex = cups.indexOf(dest);

      int insertionIndex = nextIndex(destIndex);
      cups.add(insertionIndex, liftedCups[2]);
      cups.add(insertionIndex, liftedCups[1]);
      cups.add(insertionIndex, liftedCups[0]);

      cursor = nextIndex(cups.indexOf(currentLabel));
    }

    public String collect() {
      StringBuilder sb = new StringBuilder();
      int start = cups.indexOf(1);
      int i = start + 1;
      do {
        sb.append(cups.get(i));
        i = nextIndex(i);
      } while (i != start);

      return sb.toString();
    }

    private int cursor = 0;
    LinkedList<Integer> cups = new LinkedList<>();
    private int[] liftedCups = new int[3];

    private int minLabel = Integer.MAX_VALUE;
    private int maxLabel = 0;
  }

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").collect(Collectors.toList());

    var circle = new CupCircle(data.get(0));
    for (int i = 0; i < 100; ++i) {
      circle.move(i + 1);
    }

    System.out.println(circle.collect());
  }
}