package jbookreader.formatengine.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jbookreader.book.IBinaryBlob;
import jbookreader.book.IContainerNode;
import jbookreader.book.IImageNode;
import jbookreader.book.INode;
import jbookreader.book.INodeVisitor;
import jbookreader.book.ITextNode;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IFont;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.style.Display;
import jbookreader.style.FontStyle;
import jbookreader.style.IStyleStack;


public class FormatEngine implements IFormatEngine {

	public List<IDrawable> format(IGraphicDriver driver, ICompositor compositor, INode node, IStyleStack styleStack) {
		List<IDrawable> result = new ArrayList<IDrawable> ();
		node.accept(new BlockFormattingVisitor(driver, compositor, result, styleStack));
		return result;
	}

	private static class BlockFormattingVisitor implements INodeVisitor {

		private final IGraphicDriver driver;
		private final List<IDrawable> result;
		private final ICompositor compositor;
		private final IStyleStack styleStack;

		public BlockFormattingVisitor(IGraphicDriver driver, ICompositor compositor, List<IDrawable> result, IStyleStack styleStack) {
			this.driver = driver;
			this.compositor = compositor;
			this.result = result;
			this.styleStack = styleStack;
		}

		public boolean visitContainerNode(IContainerNode node) {
			styleStack.push(node);
			if (styleStack.getDisplay() == Display.INLINE) {
				styleStack.pop();
				formatInline(node.getParentNode());
				return true;
			}
			node.visitChildren(this);
			styleStack.pop();
			return false;
		}

		public boolean visitTextNode(ITextNode node) {
			formatInline(node.getParentNode());
			return true;
		}

		private void formatInline(INode node) {
			List<IDrawable> drawables = new ArrayList<IDrawable>();

			styleStack.pop();
			
			INodeVisitor visitor = new InlineFormattingVisitor(driver, drawables, styleStack);
			node.accept(visitor);
			
			styleStack.push(node);

			List<IDrawable> lines = compositor.compose(drawables, driver.getPaperWidth(),
					styleStack.getTextAlign(), driver);

			result.addAll(lines);

		}

		public boolean visitImageNode(IImageNode node) {
			styleStack.push(node);
			if (styleStack.getDisplay() == Display.INLINE) {
				styleStack.pop();
				formatInline(node.getParentNode());
				return true;
			}
			formatInline(node);
			styleStack.pop();
			return false;
		}

	}

	private static class InlineFormattingVisitor implements INodeVisitor {
		private final List<IDrawable> result;
		private final IGraphicDriver driver;
		private final IStyleStack styleStack;

		public InlineFormattingVisitor(IGraphicDriver driver, List<IDrawable> result, IStyleStack styleStack) {
			this.driver = driver;
			this.result = result;
			this.styleStack = styleStack;
		}

		public boolean visitContainerNode(IContainerNode node) {
			styleStack.push(node);
			node.visitChildren(this);
			styleStack.pop();
			return false;
		}

		public boolean visitTextNode(ITextNode node) {
			styleStack.push(node);

			// FIXME: better italic
			IFont font = driver.getFont(
					styleStack.getFirstFontFamily(),
					styleStack.getFontSize(),
					styleStack.getFontWeight() > 500,
					styleStack.getFontStyle() != FontStyle.NORMAL);

			String text = node.getText();
			if (text == null) {
				styleStack.pop();
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
			styleStack.pop();
			return false;
		}

		public boolean visitImageNode(IImageNode node) {
			styleStack.push(node);
			String href = node.getHRef();
			if (href.length() >= 1 && href.charAt(0) == '#') {
				IBinaryBlob blob = node.getBook().getBinaryBlob(href.substring(1));
				if (blob != null) {
					try {
						result.add(driver.renderImage(blob.getContentType(), blob.getDataStream()));
						styleStack.pop();
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

			styleStack.pop();
			return visitTextNode(node);
		}

	}

}

