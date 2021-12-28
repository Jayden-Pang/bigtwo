import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * This class is used to model a Big Two card game.
 * 
 * @author Pang Po Hean
 */
public class BigTwo {
	
	/**
	 * Creates and returns an instance of the BigTwo class.
	 */
	public BigTwo() {
		ArrayList<CardGamePlayer> playerList = new ArrayList<CardGamePlayer>();
		playerList.add(new CardGamePlayer(""));
		for (int i = 0; i < 3; i++) {
			playerList.add(new CardGamePlayer(""));
		}
		this.playerList = playerList;
		this.numOfPlayers = playerList.size();
		handsOnTable = new ArrayList<Hand>();
		this.gui = new BigTwoGUI(this);
		this.client = new BigTwoClient(this, gui);
	}
	
	private static int maxPassNumber = 3; // maximum passes allowed
	
	private int numOfPlayers; // the number of players in the game
	private Deck deck; // the deck of cards used in the game
	private	ArrayList<CardGamePlayer> playerList; // the players in the game
	private ArrayList<Hand> handsOnTable; // all played hands in the game
	private int currentPlayerIdx; // the index of the current player
	private BigTwoGUI gui; // A BigTwoGUI object for providing the graphical user interface
	private BigTwoClient client; // A BigTwoClient object
	private int passCount = 0; // counter to calculate number of passes 
	
	/**
	 * Returns the number of players in the game.
	 * 
	 * @return the number of players in the game
	 */
	public int getNumOfPlayers() {return numOfPlayers;}
	
	/**
	 * Returns the deck of cards used in the game.
	 * 
	 * @return the deck of cards in the game
	 */
	public Deck getDeck() {return deck;}
	
	/**
	 * Returns the list of players in the game.
	 * 
	 * @return the list of players in the game
	 */
	public ArrayList<CardGamePlayer> getPlayerList() {return playerList;}
	
	/**
	 * Returns the list of hands played in the game.
	 * 
	 * @return the list of hands player in the game
	 */
	public ArrayList<Hand> getHandsOnTable() {return handsOnTable;}
	
	/**
	 * Returns the index of the current player.
	 * 
	 * @return the index of the current player
	 */
	public int getCurrentPlayerIdx() {return currentPlayerIdx;}
	
	public BigTwoClient getClient() {return this.client;}
	
	/**
	 * Starts the game with a given shuffled deck of cards.
	 * 
	 * @param deck the deck of cards to be used in the game
	 */
	public void start(Deck deck) {
		// remove all cards from players
		for (CardGamePlayer player:playerList) {
			player.removeAllCards();
		}
		// remove all cards from players
		handsOnTable = new ArrayList<Hand>();
		// distribute cards to players
		for (int i = 0; i < deck.size(); i++) {
			playerList.get(i%4).addCard(deck.getCard(i));
		}
		// find the player to go first
		for (int i = 0; i < numOfPlayers; i++) {
			for (int j = 0; j < playerList.get(i).getNumOfCards(); j++) {
				if (playerList.get(i).getCardsInHand().getCard(j).getRank()==2 && playerList.get(i).getCardsInHand().getCard(j).getSuit()==0 ) {
					currentPlayerIdx = i;
					gui.setActivePlayer(i);
					break;
				}	
			}
		}
		// sort players' hand
		for (int i = 0; i < numOfPlayers; i++) {
			playerList.get(i).sortCardsInHand();
		}
		
		// repaint the interface
		gui.repaint();
		// prompt the active player
		gui.promptActivePlayer();
	}
	
	
	/**
	 * Sends the move made to the server.
	 * 
	 * @param playerIdx the index of the player playing the selected card indexes
	 * @param cardIdx the card indexes selected by the player
	 */
	public void makeMove(int playerIdx, int[] cardIdx) {
		client.sendMessage(new CardGameMessage(CardGameMessage.MOVE,-1,cardIdx));
	}
	
