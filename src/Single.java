
/**
 * This class is used to model a hand of a Single.
 * 
 * @author Pang Po Hean
 */
public class Single extends Hand{
	
	/**
	 * Creates and returns an instance of the Single class.
	 * 
	 * @param player the player with this hand
	 * @param cards the cards in this hand
	 */
	public Single (CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Checks whether this hand is a Single or not.
	 * 
	 * @return A boolean value specifying whether this is a Single or not
	 */
	public boolean isValid() {
		if (this.size() == 1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Returns a string specifying the type of this hand.
	 * 
	 * @return A string value of "Single"
	 */
	public String getType() {
		return("Single");
	}

}
