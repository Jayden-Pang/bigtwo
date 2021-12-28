
/**
 * This class is used to model a hand of a Quad.
 * 
 * @author Pang Po Hean
 */
public class Quad extends Hand {
	
	/**
	 * Creates and returns an instance of the Quad class.
	 * 
	 * @param player the player with this hand
	 * @param cards the cards in this hand
	 */
	public Quad (CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Checks whether this hand is a Quad or not.
	 * 
	 * @return A boolean value specifying whether this is a Quad or not
	 */
	public boolean isValid() {
		if (this.size() == 5) {
			this.sort();
			if (this.getCard(0).rank == this.getCard(3).rank || this.getCard(1).rank == this.getCard(4).rank) {
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
	 * @return A string value of "Quad"
	 */
	public String getType() {
		return("Quad");
	}
}