package jbookreader.formatengine;

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

	private int stretch;
	private int shrink;

	public void draw(Position position) {
		for (ListIterator<IDrawable> it = elements.listIterator();
			it.hasNext();) {
			IDrawable drawable = it.next();
			if (it.previousIndex() == 0) {
				drawable.draw(Position.START_OF_LINE);
			} else if (it.hasNext()) {
				drawable.draw(Position.MIDDLE_OF_LINE);
			} else {
				drawable.draw(Position.END_OF_LINE);
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

	public int getShrink() {
		return shrink;
	}

	public int getStretch() {
		return stretch;
	}

	public int calculateWidthAddition(int index, IDrawable drawable) {
		if (index == -1) {
			index = elements.size();
		}
		if (elements.isEmpty()) {
			return drawable.getWidth(Position.START_OF_LINE);
		} else if (index == 0) {
			IDrawable first = elements.get(0);
			return drawable.getWidth(Position.START_OF_LINE)
				+ first.getWidth(Position.MIDDLE_OF_LINE)
				- first.getWidth(Position.START_OF_LINE);
		} else if (index == elements.size()) {
			IDrawable last = elements.get(elements.size()-1);
			return drawable.getWidth(Position.END_OF_LINE)
				+ last.getWidth(Position.MIDDLE_OF_LINE)
				- last.getWidth(Position.END_OF_LINE);
		}
		return drawable.getWidth(Position.MIDDLE_OF_LINE);
	}

	public void add(int index, IDrawable drawable) {
		width += calculateWidthAddition(index, drawable);
		addDimensions(drawable);

		elements.add(index, drawable);
	}

	private void addDimensions(IDrawable drawable) {
		stretch += drawable.getStretch();
		shrink += drawable.getShrink();
		if (height < drawable.getHeight()) {
			height = drawable.getHeight();
		}
		if (depth < drawable.getDepth()) {
			depth = drawable.getDepth();
		}
	}

	public void add(IDrawable drawable) {
		add(elements.size(), drawable);
	}

	public void addAll(List<IDrawable> drawables) {
		for (ListIterator<IDrawable> it = drawables.listIterator();
			it.hasNext();
			) {
			IDrawable drawable = it.next();
			if (it.previousIndex() == 0 && elements.size() == 0) {
				width += drawable.getWidth(Position.START_OF_LINE);
			} else if (it.hasNext()) {
				width += drawable.getWidth(Position.MIDDLE_OF_LINE);
			} else {
				width += drawable.getWidth(Position.END_OF_LINE);
			}
			addDimensions(drawable);
			elements.add(drawable);
		}
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

}
