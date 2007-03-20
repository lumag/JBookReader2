package jbookreader.rendering.model;

import java.util.ArrayList;
import java.util.List;

import jbookreader.book.IBook;
import jbookreader.book.INode;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.IRenderingModel;
import jbookreader.rendering.Position;
import jbookreader.style.IStyleConfig;
import jbookreader.style.IStyleStack;
import jbookreader.style.IStylesheet;
import lumag.util.ClassFactory;

public class ContinuousBookRenderingModel implements IRenderingModel<INode> {
	private IFormatEngine<INode> formatEngine;
	private Compositor<INode> compositor;
	private IBook book;
	private List<IDrawable<INode>> lines;

	private IStylesheet<INode> defaultStylesheet;
	private IStylesheet<INode> formatStylesheet;

	private IStyleConfig config;
	
	public ContinuousBookRenderingModel() {
		clear();
	}
	
	private void clear() {
		lines = new ArrayList<IDrawable<INode>>(1);
	}

	public void setConfig(IStyleConfig config) {
		this.config = config;
		clear();
	}
	
	public void setCompositor(ICompositor<INode> compositor) {
		this.compositor = new Compositor<INode>(compositor);
		clear();
	}

	public void setFormatEngine(IFormatEngine<INode> engine) {
		this.formatEngine = engine;
		clear();
	}

	public void setBook(IBook book) {
		this.book = book;
		clear();
	}

	public void setDefaultStylesheet(IStylesheet<INode> defaultStylesheet) {
		this.defaultStylesheet = defaultStylesheet;
	}

	public void setFormatStylesheet(IStylesheet<INode> formatStylesheet) {
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
			IStylesheet<INode> bookStylesheet = book.getStylesheet();
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
				System.out.println(dr.getContext().getNodeRef());
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
