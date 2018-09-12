package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import edu.cmu.cs.cs214.hw4.core.GameSystem;
import edu.cmu.cs.cs214.hw4.core.Player;
import edu.cmu.cs.cs214.hw4.core.Position;
import edu.cmu.cs.cs214.hw4.core.Tile;
import edu.cmu.cs.cs214.hw4.core.TileCopier;
import edu.cmu.cs.cs214.hw4.segments.City;
import edu.cmu.cs.cs214.hw4.segments.Cloister;
import edu.cmu.cs.cs214.hw4.segments.Farm;
import edu.cmu.cs.cs214.hw4.segments.Road;

/**
 * Class to create game panel for Carcassonne.
 */
public class GamePanel extends JPanel {
	private GameSystem game;
	private JPanel boardPanel;
	private GridBagLayout gridLayout;
	private LinkedList<MyButton> buttons;
	private LinkedList<Position> positions;
	private BufferedImage carcassonneImg;
	private Tile currentTile;
	private BufferedImage currentImg;
	private TileCopier copier = new TileCopier();
	private JLabel nullTileWarning = new JLabel(" ");
	private JButton randomTile;
	private JLabel currentPlayerLabel;
	private Player currentPlayer;
	private int currentPlayerIndex;
	private Color[] colors = { Color.BLUE, Color.ORANGE, Color.GREEN, Color.RED, Color.BLACK };
	private String[] colorsString = { "Blue", "Yellow", "Green", "Red", "Black" };
	private MyButton currentTileButton;
	private JButton rotate;
	private LinkedList<JButton> playerButtons;

	/**
	 * Helper function to get the buffered image of the input tile.
	 * @param tile is the input tile
	 * @return the image corresponding to this tile
	 */
	private BufferedImage getImage(Tile tile) {
		int index = tile.getName().charAt(0) - 65;
		int x = (index % 6) * 80;
		int y = (index / 6) * 80;
		BufferedImage tileImg = carcassonneImg.getSubimage(x, y, 80, 80);
		return tileImg;
	}

	/**
	 * Helper function to rotate the given image counter clockwise.
	 * @param src is the given image
	 * @param n is applying n 90-degree counter clockwise rotation
	 * @return the rotated image
	 */
	private BufferedImage rotateCounterClockwise(BufferedImage src) {
		int w = src.getWidth();
		int h = src.getHeight();

		AffineTransform at = AffineTransform.getQuadrantRotateInstance(3, w / 2.0, h / 2.0);
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

		BufferedImage dest = new BufferedImage(w, h, src.getType());
		op.filter(src, dest);
		return dest;
	}