	/**
	 * Iteratively prompts the player to select a move and checks the move selected by the player.
	 * Prints the move and continues the turn if the move selected is valid; prints "Not a legal move!!!" otherwise.
	 * Ends the game and prints the final result when the winning condition is met. 
	 * 
	 * @param playerIdx the index of the player playing the selected card indexes
	 * @param cardIdx the card indexes selected by the player
	 */
	public void checkMove(int playerIdx, int[] cardIdx) {
		CardGamePlayer player = playerList.get(playerIdx);
		CardList cardList = player.play(cardIdx);
		Hand lastHandOnTable = (handsOnTable.isEmpty()) ? null : handsOnTable.get(handsOnTable.size() - 1);
		// if player choose pass
		if (cardIdx == null) {
			// if this is the first turn
			if (lastHandOnTable == null) {
				gui.printMsg("Not a legal Move!!!\n");
			}
			// if last hand is played by the same player
			else if (passCount >= maxPassNumber){
				gui.printMsg("Not a legal Move!!!\n");
			}
			// player passes
			else {
				gui.printMsg("{Pass}\n");
				currentPlayerIdx = (currentPlayerIdx + 1)%4;
				gui.setActivePlayer(currentPlayerIdx);
				passCount++;
			}
		}
		// if player chooses a hand
		else {
			// compose a hand
			Hand hand = BigTwo.composeHand(player, cardList);
			// check whether the hand is valid
			if (hand != null) {
				hand.sort();
				// if this is first turn
				if (lastHandOnTable == null) {
					// if cards selected contains three of diamonds
					if (hand.contains(new BigTwoCard(0,2))) {
						gui.printMsg("{"+hand.getType()+"} ");
						gui.printMsg(hand.toString() +"\n");
						playerList.get(playerIdx).removeCards(cardList);
						handsOnTable.add(hand);
						currentPlayerIdx = (currentPlayerIdx + 1)%4;
						gui.setActivePlayer(currentPlayerIdx);
						passCount = 0;
					}
					// if cards selected does not contain three of diamonds
					else {
						gui.printMsg("Not a legal move!!!\n");
					}
				}
				// if this is not first turn
				else {
					// if last hand on table is played by the same player
					if (passCount == maxPassNumber) {
						gui.printMsg("{"+hand.getType()+"} ");
						gui.printMsg(hand.toString() +"\n");
						playerList.get(playerIdx).removeCards(cardList);
						handsOnTable.add(hand);
						currentPlayerIdx = (currentPlayerIdx + 1)%4;
						gui.setActivePlayer(currentPlayerIdx);
						passCount = 0;
					}
					// if the number of cards in the last hand on table and hand played by the player is the same
					else if (lastHandOnTable.size() == hand.size()) {
						// if hand beats the last hand on table
						if (hand.beats(lastHandOnTable)) {
							gui.printMsg("{"+hand.getType()+"} ");
							gui.printMsg(hand.toString() + "\n");
							playerList.get(playerIdx).removeCards(cardList);
							handsOnTable.add(hand);
							currentPlayerIdx = (currentPlayerIdx + 1)%4;
							gui.setActivePlayer(currentPlayerIdx);
							passCount = 0;
						}
						// if hand does not beat the last hand on table
						else {
							gui.printMsg("Not a legal move!!!\n");
						}
					}
					// if the number of cards in the last hand on table and hand played by the player is not the same
					// and the last hand on table is not played by this player
					else {
						gui.printMsg("Not a legal move!!!\n");
					}
				}
			}
			else {
				gui.printMsg("Not a legal move!!!\n");
			}
		}
		// check whether game has ended
		if (!endOfGame()) {
			if (playerIdx != currentPlayerIdx) {
				gui.printDivider();
				gui.repaint();
			}
			gui.promptActivePlayer();
		}
		else {
			// prints all players' hand and table at the end of the game
			passCount = 0;
			currentPlayerIdx = (currentPlayerIdx-1 == -1) ? 3 :currentPlayerIdx-1;
			gui.setActivePlayer(currentPlayerIdx);
			gui.repaint();
			gui.disable();
			gui.printMsg("\n");
			for (CardGamePlayer currentPlayer:playerList) {
				String name = currentPlayer.getName();
				gui.printMsg("<" + name + ">");
				gui.printMsg("    ");
				gui.printMsg(currentPlayer.getCardsInHand().toString()+"\n");
			}
			gui.printMsg("<Table>");
			Hand finalHandOnTable = handsOnTable.get(handsOnTable.size() - 1);
			gui.printMsg("    <" + finalHandOnTable.getPlayer().getName() + "> {" + finalHandOnTable.getType() + "} ");
			gui.printMsg(finalHandOnTable.toString()+"\n");
			
			// prints the winning game message
			String message = "";
			message += "Game ends.\n";
			for (CardGamePlayer currentPlayer:playerList) {
				if (currentPlayer.getCardsInHand().size() == 0) {
					message += currentPlayer.getName() + " wins the game.\n";
				}
				else {
					message += currentPlayer.getName() + " has " + currentPlayer.getNumOfCards() +" cards in hand.\n";
				}	
			}
			JOptionPane.showMessageDialog(null, message);
			client.sendMessage(new CardGameMessage(CardGameMessage.READY,-1,null));
		}
	}
	
	/**
	 * Checks whether the game has ended or not.
	 * 
	 * @return true if the game ends; false otherwise
	 */
	public boolean endOfGame() {
		for (int i = 0; i < numOfPlayers; i++) {
			if (playerList.get(i).getNumOfCards() == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the playerID of the client associated with this game.
	 * 
	 * @return playerID of the client associated with this game
	 */
	public int getPlayerID () {
		return client.getPlayerID();
	}
	
	/**
	 * Sets the player names.
	 * 
	 * @param playerNames An array of strings containing the player names
	 */
	public void setPlayerNames (String[] playerNames) {
		for (int i = 0; i < playerNames.length; i++) {
			if (playerNames[i] != null) {
				playerList.get(i).setName(playerNames[i]);
			}
		}
	}
	
	/**
	 * Sets the name of the player at the given index.
	 * 
	 * @param idx index of the player
	 * @param name name of the player
	 */
	public void setPlayerName (int idx, String name) {
		this.playerList.get(idx).setName(name);
	}
	
	/**
	 * This is the main method for starting the Big Two game.
	 * 
	 * @param args not being used in this application
	 */
	public static void main(String[] args) {
		new BigTwo();
	}
	
	/**
	 * Returns a valid hand from the specified list of cards of the player.
	 * 
	 * @param player the player that chooses the cards
	 * @param cards the chosen cards by the player
	 * @return a valid hand from the specified list of cards
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		Single single = new Single(player,cards);
		Pair pair = new Pair(player,cards);
		Triple triple = new Triple(player,cards);
		Straight straight = new Straight(player,cards);
		Flush flush = new Flush(player,cards);
		FullHouse fullHouse = new FullHouse(player,cards);
		Quad quad = new Quad(player,cards);
		StraightFlush straightFlush = new StraightFlush(player,cards);
		if (single.isValid()) {return single;}
		if (pair.isValid()) {return pair;}
		if (triple.isValid()) {return triple;}
		if (straight.isValid()) {return straight;}
		if (flush.isValid()) {return flush;}
		if (fullHouse.isValid()) {return fullHouse;}
		if (quad.isValid()) {return quad;}
		if (straightFlush.isValid()) {return straightFlush;}
		return null;
	}	
}
