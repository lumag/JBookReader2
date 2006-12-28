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

	public int getShrink(Position position) {
		return shrink;
	}

	public int getStretch(Position position) {
		return stretch;
	}

	public void add(int index, IDrawable drawable) {
		if (index == -1) {
			index = elements.size();
		}
		if (elements.isEmpty()) {
			width += drawable.getWidth(Position.START_OF_LINE);
			stretch += drawable.getStretch(Position.START_OF_LINE);
			shrink += drawable.getShrink(Position.START_OF_LINE);
		} else if (index == 0) {
			IDrawable first = elements.get(0);
			width += drawable.getWidth(Position.START_OF_LINE)
				+ first.getWidth(Position.MIDDLE_OF_LINE)
				- first.getWidth(Position.START_OF_LINE);
			stretch += drawable.getStretch(Position.START_OF_LINE)
				+ first.getStretch(Position.MIDDLE_OF_LINE)
				- first.getStretch(Position.START_OF_LINE);
			shrink += drawable.getShrink(Position.START_OF_LINE)
				+ first.getShrink(Position.MIDDLE_OF_LINE)
				- first.getShrink(Position.START_OF_LINE);
		} else if (index == elements.size()) {
			IDrawable last = elements.get(elements.size()-1);
			width += drawable.getWidth(Position.END_OF_LINE)
				+ last.getWidth(Position.MIDDLE_OF_LINE)
				- last.getWidth(Position.END_OF_LINE);
			stretch += drawable.getStretch(Position.END_OF_LINE)
				+ last.getStretch(Position.MIDDLE_OF_LINE)
				- last.getStretch(Position.END_OF_LINE);
			shrink += drawable.getShrink(Position.END_OF_LINE)
				+ last.getShrink(Position.MIDDLE_OF_LINE)
				- last.getShrink(Position.END_OF_LINE);
		} else {
			width += drawable.getWidth(Position.MIDDLE_OF_LINE);
			stretch += drawable.getStretch(Position.MIDDLE_OF_LINE);
			shrink += drawable.getShrink(Position.MIDDLE_OF_LINE);
		}
		if (height < drawable.getHeight()) {
			height = drawable.getHeight();
		}
		if (depth < drawable.getDepth()) {
			depth = drawable.getDepth();
		}

		elements.add(index, drawable);
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
				stretch += drawable.getStretch(Position.START_OF_LINE);
				shrink += drawable.getShrink(Position.START_OF_LINE);
			} else if (it.hasNext()) {
				width += drawable.getWidth(Position.MIDDLE_OF_LINE);
				stretch += drawable.getStretch(Position.MIDDLE_OF_LINE);
				shrink += drawable.getShrink(Position.MIDDLE_OF_LINE);
			} else {
				width += drawable.getWidth(Position.END_OF_LINE);
				stretch += drawable.getStretch(Position.END_OF_LINE);
				shrink += drawable.getShrink(Position.END_OF_LINE);
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

	public void adjustWidth(int adjust) {
		int err = 0;
		if (adjust < 0) {
			// FIXME: implement shrinking
			throw new UnsupportedOperationException("Shrinking isn't supported yet");
		}

		int step = adjust/stretch;

		for (ListIterator<IDrawable> iter = elements.listIterator(); iter.hasNext();) {
			IDrawable d = iter.next();
			
			int adj_i;
			if (iter.previousIndex() == 0) {
				adj_i = d.getStretch(Position.START_OF_LINE);
			} else if (iter.hasNext()) {
				adj_i = d.getStretch(Position.MIDDLE_OF_LINE);
			} else {
				adj_i = d.getStretch(Position.END_OF_LINE);
			}
			if (adj_i != 0) {
				err += 2 * adjust * adj_i - 2 * step * stretch;
				if (err > stretch) {
					err -= stretch;
					d.adjustWidth(step*adj_i+1);
					width += step*adj_i+1;
				} else {
					d.adjustWidth(step*adj_i);
					width += step*adj_i;
				}
			}
		}
	}

}
