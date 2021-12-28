import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders.*;
import javax.swing.text.DefaultCaret;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * This class is used for modelling a graphical user interface for the Big Two card game.
 * 
 * @author Pang Po Hean
 */
public class BigTwoGUI implements CardGameUI{
	
	/**
	 * Creates and returns an instance of the BigTwoGUI class.
	 * 
	 * @param game a BigTwo object associated with this GUI
	 */
	public BigTwoGUI(BigTwo game) {
		this.game = game;
		this.selected = new boolean[13];
		for (int i = 0; i < 13; i++) {
			selected[i] = false;
		}
		
		// import all avatar images
		avatarImages = new ArrayList<Image>();
		cardImages = new ArrayList<Image>();
		for (int i = 0; i < 4; i++) {
			avatarImages.add(new ImageIcon("src/Avatars/"+(i+1)+".JPG").getImage());
		}
		
		// import all card images
		char[] suits = {'d', 'c', 'h', 's'};
		char[] ranks = {'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k'};
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				cardImages.add(new ImageIcon("src/Cards/"+ranks[j]+suits[i]+".gif").getImage())
;			}
		}
		cardImages.add(new ImageIcon("src/Cards/b.gif").getImage());
		cardImages.add(new ImageIcon("src/Cards/table.jpg").getImage());
		
		// creating the frame
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		
		// creating the Big Two panel
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setBorder(new FieldBorder(Color.DARK_GRAY,Color.LIGHT_GRAY,Color.WHITE,Color.GRAY));
		
		// creating the play and pass buttons
		playButton = new JButton("Play");
		playButton.addActionListener(new PlayButtonListener());
		passButton = new JButton("Pass");
		passButton.addActionListener(new PassButtonListener());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,15,0));
		buttonPanel.add(playButton);
		buttonPanel.add(passButton);
		
		// creating the menu bar
		JMenu gameMenu = new JMenu("Game");
		connectMenuItem = new JMenuItem("Connect");
		connectMenuItem.addActionListener(new ConnectMenuItemListener());
		gameMenu.add(connectMenuItem);
		quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(new QuitMenuItemListener());
		gameMenu.add(quitMenuItem);
		
		JMenu msgMenu = new JMenu("Message");
		JMenuItem sendMenuItem = new JMenuItem("Send");
		sendMenuItem.addActionListener(new SendMenuItemListener());
		JMenuItem clearMsgMenuItem = new JMenuItem("Clear Message");
		clearMsgMenuItem.addActionListener(new ClearMsgItemListener());
		JMenuItem clearChatMenuItem = new JMenuItem("Clear Chat");
		clearChatMenuItem.addActionListener(new ClearChatItemListener());
		
		msgMenu.add(sendMenuItem);
		msgMenu.add(clearMsgMenuItem);
		msgMenu.add(clearChatMenuItem);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(gameMenu);
		menuBar.add(msgMenu);
		
		// creating the message area
		msgArea = new JTextArea();
		msgArea.setEditable(false);
		DefaultCaret msgCaret = (DefaultCaret) msgArea.getCaret();
		msgCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		msgArea.setCaret(msgCaret);
		JScrollPane msgScroller = new JScrollPane(msgArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		// creating the chat area
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		DefaultCaret chatCaret = (DefaultCaret) chatArea.getCaret();
		chatCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		chatArea.setCaret(chatCaret);
		JScrollPane chatScroller = new JScrollPane(chatArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		// combining the message and chat label
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
		rightPanel.add(msgScroller);
		rightPanel.add(chatScroller);
		
		// creating the chat input
		chatInput = new JTextField();
		chatInput.addActionListener(new ChatInputItemListener());
		
		// setting the sizes of each component
		bigTwoPanel.setMinimumSize(new Dimension(700, 750));
		bigTwoPanel.setPreferredSize(new Dimension(700, 750));
		buttonPanel.setMinimumSize(new Dimension(700, passButton.getPreferredSize().height));
		buttonPanel.setPreferredSize(new Dimension(700, passButton.getPreferredSize().height));
		msgScroller.setMinimumSize(new Dimension(500, 375));
		msgScroller.setPreferredSize(new Dimension(500, 375));
		chatScroller.setMinimumSize(new Dimension(500, 375));
		chatScroller.setPreferredSize(new Dimension(500, 375));
		passButton.setMinimumSize(passButton.getPreferredSize());
		playButton.setMinimumSize(passButton.getPreferredSize());
		
		// adding all items into the frame
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 12;
		c.gridheight = 1;
		frame.add(menuBar, c);
		c.insets = new Insets(2,2,2,2);
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 8;
		c.gridheight = 2;
		c.weightx = 1;
		c.weighty = 1;
		frame.add(bigTwoPanel, c);
		c.gridx = 8;
		c.gridy = 1;
		c.gridwidth = 4;
		c.gridheight = 2;
		c.weightx = 1;
		c.weighty = 0;
		frame.add(rightPanel, c);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 6;
		c.gridheight = 1;
		c.weightx = 1;
		frame.add(buttonPanel, c);
		c.gridx = 8;
		c.gridy = 3;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		frame.add(new JLabel("Message: "), c);
		c.gridx = 9;
		c.gridy = 3;
		c.gridheight = 1;
		c.gridwidth = 3;
		c.weightx = 1;
		frame.add(chatInput, c);
		frame.pack();
		frame.setVisible(true);
	}
	
	private BigTwo game; // a Big Two card game associates with this GUI
	private boolean[] selected; // a boolean array indicating which cards are being selected
	private int activePlayer; // the index of the active player
	private JFrame frame; // the main window
	private JMenuItem connectMenuItem; // a connect menu item for the player to connect to the server
	private JMenuItem quitMenuItem; //  a quit menu item for the player to quit the game
	private JPanel bigTwoPanel; // the panel for showing the cards of each player and the cards played on the table
	private JButton playButton; // a “Play” button for the active player to play the selected cards
	private JButton passButton; // a “Pass” button for the active player to pass his/her turn to the next player
	private JTextArea msgArea; // the text area to display game status
	private JTextArea chatArea; // the text area to display player chat
	private JTextField chatInput; // the chat input area
	private ArrayList<Image> avatarImages; // list of avatar images
	private ArrayList<Image> cardImages; // list of card images including the table background
	
	/**
	 * Sets the index of the active player.
	 * 
	 * @param activePlayer the index of the active player (i.e., the player who can
	 *                     make a move)
	 */
	public void setActivePlayer(int activePlayer) {
		if (activePlayer < 0 || activePlayer >= game.getNumOfPlayers()) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}
	}
	
	/**
	 * Redraws the GUI.
	 */
	public void repaint() {
		// play and pass buttons
		if (game.getPlayerID() == activePlayer) {
			enable();
		}
		else {
			playButton.setEnabled(false);
			passButton.setEnabled(false);
		}
		// connect menu item
		if (game.getClient().isConnected()) {
			allowConnect(false);
		}
		else {
			allowConnect(true);
		}
		// repaint
		frame.repaint();
		
	}
	
	/**
	 * Prints the specified string to the message area in the GUI.
	 * 
	 * @param msg the string to be printed to the message area in the GUI.
	 */
	public void printMsg(String msg) {
		msgArea.append(msg);
	}
	
	/**
	 * Clears the message area of the GUI.
	 */
	public void clearMsgArea() {
		msgArea.setText("");
	}
	
	/**
	 * Clears the message area of the GUI.
	 */
	public void clearChatArea() {
		chatArea.setText("");
	}
	
	/**
	 * Prints the specified string to the chat area in the GUI.
	 * 
	 * @param chat the string to be printed to the chat area in the GUI.
	 */
	public void printChat(String chat) {
		chatArea.append(chat);
	}
	
	
	/**
	 * Resets the GUI.
	 */
	public void reset() {
		for (int i = 0; i < selected.length; i++) {
			selected[i] = false;
		}
		clearMsgArea();
		enable();
	}
	
	/**
	 * Enables user interactions.
	 */
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
	}
	
	/**
	 * Disables user interactions.
	 */
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
	}
	
	/**
	 * Prompts active player to select cards and make his/her move.
	 */
	public void promptActivePlayer() {
		printMsg(game.getPlayerList().get(activePlayer).getName() + "'s turn:\n");
		for (int i = 0; i < 13; i++) {
			selected[i] = false;
		}
		this.repaint();
	}
	
	/**
	 * Enable or disable the connect menu item.
	 * 
	 * @param allow true to enable; false otherwise
	 */
	private void allowConnect(boolean allow) {
		connectMenuItem.setEnabled(allow);
	}
	
	/**
	 * Returns an array of indices of the cards selected through the GUI.
	 * 
	 * @return an array of indices of the cards selected, or null if no valid cards
	 *         have been selected
	 */
	private int[] getSelected() {
		ArrayList<Integer> selectedIndexArray  = new ArrayList<Integer>();
		for(int i = 0; i < selected.length; i++ ) {
			if (selected[i] == true) {
				selectedIndexArray.add(i);
			}
		}
		int[] selectedIndexList = new int[selectedIndexArray.size()];
		for(int i = 0; i < selectedIndexArray.size(); i++ ) {
			selectedIndexList[i] = selectedIndexArray.get(i);
		}
		if (selectedIndexArray.size() == 0) {
			return null;
		}
		else {
			return selectedIndexList;
		}
	}
	
	/**
	 * Prints a divider for the message area
	 */
	public void printDivider () {
		printMsg("----------------------------------------------------------------------------------------------------------------\n");
	}
	
	/**
	 * This class is used for drawing the Big Two card game table. Implements the mouseReleased() method
	 * from the MouseListener interface to handle mouse click events.
	 * 
	 * @author Pang Po Hean
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener {
		
		/**
		 * Creates and returns an instance of the BigTwoPanel class.
		 */
		public BigTwoPanel() {
			this.addMouseListener(this);
		}
		
		private int cardStartX = 120; // the distance between the leftmost card in a hand of cards and the leftmost edge of the panel
		private int cardHeight = cardImages.get(0).getHeight(this); // height of a card
		private int cardWidth = cardImages.get(0).getWidth(this); // width of a card
		private int cardRaisedHeight = cardHeight/5; // the distance in which a selected card is raised compared to an unselected card
		
		/**
		 * Draws the avatars, cards, and player names in a Big Two game table to the panel.
		 * 
		 * @param g a Graphics object provided by the system to allow drawing
		 */
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			int containerHeight = this.getHeight()/5 < 150 ? 150 : this.getHeight()/5; // the height of a player container
			int containerWidth = this.getWidth(); // the width of a player container
			int cardStartYSelected = (containerHeight-cardHeight-cardRaisedHeight)/2; // the distance between the top edge of a selected card and the top edge of a player container
			int cardStartYUnselected = cardStartYSelected + cardRaisedHeight; // the distance between the top edge of a unselected card and the top edge of a player container
			
			
			// draws the containers
			Graphics2D g2D = (Graphics2D) g;
			Paint paint = new GradientPaint(0,0,Color.RED.darker(),0,0, new Color(53, 101, 77));
			
			g.setColor(new Color(53, 101, 77));
			g.fillRect(0, 0, containerWidth, containerHeight*4);
			for (int i = 0; i < game.getNumOfPlayers(); i++) {
				if (i == activePlayer) {
					paint = new GradientPaint(0,containerHeight*i,Color.RED.darker(), containerWidth/2, (containerWidth+containerHeight*i)/2, new Color(53, 101, 77));
					g2D.setPaint(paint);
					g2D.fillRect(0, containerHeight*i, containerWidth, containerHeight);
					break;
				}
			}
			g.drawImage(cardImages.get(53), 0 , containerHeight*4, this);
			g.setColor(Color.BLACK);
			for (int i = 0; i < 4; i++) {
				g.drawLine(0,containerHeight*(i+1), containerWidth, containerHeight*(i+1));
			}
			
			g.setFont(new Font("Arial",Font.BOLD,15));
			
			// draws the player container for each player, including names, avatars, and cards held by the player
			if (game.getClient() != null) {
				if (!game.endOfGame()) {
					for (int i = 0; i < game.getNumOfPlayers(); i++) {
						if (game.getPlayerList().get(i).getName() != "") {
							if (i == game.getPlayerID()) {
								Image avatarImage = avatarImages.get(i);
								g.setColor(Color.CYAN);
								g.drawString("You", 15, containerHeight*i+cardStartYUnselected-10);
								g.drawImage(avatarImage,15, containerHeight*i+cardStartYUnselected, this);
								for (int j = 0; j < game.getPlayerList().get(i).getNumOfCards(); j++) {
									int cardSuit = game.getPlayerList().get(i).getCardsInHand().getCard(j).getSuit();
									int cardRank = game.getPlayerList().get(i).getCardsInHand().getCard(j).getRank(); 
									Image cardImage = cardImages.get(13*cardSuit+cardRank);
									if (selected[j] == true) {
										g.drawImage(cardImage,cardStartX+cardImage.getWidth(this)/4*j, containerHeight*i+cardStartYSelected, this);
									}
									else {
										g.drawImage(cardImage,cardStartX+cardImage.getWidth(this)/4*j, containerHeight*i+cardStartYUnselected, this);
									}
								}
							}
							else {
								g.setColor(Color.WHITE);
								g.drawString(game.getPlayerList().get(i).getName(), 15, containerHeight*(i)+cardStartYUnselected-10);
								Image avatarImage = avatarImages.get(i);
								g.drawImage(avatarImage, 15, containerHeight*i+cardStartYUnselected, this);
								Image cardImage = cardImages.get(52);
								for (int j = 0; j < game.getPlayerList().get(i).getNumOfCards(); j++) {
									g.drawImage(cardImage, cardStartX+cardImage.getWidth(this)/4*j, containerHeight*i+cardStartYUnselected, this);
								}
							}
						}
					}
				}
				else {
					for (int i = 0; i < game.getNumOfPlayers(); i++) {
						if (game.getPlayerList().get(i).getName() != "") {
							if (i == game.getPlayerID()) {
								g.setColor(Color.CYAN);
								g.drawString("You", 15, containerHeight*i+cardStartYUnselected-10);
							}
							else {
								g.setColor(Color.WHITE);
								g.drawString(game.getPlayerList().get(i).getName(), 15,  containerHeight*i+cardStartYUnselected-10);
							}
							Image avatarImage = avatarImages.get(i);
							g.drawImage(avatarImage, 15,  containerHeight*i+cardStartYUnselected, this);
							for (int j = 0; j < game.getPlayerList().get(i).getNumOfCards(); j++) {
								int cardSuit = game.getPlayerList().get(i).getCardsInHand().getCard(j).getSuit();
								int cardRank = game.getPlayerList().get(i).getCardsInHand().getCard(j).getRank(); 
								Image cardImage = cardImages.get(13*cardSuit+cardRank);
								g.drawImage(cardImage,cardStartX+cardImage.getWidth(this)/4*j, containerHeight*i+cardStartYUnselected, this);
							}
						}
					}
				}
			}
			

			// draws the last hand played on the table, including the cards played and the name of the player who played the hand
			Hand lastHandOnTable = (game.getHandsOnTable().isEmpty()) ? null :game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
			if (lastHandOnTable != null) {
				g.setColor(Color.WHITE);
				g.drawString("Played By " + lastHandOnTable.getPlayer().getName(), 15, containerHeight*4+cardStartYUnselected-10);
				for (int i = 0; i < lastHandOnTable.size(); i++) {
					int cardSuit = lastHandOnTable.getCard(i).getSuit();
					int cardRank = lastHandOnTable.getCard(i).getRank();
					Image cardImage = cardImages.get(13*cardSuit+cardRank);
					g.drawImage(cardImage,15+cardImage.getWidth(this)/2*i, containerHeight*4+cardStartYUnselected, this);
				}
			}
		}
		
		/**
		 * Not used.
		 * 
		 * @param event mouse event created when mouse is clicked (pressed and released) on a component.
		 */
		@Override
		public void mouseClicked(MouseEvent event) {}
		
		/**
		 * Not used.
		 * 
		 * @param event mouse event created when mouse is pressed on a component.
		 */
		@Override
		public void mousePressed(MouseEvent event) {}
		
		/**
		 * Register mouse released events from the active player to select and unselect cards. Repaints GUI afterwards to reflect changes.
		 * 
		 * @param event mouse event created when mouse is released on a component.
		 */
		@Override
		public void mouseReleased(MouseEvent event) {
				
				int thisPlayer = game.getPlayerID();
				int containerHeight = this.getHeight()/5 < 150 ? 150 : this.getHeight()/5; // the height of a player container
				int cardStartYSelected = (containerHeight-cardHeight-cardRaisedHeight)/2; // the distance between the top edge of a selected card and the top edge of a player container
				int cardStartYUnselected = cardStartYSelected + cardRaisedHeight; // the distance between the top edge of a unselected card and the top edge of a player container
				int numOfCards = game.getPlayerList().get(thisPlayer).getNumOfCards(); // number of cards held by the active player
				int x = event.getX(); // x-coordinate of the mouse released event
				int y = event.getY(); // y-coordinate of the mouse released event
				
				// attempts to find the clicked rectangular region of a hand of card. If the click did not land on the visible region of the intended card, check whether the card is selected.
				// If the card is selected, check whether the previous 3 cards are not selected. If the card is not selected, check whether the previous 3 cards are selected. If the condition is true,
				// change the true value of the intended card in the boolean array to false and vice versa.
				if (numOfCards >0 && x >= cardStartX && x <= cardStartX+cardWidth/4*(numOfCards-1) + cardWidth && y >= thisPlayer*containerHeight + cardStartYSelected && y <= thisPlayer*containerHeight + cardStartYUnselected + cardHeight) {
					int clickedLocation = (int) (x-cardStartX)/(cardWidth/4);
					int clickedCardNumber;
					if (clickedLocation > numOfCards - 1) {
						clickedCardNumber = numOfCards - 1;
					}
					else {
						clickedCardNumber = clickedLocation;
					}
					int searchingLoop = (clickedLocation-(numOfCards-1) > 0) ? 4-(clickedLocation-(numOfCards-1)): 4;
					
					if (!selected[clickedCardNumber]) {
						if (y >= thisPlayer*containerHeight+cardStartYUnselected) {
							selected[clickedCardNumber] = true;
						}
						else {
							for (int i = 1; i < searchingLoop; i ++) {
								if (clickedCardNumber-i < 0) {
									break;
								}
								if (selected[clickedCardNumber-i]) {
									selected[clickedCardNumber-i] = false;
									break;
								}
							}
						}
					}		
					else {
						if (y <= thisPlayer*containerHeight+cardStartYSelected+cardHeight) {
							selected[clickedCardNumber] = false;
						}
						else {
							for (int i = 1; i < searchingLoop; i ++) {
								if (clickedCardNumber-i < 0) {
									break;
								}
								if (!selected[clickedCardNumber-i]) {
									selected[clickedCardNumber-i] = true;
									break;
								}
							}
						}
					}
					this.repaint();
				}
		}
		
		/**
		 * Not used.
		 * 
		 * @param event mouse event created when mouse entered a component.
		 */
		@Override
		public void mouseEntered(MouseEvent event) {}
		
		/**
		 * Not used.
		 * 
		 * @param event mouse event created when mouse exited a component.
		 */
		@Override
		public void mouseExited(MouseEvent event) {}
	}
	
	/**
	 * This class is used to handle button-click events for the "Play" button.
	 * 
	 * @author Pang Po Hean
	 */
	class PlayButtonListener implements ActionListener {
		
		/**
		 * Play the cards selected by the player when the registered component is clicked.
		 * 
		 * @param event event created when the registered component is clicked
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			if (getSelected() != null) {
				game.makeMove(activePlayer,getSelected());
			}
			else {
				printMsg("No Cards Selected!!!\n");
				printMsg(game.getPlayerList().get(activePlayer).getName()+"'s turn:\n");
			}
		}
	}
	
	/**
	 * This class is used to handle button-click events for the "Pass" button.
	 * 
	 * @author Pang Po Hean
	 */
	class PassButtonListener implements ActionListener {
		/**
		 * Makes a pass move when the registered component is clicked.
		 * 
		 * @param event event created when the registered component is clicked
		 */
		@Override
		public void actionPerformed(ActionEvent event) {	
			game.makeMove(activePlayer,null);
		}
	}
	
	/**
	 * This class is used to handle button-click events for the "Connect" button.
	 * 
	 * @author Pang Po Hean
	 */
	class ConnectMenuItemListener implements ActionListener {
		/**
		 * Connects to the server when the registered component is clicked.
		 * 
		 * @param event event created when the registered component is clicked
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			game.getClient().connect();
		}
	}
	
	/**
	 * This class is used to handle button-click events for the "Quit" button.
	 * 
	 * @author Pang Po Hean
	 */
	class QuitMenuItemListener implements ActionListener {
		/**
		 * Quits the game when the registered component is clicked.
		 * 
		 * @param event event created when the registered component is clicked
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			System.exit(0);
		}
	}
	
	/**
	 * This class is used to handle button-click events for the "Send" button.
	 * 
	 * @author Pang Po Hean
	 */
	class SendMenuItemListener implements ActionListener {
		/**
		 * Send messages from the chat input area into the chat area when the registered component is clicked.
		 * 
		 * @param event event created when the registered component is clicked
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			if (!chatInput.getText().isEmpty()) {
				if (game.getClient().isConnected()) {
					game.getClient().sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, chatInput.getText() + "\n"));
				}
				chatInput.setText("");
			}
		}
	}
	
	/**
	 * This class is used to handle the "Enter" keyboard entry event.
	 * 
	 * @author Pang Po Hean
	 */
	class ChatInputItemListener implements ActionListener {
		/**
		 * Send messages from the chat input area into the chat area when a keyboard entry of "Enter" is detected.
		 * 
		 * @param event event created when a keyboard entry of "Enter" is detected
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			if (!chatInput.getText().isEmpty()) {
				if (game.getClient().isConnected()) {
					game.getClient().sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, chatInput.getText() + "\n"));
				}
				chatInput.setText("");
			}
		}
	}
	
	/**
	 * This class is used to handle button-click events for the "Clear Message" button.
	 * 
	 * @author Pang Po Hean
	 */
	class ClearMsgItemListener implements ActionListener {
		/**
		 * Clears the message area when the registered component is clicked.
		 * 
		 * @param event event created when the registered component is clicked
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			clearMsgArea();
		}
	}
	
	/**
	 * This class is used to handle button-click events for the "Clear Chat" button.
	 * 
	 * @author Pang Po Hean
	 */
	class ClearChatItemListener implements ActionListener {
		/**
		 * Clears the chat area when the registered component is clicked.
		 * 
		 * @param event event created when the registered component is clicked
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			clearChatArea();
		}
	}
}
