package jbookreader.ui.text;

import jbookreader.book.IBook;
import jbookreader.book.IBookFactory;
import jbookreader.book.INode;
import jbookreader.fileformats.IErrorHandler;
import jbookreader.fileformats.IFileFormatDescriptor;
import jbookreader.fileformats.impl.FileFormatsLibrary;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.IRenderingModel;
import jbookreader.rendering.text.TextRenderer;
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
		final IFileFormatDescriptor fileFormat = FileFormatsLibrary.getDescriptorForFile(uri);
		final IBook book = fileFormat.parse(
				uri, handler,
				ClassFactory.createClass(IBookFactory.class, "jbookreader.factory.book"));
		System.err.println("parsed");
//		book.getFirstBody().accept(new BookDumper());

		IGraphicDriver<INode> driver = args.length > 1? 
				new TextRenderer<INode>(args[1]):
				new TextRenderer<INode>();

		IRenderingModel<INode> model = ClassFactory.createClass(IRenderingModel.class,
				"jbookreader.text.model");
		model.setFormatEngine(ClassFactory.createClass(IFormatEngine.class,
				"jbookreader.formatengine"));
		model.setCompositor(ClassFactory.createClass(ICompositor.class,
				"jbookreader.compositor"));
		model.setFormatStylesheet(fileFormat.getStylesheet());
		model.setConfig(new TextConfig());

		model.setBook(book);

		float height = model.getHeight(driver);

		System.err.println("formatted");

		model.render(driver, 0, Math.round(height), 0);
	}

}
