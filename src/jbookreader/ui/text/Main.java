package jbookreader.ui.text;

import java.util.List;

import jbookreader.book.IBook;
import jbookreader.book.IBookFactory;
import jbookreader.book.INode;
import jbookreader.fileformats.IErrorHandler;
import jbookreader.fileformats.impl.FileFormatsLibrary;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;
import jbookreader.rendering.text.TextRenderer;
import jbookreader.style.IStyleStack;
import lumag.util.ClassFactory;


public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		ClassFactory.loadProperies("jbookreader");

		IErrorHandler handler = new IErrorHandler() {

			public boolean error(boolean fatal, String message) {
				System.err.println(message);
				return true;
			}

			public void warning(String message) {
				System.err.println(message);
			}

		};

		String uri = args.length > 0 ? args[0] : 
				"tests/simple.fb2"
				//"tests/exupery_malenkiyi_princ.fb2"
			;
		IBook book = FileFormatsLibrary.getDescriptorForFile(uri).parse(
				uri, handler,
				ClassFactory.createClass(IBookFactory.class, "jbookreader.factory.book"));
		System.err.println("parsed");
//		book.getFirstBody().accept(new BookDumper());
		IGraphicDriver<INode> driver = new TextRenderer<INode>();
		ICompositor<INode> compositor = ClassFactory.createClass(ICompositor.class,
				"jbookreader.compositor");
		IFormatEngine<INode> engine = ClassFactory.createClass(IFormatEngine.class,
				"jbookreader.formatengine");
		IStyleStack<INode> styleStack = ClassFactory.createClass(IStyleStack.class,
				"jbookreader.stylestack");
		List<IDrawable<INode>> lines = engine.format(driver, compositor, book.getFirstBody(),
				styleStack);
		System.err.println("formatted");
		for (IDrawable<?> dr: lines) {
			dr.draw(Position.MIDDLE);
			driver.clear();
			System.out.println();
		}
	}

}
