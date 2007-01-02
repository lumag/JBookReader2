package lumag.util;


public final class ClassFactory {
	private ClassFactory() {
		// empty
	}
	
	public static <T> T createClass(Class<T> intf, String property) {
		// FIXME
		String className = "jbookreader.book.impl.BookFactory";
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			Class<? extends T> klass;
			if (loader == null) {
				klass = Class.forName(className).asSubclass(intf);
			} else {
				klass = loader.loadClass(className).asSubclass(intf);
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
