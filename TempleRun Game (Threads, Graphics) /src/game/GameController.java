package game;

public class GameController {

	private static GameController controller;

	private GameState state;
	private GameFrame frame;

	public static GameController getInstance() {
		if (controller == null) {
			controller = new GameController();
		}

		return controller;
	}

	private GameController() {
		this.state = new GameState();
		this.frame = new GameFrame();
	}

	public GameState getGameState() {
		return this.state;
	}

	public GameFrame getGameFrame() {
		return this.frame;
	}
}
