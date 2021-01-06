import java.io.IOException;
import java.util.stream.Collectors;

class Part2 {

  static class Game {
    public Game() {
    }

    public Player play(Player p1, Player p2, int game) {
      while (true) {
        if (!p1.hasCards())
          return p2;
        else if (!p2.hasCards())
          return p1;
        else if (p1.inOldState() && p2.inOldState()) {
          return p1;
        }
        p1.saveState();
        p2.saveState();
        int c1 = p1.draw();
        int c2 = p2.draw();

        Player winner;

        if (p1.deckSize() >= c1 && p2.deckSize() >= c2) {
          // Play a sub-game
          var sub1 = p1.sub(c1);
          var sub2 = p2.sub(c2);
          var result = play(sub1, sub2, game + 1);

          winner = result == sub1 ? p1 : p2;

        } else {
          winner = c1 > c2 ? p1 : p2;
        }

        if (winner == p1) {
          winner.putAtBottom(c1);
          winner.putAtBottom(c2);
        } else {
          winner.putAtBottom(c2);
          winner.putAtBottom(c1);
        }
      }
    }

  }

  public static void main(String[] args) throws IOException {
    var start = System.nanoTime();
    var data = Parser.create("./input.txt").groupOn(String::isEmpty).construct(Player::new)
        .collect(Collectors.toList());

    Player p1 = data.get(0);
    Player p2 = data.get(1);

    var game = new Game();
    var winner = game.play(p1, p2, 1);
    var end = System.nanoTime();
    System.out.println("Winner score: " + winner.score() + " (" + Parser.showTime(start, end) + ").");
  }
}