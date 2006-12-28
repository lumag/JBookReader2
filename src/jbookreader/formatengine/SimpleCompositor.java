package jbookreader.formatengine;

import java.util.ArrayList;
import java.util.List;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;
import jbookreader.style.Alignment;


public class SimpleCompositor implements ICompositor {
	private Alignment alignment = Alignment.JUSTIFY;

	public List<IDrawable> compose(List<IDrawable> particles, int width, IGraphicDriver driver) {
		List<IDrawable> result = new ArrayList<IDrawable>();
		List<IDrawable> line = new ArrayList<IDrawable>();
		int currentWidth = 0;
		for (IDrawable d: particles) {
			if (line.isEmpty()) {
				line.add(d);
				currentWidth = d.getWidth(Position.START_OF_LINE);
			} else if (currentWidth + d.getWidth(Position.END_OF_LINE) > width) {
				// we can't add current element as last or middle
				IDrawable last = line.get(line.size()-1);
				currentWidth = currentWidth
							- last.getWidth(Position.MIDDLE_OF_LINE)
							+ last.getWidth(Position.END_OF_LINE);

				// flush line
				result.add(makeHBox(driver, line, currentWidth, width, false));
				line.clear();

				line.add(d);
				currentWidth = d.getWidth(Position.START_OF_LINE);
			} else if (currentWidth + d.getWidth(Position.MIDDLE_OF_LINE) > width) {
				// can't add the element in the middle, but it could be last item
				line.add(d);
				currentWidth += d.getWidth(Position.END_OF_LINE);

				// flush line
				result.add(makeHBox(driver, line, currentWidth, width, false));
				line.clear();
				currentWidth = 0;
			} else {
				line.add(d);
				currentWidth += d.getWidth(Position.MIDDLE_OF_LINE);
			}
		}
		if (!line.isEmpty()) {
			IDrawable last = line.get(line.size()-1);
			currentWidth = currentWidth
						- last.getWidth(Position.MIDDLE_OF_LINE)
						+ last.getWidth(Position.END_OF_LINE);

			// flush line
			result.add(makeHBox(driver, line, currentWidth, width, true));
			line.clear();
		}
		return result;
	}

	private IDrawable makeHBox(IGraphicDriver driver, List<IDrawable> line, int currentWidth, int width, boolean last) {
		HBox hbox = new HBox();
		for (IDrawable d: line) {
			hbox.add(d);
		}
		int defect = width - currentWidth;
		if (defect == 0) {
			return hbox;
		}
		
		HBox wrapperBox;
		switch (alignment) {
		case LEFT:
			wrapperBox = new HBox();
			wrapperBox.add(hbox);
			wrapperBox.add(new SimpleWhitespace(driver, defect));
			hbox = wrapperBox;
			break;
		case RIGHT:
			wrapperBox = new HBox();
			wrapperBox.add(new SimpleWhitespace(driver, defect));
			wrapperBox.add(hbox);
			hbox = wrapperBox;
			break;
		case CENTER:
			wrapperBox = new HBox();
			wrapperBox.add(new SimpleWhitespace(driver, defect / 2));
			wrapperBox.add(hbox);
			wrapperBox.add(new SimpleWhitespace(driver, (defect + 1) / 2));
			hbox = wrapperBox;
			break;
		case JUSTIFY:
			if (hbox.getStretch(Position.MIDDLE_OF_LINE) != 0
					&& !last) {
				hbox.adjustWidth(defect);
			} else {
				wrapperBox = new HBox();
				wrapperBox.add(hbox);
				wrapperBox.add(new SimpleWhitespace(driver, defect));
				hbox = wrapperBox;
			}
			break;
		}
		return hbox;
	}

}
