package jbookreader.style.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import jbookreader.book.IContainerNode;
import jbookreader.book.IImageNode;
import jbookreader.book.INode;
import jbookreader.book.INodeVisitor;
import jbookreader.book.ITextNode;
import jbookreader.style.Alignment;
import jbookreader.style.Display;
import jbookreader.style.IStyleStack;

public class FB2StyleStackImpl implements IStyleStack {
	private class FB2StyleNodeVisitor implements INodeVisitor {
		
		public boolean visitContainerNode(IContainerNode node) {
			currentState.nodeTag = node.getNodeTag();
			currentState.nodeClass = node.getNodeClass();
			
			applyStyles();

			return false;
		}

		public boolean visitImageNode(IImageNode node) {
			currentState.nodeTag = node.getNodeTag();
			currentState.nodeClass = node.getNodeClass();

			applyStyles();

			return false;
		}

		public boolean visitTextNode(ITextNode node) {
			currentState.nodeTag = "#text";
			currentState.nodeClass = null;

			currentState.display = Display.INLINE;
			return false;
		}

		// FIXME: FB2-dependant part. Rewrite in a generic way
		private void applyStyles() {
			if (isInlineNode()) {
				currentState.display = Display.INLINE;
			} else {
				currentState.display = Display.BLOCK;
			}
			
			if ("title".equals(currentState.nodeTag)) {
				currentState.textAlignment = Alignment.CENTER;
				currentState.fontFamily = new ArrayList<String>();
				currentState.fontFamily.add("sans");
				currentState.fontSize = (int) (currentState.fontSize * 1.5 * 1.5);
			}
		}

		private boolean isInlineNode() {
			String tag = currentState.nodeTag;
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
				for (ListIterator<StyleStackState> it = stateStack.listIterator(stateStack.size());
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

	private List<StyleStackState> stateStack = new ArrayList<StyleStackState>();
	private StyleStackState currentState = new StyleStackState();
	private final INodeVisitor visitor = new FB2StyleNodeVisitor(); 
	
	public FB2StyleStackImpl() {
		stateStack.add(currentState);
	}

	public void push(INode node) {
		currentState = currentState.clone();
		
		node.accept(visitor);
		
		stateStack.add(currentState);
	}

	public void pop() {
		stateStack.remove(currentState);
		if (stateStack.isEmpty()) {
			currentState = new StyleStackState();
			stateStack.add(currentState);
			System.err.println("style stack became empty!");
		} else {
			currentState = stateStack.get(stateStack.size() - 1);
		}
	}

	public Display getDisplay() {
		return currentState.display;
	}

	public Alignment getTextAlign() {
		return currentState.textAlignment;
	}

	public List<String> getFontFamily() {
		return Collections.unmodifiableList(currentState.fontFamily);
	}

	public String getFirstFontFamily() {
		return currentState.fontFamily.get(0);
	}

	public int getFontSize() {
		return currentState.fontSize;
	}

}
