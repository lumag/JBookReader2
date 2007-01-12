package lumag.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class ClassFactory {
	private ClassFactory() {
		// empty
	}
	
	private static Properties defaults = new Properties();
	
	private static String findClassName(String property) {
		String className = null;
		try {
			className = System.getProperty(property);
		} catch (RuntimeException e) {
			/* normally fails for applets */
		}
		
		if (className == null) {
			className = defaults.getProperty(property);
		}

		if (className == null) {
			throw new IllegalArgumentException("Can't find a class for '"
					+ property + "' property");
		}
		
		return className;
	}
	
	public static void loadProperies(String pkg) {
		InputStream stream = null;

		String resourceName = "";
		if (pkg != null && ! "".equals(pkg)) {
			resourceName = pkg + ".";
		}
		resourceName = resourceName.replace('.', '/');
		resourceName = resourceName + "classes.properties";

		ClassLoader loader = getClassLoader();

		try {
			if (loader == null) { 
				stream = new FileInputStream(resourceName);
			} else {
				stream = loader.getResourceAsStream(resourceName);
			}
			defaults.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static ClassLoader getClassLoader() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = ClassLoader.class.getClassLoader();
		}
		return loader;
	}

	public static <T> T createClass(Class<T> intf, String property) {
		String className = findClassName(property); 
		ClassLoader loader = getClassLoader();

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
