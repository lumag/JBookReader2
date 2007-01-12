package jbookreader.rendering.swing;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.Position;

public class AWTImageAdapter implements IDrawable {

	private final JGraphicDriver driver;
	private BufferedImage image;
	private final int width;
	private final int height;

	public AWTImageAdapter(JGraphicDriver driver, String contentType, InputStream dataStream) throws IOException {
		this.driver = driver;

		Iterator<ImageReader> readers = ImageIO.getImageReadersByMIMEType(contentType);
		if (!readers.hasNext()) {
			throw new UnsupportedOperationException("content type '" + contentType + "' isn't supported");
		}
		final ImageReader reader = readers.next();
		ImageInputStream stream = ImageIO.createImageInputStream(dataStream);
		if (stream == null) {
			throw new UnsupportedOperationException("Can't create image input stream");
		}
		reader.setInput(stream, true);

		width = JGraphicDriver.pixelToDimension(reader.getWidth(0));
		height = JGraphicDriver.pixelToDimension(reader.getHeight(0));

		new Thread(new Runnable() {
			public void run() {
				try {
					BufferedImage readImage = reader.read(0);
					synchronized (AWTImageAdapter.this) {
						image = readImage;
					}
					AWTImageAdapter.this.driver.repaint();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public AWTImageAdapter(JGraphicDriver driver, BufferedImage image) {
		this.driver = driver;
		this.image = image;
		width = JGraphicDriver.pixelToDimension(image.getWidth());
		height = JGraphicDriver.pixelToDimension(image.getHeight());
	}

	public void draw(Position position) {
		BufferedImage drawImage = null;

		synchronized (this) {
			drawImage = image;
		}
		if (drawImage == null) {
			driver.getPaperGraphics().draw(new Rectangle2D.Float(
						JGraphicDriver.dimensionToPixel(driver.getHorizontalPosition()),
						JGraphicDriver.dimensionToPixel(driver.getVerticalPosition()),
						JGraphicDriver.dimensionToPixel(width),
						JGraphicDriver.dimensionToPixel(height)));
		} else {
			driver.getPaperGraphics().drawImage(image,
					AffineTransform.getTranslateInstance(
						JGraphicDriver.dimensionToPixel(driver.getHorizontalPosition()),
						JGraphicDriver.dimensionToPixel(driver.getVerticalPosition())),
					driver);
		}
		driver.horizontalPosition += width;
	}

	public int getDepth() {
		return 0;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth(Position position) {
		return width;
	}

}
