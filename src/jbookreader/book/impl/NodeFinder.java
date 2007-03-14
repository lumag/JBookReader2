/**
 * 
 */
package jbookreader.book.impl;

import jbookreader.book.IBook;
import jbookreader.book.IContainerNode;
import jbookreader.book.IImageNode;
import jbookreader.book.INamedNode;
import jbookreader.book.INode;
import jbookreader.book.INodeVisitor;
import jbookreader.book.ITextNode;

class NodeFinder implements INodeVisitor {
	private final String[] parts;
	private int partNum;
	
	private String tag;
	private int left;
	private INode lastNode;
	private boolean found = false;
	
	NodeFinder(IBook book, String ref) {
		parts = ref.split("/");

		partNum = 0;

		lastNode = book.getFirstBody();
		
		if (parts.length > partNum) {
			pushPart();

			lastNode.accept(this);
		}
	}

	private void pushPart() {
		partNum ++;
		if (partNum >= parts.length) {
			throw new IllegalStateException("Can't push below last part");
		}
		tag = parts[partNum];
		left = 1;
		int pos = tag.indexOf('[');
		if (pos != -1) {
			left = Integer.parseInt(tag.substring(pos+1, tag.length()-1));
			tag = tag.substring(0, pos);
		}
	}

	public void flush() {
		// do nothing
	}

	private boolean checkNamedNode(INamedNode node) {
		if (tag.equals(node.getNodeTag())) {
			return true;
		}
		return false;
	}

	private void visitNamedNode(INamedNode node) {
		if (found) {
			return;
		}

		if (!checkNamedNode(node)) {
			return;
		}
		
		left --;
		
		if (left == 0) {
			lastNode = node;
			
			if (parts.length > partNum+1) {
				if (node instanceof IContainerNode) {
					pushPart();

					((IContainerNode) node).visitChildren(this);
					if (!found) {
						System.err.println("Can't find specified node, returning parent");
					}
				} else {
					System.err.println("Can't go inside non-container node");
				}
			}
			found = true;
		}
	}

	public void visitContainerNode(IContainerNode node) {
		visitNamedNode(node);
	}

	public void visitImageNode(IImageNode node) {
		visitNamedNode(node);
	}

	public void visitTextNode(ITextNode node) {
		// TODO Auto-generated method stub
	}
	
	public INode getNode() {
		return lastNode;
	}
}
