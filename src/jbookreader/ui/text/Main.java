package jbookreader.ui.text;

import java.util.List;

import jbookreader.book.IBook;
import jbookreader.book.IBookFactory;
import jbookreader.fileformats.IErrorHandler;
import jbookreader.fileformats.impl.FileFormatsLibrary;
import jbookreader.formatengine.FormatEngine;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.formatengine.SimpleCompositor;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;
import jbookreader.rendering.text.TextRenderer;
import lumag.util.ClassFactory;


public class Main {

	public static void main(String[] args) throws Exception {
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
				ClassFactory.createClass(IBookFactory.class, "jbookreader.book.factory"));
		System.err.println("parsed");
//		book.getFirstBody().accept(new BookDumper());
		IGraphicDriver driver = new TextRenderer();
		ICompositor compositor = new SimpleCompositor();
		IFormatEngine engine = new FormatEngine();
		@SuppressWarnings("unused")
		List<IDrawable> lines = engine.format(driver, compositor, book.getFirstBody());
		System.err.println("formatted");
		for (IDrawable dr: lines) {
			dr.draw(Position.MIDDLE);
			driver.clear();
			System.out.println();
		}
	}

}
