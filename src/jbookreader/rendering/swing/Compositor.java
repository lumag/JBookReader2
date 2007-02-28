package jbookreader.rendering.swing;

import java.util.List;

import jbookreader.formatengine.ICompositor;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.style.Alignment;

class Compositor implements ICompositor {
	private final ICompositor realCompositor;
	private long total;

	Compositor(ICompositor realCompositor) {
		this.realCompositor = realCompositor;
	}

	long getTotal() {
		return total;
	}

	void clearTotal() {
		total = 0;
	}

	public List<IDrawable> compose(List<IDrawable> particles, int width, Alignment alignment, IGraphicDriver driver) {

		long before = System.nanoTime();

		List<IDrawable> result = realCompositor.compose(particles, width, alignment, driver);

		long after = System.nanoTime();
		total += (after - before)/1000000;

		return result;
	}
}
