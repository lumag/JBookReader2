package jbookreader.formatengine;

import java.util.List;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.style.Alignment;


public interface ICompositor {
	List<IDrawable> compose(List<IDrawable> particles, int width, Alignment alignment, IGraphicDriver driver); 
}
