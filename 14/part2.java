import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Program2 {

  void set(long ptr, long value) {
    ptr &= mask0;
    ptr |= mask1;

    int endPattern = (int) Math.pow(2, floatingBits.size());
    for (int i = 0; i < endPattern; ++i) {
      long address = ptr;
      int offset = 0;
      int bitPattern = i;

      while (bitPattern > 0) {
        if ((bitPattern & 1) == 1) {
          address |= (1L << floatingBits.get(offset));
        }
        bitPattern >>= 1;
        ++offset;
      }

      memory.put(address, value);
    }
  }

  void setMask(String mask) {
    mask0 = Long.MAX_VALUE;
    mask1 = 0;
    floatingBits.clear();

    int end = mask.length() - 1;

    for (int i = end; i >= 0; --i) {
      int maskBitPos = end - i;
      switch (mask.charAt(i)) {
        case '0':
          break;
        case '1':
          mask1 |= (1L << maskBitPos);
          break;
        case 'X':
          floatingBits.add(maskBitPos);
          mask0 &= ~(1L << maskBitPos);
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
  private ArrayList<Integer> floatingBits = new ArrayList<>();

  HashMap<Long, Long> memory = new HashMap<>();
}

class Part2 {

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").collect(Collectors.toList());

    var pattern = Pattern.compile("\\[(\\d+)\\] = (\\d+)");
    var program = new Program2();

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