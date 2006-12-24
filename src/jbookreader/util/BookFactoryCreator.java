package jbookreader.util;

import jbookreader.book.IBookFactory;

public final class BookFactoryCreator {
    private static final String DEFAULT_FACTORY_CLASS = "jbookreader.book.impl.BookFactory";
	private static final String PROPERTY_NAME = "jbookreader.book.factory";
    
    private BookFactoryCreator() {
    	// empty
    }

	public static IBookFactory getBookFactory() {
		String		className = null;
		
		try {
			className = System.getProperty(PROPERTY_NAME);
		} catch (RuntimeException e) {
			/* normally fails for applets */
		}

		if (className == null) {
			className = DEFAULT_FACTORY_CLASS;
		}
		
	    return getBookFactory(className);
	}

	private static IBookFactory getBookFactory(String className) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			Class<? extends IBookFactory> klass;
			if (loader == null) {
				klass = Class.forName(className).asSubclass(IBookFactory.class);
			} else {
				klass = loader.loadClass(className).asSubclass(IBookFactory.class);
			}
			return klass.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new UnknownError(e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new UnknownError(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new UnknownError(e.getMessage());
		}
	}
}
