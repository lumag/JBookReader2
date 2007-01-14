package jbookreader.formatengine;

import java.util.List;

import jbookreader.book.INode;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;

public interface IFormatEngine {

	List<IDrawable> format(IGraphicDriver driver,
			ICompositor compositor, INode node, IStyleStack styleStack);

}