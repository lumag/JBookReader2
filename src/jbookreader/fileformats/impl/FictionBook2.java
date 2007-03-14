package jbookreader.fileformats.impl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipInputStream;

import jbookreader.book.IBinaryBlob;
import jbookreader.book.IBook;
import jbookreader.book.IBookFactory;
import jbookreader.book.IContainerNode;
import jbookreader.book.IImageNode;
import jbookreader.book.INamedNode;
import jbookreader.book.INode;
import jbookreader.book.ITextNode;
import jbookreader.css.CSSParser;
import jbookreader.fileformats.IErrorHandler;
import jbookreader.fileformats.IFileFormatDescriptor;
import jbookreader.style.IStyleRule;
import jbookreader.style.IStylesheet;
import lumag.util.Base64;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

class FictionBook2 implements IFileFormatDescriptor {
	private static class FB2ContentsHandler extends DefaultHandler {
		private static final Set<String> MIXED_NODES = new HashSet<String>();
		
		private final IBookFactory factory;
		private final IBook book;
		private IContainerNode containerNode;
		private IBinaryBlob binaryBlob;

		private final StringBuilder textAccumulator = new StringBuilder();
		private boolean mixedNode;

		static {
			String[] mixedNodeTags = new String[]{
					"a",
					"code",
					"emphasis",
					"p",
					"strikethrough",
					"strong",
					"style",
					"sub",
					"subtitle",
					"sup",
					"td",
					"text-author",
					"th",
					"v",
			};
			for (String tag: mixedNodeTags) {
				MIXED_NODES.add(tag);
			}
		}

		FB2ContentsHandler(final IBookFactory factory) {
			this.factory = factory;
			book = factory.newBook();

		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
										throws SAXException {
			if (localName.equals("FictionBook")) {
				// XXX: root node
				return;
			}
			if (containerNode != null) { // internal book part
				processTextNode();
//				System.out.print("<" + localName + ">");
				if (localName.equals("image")) {
					IImageNode node = factory.newImageNode();
					node.setNodeTag(localName);
					String altText = attributes.getValue("", "alt");
					node.setText(altText != null? altText: "");
					node.setHRef(attributes.getValue("http://www.w3.org/1999/xlink", "href"));
					containerNode.add(node);
				} else {
					IContainerNode node = factory.newContainerNode();
					node.setNodeTag(localName);
					containerNode.add(node);
					containerNode = node;
					mixedNode = isMixedNode(containerNode);
				}
			} else if (localName.equals("body")) {
				IContainerNode node = factory.newContainerNode();
				node.setNodeTag(localName);
				book.addBody(node, attributes.getValue("", "name"));
				containerNode = node;
			} else if (localName.equals("binary")) {
				binaryBlob = factory.newBinaryBlob();
				binaryBlob.setContentType(
						attributes.getValue("", "content-type"));
				book.addBinaryBlob(binaryBlob, attributes.getValue("", "id"));
				mixedNode = true;
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (containerNode != null) {
				processTextNode();
				if (localName.equals("image")) {
					// do nothing
				} else {
//					System.out.print("</" + localName + ">");
					containerNode = containerNode.getParentNode();
					if (containerNode != null) {
						mixedNode = isMixedNode(containerNode);
					} else {
						mixedNode = false;
					}
				}
			} else if (localName.equals("binary")) {
				// System.out.println("binary " + binaryName + " ctype " + binaryContentType);
				byte[] data = Base64.decode(textAccumulator.toString());
				textAccumulator.setLength(0);
				
				binaryBlob.setData(data);

				binaryBlob = null;
				mixedNode = false;
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (mixedNode) {
				textAccumulator.append(ch, start, length);
			}
		}

		private boolean isMixedNode(INamedNode node) {
			String tag = node.getNodeTag();
			if (MIXED_NODES.contains(tag)) {
				return true;
			}
			return false;
		}

		private void processTextNode() {
			if (textAccumulator.length() == 0) {
				return;
			}
			ITextNode node = factory.newTextNode();
			String str = textAccumulator.toString();
			textAccumulator.setLength(0);
			node.setText(str);
			containerNode.add(node);
//			System.out.print(str);
		}

		public IBook getBook() {
			return book;
		}
	}
	private final Collection<String> extensions;
	private IStylesheet<INode> stylesheet;

	FictionBook2() {
		extensions = new ArrayList<String>();
		extensions.add(".fb2");
		extensions.add(".fb2.zip");
		extensions.add(".fbz");
	}
	
	private InputStream constructInputStream(InputStream ins) throws IOException {
		InputStream stream;
		if (ins.markSupported()) {
			stream = ins;
		} else {
			stream = new BufferedInputStream(ins);
		}
		stream.mark(2);
		byte[] header = new byte[2];
		stream.read(header);
		stream.reset();
		if (header[0] == 'P' && header[1] == 'K') {
			ZipInputStream zip = new ZipInputStream(stream);
			zip.getNextEntry();
			return zip;
		}
		return stream;
	}

	public IBook parse(String uri, IErrorHandler handler, IBookFactory factory) throws SAXException, IOException {
		InputSource source = new InputSource();
		source.setByteStream(constructInputStream(new BufferedInputStream(new FileInputStream(uri))));
		source.setSystemId(uri);
		return parse(source, handler, factory);
	}

	private IBook parse(InputSource source, IErrorHandler handler, IBookFactory factory) throws SAXException, IOException {
		XMLReader reader = XMLReaderFactory.createXMLReader();
		FB2ContentsHandler fb2handler = new FB2ContentsHandler(factory);
		reader.setErrorHandler(new SAXParseErrorHandler(handler));
		reader.setContentHandler(fb2handler);

		reader.parse(source);

		return fb2handler.getBook();
	}

	public String getDescription() {
		return "FictionBook2";
	}

	public Collection<String> getExtensions() {
		return Collections.unmodifiableCollection(extensions);
	}

	public IStylesheet<INode> getStylesheet() {
		if (stylesheet == null) {
			try {
				stylesheet = CSSParser.parse("resources/css/fb2.css");
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
