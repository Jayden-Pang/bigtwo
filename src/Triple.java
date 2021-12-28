
/**
 * This class is used to model a hand of a Triple.
 * 
 * @author Pang Po Hean
 */
public class Triple extends Hand {
	
	/**
	 * Creates and returns an instance of the Triple class.
	 * 
	 * @param player the player with this hand
	 * @param cards the cards in this hand
	 */
	public Triple (CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Checks whether this hand is a Triple or not.
	 * 
	 * @return A boolean value specifying whether this is a Triple or not
	 */
	public boolean isValid() {
		if (this.size() == 3) {
			if (this.getCard(0).rank == this.getCard(1).rank && this.getCard(1).rank == this.getCard(2).rank) {
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
	 * @return A string value of "Triple"
	 */
	public String getType() {
		return("Triple");
	}
}
