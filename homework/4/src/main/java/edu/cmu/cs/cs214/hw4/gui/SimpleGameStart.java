package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.cmu.cs.cs214.hw4.core.GameSystem;

/**
 * Class to create the panel when a new game begin, the user should set the 
 * player number here.
 */
public class SimpleGameStart extends JPanel {
	/** The JFrame from which this chat is established. */
	private JFrame parentFrame;

	/** The number of the players in this game. */
	private int playerNum;

	public SimpleGameStart(JFrame parentFrame) {
		this.parentFrame = parentFrame;
		JLabel numberLabel = new JLabel("Player Number: ");
		JTextField numberText = new JTextField(10);
		
		JButton startButton = new JButton("Start Game");
		JPanel numberPanel = new JPanel();
		numberPanel.setLayout(new BorderLayout());
		numberPanel.add(numberLabel, BorderLayout.WEST);
		numberPanel.add(numberText, BorderLayout.EAST);
		JLabel warningLabel = new JLabel("");	                                 

		setLayout(new BorderLayout());
		add(numberPanel, BorderLayout.NORTH);
		add(warningLabel, BorderLayout.CENTER);
		add(startButton, BorderLayout.SOUTH);
		
		ActionListener newGameNum = e ->{
			String number = numberText.getText().trim();
			if(number.isEmpty() || !validInput(number)) {
				numberText.setText("");
				numberText.requestFocus();
				warningLabel.setText("Invalid Input! The Input Player Number Should be Between 2 to 5!");
				warningLabel.requestFocus();
				parentFrame.pack();
			}else {
				playerNum = Integer.parseInt(number);
				startGame();
			}
		};
		//notify the action listener when Start Game button is pressed
		startButton.addActionListener(newGameNum);
		//notify the action listener when "Enter" key is hit
		numberText.addActionListener(newGameNum);
	}
	
	/**
	 * Helper function to check whether the input is valid or not.
	 * @param input is the input string to be checked
	 * @return true if the input is valid, false if not
	 */
	private boolean validInput(String input) {
		if(input.length() != 1) return false;
		int num = input.charAt(0) - 0;
		return num >= 50 && num <= 53;
	}
	
	private void startGame(){
		parentFrame.dispose();
		parentFrame = null;
		
		JFrame frame = new JFrame("Carcassonne");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setSize(new Dimension(1200, 800));
		GameSystem system = new GameSystem(playerNum);
		GamePanel gamePanel = new GamePanel(system);
		
		gamePanel.setOpaque(true);
		frame.setContentPane(gamePanel);

        frame.setVisible(true);
	}
}
