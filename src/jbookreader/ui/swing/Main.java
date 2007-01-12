package jbookreader.ui.swing;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import jbookreader.book.IBook;
import jbookreader.book.IBookFactory;
import jbookreader.fileformats.IErrorHandler;
import jbookreader.fileformats.impl.FileFormatsLibrary;
import jbookreader.fileformats.impl.UnknownFormatException;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.rendering.swing.JGraphicDriver;
import lumag.util.ClassFactory;

import org.xml.sax.SAXException;

public class Main {
	public static void main(String[] args) {
		ClassFactory.loadProperies("jbookreader");

		final String filename = args.length == 0?
				//"tests/simple.fb2"
				"tests/exupery_malenkiyi_princ.fb2"
				//"tests/test.rtf"
				//"tests/yekzyuperi_antuan_malenkii_princ.rtf"
				: args[0];
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				// JFrame.setDefaultLookAndFeelDecorated(true);
				MainWindow window = new MainWindow();
				final JGraphicDriver driver = window.getGraphicDriver();
				
				driver.setCompositor(
						ClassFactory.createClass(ICompositor.class,
								"jbookreader.compositor"));
				driver.setFormatEngine(
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
							final IBook book = FileFormatsLibrary.getDescriptorForFile(filename).parse(
									filename, handler,
									ClassFactory.createClass(IBookFactory.class, "jbookreader.factory.book"));
							SwingUtilities.invokeAndWait(new Runnable() {
								public void run() {
									driver.setBook(book);
									
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
