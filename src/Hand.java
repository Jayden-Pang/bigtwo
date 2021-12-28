
/**
 * This abstract class is used to model a hand of cards.
 * 
 * @author Pang Po Hean
 */
public abstract class Hand extends CardList {
	
	/**
	 * Creates and returns an instance of any concrete subclass of the Hand class.
	 * This constructor is only meant to be called from concrete subclasses of the Hand class.
	 * 
	 * @param player the player with this hand
	 * @param cards the cards in this hand
	 */
	public Hand (CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i = 0 ; i < cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
	}
	
	private CardGamePlayer player; // the player that holds this hand
	
	/**
	 * Returns the player that holds this hand.
	 * 
	 * @return the player that holds this hand
	 */
	public CardGamePlayer getPlayer() { 
		return this.player;
	}
	
	/**
	 * Returns the top card of this hand.
	 * 
	 * @return the top card of this hand
	 */
	public Card getTopCard() {
		if (this.getType() == "Single" || this.getType() == "Pair" || this.getType() == "Triple" || this.getType() == "Straight" || this.getType() == "Flush" || this.getType() == "StraightFlush") {
			int highIdx = 0;
			for (int i = 1; i < this.size(); i++) {
				if (this.getCard(i).compareTo(this.getCard(highIdx)) == 1) {
					highIdx = i;
				}
			}
			return this.getCard(highIdx);
		}
		else {
			int rank = this.getCard(0).getRank();
			int rankTemp = 0;
			int count = 1;
			for (int i = 1; i < this.size(); i++) {
				if (this.getCard(i).getRank() == rank) {
					count += 1;
				}
				else {
					rankTemp = this.getCard(i).getRank();
				}
			}
			if (count < 3) {
				rank = rankTemp;
			}
			int highIdx = -1;
			for (int i = 0; i < this.size(); i++) {
				if (this.getCard(i).getRank() == rank) {
					if (highIdx != -1) {
						if (this.getCard(i).compareTo(this.getCard(highIdx)) == 1) {
							highIdx = i;
						}
					}
					else {
						highIdx = i;
					}
				}
			}
			return this.getCard(highIdx);
		}
	}
	
	/**
	 * Checks whether this hand beats a specified hand.
	 * 
	 * @param hand the hand to be compared
	 * @return true if this hand beats the specified hand; false otherwise
	 */
	public boolean beats(Hand hand) {
		if (this.getType() == "Single" || this.getType() == "Pair" || this.getType() == "Triple") {
			if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			}
			else {
				return false;
			}
		}
		else if (this.getType() == "Straight") {
			if (hand.getType() == "Straight") {
				if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
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
		else if (this.getType() == "Flush") {
			if (hand.getType() == "Flush") {
				if (this.getTopCard().getSuit() > hand.getTopCard().getSuit()) {
					return true;
				}
				else if (this.getTopCard().getSuit() < hand.getTopCard().getSuit()) {
					return false;
				}
				else if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true;
				}
				else {
					return false;
				}
			}
			else if (hand.getType() == "Straight") {
				return true;
			}
			else {
				return false;
			}
		}
		else if (this.getType() == "FullHouse") {
			if (hand.getType() == "FullHouse") {
				if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true;
				}
				else {
					return false;
				}
			}
			else if (hand.getType() == "Straight" || hand.getType() == "Flush") {
				return true;
			}
			else {
				return false;
			}
		}
		else if (this.getType() == "Quad") {
			if (hand.getType() == "Quad") {
				if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true;
				}
				else {
					return false;
				}
			}
			else if (hand.getType() == "StraightFlush") {
				return false;
			}
			else {
				return true;
			}
		}
		else {
			if (hand.getType() == "StraightFlush") {
				if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return true;
			}
		}
	}
	
	/**
	 * Checks whether this hand is a valid hand.
	 * This method is to be overridden.
	 * 
	 * @return true if this hand is a valid hand; false otherwise
	 */
	public abstract boolean isValid();
	
	/**
	 * Returns a string specifying the type of this hand.
	 * This method is to be overridden.
	 * 
	 * @return a string specifying the type of this hand
	 */
	public abstract String getType();
}
