package jbookreader.ui.text;

import java.util.List;

import jbookreader.book.IBook;
import jbookreader.fileformats.IErrorHandler;
import jbookreader.fileformats.impl.FictionBook2;
import jbookreader.formatengine.FormatEngine;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.formatengine.SimpleCompositor;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;
import jbookreader.rendering.text.TextRenderer;
import jbookreader.util.BookFactoryCreator;


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

		IBook book = new FictionBook2().parse(
				args.length > 0 ? args[0] : 
				"tests/simple.fb2"
				//"tests/exupery_malenkiyi_princ.fb2"
				,handler, BookFactoryCreator.getBookFactory());
		System.err.println("parsed");
//		book.getFirstBody().accept(new BookDumper());
		IGraphicDriver driver = new TextRenderer();
		ICompositor compositor = new SimpleCompositor();
		IFormatEngine engine = new FormatEngine();
		@SuppressWarnings("unused")
		List<IDrawable> lines = engine.format(driver, compositor, book.getFirstBody());
		System.err.println("formatted");
		for (IDrawable dr: lines) {
			dr.draw(Position.MIDDLE_OF_LINE);
			driver.clear();
			System.out.println();
		}
	}

}
