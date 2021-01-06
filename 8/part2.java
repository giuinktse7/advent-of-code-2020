import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

enum OpCode {
  Acc, Jmp, Nop
}

class Instruction {
  public OpCode code;
  public int value;
}

class Code {
  public Code(Stream<String> lines) {
    instructions = new ArrayList<>();

    lines.forEach(line -> {
      var parts = line.split(" ");

      var instruction = new Instruction();
      String op = parts[0];
      switch (op) {
        case "acc":
          instruction.code = OpCode.Acc;
          break;
        case "jmp":
          instruction.code = OpCode.Jmp;
          break;
        case "nop":
          instruction.code = OpCode.Nop;
          break;
      }

      instruction.value = Integer.parseInt(parts[1]);

      instructions.add(instruction);
    });
  }

  public int size() {
    return instructions.size();
  }

  public Instruction instruction(int index) {
    return instructions.get(index);
  }

  public void setMutation(int index) {
    if (mutated != -1) {
      swapCode(mutated);
    }

    swapCode(index);
    mutated = index;
  }

  private void swapCode(int index) {
    var instruction = instructions.get(index);
    instruction.code = instruction.code == OpCode.Jmp ? OpCode.Nop : OpCode.Jmp;
  }

  int mutated = -1;
  private List<Instruction> instructions;
}

class Memory {
  Memory(int codeSize) {
    accumulatedValues = new long[codeSize];
    cursors = new ArrayList<>();

    Arrays.fill(accumulatedValues, -1);
  }

  public int cycles() {
    return cursors.size();
  }

  public long value() {
    if (cursors.isEmpty())
      return 0;

    long value = accumulatedValues[cursors.get(cursors.size() - 1)];
    return Math.max(value, 0);
  }

  public void record(int cursor, long accumulatedValue) {
    cursors.add(cursor);
    accumulatedValues[cursor] = accumulatedValue;
  }

  public boolean visited(int cursor) {
    return accumulatedValues[cursor] != -1;
  }

  public int undo() {
    int index = cursors.size() - 1;

    int cursor = cursors.get(index);
    accumulatedValues[cursor] = -1;
    cursors.remove(index);

    return cursor;
  }

  public long[] accumulatedValues;
  public ArrayList<Integer> cursors;
}

class Interpreter {
  Interpreter(Code code) {
    this.code = code;
    this.cursor = 0;

    this.memory = new Memory(code.size());
  }

  boolean run() {
    long accumulator = memory.value();

    while (cursor < code.size() && !memory.visited(cursor)) {
      var instruction = code.instruction(cursor);

      switch (instruction.code) {
        case Acc:
          accumulator += instruction.value;
          break;
        case Jmp:
          memory.record(cursor, accumulator);
          cursor += instruction.value;
          continue;
        default:
          break;
      }

      memory.record(cursor, accumulator);
      ++cursor;
    }

    return cursor == code.size();
  }

  public void rollback() {
    int rollbackCursor = 0;

    do {
      rollbackCursor = memory.undo();
    } while (memory.cycles() >= maxRollbackCycle || code.instruction(rollbackCursor).code == OpCode.Acc);

    maxRollbackCycle = memory.cycles();
    cursor = rollbackCursor;
  }

  public Code code;
  public Memory memory;

  public int cursor;

  public int maxRollbackCycle = Integer.MAX_VALUE;
};

class Part2 {

  static void interpreterVersion() throws IOException {
    var lines = Parser.lines("./input.txt");
    var code = new Code(lines);

    var interpreter = new Interpreter(code);
    boolean success = interpreter.run();

    if (!success) {
      do {
        interpreter.rollback();

        interpreter.code.setMutation(interpreter.cursor);
      } while (!interpreter.run());
    }

    System.out.println("Accumulator: " + interpreter.memory.value());
  }

  static void swapCode(List<String> lines, int index) {
    var line = lines.get(index);
    if (line.contains("jmp")) {
      lines.set(index, line.replace("jmp", "nop"));
    } else {
      lines.set(index, line.replace("nop", "jmp"));
    }
  }

  static void bruteForceVersion() throws IOException {
    var lines = Parser.lines("./input.txt").collect(Collectors.toList());
    Queue<Integer> replaceIndices = new ArrayDeque<>();

    for (int i = 0; i < lines.size(); ++i) {
      if (!lines.get(i).contains("acc"))
        replaceIndices.add(i);
    }

    int prevIndex = -1;
    while (!replaceIndices.isEmpty()) {
      int index = replaceIndices.poll();
      if (prevIndex != -1)
        swapCode(lines, prevIndex);

      swapCode(lines, index);

      boolean[] visited = new boolean[lines.size()];

      int cursor = 0;
      long accumulated = 0;
      while (cursor != lines.size() && !visited[cursor]) {
        visited[cursor] = true;

        var parts = lines.get(cursor).split(" ");
        String op = parts[0];
        int value = Integer.parseInt(parts[1]);

        if (op.equals("acc")) {
          accumulated += value;
        } else if (op.equals("jmp")) {
          cursor += value;
          continue;
        }
        ++cursor;
      }

      if (cursor == lines.size()) {
        System.out.println("Accumulator: " + accumulated);
        break;
      }

      prevIndex = index;
    }
  }

  public static void main(String[] args) throws IOException {
    long startTime = System.nanoTime();
    interpreterVersion();
    // bruteForceVersion();
    long endTime = System.nanoTime();
    long duration = (endTime - startTime) / 1000000;
    System.out.println(duration + " ms");
  }
}