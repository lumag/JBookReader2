package jbookreader.rendering.swing;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.Position;

class AWTImageAdapter implements IDrawable {

	private final JGraphicDriver driver;
	private BufferedImage image;
	private final int width;
	private final int height;

	AWTImageAdapter(JGraphicDriver driver, String contentType, InputStream dataStream) throws IOException {
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

		width = reader.getWidth(0);
		height = reader.getHeight(0);

		Thread imageThread =
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
		});
		imageThread.setPriority(Thread.MIN_PRIORITY);
		imageThread.start();
	}

	AWTImageAdapter(JGraphicDriver driver, BufferedImage image) {
		this.driver = driver;
		this.image = image;
		width = image.getWidth();
		height = image.getHeight();
	}

	public void draw(Position position) {
		BufferedImage drawImage = null;

		synchronized (this) {
			drawImage = image;
		}
		if (drawImage == null) {
			driver.getPaperGraphics().draw(new Rectangle2D.Float(
						driver.getHorizontalPosition(),
						driver.getVerticalPosition(),
						width,
						height));
		} else {
			driver.getPaperGraphics().drawImage(image,
					AffineTransform.getTranslateInstance(
						driver.getHorizontalPosition(),
						driver.getVerticalPosition()),
					driver);
		}
		driver.horizontalPosition += width;
	}

	public float getDepth() {
		return 0;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth(Position position) {
		return width;
	}

}
