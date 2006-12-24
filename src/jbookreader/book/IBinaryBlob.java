package jbookreader.book;

import java.io.InputStream;

public interface IBinaryBlob {
	void setContentType(String contntType);
	String getContentType();
	void setData(byte[] data);
	InputStream getDataStream();
}
