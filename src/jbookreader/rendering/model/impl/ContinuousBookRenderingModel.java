package jbookreader.rendering.model.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jbookreader.book.IBook;
import jbookreader.book.IContainerNode;
import jbookreader.book.INode;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;
import jbookreader.rendering.model.IRenderingModel;
import jbookreader.style.IStyleConfig;
import jbookreader.style.IStyleStack;
import jbookreader.style.IStylesheet;
import lumag.util.ClassFactory;
import lumag.util.RevertIterable;

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
	
	public void clear() {
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

	public float findNextHeight(float height, int direction) {
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
					return move;
				}
			}
		} else {
			for (IDrawable<?> dr: lines) {
				// FIXME: correct inter-line value!
				if (h > dr.getHeight() + dr.getDepth() + 1) {
					h -= dr.getHeight() + dr.getDepth();
				} else {
					float move = h; 
					return move;
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
//				System.out.println(dr.getContext().getNodeRef());
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
	
	INode findRoot(INode node, List<Integer> path) {
		INode temp = node;
		
		for (IContainerNode parent = temp.getParentNode();
				parent != null;
				temp = parent, parent = temp.getParentNode()
				) {
			path.add(parent.getNumber(temp));
		}
		
		return temp;
	}
	
	private int comparePaths(Iterable<Integer> path1, Iterable<Integer> path2) {
		Iterator<Integer> it1 = path1.iterator();
		Iterator<Integer> it2 = path2.iterator();
		
		while (it1.hasNext() && it2.hasNext()) {
			int idx1 = it1.next();
			int idx2 = it2.next();
			
			if (idx1 != idx2) {
				return idx1 - idx2;
			}
		}
		
		if (it1.hasNext()) {
			return 1;
		} else if (it2.hasNext()) {
			return -1;
		} else {
			return 0;
		}
	}
	
	private int comparePathNode(Iterable<Integer> path, INode node) {
		List<Integer> nodePath = new ArrayList<Integer>();
		findRoot(node, nodePath);

		Iterable<Integer> path2 = new RevertIterable<Integer>(nodePath);
		
		int res = comparePaths(path, path2);
		// System.out.println(node + "(" + node.getNodeRef() + "): " + res);
		return res;
	}
	
	public int searchPathIndex(Iterable<Integer> path) {
		int start = 0;
		int end = lines.size() - 1;
		
		if (comparePathNode(path, lines.get(start).getContext()) < 0) {
			return start;
		}
		
		if (comparePathNode(path, lines.get(end).getContext()) > 0) {
			return end;
		}
		
		while (start + 1 < end) {
			int mid = (start + end) / 2;

			int res = comparePathNode(path, lines.get(mid).getContext());
			
			if (res == 0) {
				return mid;
			} else if (res > 0) {
				start = mid;
			} else {
				end = mid;
			}
		}
		return end;
	}
	
	public float getOffset(IGraphicDriver<INode> driver, INode node) {
		List<Integer> rPath = new ArrayList<Integer>();
		/*INode root = */findRoot(node, rPath);
		
		reformatIfNecessary(driver);
		
		Iterable<Integer> basePath = new RevertIterable<Integer>(rPath);
		
		int idx = searchPathIndex(basePath);
		
//		System.out.println("Index: " + idx);
		
		float height = 0;
		
		for (int i = 0; i < idx; i++) {
			IDrawable<INode> dr = lines.get(i);
			// FIXME: correct inter-line value!
			height += dr.getHeight() + dr.getDepth();
		}

		return height;
	}

}
