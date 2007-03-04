/**
 * 
 */
package jbookreader.rendering.swing;

import jbookreader.formatengine.IStyleConfig;

class JGraphicDriverConfig implements IStyleConfig {

	private final JGraphicDriver driver;

	JGraphicDriverConfig(JGraphicDriver driver) {
		this.driver = driver;
	}

	public int getMediumFontSize() {
		// FIXME: use real configuration engine
		return 10;
	}

	public int getPageWidth() {
		return this.driver.getPaperWidth();
	}
	
}