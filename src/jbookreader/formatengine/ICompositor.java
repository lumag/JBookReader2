package jbookreader.formatengine;

import java.util.List;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.style.Alignment;


public interface ICompositor<T> {
	List<IDrawable<T>> compose(List<IDrawable<? extends T>> particles, int width, Alignment alignment, IGraphicDriver<? extends T> driver); 
}
