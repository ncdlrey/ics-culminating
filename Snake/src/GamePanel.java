/**
 * 
 * A simple snake game that incorporates Mr. So's catch phrases
 * 
 * date      	2020/06/20
 * @filename    GamePanel.java
 * @authors     Nicole D. & Sadiksha D. 
 *
 **/

// SOURCES:
// followed https://www.youtube.com/watch?v=bI6e6qjJ8JQ&list=LL&index=4 to make game 
// followed https://www.youtube.com/watch?v=qPVkRtuf9CQ&t=1194s to try adding sound


import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.sound.sampled.AudioSystem;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.util.Random;
import javax.swing.JPanel;



public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int bagelsEaten;
	int bagelX;
	int bagelY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	

	
		
		public void playSound(){
			File stayInYourLane = new File(".//res//stayInYourLane.wav").getAbsoluteFile();

			
			try {
				
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(stayInYourLane));
				clip.start();
				clip.loop(5);

			}
			catch(Exception e) {
	            System.out.println("Error playing sound");
			}
		}
	
	
	GamePanel(){
		
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_WIDTH));
		this.setBackground(new Color(106, 153, 78));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if (running) {
			super.paintComponent(g);
		    Graphics2D g2 = (Graphics2D) g;
		    g2.setStroke(new BasicStroke(9));
			g.setColor(new Color(212, 163, 115));
			g.drawOval(bagelX, bagelY,UNIT_SIZE,UNIT_SIZE);
			
			for (int i = 0;i < bodyParts; i++) {
				if (i==0) {
					g.setColor(new Color(167, 201, 87));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(167, 201, 87));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			g.setColor(new Color(232, 237, 223));
			g.setFont(new Font("Helvetica", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + bagelsEaten, (SCREEN_WIDTH-metrics.stringWidth("Score: " + bagelsEaten))/2, g.getFont().getSize());
			
			g.setColor(new Color(232, 237, 223));
			g.setFont(new Font("Helvetica", Font.BOLD, 25));
			FontMetrics metrics2 = getFontMetrics(g.getFont());
			g.drawString("GET THOSE BAGELS!", (SCREEN_WIDTH-metrics2.stringWidth("GET THOSE BAGELS!"))/2, SCREEN_HEIGHT/2 -200);
		}
		
		else {
			gameOver(g);
		}
	}
	
	public void newApple() {
		bagelX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		bagelY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	
	public void move() {
		for (int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	public void checkApple() {
		if ((x[0] == bagelX) && (y[0] == bagelY)) {
			bodyParts++;
			bagelsEaten++;
			newApple();
			
		}
	}
	public void checkCollisions() {
		//check if head collides with body
		for (int i = bodyParts; i>0; i--) {
			if ((x[0]==x[i]) && (y[0] == y[i])) {
				running = false; //GAME OVER
			}
		}
		
		//check if head touches left border
		if (x[0]<0) {
			running = false;
		}
		//check if head touches right border
		if (x[0]>SCREEN_WIDTH) {
			running = false;
		}
		//check if head touches top border
		if (y[0]<0) {
			running = false;
		}
		//check if head touches bottom border
		if (y[0]>SCREEN_HEIGHT) {
			running = false;
		}
		
		if (!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {		
		
		//SFX
		playSound();
		
		// Score
		g.setColor(new Color(188, 71, 73));
		g.setFont(new Font("Helvatica", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + bagelsEaten, (SCREEN_WIDTH-metrics1.stringWidth("Score: " + bagelsEaten))/2, g.getFont().getSize()); 
		
		
		// Game Over Text
		g.setColor(new Color(188, 57, 8));
		g.setFont(new Font("Helvatica", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH-metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2); 
		
		
		// Stay in your lane
		g.setColor(new Color(191, 67, 66));
		g.setFont(new Font("Helvatica", Font.BOLD, 35));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString("STAY IN YOUR LANE!!!", (SCREEN_WIDTH-metrics3.stringWidth("STAY IN YOUR LANE!!!"))/2, SCREEN_HEIGHT/2 + 200);

	
		g.setColor(new Color(188, 57, 8));
		g.setFont(new Font("Helvatica", Font.BOLD, 25));
		FontMetrics metrics4 = getFontMetrics(g.getFont());
		g.drawString("BYEEEEEEEEEEEEEEEEEEEE", (SCREEN_WIDTH-metrics4.stringWidth("BYEEEEEEEEEEEEEEEEEEEE"))/2, SCREEN_HEIGHT/2+240);
		

		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
			
		}
		
	}

}
