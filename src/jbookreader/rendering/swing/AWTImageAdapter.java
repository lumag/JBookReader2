package jbookreader.rendering.swing;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.Position;

public class AWTImageAdapter implements IDrawable {

	private final JGraphicDriver driver;
	private final BufferedImage image;
	private int width;

	public AWTImageAdapter(JGraphicDriver driver, BufferedImage image) {
		this.driver = driver;
		// TODO Auto-generated constructor stub
		this.image = image;
		width = JGraphicDriver.pixelToDimension(image.getWidth());
	}

	public void draw(Position position) {
		driver.getPaperGraphics().drawImage(image,
				AffineTransform.getTranslateInstance(
						JGraphicDriver.dimensionToPixel(driver.getHorizontalPosition()),
						JGraphicDriver.dimensionToPixel(driver.getVerticalPosition())),
				driver);
		driver.horizontalPosition += width;
	}

	public int getDepth() {
		return 0;
	}

	public int getHeight() {
		return JGraphicDriver.pixelToDimension(image.getHeight());
	}

	public int getShrink(Position position) {
		return 0;
	}

	public int getStretch(Position position) {
		return 0;
	}

	public int getWidth(Position position) {
		return width;
	}

	public void adjustWidth(int adjust) {
		throw new UnsupportedOperationException("Width adjustment not supported");
	}

}
