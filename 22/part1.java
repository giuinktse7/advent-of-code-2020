import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class Part1 {
  static class Player {
    public Player(List<String> input) {
      cards = new ArrayDeque<>();
      int cursor = 0;
      var idLine = input.get(cursor++);
      this.id = Integer.parseInt(idLine.substring(0, idLine.length() - 1).split(" ")[1]);

      while (cursor < input.size()) {
        var line = input.get(cursor++);
        cards.add(Integer.parseInt(line));
      }
    }

    public int draw() {
      return cards.pop();
    }

    public void putAtBottom(int card) {
      cards.add(card);
    }

    public boolean hasCards() {
      return cards.size() > 0;
    }

    public long score() {
      long total = 0;
      var currentCards = cards.stream().collect(Collectors.toList());
      Collections.reverse(currentCards);
      for (int i = 0; i < currentCards.size(); ++i) {
        total += currentCards.get(i) * (i + 1);
      }

      return total;
    }

    public String showDeck() {
      return cards.stream().map(x -> x + "").collect(Collectors.joining(", "));
    }

    public int id;
    private ArrayDeque<Integer> cards;
  }

  static class Game {
    public Game(Player p1, Player p2) {
      this.p1 = p1;
      this.p2 = p2;
    }

    public long play() {
      // long round = 1;
      while (p1.hasCards() && p2.hasCards()) {
        // System.out.println("-- Round " + round + " --");
        // System.out.println("Player 1:");
        // System.out.println(p1.showDeck());
        // System.out.println("Player 2:");
        // System.out.println(p2.showDeck());
        int c1 = p1.draw();
        int c2 = p2.draw();

        if (c1 > c2) {
          // System.out.println("Player 1 wins the round!");
          p1.putAtBottom(Math.max(c1, c2));
          p1.putAtBottom(Math.min(c1, c2));
        } else {
          // System.out.println("Player 2 wins the round!");
          p2.putAtBottom(Math.max(c1, c2));
          p2.putAtBottom(Math.min(c1, c2));
        }

        System.out.println();

        // ++round;
      }

      return winningScore();
    }

    public long winningScore() {
      long score1 = p1.score();
      long score2 = p2.score();

      return Math.max(score1, score2);
    }

    private Player p1;
    private Player p2;
  }

  public static void main(String[] args) throws IOException {
    var data = Parser.create("./input.txt").groupOn(String::isEmpty).construct(Player::new)
        .collect(Collectors.toList());

    var game = new Game(data.get(0), data.get(1));
    System.out.println(game.play());

    System.out.println("Done");
  }
}