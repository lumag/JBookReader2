package jbookreader.fileformats.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import jbookreader.book.IBinaryBlob;
import jbookreader.book.IBook;
import jbookreader.book.IBookFactory;
import jbookreader.book.IContainerNode;
import jbookreader.book.IImageNode;
import jbookreader.book.INamedNode;
import jbookreader.book.ITextNode;
import jbookreader.fileformats.IErrorHandler;
import jbookreader.fileformats.IFileFormatDescriptor;
import lumag.util.Base64;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

class FictionBook2 implements IFileFormatDescriptor {
	private static class FB2ContentsHandler extends DefaultHandler {
		private final IBookFactory factory;
		private final IBook book;
		private IContainerNode containerNode;
		private IBinaryBlob binaryBlob;

		private final StringBuilder textAccumulator = new StringBuilder();
		private boolean mixedNode;

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
			if (
					tag.equals("a") ||
					tag.equals("code") ||
					tag.equals("emphasis") ||
					tag.equals("p") ||
					tag.equals("strikethrough") ||
					tag.equals("strong") ||
					tag.equals("style") ||
					tag.equals("sub") ||
					tag.equals("subtitle") ||
					tag.equals("sup") ||
					tag.equals("td") ||
					tag.equals("text-author") ||
					tag.equals("th") ||
					tag.equals("v") ||
					false
					) {
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

	FictionBook2() {
		extensions = new ArrayList<String>();
		extensions.add(".fb2");
// FIXME: support zipped fb2
//		extensions.add(".fb2.zip");
//		extensions.add(".fbz");
	}

	public IBook parse(String uri, IErrorHandler handler, IBookFactory factory) throws SAXException, IOException {
		return parse(new InputSource(uri), handler, factory);
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

}
