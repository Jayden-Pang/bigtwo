
/**
 * This class is used to represent a deck of Big Two cards.
 * 
 * @author Pang Po Hean
 */
public class BigTwoDeck extends Deck{
	
	/**
	 * Initializes the deck of Big Two cards (called implicitly inside the constructor).
	 */
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard bigTwoCard = new BigTwoCard(i, j);
				addCard(bigTwoCard);
			}
		}
	}
}
