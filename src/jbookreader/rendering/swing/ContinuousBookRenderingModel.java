package jbookreader.rendering.swing;

import java.util.ArrayList;
import java.util.List;

import jbookreader.book.IBook;
import jbookreader.book.INode;
import jbookreader.book.IStylesheet;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.formatengine.IStyleConfig;
import jbookreader.formatengine.IStyleStack;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;
import lumag.util.ClassFactory;

public class ContinuousBookRenderingModel implements IRenderingModel {
	private IFormatEngine<INode> formatEngine;
	private Compositor<INode> compositor;
	private IBook book;
	private List<IDrawable<INode>> lines = new ArrayList<IDrawable<INode>>(1);

	private IStylesheet defaultStylesheet;
	private IStylesheet formatStylesheet;

	private IStyleConfig config = new JGraphicDriverConfig();
	
	public void setCompositor(ICompositor<INode> compositor) {
		this.compositor = new Compositor<INode>(compositor);
	}

	public void setFormatEngine(IFormatEngine<INode> engine) {
		this.formatEngine = engine;
	}

	public void setBook(IBook book) {
		this.book = book;
		lines = new ArrayList<IDrawable<INode>>(1);
	}

	public void setDefaultStylesheet(IStylesheet defaultStylesheet) {
		this.defaultStylesheet = defaultStylesheet;
	}

	public void setFormatStylesheet(IStylesheet formatStylesheet) {
		this.formatStylesheet = formatStylesheet;
	}

	@SuppressWarnings("unchecked")
	private IStyleStack<INode> createStyleStack() {
		return ClassFactory.createClass(IStyleStack.class, "jbookreader.stylestack");
	}

	public int findNextHeight(float height, int direction) {
		if (lines == null || lines.isEmpty() || book == null) {
			return 0;
		}
		
		float h = height;
		if (direction > 0) {
			for (IDrawable<?> dr: lines) {
				// FIXME: correct inter-line value!
				h -= dr.getHeight() + dr.getDepth();
				float move = - h;
				if (move > 1) {
					return Math.round(move);
				}
			}
		} else {
			for (IDrawable<?> dr: lines) {
				// FIXME: correct inter-line value!
				if (h > dr.getHeight() + dr.getDepth() + 1) {
					h -= dr.getHeight() + dr.getDepth();
				} else {
					float move = h; 
					return Math.round(move);
				}
			}
		}
		return 0;
	}

	private void reformatIfNecessary(IGraphicDriver<INode> driver) {
		if (book != null && (lines.isEmpty()
				|| driver.getPaperWidth() != lines.get(0).getWidth(Position.MIDDLE)
				)) {
			// FIXME: move to separate thread!
			System.err.println("formatting");
			IStyleStack<INode> styleStack = createStyleStack();
			styleStack.setConfig(config);
			if (defaultStylesheet != null ) {
				styleStack.addStylesheet(defaultStylesheet);
			}
			if (formatStylesheet != null ) {
				styleStack.addStylesheet(formatStylesheet);
			}
			IStylesheet bookStylesheet = book.getStylesheet();
			if (bookStylesheet != null) {
				styleStack.addStylesheet(bookStylesheet);
			}
			compositor.clearTotal();
			long before = System.nanoTime();
			lines = formatEngine.format(driver, compositor, book.getFirstBody(),
					styleStack);
			long after = System.nanoTime();
			long format = (after - before)/1000000;
			long compose = compositor.getTotal();
			System.err.println("done " + format + "ms (" + compose+ "ms for composition)");
		}
	}

	public void render(IGraphicDriver<INode> driver, int minHeight, int maxHeight, int offset) {
		reformatIfNecessary(driver);

		System.out.print("rendering from " + minHeight + " to " + maxHeight + "... ");
		driver.clear();
		driver.addVerticalSpace(-offset);
		long before = System.nanoTime();
		for (IDrawable<INode> dr: lines) {
			if (driver.getVerticalPosition() + dr.getHeight() > minHeight) {
				dr.draw(Position.MIDDLE);
				driver.addHorizontalSpace(-driver.getHorizontalPosition());
			}
			// FIXME: correct inter-line value!
			driver.addVerticalSpace(dr.getHeight() + dr.getDepth());
			if (driver.getVerticalPosition() > maxHeight) {
				break;
			}
		}
		long after = System.nanoTime();
		System.err.println("done " + (after - before)/1000000 + "ms");
	}

	public float getHeight(IGraphicDriver<INode> driver) {
		reformatIfNecessary(driver);

		float height = 0;

		for (IDrawable<?> dr: lines) {
			// FIXME: correct inter-line value!
			height += dr.getHeight() + dr.getDepth();
		}
		
		return height;
	}

}
