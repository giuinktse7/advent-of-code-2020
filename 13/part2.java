import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

class Target {
  Target(int offset, int id) {
    this.offset = offset;
    this.id = id;
  }

  public int offset;
  public int id;
}

class Part2 {
  static long modInverse(long base, long mod) {
    if (mod == 1)
      return 0;

    long current = base;
    long divisor = mod;

    long inverse = 1;
    long accumulated = 0;

    while (current > 1) {

      long quotient = current / divisor;
      long rem = current % divisor;

      current = divisor;
      divisor = rem;

      long temp = accumulated;
      accumulated = inverse - quotient * accumulated;
      inverse = temp;
    }

    return inverse < 0 ? inverse + mod : inverse;
  }

  public static void main(String[] args) throws IOException {
    var data = Parser.lines("./input.txt").collect(Collectors.toList());

    var ids = data.get(1).split(",");
    ArrayList<Target> targets = new ArrayList<>();
    long M = 1;

    for (int i = 0; i < ids.length; ++i) {
      if (ids[i].equals("x"))
        continue;

      var id = Integer.parseInt(ids[i]);
      targets.add(new Target(i, id));
      M *= id;
    }

    long sum = 0;
    for (var target : targets) {
      long base = M / target.id;
      sum += (target.id - target.offset) * modInverse(base, target.id) * base;
    }

    System.out.println(sum % M);
  }
}