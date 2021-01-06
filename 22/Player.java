import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Player {
  public Player(List<String> input) {
    deck = new ArrayDeque<>();
    // Skip 'Player x:' line
    input.stream().skip(1).map(x -> Integer.parseInt(x)).forEach(deck::add);
  }

  private Player(ArrayDeque<Integer> deck) {
    this.deck = deck;
  }

  public int draw() {
    return deck.pop();
  }

  public void putAtBottom(int card) {
    deck.add(card);
  }

  public Player sub(int cardAmount) {
    ArrayDeque<Integer> subDeck = new ArrayDeque<>(cardAmount);
    var it = deck.iterator();
    for (int i = 0; i < cardAmount; ++i) {
      subDeck.add(it.next());
    }

    return new Player(subDeck);
  }

  public int deckSize() {
    return deck.size();
  }

  public long score() {
    long total = 0;
    var currentCards = deck.stream().collect(Collectors.toList());
    Collections.reverse(currentCards);
    for (int i = 0; i < currentCards.size(); ++i) {
      total += currentCards.get(i) * (i + 1);
    }

    return total;
  }

  public String showDeck() {
    return deck.stream().map(x -> x + "").collect(Collectors.joining(", "));
  }

  public void saveState() {
    int deckSize = deck.size();
    var cardArray = deck.toArray();

    if (!oldDecks.containsKey(deckSize)) {
      ArrayList<Object[]> list = new ArrayList<>();
      list.add(cardArray);
      oldDecks.put(deckSize, list);
    } else {
      oldDecks.get(deckSize).add(cardArray);
    }
  }

  public boolean inOldState() {
    var states = oldDecks.get(deck.size());
    if (states == null)
      return false;

    var cardArray = deck.toArray();
    for (var state : states) {
      if (Arrays.equals(cardArray, state)) {
        return true;
      }
    }

    return false;
  }

  public boolean hasCards() {
    return deck.size() > 0;
  }

  private ArrayDeque<Integer> deck;

  HashMap<Integer, ArrayList<Object[]>> oldDecks = new HashMap<>();
}