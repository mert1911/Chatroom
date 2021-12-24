package game;

// TODO Exercise 3
public class LevelThread extends Thread {

	private boolean running;

	public LevelThread() {
		this.running = true;
	}

	public void run() {
		GameConstants.numSteps = 50;
		GameConstants.numObstacles = 2;

		int threshold = 1000;

		while (this.running) {
			try {
				sleep(500);
			} catch (InterruptedException interruptedException) {
				interruptedException.printStackTrace();
			}

			int score = GameController.getInstance().getGameState().getScore();

			if (score > 5000) {
				GameConstants.numObstacles = 3;
			}

			if (score > threshold) {
				GameConstants.numSteps--;
				threshold += 1000;
			}
		}
	}

	public void stopThread() {
		this.running = false;
	}

}
