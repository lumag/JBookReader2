package jbookreader.book.impl;

import jbookreader.book.INamedNode;

abstract class AbstractNamedNode extends AbstractNode implements INamedNode {

	private String nodeClass;
	private String nodeTag;

	public String getNodeClass() {
		return nodeClass;
	}

	public String getNodeTag() {
		return nodeTag;
	}

	public void setNodeClass(String nodeClass) {
		this.nodeClass = nodeClass;
	}

	public void setNodeTag(String nodeTag) {
		this.nodeTag = nodeTag;
	}

	@Override
	public String toString() {
		if (nodeClass != null) {
			return nodeTag + "." + nodeClass;
		}
		return nodeTag;
	}
}