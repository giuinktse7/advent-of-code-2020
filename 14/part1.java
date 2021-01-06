import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Program {

  void set(int ptr, long value) {
    value &= mask0;
    value |= mask1;
    memory.put(ptr, value);
  }

  void setMask(String mask) {
    mask0 = Long.MAX_VALUE;
    mask1 = 0;

    int end = mask.length() - 1;

    for (int i = end; i >= 0; --i) {
      int maskBitPos = end - i;
      switch (mask.charAt(i)) {
        case '0':
          mask0 &= ~(1L << maskBitPos);
          break;
        case '1':
          mask1 |= (1L << maskBitPos);
          break;
        default:
          break;
      }
    }
  }

  long memorySum() {
    return memory.values().stream().mapToLong(x -> x).sum();
  }

  private long mask0 = Long.MAX_VALUE;
  private long mask1 = 0;

  HashMap<Integer, Long> memory = new HashMap<>();
}

class Part1 {

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").collect(Collectors.toList());

    var pattern = Pattern.compile("\\[(\\d+)\\] = (\\d+)");
    var program = new Program();

    for (var line : data) {
      if (line.startsWith("ma")) {
        String mask = line.substring(7);
        program.setMask(mask);
      } else {
        Matcher m = pattern.matcher(line);
        m.find();
        int address = Integer.parseInt(m.group(1));
        long value = Long.parseLong(m.group(2));
        program.set(address, value);
      }
    }

    System.out.println(program.memorySum());
  }
}