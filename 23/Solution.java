public class Solution {
  public static final int size = (int) Math.pow(10, 6);

  public Solution(String input) {
    int firstLabel = Integer.parseInt(input.charAt(0) + "");

    currentLabel = firstLabel;
    nextLabel[size - 1] = firstLabel;
    int prevLabel = firstLabel;

    for (int i = 1; i < input.length(); ++i) {
      int label = Integer.parseInt(input.charAt(i) + "");
      nextLabel[prevLabel - 1] = label;
      prevLabel = label;
    }

    nextLabel[prevLabel - 1] = 10;

    int i = 10;
    while (i < size) {
      nextLabel[i - 1] = i + 1;
      ++i;
    }
  }

  public void move() {
    int p0 = getNextLabel(currentLabel);
    int p1 = getNextLabel(p0);
    int p2 = getNextLabel(p1);
    int nextLabelForCurrent = getNextLabel(p2);

    int destLabel = currentLabel;

    do {
      --destLabel;
      if (destLabel < 1)
        destLabel = size;
    } while (destLabel == p0 || destLabel == p1 || destLabel == p2);

    setNextLabel(currentLabel, nextLabelForCurrent);

    int nextFromDest = getNextLabel(destLabel);
    setNextLabel(destLabel, p0);
    setNextLabel(p2, nextFromDest);

    currentLabel = getNextLabel(currentLabel);
  }

  private void setNextLabel(int label, int next) {
    nextLabel[label - 1] = next;
  }

  private int getNextLabel(int label) {
    return nextLabel[label - 1];
  }

  public long answer() {
    int l1 = getNextLabel(1);
    long l2 = getNextLabel(l1);

    return l1 * l2;
  }

  int currentLabel;
  int[] nextLabel = new int[1000000];
}
