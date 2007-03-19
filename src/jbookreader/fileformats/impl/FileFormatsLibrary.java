package jbookreader.fileformats.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import jbookreader.fileformats.IFileFormatDescriptor;
import jbookreader.fileformats.UnknownFormatException;
import jbookreader.fileformats.rtf.RichTextFormat;

//FIXME: rework to remove public class from .impl package!
public class FileFormatsLibrary {
	private static final Collection<IFileFormatDescriptor> FILE_FORMATS = new ArrayList<IFileFormatDescriptor>();
	
	private FileFormatsLibrary() {
		// emty
	}
	
	private static void populateFormats() {
		if (FILE_FORMATS.size() != 0) {
			return;
		}
		
		FILE_FORMATS.add(new FictionBook2());
		FILE_FORMATS.add(new RichTextFormat());
	}

	public static Collection<IFileFormatDescriptor> getFileFormats() {
		populateFormats();
		return Collections.unmodifiableCollection(FILE_FORMATS);
	}
	
	public static IFileFormatDescriptor getDescriptorForFile(String file) throws UnknownFormatException {
		populateFormats();
		for (IFileFormatDescriptor d: FILE_FORMATS) {
			for (String ext : d.getExtensions()) {
				if (file.endsWith(ext)) {
					return d;
				}
			}
		}
		throw new UnknownFormatException();
	}
	
}
