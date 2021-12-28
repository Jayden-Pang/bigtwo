
/**
 * This class is used to model a hand of a Pair.
 * 
 * @author Pang Po Hean
 */
public class Pair extends Hand {
	
	/**
	 * Creates and returns an instance of the Pair class.
	 * 
	 * @param player the player with this hand
	 * @param cards the cards in this hand
	 */
	public Pair (CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Checks whether this hand is a Pair or not.
	 * 
	 * @return A boolean value specifying whether this is a Pair or not
	 */
	public boolean isValid() {
		if (this.size() == 2) {
			if (this.getCard(0).rank == this.getCard(1).rank) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * Returns a string specifying the type of this hand.
	 * 
	 * @return A string value of "Pair"
	 */
	public String getType() {
		return("Pair");
	}
}
