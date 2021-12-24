package domain;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.GameConstants;

public class Runner {

	private static String sep = System.getProperty("file.separator");
	private static String imgFilePath = System.getProperty("user.dir") + sep + "resources" + sep + "figur.png";
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

	private int location;

	public Runner() {
		this.location = 0;
	}

	// TODO Exercise 1.b
	public Rectangle getBounds() {
		int x = GameConstants.middleX + this.location * GameConstants.dX;
		int y = GameConstants.runnerY;
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
	public synchronized void moveRight() {
		this.location = (this.location + 1 <= 1) ? this.location + 1 : this.location;
	}

	// TODO Exercise 1.d
	public synchronized void moveLeft() {
		this.location = (this.location - 1 >= -1) ? this.location - 1 : this.location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public int getLocation() {
		return this.location;
	}

}