	public GamePanel(GameSystem game) {
		String fileName = "src/main/resources/Carcassonne.png";
		try {
			carcassonneImg = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		currentTile = null;
		this.game = game;
		gridLayout = new GridBagLayout();
		boardPanel = new JPanel();
		buttons = new LinkedList<MyButton>();
		positions = new LinkedList<Position>();
		randomTile = new JButton();
		randomTile.setPreferredSize(new Dimension(80, 80));
		currentPlayerLabel = new JLabel("CurrentPlayer: Player" + game.getPlayers().getFirst().getName());
		currentPlayerIndex = 0;
		currentPlayer = game.getPlayers().getFirst();
		currentTileButton = null;
		rotate = new JButton("Rotate");
		playerButtons = new LinkedList<JButton>();
		for(int i = 0; i < game.getPlayers().size(); i++) {
			playerButtons.add(new JButton());
		}
		updatePlayerInfo();

		JScrollPane scrollPanel = new JScrollPane(boardPanel);

		boardPanel.setLayout(gridLayout);
		addButton(0, 0);

		setLayout(new BorderLayout());
		add(scrollPanel, BorderLayout.CENTER);
		add(createInfoPanel(), BorderLayout.EAST);
	}

	/**
	 * Helper function to show the information of players.
	 * @return the player information panel
	 */
	private JPanel playerInfo() {
		int playerNum = game.getPlayers().size();
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		int y = 0;
		for (int i = 0; i < playerNum; i++) {
			GridBagConstraints gridCon = new GridBagConstraints();
			gridCon.fill = GridBagConstraints.BOTH;
			gridCon.gridx = 0;
			gridCon.gridy = y;
			gridCon.weightx = 0;
			gridCon.weighty = 0;
			panel.add(playerButtons.get(i), gridCon);
			y += 1;
		}
		return panel;
	}
	
	/**
	 * Helper function to change the current player and the current player index,
	 * in addition with the current player label on the game window
	 */
	private void currentPlayerChange() {
		currentPlayerIndex = (currentPlayerIndex + 1) % game.getPlayers().size();
		currentPlayer = game.getPlayers().get(currentPlayerIndex);
		currentPlayerLabel.setText("CurrentPlayer: Player" + currentPlayer.getName());
	}
	
	/**
	 * Helper function to update the information of each player on the game window.
	 */
	private void updatePlayerInfo() {
		for(int i = 0; i < playerButtons.size(); i++) {
			Player player = game.getPlayers().get(i);
			playerButtons.get(i).setText("<html>Player " + player.getName() + " Info:<br>Score: " + player.getScore()
			+ "<br>Left Followers: " + player.leftFollowers() + "<br>Follower Color: " + colorsString[i]
			+ "</html>");
			playerButtons.get(i).setOpaque(true);
			playerButtons.get(i).setForeground(colors[i]);
		}
	}

	private boolean currentTilePlaced = false;
	private JLabel followerWarning = new JLabel(" ");

	/**
	 * Helper function to create the player information panel on the game window.
	 * @return the created player information panel
	 */
	private JPanel createCurrentPlayerInfo() {
		JPanel playerInfo = new JPanel();
		playerInfo.setLayout(new GridBagLayout());

		GridBagConstraints gridCon = new GridBagConstraints();
		gridCon.fill = GridBagConstraints.NORTH;
		gridCon.gridx = 0;
		gridCon.gridy = 0;
		gridCon.weightx = 0;
		gridCon.weighty = 0;
		playerInfo.add(currentPlayerLabel, gridCon);

		JButton nextTurn = new JButton("Next Player");
		gridCon.gridy = 1;
		playerInfo.add(nextTurn, gridCon);

		gridCon.gridy = 2;
		playerInfo.add(followerWarning, gridCon);

		ActionListener nextPlayer = e -> {
			if (currentTilePlaced) {
				game.updateScore();
				updatePlayerInfo();
				followerWarning.setText(" ");
				currentPlayerChange();
				currentTilePlaced = false;
				playerTurnShouldChange = false;
				nullTileWarning.setText(" ");
				updateButtonFollower();
				/******************************************/
				System.out.print(game.getGameBoard().getFarmSegments().size() + ", ");
				for(Farm farm : game.getGameBoard().getFarmSegments()) {
					System.out.print(farm.getFollowers().size() + ", ");
				}
				System.out.println("");
			} else {
				followerWarning.setText("Place Tile First!");
			}
		};
		nextTurn.addActionListener(nextPlayer);
		return playerInfo;
	}
	
	/**
	 * Helper function to update the img shown on the buttons.
	 * The image shown on the buttons would change when one of the segments is completed,
	 * then the followers should be returned, which means the follower 
	 * shown on the buttons would disappear
	 */
	private void updateButtonFollower() {
		//check city segments
		for(City city : game.getGameBoard().getCitySegments()) {
			if(city.checkCompletion()) {
				for(MyButton button : buttons) {
					if(city.getFollowersPos().contains(button.getPosition())) {
						String name = button.getName();
						button.setIcon(new ImageIcon(button.getInitialImg()));
					}
				}
			}
		}
		for(Road road : game.getGameBoard().getRoadSegments()) {
			if(road.checkCompletion()) {
				for(MyButton button : buttons) {
					if(road.getFollowersPos().contains(button.getPosition())) {
						button.setIcon(new ImageIcon(button.getInitialImg()));
					}
				}
			}
		}
		for(Cloister cloister : game.getGameBoard().getCloisterSegments()) {
			if(cloister.checkCompletion()) {
				for(MyButton button : buttons) {
					if(cloister.getFollowersPos().contains(button.getPosition())) {
						button.setIcon(new ImageIcon(button.getInitialImg()));
					}
				}
			}
		}
	}

	private boolean playerTurnShouldChange = false;
	private JLabel leftTileNum = new JLabel("Left Tiles Number: 72");
	/**
	 * Function to create the information panel.
	 * @return the created information panel
	 */
	public JPanel createInfoPanel() {
		JPanel totalPanel = new JPanel();
		totalPanel.setLayout(new BorderLayout());
		totalPanel.add(createCurrentPlayerInfo(), BorderLayout.NORTH);

		JButton pickRandom = new JButton("Pick Tile");
		pickRandom.setPreferredSize(new Dimension(150, 25));

		rotate.setPreferredSize(new Dimension(150, 25));

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gridCon = new GridBagConstraints();
		gridCon.fill = GridBagConstraints.NORTH;
		gridCon.gridx = 0;
		gridCon.gridy = 0;
		gridCon.weightx = 0;
		gridCon.weighty = 0;
		panel.add(leftTileNum, gridCon);

		gridCon.gridy = 1;
		panel.add(pickRandom, gridCon);

		gridCon.gridy = 2;
		panel.add(rotate, gridCon);

		gridCon.gridy = 3;
		panel.add(randomTile, gridCon);

		ActionListener showTile = e -> {
			if (currentTile != null) {
				nullTileWarning.setText("Place Current Tile First!");
			} else if (playerTurnShouldChange) {
				nullTileWarning.setText("Change Player Turn First!");
			} else {
				currentTilePlaced = false;
				rotate.setText("Rotate");
				Tile tile = game.pickRandomTile();
				leftTileNum.setText("Left Tiles Number: " + game.getLeftTileNum());
				if(tile == null) {
					nullTileWarning.setText("Game Over!");
					gameEnded();
				}else {
					currentTile = copier.copy(tile);
				    currentImg = getImage(currentTile);
				    randomTile.setIcon(new ImageIcon(currentImg));
				}
			}
		};
		pickRandom.addActionListener(showTile);

		ActionListener rotateTile = e -> {
			if (rotate.getText().equals("Rotate") && currentTile != null) {
				currentTile.rotate();
				currentImg = rotateCounterClockwise(currentImg);
				randomTile.setIcon(new ImageIcon(currentImg));
			}
			if (rotate.getText().equals("Place Follower")) {
				if(game.getPlayers().get(currentPlayerIndex).leftFollowers() == 0) {
					nullTileWarning.setText("No Follower Left!");
				}else {
					currentTileButton.setCouldPlaceFollower(true);
				    nullTileWarning.setText(" ");
				    rotate.setText("Rotate");
				}
			}
		};
		rotate.addActionListener(rotateTile);

		gridCon.gridy = 4;
		panel.add(nullTileWarning, gridCon);
		
		/*************************************** */
		JButton end = new JButton("end game");
		gridCon.gridy = 5;
		panel.add(end, gridCon);
		ActionListener endgame = e -> {
			gameEnded();
		};
		end.addActionListener(endgame);

		totalPanel.add(playerInfo(), BorderLayout.CENTER);
		totalPanel.add(panel, BorderLayout.SOUTH);
		return totalPanel;
	}
	
	/**
	 * Function called when the user is going to pick a new tile, but
	 * there is no tile left, which means the game is over.
	 */
	private void gameEnded() {
		game.gameOver();
		updatePlayerInfo();
		
		JFrame frame = (JFrame)SwingUtilities.getRoot(this);
		LinkedList<String> winner = new LinkedList<String>();
		int maxScore = Integer.MIN_VALUE;
		for(Player player : game.getPlayers()) {
			maxScore = Math.max(maxScore, player.getScore());
		}
		for(Player player : game.getPlayers()) {
			if(player.getScore() == maxScore) {
				winner.add(player.getName());
			}
		}
		showDialog(frame, "Winner! Player", winner + " won the game!");
		JFrame newframe = new JFrame("Start New Carcassonne Game");
        newframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	private void showDialog(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	private Tile currentFollowerTile = null;

	/**
	 * Function to add new button the the specified position in the game board.
	 * @param x is the x position
	 * @param y is the y position
	 */
	public void addButton(int x, int y) {

		int row = x + 71;
		int col = y + 71;
		MyButton button = new MyButton();
		button.setPreferredSize(new Dimension(80, 80));
		GridBagConstraints gridCon = new GridBagConstraints();
		gridCon.fill = GridBagConstraints.BOTH;
		gridCon.gridx = row;
		gridCon.gridy = col;
		gridCon.weightx = 0;
		gridCon.weighty = 0;
		gridLayout.setConstraints(button, gridCon);
		buttons.add(button);
		positions.add(new Position(x, y));

		ActionListener placeTile = e -> {
			if (currentTile == null) {
				nullTileWarning.setText("Pick Tile First!");
			} else {
				if (!game.placeTile(currentTile, new Position(x, y))) {
					nullTileWarning.setText("<html>Wrong Placement!<br>Try Again!</html>");
				} else {
					currentFollowerTile = copier.copy(currentTile);
					currentFollowerTile.setPosition(currentTile.getPosition());
					button.setButtonColor(colors[currentPlayerIndex]);
					button.setInitialImg(currentImg);
					button.setPosition(currentFollowerTile.getPosition());
					playerTurnShouldChange = true;
					followerWarning.setText(" ");
					currentTilePlaced = true;
					rotate.setText("Place Follower");
					currentTileButton = button;
					button.setIcon(new ImageIcon(currentImg));
					
					button.setCurrentImg(currentImg);
					nullTileWarning.setText(" ");
					currentTile = null;
					randomTile.setIcon(null);
					Position pos = new Position(x - 1, y);
					if (!positions.contains(pos)) {
						addButton(x - 1, y);
					}
					pos = new Position(x + 1, y);
					if (!positions.contains(pos)) {
						addButton(x + 1, y);
					}
					pos = new Position(x, y - 1);
					if (!positions.contains(pos)) {
						addButton(x, y - 1);
					}
					pos = new Position(x, y + 1);
					if (!positions.contains(pos)) {
						addButton(x, y + 1);
					}
				}
			}
		};

		button.addActionListener(placeTile);

		boardPanel.add(button);
	}
	
	/**
	 * MyButton represents the buttons on the game board, which implements MouseListener,
	 * so the game could decide the position and the chosen feature when the user need 
	 * to place follower.
	 */
	public class MyButton extends JButton implements MouseListener {
		private BufferedImage currentImg;
		private boolean couldPlaceFollower;
		private int position = -1;
		
		private BufferedImage initialImg;
		/**
		 * Function to set the initial image of this button.
		 * When new tile placed, each button would store the initial image of this button,
		 * in which condition the button would show the initial image when the follower of this
		 * tile is returned to the host player
		 * @param initialImg is the initial image
		 */
		public void setInitialImg(BufferedImage initialImg) { this.initialImg = initialImg; }
		/**
		 * Function to get the initial image of this button.
		 * @return the initial image
		 */
		public BufferedImage getInitialImg() { return initialImg; }

		/**
		 * Function to set whether this button could be place follower or not.
		 * @param couldPlaceFollower is the input boolean value
		 */
		public void setCouldPlaceFollower(boolean couldPlaceFollower) {
			this.couldPlaceFollower = couldPlaceFollower;
		}

		private Color playerColor;

		/**
		 * Function to set the color of this button, the color is the same with the player who place this tile.
		 * This color would decide the color of the follower shown on the button
		 * @param color is the player color who place this button
		 */
		public void setButtonColor(Color color) {
			playerColor = color;
		}

		public MyButton() {
			this.addMouseListener(this);
			currentImg = null;
			couldPlaceFollower = false;
			playerColor = null;
		}
		
		private Position pos;
		/** Function to set the position of this button, which represent the position of the placed tile.
		 * @param pos is the position of the tile */
		public void setPosition(Position pos) { this.pos = pos; }
		/** Function to get the position of this button.
		 * @return the position of this button */
		public Position getPosition() { return pos; }

		/**
		 * Function to set the current image of this button, the image shown on this button
		 * would be updated when one of the player place follower on this tile.
		 * @param img is the image going to show on this button
		 */
		public void setCurrentImg(BufferedImage img) {
			currentImg = img;
		}

		private BufferedImage withCircle(BufferedImage src, Color color, int x, int y, int radius) {
			BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

			Graphics2D g = (Graphics2D) dest.getGraphics();
			g.drawImage(src, 0, 0, null);
			g.setColor(color);
			g.fillOval(x - radius, y - radius, radius, radius);
			g.dispose();

			return dest;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (couldPlaceFollower && e.getButton() == MouseEvent.BUTTON1 && this.getIcon() != null) {
				int x;
				int y;
				if (e.getX() < 27) {
					if (e.getY() < 27) { x = 10; y = 10; position = 5; } 
					else if (e.getY() > 53) { x = 10; y = 80; position = 6; } 
					else { x = 10; y = 45; position = 1; }
				} else if (e.getX() > 53) {
					if (e.getY() < 27) { x = 80; y = 10; position = 8; } 
					else if (e.getY() > 53) { x = 80; y = 80; position = 7; } 
					else { x = 80; y = 45; position = 3; }
				} else {
					if (e.getY() < 27) { x = 45; y = 10; position = 0; } 
					else if (e.getY() > 53) { x = 45; y = 80; position = 2; } 
					else { x = 45; y = 45; position = 4; }
				}
				if (checkValidFollower(currentFollowerTile, position, currentPlayerIndex)) {
					updatePlayerInfo();
					couldPlaceFollower = false;
					currentImg = withCircle(currentImg, playerColor, x, y, 10);
					this.setIcon(new ImageIcon(currentImg));
				}
			}
		}

		private boolean checkValidFollower(Tile tile, int position, int index) {
			return game.placeFollower(tile, position, index);
		}
		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
	}
}
