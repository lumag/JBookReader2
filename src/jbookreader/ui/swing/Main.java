package jbookreader.ui.swing;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import jbookreader.book.IBook;
import jbookreader.book.IBookFactory;
import jbookreader.book.INode;
import jbookreader.css.CSSParser;
import jbookreader.fileformats.IErrorHandler;
import jbookreader.fileformats.IFileFormatDescriptor;
import jbookreader.fileformats.UnknownFormatException;
import jbookreader.fileformats.impl.FileFormatsLibrary;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.rendering.swing.ContinuousBookRenderingModel;
import jbookreader.rendering.swing.IRenderingModel;
import jbookreader.rendering.swing.JBookComponent;
import jbookreader.style.IStylesheet;
import lumag.util.ClassFactory;

import org.xml.sax.SAXException;

public class Main {
	public static void main(String[] args) {
		ClassFactory.loadProperies("jbookreader");

		final String filename = args.length == 0?
				"tests/simple.fb2"
				//"tests/exupery_malenkiyi_princ.fb2"
				//"tests/test.rtf"
				//"tests/yekzyuperi_antuan_malenkii_princ.rtf"
				: args[0];
		final String ref = args.length < 2 ? null : args[1];
		SwingUtilities.invokeLater(new Runnable() {

			@SuppressWarnings("unchecked")
			public void run() {
				final IRenderingModel<INode> model = new ContinuousBookRenderingModel();
				// JFrame.setDefaultLookAndFeelDecorated(true);

				final JBookComponent<INode> driver = new JBookComponent<INode>();
				driver.setRenderingModel(model);

				new MainWindow().setMainComponent(driver);
				
				model.setCompositor(
						ClassFactory.createClass(ICompositor.class,
								"jbookreader.compositor"));
				model.setFormatEngine(
						ClassFactory.createClass(IFormatEngine.class,
								"jbookreader.formatengine"));
				
				new Thread(new Runnable() {
					public void run() {
						IErrorHandler handler = new IErrorHandler() {

							public boolean error(boolean fatal, String message) {
								System.err.println(message);
								return true;
							}

							public void warning(String message) {
								System.err.println(message);
							}

						};

						try {
							final IStylesheet<INode> defaultStylesheet = CSSParser.parse("resources/css/default.css");
							final IFileFormatDescriptor fileFormat = FileFormatsLibrary.getDescriptorForFile(filename);
							final IBook book = fileFormat.parse(
									filename, handler,
									ClassFactory.createClass(IBookFactory.class, "jbookreader.factory.book"));
							SwingUtilities.invokeAndWait(new Runnable() {
								public void run() {
									model.setDefaultStylesheet(defaultStylesheet);
									model.setFormatStylesheet(fileFormat.getStylesheet());
									model.setBook(book);
									
									if (ref != null) {
										INode node = book.getNodeByRef(ref);
										System.out.println(node);
										// FIXME: scroll to node
									}
									
									driver.repaint();
								}
							});
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (UnknownFormatException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
	}
}
