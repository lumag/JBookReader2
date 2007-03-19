package jbookreader.fileformats.rtf;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import jbookreader.book.IBook;
import jbookreader.book.IBookFactory;
import jbookreader.book.INode;
import jbookreader.css.CSSParser;
import jbookreader.fileformats.IErrorHandler;
import jbookreader.fileformats.IFileFormatDescriptor;
import jbookreader.style.IStyleRule;
import jbookreader.style.IStylesheet;
import lumag.rtf.RTFParser;

import org.xml.sax.SAXException;

public class RichTextFormat implements IFileFormatDescriptor {

	private final Collection<String> extensions;
	private IStylesheet<INode> stylesheet;
	
	public RichTextFormat() {
		extensions = new ArrayList<String>();
		extensions.add(".rtf");
	}

	public IBook parse(String uri, IErrorHandler handler, IBookFactory factory)
			throws SAXException, IOException {
		RTFParser parser = new RTFParser();
		RTFHandler rtfHandler = new RTFHandler(factory, parser);
		parser.setHandler(rtfHandler);
		InputStream stream = new BufferedInputStream(
				new FileInputStream(uri));
		byte[] buffer = new byte[1024];
		while (true) {
			int len = stream.read(buffer);
			if (len < 0) {
				break;
			}
			parser.process(buffer, 0, len);
		}
		stream.close();
		return rtfHandler.getBook();
	}

	public String getDescription() {
		return "Rich Text Format";
	}

	public Collection<String> getExtensions() {
		return extensions;
	}

	public IStylesheet<INode> getStylesheet() {
		if (stylesheet == null) {
			try {
				stylesheet = CSSParser.parse("resources/css/rtf.css");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (stylesheet == null) {
			return new IStylesheet<INode>() {
				public List<IStyleRule> getApplicableRules(INode node) {
					return new LinkedList<IStyleRule>();
				}
			};
		}
		return stylesheet;
	}

}
