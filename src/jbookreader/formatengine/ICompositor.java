package jbookreader.formatengine;

import java.util.List;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;


public interface ICompositor {
	List<IDrawable> compose(List<IDrawable> particles, int width, IGraphicDriver driver); 
}
