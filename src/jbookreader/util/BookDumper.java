package jbookreader.util;

import jbookreader.book.IContainerNode;
import jbookreader.book.IImageNode;
import jbookreader.book.INodeVisitor;
import jbookreader.book.ITextNode;


public class BookDumper implements INodeVisitor {

	public boolean visitContainerNode(IContainerNode node) {
		if (node.isEmpty()) {
			System.out.print("<" + node.getNodeTag() + " />");
		} else {
			System.out.print("<" + node.getNodeTag() + ">");
			node.visitChildren(this);
			System.out.print("</" + node.getNodeTag() + ">");
		}
		return false;
	}

	public boolean visitTextNode(ITextNode node) {
		System.out.print(node.getText());
		return false;
	}

	public boolean visitImageNode(IImageNode node) {
		System.out.print("<image alt=\"" + node.getText() + "\" xlink:href=\"" + node.getHRef() + "\" />");
		return false;
	}

	
}