
/**
 * This class is used to model a hand of a Full House.
 * 
 * @author Pang Po Hean
 */
public class FullHouse extends Hand {
	
	/**
	 * Creates and returns an instance of the Full House class.
	 * 
	 * @param player the player with this hand
	 * @param cards the cards in this hand
	 */
	public FullHouse (CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Checks whether this hand is a Full House or not.
	 * 
	 * @return A boolean value specifying whether this is a Full House or not
	 */
	public boolean isValid() {
		if (this.size() == 5) {
			this.sort();
			if ((this.getCard(0).rank == this.getCard(1).rank && this.getCard(2).rank == this.getCard(4).rank) || (this.getCard(0).rank == this.getCard(2).rank && this.getCard(3).rank == this.getCard(4).rank)) {
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
	 * @return A string value of "FullHouse"
	 */
	public String getType() {
		return("FullHouse");
	}
}