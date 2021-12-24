package game;

public class GameState {

	private boolean active;
	private int score;

	public GameState() {
		this.active = false;
		this.score = 0;
	}

	public void setActive(boolean state) {
		this.active = state;
	}

	public boolean isActive() {
		return this.active;
	}

	public int getScore() {
		return this.score;
	}

	public void resetScore() {
		this.score = 0;
	}

	public void addScore(int inc) {
		int newScore = this.score + inc;
		this.score = (newScore >= 0) ? newScore : 0;
	}

}
