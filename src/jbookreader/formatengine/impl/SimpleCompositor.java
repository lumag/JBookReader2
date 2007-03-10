package jbookreader.formatengine.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import jbookreader.formatengine.IAdjustableDrawable;
import jbookreader.formatengine.ICompositor;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;
import jbookreader.style.Alignment;


public class SimpleCompositor<T> implements ICompositor<T> {
	public List<IDrawable<T>> compose(List<IDrawable<? extends T>> particles, int width, Alignment alignment, IGraphicDriver<? extends T> driver) {
		List<IDrawable<T>> result = new ArrayList<IDrawable<T>>();
		List<IDrawable<? extends T>> line = new ArrayList<IDrawable<? extends T>>();
		float currentWidth = 0;
		for (IDrawable<? extends T> d: particles) {
			if (line.isEmpty()) {
				line.add(d);
				currentWidth = d.getWidth(Position.START);
			} else if (currentWidth + d.getWidth(Position.END) > width) {
				// we can't add current element as last or middle
				IDrawable<? extends T> last = line.get(line.size()-1);
				currentWidth = currentWidth
							- last.getWidth(Position.MIDDLE)
							+ last.getWidth(Position.END);

				// flush line
				result.add(makeHBox(driver, line, width, alignment, false));
				line.clear();

				line.add(d);
				currentWidth = d.getWidth(Position.START);
			} else if (currentWidth + d.getWidth(Position.MIDDLE) > width) {
				// can't add the element in the middle, but it could be last item
				line.add(d);
				currentWidth += d.getWidth(Position.END);

				// flush line
				result.add(makeHBox(driver, line, width, alignment, false));
				line.clear();
				currentWidth = 0;
			} else {
				line.add(d);
				currentWidth += d.getWidth(Position.MIDDLE);
			}
		}
		if (!line.isEmpty()) {
			IDrawable<? extends T> last = line.get(line.size()-1);
			currentWidth = currentWidth
						- last.getWidth(Position.MIDDLE)
						+ last.getWidth(Position.END);

			// flush line
			result.add(makeHBox(driver, line, width, alignment, true));
			line.clear();
		}
		return result;
	}

	private IDrawable<T> makeHBox(IGraphicDriver<? extends T> driver, List<IDrawable<? extends T>> line, float width, Alignment alignment, boolean last) {
		HBox<T> hbox;
		switch (alignment) {
		case JUSTIFY:
		{
			hbox = makeJustifiedHBox(line, width, last);
			float boxWidth = hbox.getWidth(Position.MIDDLE);
			float defect = width - boxWidth;
			if (defect != 0) {
				HBox<T> wrapperBox = new HBox<T>();
				wrapperBox.add(hbox);
				wrapperBox.add(new SimpleWhitespace<T>(driver, defect, hbox.getContext()));
				hbox = wrapperBox;
			}
		}
		break;
		case LEFT:
		{
			hbox = new HBox<T>();
			hbox.addAll(line);
			float boxWidth = hbox.getWidth(Position.MIDDLE);
			float defect = width - boxWidth;
			if (defect != 0) {
				HBox<T> wrapperBox = new HBox<T>();
				wrapperBox.add(hbox);
				wrapperBox.add(new SimpleWhitespace<T>(driver, defect, hbox.getContext()));
				hbox = wrapperBox;
			}
		}
		break;
		case RIGHT:
		{
			hbox = new HBox<T>();
			hbox.addAll(line);
			float boxWidth = hbox.getWidth(Position.MIDDLE);
			float defect = width - boxWidth;
			if (defect != 0) {
				HBox<T> wrapperBox = new HBox<T>();
				wrapperBox.add(new SimpleWhitespace<T>(driver, defect, hbox.getContext()));
				wrapperBox.add(hbox);
				hbox = wrapperBox;
			}
		}
		break;
		case CENTER:
		{
			hbox = new HBox<T>();
			hbox.addAll(line);
			float boxWidth = hbox.getWidth(Position.MIDDLE);
			float defect = width - boxWidth;
			if (defect != 0) {
				HBox<T> wrapperBox = new HBox<T>();
				wrapperBox.add(new SimpleWhitespace<T>(driver, defect / 2, hbox.getContext()));
				wrapperBox.add(hbox);
				wrapperBox.add(new SimpleWhitespace<T>(driver, defect - (defect / 2), hbox.getContext()));
				hbox = wrapperBox;
			}
		}
		break;
		default: throw new InternalError("Unhandled alignment type: " + alignment);
		}
		return hbox;
	}

	private HBox<T> makeJustifiedHBox(List<IDrawable<? extends T>> line, float width, boolean last) {
		if (last) {
			HBox<T> hbox = new HBox<T>();
			hbox.addAll(line);
			return hbox;
		}
		float currentWidth = 0;
		float currentStretch = 0;
		for (ListIterator<IDrawable<? extends T>> iter = line.listIterator(); iter.hasNext();) {
			IDrawable<? extends T> d = iter.next();
			if (iter.previousIndex() == 0) {
				currentWidth += d.getWidth(Position.START);
			} else if (iter.hasNext()) {
				currentWidth += d.getWidth(Position.MIDDLE);
			} else {
				currentWidth += d.getWidth(Position.END);
			}
			
			if (d instanceof IAdjustableDrawable) {
				IAdjustableDrawable<?> ad = (IAdjustableDrawable<?>)d;
				if (iter.previousIndex() == 0) {
					currentStretch += ad.getStretch(Position.START);
				} else if (iter.hasNext()) {
					currentStretch += ad.getStretch(Position.MIDDLE);
				} else {
					currentStretch += ad.getStretch(Position.END);
				}
			}
		}
			
		if (currentStretch == 0) {
			HBox<T> hbox = new HBox<T>();
			hbox.addAll(line);
			return hbox;
		}
		
		float adjust = width - currentWidth;
		if (adjust < 0) {
			// FIXME: implement shrinking
			throw new UnsupportedOperationException("Shrinking isn't supported yet");
		}

		HBox<T> hbox = new HBox<T>();
		for (ListIterator<IDrawable<? extends T>> iter = line.listIterator(); iter.hasNext();) {
			IDrawable<? extends T> drawable = iter.next();

			if (drawable instanceof IAdjustableDrawable) {
				IAdjustableDrawable<?> d = (IAdjustableDrawable<?>) drawable;
				
				float adj_i;
				if (iter.previousIndex() == 0) {
					adj_i = d.getStretch(Position.START);
				} else if (iter.hasNext()) {
					adj_i = d.getStretch(Position.MIDDLE);
				} else {
					adj_i = d.getStretch(Position.END);
				}
				if (adj_i != 0) {
					float amount = adjust * adj_i / currentStretch;
					adjust -= amount;
					currentStretch -= adj_i;
					d.adjust(amount);
				}
			}

			hbox.add(drawable);
		}
		return hbox;
	}

}
