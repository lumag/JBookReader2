/**
 * 
 */
package jbookreader.formatengine.style.impl;

import java.util.ArrayList;
import java.util.ListIterator;

import jbookreader.book.IContainerNode;
import jbookreader.book.IImageNode;
import jbookreader.book.INodeVisitor;
import jbookreader.book.ITextNode;
import jbookreader.style.Alignment;
import jbookreader.style.Display;
import jbookreader.style.FontStyle;

class FB2StyleNodeVisitor implements INodeVisitor {
	
	/**
	 * 
	 */
	private final StyleStackImpl styleStack;

	/**
	 * @param impl
	 */
	FB2StyleNodeVisitor(StyleStackImpl impl) {
		styleStack = impl;
	}

	public boolean visitContainerNode(IContainerNode node) {
		styleStack.currentState.nodeTag = node.getNodeTag();
		styleStack.currentState.nodeClass = node.getNodeClass();
		
		applyStyles();

		return false;
	}

	public boolean visitImageNode(IImageNode node) {
		styleStack.currentState.nodeTag = node.getNodeTag();
		styleStack.currentState.nodeClass = node.getNodeClass();

		applyStyles();

		return false;
	}

	public boolean visitTextNode(ITextNode node) {
		styleStack.currentState.nodeTag = "#text";
		styleStack.currentState.nodeClass = null;

		styleStack.currentState.display = Display.INLINE;
		return false;
	}

	// FIXME: FB2-dependant part. Rewrite in a generic way
	private void applyStyles() {
		if (isInlineNode()) {
			styleStack.currentState.display = Display.INLINE;
		} else {
			styleStack.currentState.display = Display.BLOCK;
		}
		
		if ("title".equals(styleStack.currentState.nodeTag)) {
			styleStack.currentState.textAlignment = Alignment.CENTER;
			styleStack.currentState.fontFamily = new ArrayList<String>();
			styleStack.currentState.fontFamily.add("Sans");
			styleStack.currentState.fontSize = (int) (styleStack.currentState.fontSize * 1.5 * 1.5);
		} else if ("strong".equals(styleStack.currentState.nodeTag)) {
			styleStack.currentState.fontWeight = 700;
		} else if ("emphasis".equals(styleStack.currentState.nodeTag)) {
			styleStack.currentState.fontStyle = FontStyle.ITALIC;
		}
	}

	private boolean isInlineNode() {
		String tag = styleStack.currentState.nodeTag;
		if (
				"strong".equals(tag) ||
				"emphasis".equals(tag) ||
				"style".equals(tag) ||
				"a".equals(tag) ||
				"strikethrough".equals(tag) ||
				"sub".equals(tag) ||
				"sup".equals(tag) ||
				"code".equals(tag) ||
				"#text".equals(tag) ||
				false
				) {
			return true;
		}
		if ("image".equals(tag)) {
			for (ListIterator<StyleStackState> it = styleStack.stateStack.listIterator(styleStack.stateStack.size());
					it.hasPrevious();) {
				StyleStackState state = it.previous();
				
				if ("p".equals(state.nodeTag) ||
					"v".equals(state.nodeTag)) {
					return true;
				}
			}
			
			return false;
		}
		return false;
	}

}