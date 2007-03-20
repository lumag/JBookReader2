/**
 * 
 */
package jbookreader.book.impl;

import jbookreader.book.IBook;
import jbookreader.book.INode;


class NodeFinder {
	private final String[] parts;
	private final IBook book;

	public NodeFinder(final IBook book, final String ref) {
		this.book = book;
		this.parts = ref.split("/");
	}

	public INode getNode() {
		INode node = book.getFirstBody();
		// skip first empty part
		// skip second "body" part
		for (int i = 2; i < parts.length; i++) {
			if (parts[i] == null || "".equals(parts[i])) {
				return node;
			}

			int idx = Integer.parseInt(parts[i]);
			
			if (node instanceof ContainerNodeImpl) {
				node = ((ContainerNodeImpl) node).getNode(idx);
			} else {
				System.err.println("Can't go inside non-container node: " + node);
				return node;
			}
		}

		return node;
	}
}
