package jbookreader.book.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import jbookreader.book.IBinaryBlob;

class BinaryBlobImpl implements IBinaryBlob {

	private String contentType;
	private byte[] data;

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getContentType() {
		return contentType;
	}

	public InputStream getDataStream() {
		return new ByteArrayInputStream(data);
	}

	public void setData(byte[] array) {
		this.data = array.clone();
	}

}
