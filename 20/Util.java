public class Util {
  private static StringBuilder borderStringBuilder = new StringBuilder();

  public static String showBorder(int border) {
    borderStringBuilder.delete(0, borderStringBuilder.length());
    for (int i = 9; i >= 0; --i) {
      borderStringBuilder.append((border & (1 << i)) == 0 ? '.' : '#');
    }

    return borderStringBuilder.toString();
  }
}
