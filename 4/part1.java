import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class Password {
  public String byr; // (Birth Year)
  public String iyr; // (Issue Year)
  public String eyr; // (Expiration Year)
  public String hgt; // (Height)
  public String hcl; // (Hair Color)
  public String ecl; // (Eye Color)
  public String pid; // (Passport ID)
  public String cid; // (Country ID)

  public boolean valid1() {
    return byr != null && iyr != null && eyr != null && hgt != null && hcl != null && ecl != null && pid != null;
  }

  public boolean valid() {
    try {
      int _byr = Integer.parseInt(byr);
      if (!(_byr >= 1920 && _byr <= 2002))
        return false;

      int _iyr = Integer.parseInt(iyr);
      if (!(_iyr >= 2010 && _iyr <= 2020))
        return false;

      int _eyr = Integer.parseInt(eyr);
      if (!(_eyr >= 2020 && _iyr <= 2030))
        return false;

      if (hgt == null)
        return false;
      if (hgt.endsWith("cm")) {
        int value = Integer.parseInt(hgt.replace("cm", ""));
        if (!(value >= 150 && value <= 193))
          return false;
      } else if (hgt.endsWith("in")) {
        int value = Integer.parseInt(hgt.replace("in", ""));
        if (!(value >= 59 && value <= 76))
          return false;
      } else {
        return false;
      }

      if (hcl == null || !hcl.startsWith("#"))
        return false;
      // mb bad
      if (!hcl.matches("^#([a-fA-F0-9]{6})$"))
        return false;

      if (ecl == null)
        return false;
      switch (ecl) {
        case "amb":
        case "blu":
        case "brn":
        case "gry":
        case "grn":
        case "hzl":
        case "oth":
          break;
        default:
          return false;
      }

      if (!pid.matches("^([0-9]{9})$"))
        return false;

      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public String toString() {
    return "byr: " + byr + ", " + "iyr: " + iyr + ", " + "eyr: " + eyr + ", " + "hgt: " + hgt + ", " + "hcl: " + hcl
        + ", " + "ecl: " + ecl + ", " + "pid: " + pid + ", " + "cid: " + cid + "";
  }
}

class Part1 {

  public static void main(String[] args) throws IOException {
    Path path = FileSystems.getDefault().getPath("./input.txt");

    List<String> rows = Files.readAllLines(path);

    long count = 0;
    int index = 0;

    Password p = new Password();
    for (String row : rows) {
      System.out.println(row);
      if (row.isEmpty()) {
        if (p.valid1()) {
          ++count;
          System.out.println(index);
        }
        p = new Password();
        ++index;

        continue;
      }

      var fields = row.split(" ");
      for (int i = 0; i < fields.length; ++i) {
        var parts = fields[i].split(":");
        switch (parts[0]) {
          case "byr":
            p.byr = parts[1];
            break;
          case "iyr":
            p.iyr = parts[1];
            break;
          case "eyr":
            p.eyr = parts[1];
            break;
          case "hgt":
            p.hgt = parts[1];
            break;
          case "hcl":
            p.hcl = parts[1];
            break;
          case "ecl":
            p.ecl = parts[1];
            break;
          case "pid":
            p.pid = parts[1];
            break;
          case "cid":
            p.cid = parts[1];
            break;
          default:
            System.out.println(parts[1]);
            break;
        }
      }
    }

    System.out.println(count);
    System.out.println("Done");
  }
}