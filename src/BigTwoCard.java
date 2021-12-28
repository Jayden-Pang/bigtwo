
/**
 * This class is used to model a Big Two card.
 * 
 * @author Pang Po Hean
 */
public class BigTwoCard extends Card {
	
	/**
	 * Creates and returns an instance of the BigTwoCard class.
	 * 
	 * @param suit an integer value representing the suit of a card: 0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
	 * @param rank an integer value representing the rank of a card: 0 = 'A', 1 = '2', 2 = '3', ..., 8 = '9', 9 = '0', 10 = 'J', 11 = 'Q', 12 = 'K'
	 */
	public BigTwoCard (int suit, int rank) {
		super (suit, rank);
	}
	
	/**
	 * Compares this Big Two card with the specified card for order.
	 * 
	 * @param card the card to be compared
	 * @return a negative integer, zero, or a positive integer as this card is less than, equal to, or greater than the specified card
	 */
	public int compareTo(Card card) {
		if ((this.rank == 1 || this.rank == 0) && !(card.getRank() == 1 || card.getRank() == 0)) {
			return 1;
		}
		else if (!(this.rank == 1 || this.rank == 0) && (card.getRank() == 1 || card.getRank() == 0)) {
			return -1;
		}
		else {
			if (this.rank > card.getRank()) {
				return 1;
			}
			else if (this.rank < card.getRank()) {
				return -1;
			}
			else if (this.suit > card.getSuit()) {
				return 1;
			}
			else if (this.suit < card.getSuit()) {
				return -1;
			}
			else {
				return 0;
			}
		}
	}
	
}

