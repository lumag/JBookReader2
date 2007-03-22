package jbookreader.rendering.model.impl;

import java.util.List;

import jbookreader.formatengine.ICompositor;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.style.Alignment;

class Compositor<T> implements ICompositor<T> {
	private final ICompositor<T> realCompositor;
	private long total;

	Compositor(ICompositor<T> realCompositor) {
		this.realCompositor = realCompositor;
	}

	long getTotal() {
		return total/1000000;
	}

	void clearTotal() {
		total = 0;
	}

	public List<IDrawable<T>> compose(List<IDrawable<? extends T>> particles, int width, Alignment alignment, IGraphicDriver<? extends T> driver) {

		long before = System.nanoTime();

		List<IDrawable<T>> result = realCompositor.compose(particles, width, alignment, driver);

		long after = System.nanoTime();
		total += (after - before);

		return result;
	}
}
