package jbookreader.fileformats.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import jbookreader.fileformats.IFileFormatDescriptor;

public class FileFormatsLibrary {
	private static final Collection<IFileFormatDescriptor> fileFormats = new ArrayList<IFileFormatDescriptor>();
	
	private FileFormatsLibrary() {
		// emty
	}
	
	private static void populateFormats() {
		if (fileFormats.size() != 0) {
			return;
		}
		
		fileFormats.add(new FictionBook2());
		fileFormats.add(new RichTextFormat());
	}

	public static Collection<IFileFormatDescriptor> getFileFormats() {
		populateFormats();
		return Collections.unmodifiableCollection(fileFormats);
	}
	
	public static IFileFormatDescriptor getDescriptorForFile(String file) throws UnknownFormatException {
		populateFormats();
		for (IFileFormatDescriptor d: fileFormats) {
			for (String ext : d.getExtensions()) {
				if (file.endsWith(ext)) {
					return d;
				}
			}
		}
		throw new UnknownFormatException();
	}
	
}
