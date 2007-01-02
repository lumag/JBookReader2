package jbookreader.formatengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jbookreader.book.IBinaryBlob;
import jbookreader.book.IContainerNode;
import jbookreader.book.IImageNode;
import jbookreader.book.INode;
import jbookreader.book.INodeVisitor;
import jbookreader.book.ITextNode;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IFont;
import jbookreader.rendering.IGraphicDriver;


public class FormatEngine implements IFormatEngine {

	public List<IDrawable> format(IGraphicDriver driver, ICompositor compositor, INode node) {
		List<IDrawable> result = new ArrayList<IDrawable> ();
		node.accept(new BlockFormattingVisitor(driver, compositor, result));
		return result;
	}

	private static class BlockFormattingVisitor implements INodeVisitor {

		private final IGraphicDriver driver;
		private final List<IDrawable> result;
		private final ICompositor compositor;

		public BlockFormattingVisitor(IGraphicDriver driver, ICompositor compositor, List<IDrawable> result) {
			this.driver = driver;
			this.compositor = compositor;
			this.result = result;
		}

		public boolean visitContainerNode(IContainerNode node) {
			if (isInlineNode(node)) {
				formatInline(node.getParentNode());
				return true;
			}
			node.visitChildren(this);
			return false;
		}

		// FIXME: move to style!
		private boolean isInlineNode(IContainerNode node) {
			String tag = node.getNodeTag();
			if (tag == null) {
				return false;
			}
			if (
					tag.equals("strong") ||
					tag.equals("emphasis") ||
					tag.equals("style") ||
					tag.equals("a") ||
					tag.equals("strikethrough") ||
					tag.equals("sub") ||
					tag.equals("sup") ||
					tag.equals("code") ||
					false
					) {
				return true;
			}
			return false;
		}

		public boolean visitTextNode(ITextNode node) {
			formatInline(node.getParentNode());
			return true;
		}

		private void formatInline(INode node) {

			List<IDrawable> drawables = new ArrayList<IDrawable>();
			INodeVisitor visitor = new InlineFormattingVisitor(driver, drawables );

			node.accept(visitor);

			List<IDrawable> lines = compositor.compose(drawables, driver.getPaperWidth(), driver);

			result.addAll(lines);
		}

		public boolean visitImageNode(IImageNode node) {
			// FIXME: this wouldn't work as required in one simple case:
			// if the image is a first node in a paragraph.
			// Then we will get duplicate image.
			// XXX: this will be fixed once imageType and inlineImageType
			// become different node types or will be distinguishable
			// in any other simple way.
			formatInline(node);
			return false;
		}

	}

	private static class InlineFormattingVisitor implements INodeVisitor {
		private final List<IDrawable> result;
		private final IGraphicDriver driver;
		// FIXME: style
		private IFont font;

		public InlineFormattingVisitor(IGraphicDriver driver, List<IDrawable> result) {
			this.driver = driver;
			this.result = result;

			// FIXME!
			font = driver.getFont(null, 12);
		}

		public boolean visitContainerNode(IContainerNode node) {
			node.visitChildren(this);
			return false;
		}

		public boolean visitTextNode(ITextNode node) {
			String text = node.getText();
			if (text == null) {
				return false;
			}
			char[] str = text.toCharArray();
			int size = str.length;
			for (int start = 0, end = start;
				end < size;
				) {
				while (start < size && str[start] <= '\u0020') {
					start ++;
				}
				if (start > end) {
					result.add(new Glue(driver, font.getSpaceWidth(), 1, 1));
				}
				for (end = start; end < size; end ++) {
					if (str[end] <= '\u0020') {
						break;
					}
				}

				if (end != start) {
					String s = new String(str, start, end - start);
					result.add(driver.renderString(s, font));
					start = end;
				}
			}
			return false;
		}

		public boolean visitImageNode(IImageNode node) {
			String href = node.getHRef();
			if (href.length() >= 1 && href.charAt(0) == '#') {
				IBinaryBlob blob = node.getBook().getBinaryBlob(href.substring(1));
				if (blob != null) {
					try {
						result.add(driver.renderImage(blob.getContentType(), blob.getDataStream()));
						return false;
					} catch (UnsupportedOperationException e) {
						System.err.println("Error: " + e.getMessage());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				System.err.println("Bad image HRef: " + href);
			}
			return visitTextNode(node);
		}

	}
}

