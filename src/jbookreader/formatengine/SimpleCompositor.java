package jbookreader.formatengine;

import java.util.ArrayList;
import java.util.List;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;
import jbookreader.style.Alignment;


public class SimpleCompositor implements ICompositor {
	private Alignment alignment = Alignment.LEFT;

	public List<IDrawable> compose(List<IDrawable> particles, int width, IGraphicDriver driver) {
		List<IDrawable> result = new ArrayList<IDrawable>();
		HBox hbox = new HBox();
		for (IDrawable d: particles) {
			int width_add = hbox.calculateWidthAddition(-1, d);
			if (hbox.getWidth(Position.MIDDLE_OF_LINE)
					+ width_add > width) {
				result.add(fixHBox(width, driver, hbox));
				hbox = new HBox();
			}
			hbox.add(d);
		}
		if (!hbox.isEmpty()) {
			result.add(fixHBox(width, driver, hbox));
		}
		return result;
	}

	private HBox fixHBox(int width, IGraphicDriver driver, HBox hbox) {
		int defect = width - hbox.getWidth(Position.MIDDLE_OF_LINE);
		if (defect == 0) {
			return hbox;
		}

		// XXX: it's bad to allocate the whole big box for such nonsense.
		//      either provide some other way to add leading and trailing
		//      whitespaces or provide 'light' hbox
		HBox wrapperBox = new HBox();
		switch (alignment) {
		case LEFT:
			wrapperBox.add(hbox);
			wrapperBox.add(new SimpleWhitespace(driver, defect));
			break;
		case RIGHT:
			wrapperBox.add(new SimpleWhitespace(driver, defect));
			wrapperBox.add(hbox);
			break;
		case CENTER:
			wrapperBox.add(new SimpleWhitespace(driver, defect / 2));
			wrapperBox.add(hbox);
			wrapperBox.add(new SimpleWhitespace(driver, (defect + 1) / 2));
			break;
		}
		return wrapperBox;
	}

}
