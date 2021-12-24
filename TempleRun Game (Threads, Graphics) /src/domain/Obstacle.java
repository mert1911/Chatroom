package domain;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.GameConstants;

public class Obstacle {

	private static String sep = System.getProperty("file.separator");
	private static String imgFilePath = System.getProperty("user.dir") + sep + "resources" + sep + "hole.png";
	private static BufferedImage image;

	static {
		try {
			image = ImageIO.read(new File(imgFilePath));
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
			System.exit(0);
		}
	}

	private int x, y;

	public Obstacle() {
		this.x = (int) (Math.random() * 3) - 1;
		this.y = -1;
	}

	// TODO Exercise 1.b
	private Rectangle getBounds() {
		int x = GameConstants.middleX + this.x * GameConstants.dX;
		int y = GameConstants.topY + this.y * GameConstants.dY;
		int width = image.getWidth();
		int height = image.getHeight();

		return new Rectangle(x, y, width, height);
	}

	// TODO Exercise 1.b
	public void draw(Graphics2D graphics2d) {
		Rectangle bounds = this.getBounds();
		graphics2d.drawImage(image, bounds.x, bounds.y, null);
	}

	// TODO Exercise 1.d
	public synchronized void moveDown() {
		if (this.y + 1 > GameConstants.noOfSteps) {
			this.y = 0;
			this.x = (int) (Math.random() * 3) - 1;
		} else {
			this.y++;
		}
	}

	// TODO Exercise 2.b
	public boolean isHit(int pos) {
		Runner runner = new Runner();
		runner.setLocation(pos);

		return this.getBounds().intersects(runner.getBounds());
	}

}
