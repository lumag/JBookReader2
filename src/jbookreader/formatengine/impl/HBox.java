package jbookreader.formatengine.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.Position;


class HBox implements IDrawable {
	private final List<IDrawable> elements = new ArrayList<IDrawable>();

	private int width;
	private int height;
	private int depth;

	public void draw(Position position) {
		for (ListIterator<IDrawable> it = elements.listIterator();
			it.hasNext();) {
			IDrawable drawable = it.next();
			if (it.previousIndex() == 0) {
				drawable.draw(Position.START);
			} else if (it.hasNext()) {
				drawable.draw(Position.MIDDLE);
			} else {
				drawable.draw(Position.END);
			}
		}
	}

	public int getDepth() {
		return depth;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth(Position position) {
		return width;
	}

	public void add(IDrawable drawable) {
		if (elements.isEmpty()) {
			width = drawable.getWidth(Position.START);
		} else {
			IDrawable last = elements.get(elements.size()-1);
			width += drawable.getWidth(Position.END)
				+ last.getWidth(Position.MIDDLE)
				- last.getWidth(Position.END);
		}
		if (height < drawable.getHeight()) {
			height = drawable.getHeight();
		}
		if (depth < drawable.getDepth()) {
			depth = drawable.getDepth();
		}

		elements.add(drawable);
	}

	public void addAll(List<IDrawable> drawables) {
		for (ListIterator<IDrawable> it = drawables.listIterator();
			it.hasNext();
			) {
			IDrawable drawable = it.next();
			if (it.previousIndex() == 0 && elements.size() == 0) {
				width += drawable.getWidth(Position.START);
			} else if (it.hasNext()) {
				width += drawable.getWidth(Position.MIDDLE);
			} else {
				width += drawable.getWidth(Position.END);
			}
			if (height < drawable.getHeight()) {
				height = drawable.getHeight();
			}
			if (depth < drawable.getDepth()) {
				depth = drawable.getDepth();
			}
			elements.add(drawable);
		}
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public String toString() {
		return width + " x " + height + " + " + depth;
	}
}
