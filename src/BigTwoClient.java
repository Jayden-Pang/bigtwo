import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

/**
 * The class is used to model a game client that can connect to a server.
 * 
 * @author Pang Po Hean
 */
public class BigTwoClient implements NetworkGame {
	
	/**
	 * Creates and returns an instance of the BigTwoClient class.
	 * 
	 * @param game a BigTwoGame objects associated with this client
	 * @param gui a BigTwoGUI objects associated with this client
	 */
	public BigTwoClient (BigTwo game, BigTwoGUI gui) {
		String name = JOptionPane.showInputDialog("Please Enter Your Name");
		if (name == null || name == "" || name.isEmpty()) {
			name = "The One Who Cannot Be Named";
		}
		this.setPlayerName(name);
		this.game = game;
		this.gui = gui;
		gui.printMsg("Your name is: " + name + "\n");
		gui.printMsg("Please connect to server using the connect button under the game menu.\n");
		gui.printDivider();
		this.setServerIP("127.0.0.1");
		this.setServerPort(2396);
		gui.disable();
	}
	
	private BigTwo game; // the Big Two game
	private BigTwoGUI gui; // the GUI
	private Socket sock; // a socket connection to the game server
	private ObjectOutputStream oos; // an ObjectOutputStream for sending messages to the server
	private int playerID; // the playerID (i.e., index) of the local player
	private String playerName; // the name of the local player
	private String serverIP; // the IP address of the game server
	private int serverPort; // the TCP port of the game server
	
	/**
	 *  Returns the playerID (i.e., index) of the local player.
	 *  
	 *  @return an integer specifying the playerID (i.e., index) of the local player
	 */
	public int getPlayerID() {
		return this.playerID;
	}
	
	/**
	 * sets the playerID (i.e., index) of the local player.
	 * 
	 * @param playerID an integer specifying the playerID (i.e., index) of the local player
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	/**
	 * Returns the name of the local player.
	 * 
	 * @return the name of the local player
	 */
	public String getPlayerName() {
		return this.playerName;
	}
	
	/**
	 * Sets the name of the local player.
	 * 
	 * @param playerName the name of the local player
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	/**
	 * Returns the IP address of the game server.
	 * 
	 * @return the IP address of the game server
	 */
	public String getServerIP() {
		return this.serverIP;
	}
	
	/**
	 * Sets the IP address of the game server.
	 * 
	 * @param serverIP the IP address of the game server
	 */
	public void setServerIP (String serverIP) {
		this.serverIP = serverIP;
	}
	
	/**
	 * Returns the TCP port of the game server.
	 * 
	 * @return the TCP port of the game server
	 */
	public int getServerPort() {
		return this.serverPort;
	}
	
	/**
	 * Sets the TCP port of the game server.
	 * 
	 * @param serverPort the TCP port of the game server
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	/**
	 * Establish a connection to the game server.
	 */
	public void connect() {
		try {
			this.sock = new Socket(this.getServerIP(),this.getServerPort());
			this.oos = new ObjectOutputStream(sock.getOutputStream());
			Thread thread = new Thread(new ServerHandler(sock));
			thread.start();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Parse the messages received from the server.
	 * 
	 * @param message message received from the server
	 */
	public void parseMessage(GameMessage message) {
		switch (message.getType()) {
		// deals with the PLAYER_LIST message
		case CardGameMessage.PLAYER_LIST:
			this.setPlayerID(message.getPlayerID());
			game.setPlayerNames((String[]) message.getData());
			gui.repaint();
			sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, this.playerName));
			break;
		// deals with the JOIN message
		case CardGameMessage.JOIN:
			game.setPlayerName(message.getPlayerID(), (String) message.getData());
			gui.repaint();
			if (message.getPlayerID() == this.playerID) {
				sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			}
			break;
		// deals with the FULL message
		case CardGameMessage.FULL:
			gui.printMsg("Server is full.\n");
			break;
		// deals with the QUIT message
		case CardGameMessage.QUIT:
			gui.printMsg(game.getPlayerList().get(message.getPlayerID()).getName()+ " left the game.\n");
			game.setPlayerName(message.getPlayerID(), "");
			if (!game.endOfGame()) {
				gui.disable();
				sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			}
			break;
		// deals with the READY message
		case CardGameMessage.READY:
			gui.printMsg(game.getPlayerList().get(message.getPlayerID()).getName() + " is ready!\n");
			break;
		// deals with the START message
		case CardGameMessage.START:
			gui.printMsg("All players are ready. Let the games begin!\n");
			gui.printDivider();
			game.start((BigTwoDeck) message.getData());
			break;
		// deals with the MOVE message
		case CardGameMessage.MOVE:
			game.checkMove(message.getPlayerID(), (int[]) message.getData());
			break;
		// deals with the MSG message
		case CardGameMessage.MSG:
			gui.printChat((String) message.getData());
			break;
		}		
	}
	
	/**
	 * Sends messages to the server.
	 * 
	 * @param message message to be sent
	 */
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This class is used to model a server handler that handles messages from the server.
	 * 
	 * @author Pang Po Hean
	 *
	 */
	private class ServerHandler implements Runnable {
		private Socket socket; // socket connection to the server
		private ObjectInputStream ois; // ObjectInputStream of the server

		/**
		 * Creates and returns an instance of the ServerHandler class.
		 * 
		 * @param socket the socket connection to the server
		 */
		public ServerHandler(Socket socket) {
			this.socket = socket;
			try {
				ois = new ObjectInputStream(socket.getInputStream());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} 
		
		/**
		 * Keeps receiving messages from the server and parses them.
		 */
		@Override
		public void run() {
			CardGameMessage message;
			try {
				while ((message = (CardGameMessage) ois.readObject()) != null) {
					parseMessage(message);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Checks whether the client is connected to the server.
	 * 
	 * @return true if connected; false otherwise
	 */
	public boolean isConnected() {
		if (sock != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
}
