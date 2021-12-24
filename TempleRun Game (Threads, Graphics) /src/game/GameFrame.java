package game;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.Obstacle;
import domain.Runner;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static String sep = System.getProperty("file.separator");
	private static String resourceDir = System.getProperty("user.dir") + sep + "resources" + sep;

	private JPanel gui;
	private final CardLayout cardLayout;
	private final JPanel cards;

	private JButton button;
	private JLabel score;
	private JPanel startPanel, gamePanel;
	private RunningLane runningLane;

	private Runner runner;
	private ArrayList<Obstacle> obstacles;
	private RepainterThread repainter;
	private MovementThread movement;
	private LevelThread levels;

	public GameFrame() {
		this.cardLayout = new CardLayout();
		this.cards = new JPanel(cardLayout);
		this.button = new JButton("Start");
		this.score = new JLabel("Score: 0", JLabel.CENTER);
		this.gamePanel = new JPanel();
		this.startPanel = new JPanel();

		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		this.setTitle("TempleRun 2D");
		this.setMinimumSize(new Dimension(840, 550));
		this.setLocation(50, 50);

		this.initComponents();
		this.getContentPane().add(this.gui);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}

	private void initComponents() {
		JPanel bottomBar = new JPanel(new GridLayout(1, 2));
		bottomBar.add(button);
		bottomBar.add(score);

		this.gui = new JPanel(new BorderLayout());
		this.gui.setSize(850, 550);
		this.gui.add(bottomBar, BorderLayout.SOUTH);

		JLabel title = new JLabel("TempleRun 2D");
		title.setFont(new Font(title.getName(), Font.BOLD, 64));
		this.startPanel.setLayout(new GridBagLayout());
		this.startPanel.add(title);

		this.gamePanel.setLayout(new BorderLayout());
		JPanel rightBorder = new JPanel(), leftBorder = new JPanel();
		this.runningLane = new RunningLane();

		String imgFile1 = resourceDir + "leftBorder.png";
		String imgFile2 = resourceDir + "rightBorder.png";
		ImageIcon leftPic = new ImageIcon(imgFile1), rightPic = new ImageIcon(imgFile2);

		rightBorder.add(new JLabel(rightPic));
		leftBorder.add(new JLabel(leftPic));
		this.runningLane.setMinimumSize(new Dimension(300, 500));

		this.gamePanel.add(leftBorder, BorderLayout.WEST);
		this.gamePanel.add(runningLane, BorderLayout.CENTER);
		this.gamePanel.add(rightBorder, BorderLayout.EAST);

		this.gui.add(this.cards);
		this.cards.add(startPanel, "start");
		this.cards.add(gamePanel, "game");

		this.button.addActionListener(new ActionListener() {

			// TODO Exercise 2.a
			public void actionPerformed(ActionEvent actionEvent) {
				JButton button = (JButton) actionEvent.getSource();

				if (button.getText().equals("Start")) {
					GameFrame.this.cardLayout.show(GameFrame.this.cards, "game");
					GameFrame.this.button.setText("Stop");
					GameFrame.this.obstacles = new ArrayList<>();
					GameFrame.this.runner = new Runner();
					GameFrame.this.setScore(0);

					GameController.getInstance().getGameState().resetScore();
					GameController.getInstance().getGameState().setActive(true);

					GameFrame.this.repainter = new RepainterThread();
					GameFrame.this.repainter.start();

					GameFrame.this.movement = new MovementThread();
					GameFrame.this.movement.start();

					// TODO Exercise 3
					GameFrame.this.levels = new LevelThread();
					GameFrame.this.levels.start();
				} else {
					GameFrame.this.stopGame();
				}
			}
		});

	}

	// TODO Exercise 2.a
	public void stopGame() {
		this.cardLayout.show(GameFrame.this.cards, "start");
		this.button.setText("Start");

		GameController.getInstance().getGameState().setActive(false);

		// TODO Exercise 3
		this.levels.stopThread();

		try {
			this.repainter.join();
			this.movement.join();

			// TODO Exercise 3
			this.levels.join();
		} catch (InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		}
	}

	public void setScore(int score) {
		this.score.setText("Score: " + score);
	}

	private class RunningLane extends JPanel {
		private static final long serialVersionUID = 1L;

		public RunningLane() {
			super();

			this.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent keyEvent) {
					int key = keyEvent.getKeyCode();

					if (GameController.getInstance().getGameState().isActive()) {
						if (key == KeyEvent.VK_LEFT) {
							GameFrame.this.runner.moveLeft();
						} else if (key == KeyEvent.VK_RIGHT) {
							GameFrame.this.runner.moveRight();
						}
					}
				}
			});

			this.setFocusable(true);
		}

		@Override
		public void paint(Graphics graphics) {
			// TODO Exercise 1.a
			Graphics2D graphics2d = (Graphics2D) graphics;

			float width = 1.0f;
			int cap = BasicStroke.CAP_BUTT;
			int join = BasicStroke.JOIN_MITER;
			float miterLimit = 10.0f;
			float[] dashArray = { 5.0f };
			float dashPhase = 0.0f;

			graphics2d.setStroke(new BasicStroke(width, cap, join, miterLimit, dashArray, dashPhase));
			graphics2d.drawLine(100, 0, 100, this.getHeight());
			graphics2d.drawLine(200, 0, 200, this.getHeight());

			GameFrame.this.runner.draw(graphics2d);

			for (Obstacle obstacle : GameFrame.this.obstacles) {
				obstacle.draw(graphics2d);
			}
		}

	}

	// TODO Exercise 1.c
	private class RepainterThread extends Thread {

		public void run() {
			while (GameController.getInstance().getGameState().isActive()) {
				GameFrame.this.gamePanel.repaint();
				GameFrame.this.runningLane.requestFocus();

				for (Obstacle obstacle : GameFrame.this.obstacles) {
					if (obstacle.isHit(GameFrame.this.runner.getLocation())) {
						GameFrame.this.stopGame();
					}
				}

				try {
					sleep(10);
				} catch (InterruptedException interruptedException) {
					interruptedException.printStackTrace();
				}
			}
		}

	}

	// TODO Exercise 2.c
	private class MovementThread extends Thread {

		public void run() {
			int steps = GameConstants.numSteps;

			while (GameController.getInstance().getGameState().isActive()) {
				if (GameFrame.this.obstacles.size() < GameConstants.numObstacles) {
					if (steps == 0) {
						GameFrame.this.obstacles.add(new Obstacle());
						steps = GameConstants.numSteps;
					}

					steps--;
				}

				for (Obstacle obstacle : GameFrame.this.obstacles) {
					obstacle.moveDown();
				}

				GameController.getInstance().getGameState().addScore(1);
				GameFrame.this.setScore(GameController.getInstance().getGameState().getScore());

				try {
					sleep(20);
				} catch (InterruptedException interruptedException) {
					interruptedException.printStackTrace();
				}
			}
		}
	}

}
