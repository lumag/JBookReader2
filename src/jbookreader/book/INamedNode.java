package jbookreader.book;

public interface INamedNode extends INode {

	String getNodeClass();

	void setNodeClass(String klass);

	String getNodeTag();

	void setNodeTag(String tag);

}