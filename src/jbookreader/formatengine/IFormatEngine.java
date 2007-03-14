package jbookreader.formatengine;

import java.util.List;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.style.IStyleStack;

public interface IFormatEngine<T> {

	List<IDrawable<T>> format(IGraphicDriver<T> driver,
			ICompositor<T> compositor, T context, IStyleStack<T> styleStack);

}