
/**
 * This class is used to model a hand of a Flush.
 * 
 * @author Pang Po Hean
 */
public class Flush extends Hand {
	
	/**
	 * Creates and returns an instance of the Flush class.
	 * 
	 * @param player the player with this hand
	 * @param cards the cards in this hand
	 */
	public Flush (CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Checks whether this hand is a Flush or not.
	 * 
	 * @return A boolean value specifying whether this is a Flush or not
	 */
	public boolean isValid() {
		if (this.size() == 5) {
			if (this.getCard(0).suit == this.getCard(1).suit && this.getCard(1).suit == this.getCard(2).suit && this.getCard(2).suit == this.getCard(3).suit && this.getCard(3).suit == this.getCard(4).suit) {
				this.sort();
				int rankPrevious;
				int rankNext;
				for (int i = 1; i < 5; i++) {
					rankPrevious = (this.getCard(i-1).rank < 2 ? this.getCard(i-1).rank + 13 : this.getCard(i-1).rank);
					rankNext = (this.getCard(i).rank < 2 ? this.getCard(i).rank + 13 : this.getCard(i).rank);
					if (rankNext - rankPrevious != 1) {
						return true;
					}
				}
				return false;
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
	 * @return A string value of "Flush"
	 */
	public String getType() {
		return("Flush");
	}
}