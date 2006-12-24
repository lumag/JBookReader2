package jbookreader.ui.swing;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

@SuppressWarnings("serial")
class AboutAction extends AbstractAction implements Action {
	private final Frame parent;

	AboutAction(final Frame parent) {
		this.parent = parent;
		putValue(NAME, "About");
		putValue(SHORT_DESCRIPTION, "About program");
		putValue(MNEMONIC_KEY, KeyEvent.VK_A);
	}

	public void actionPerformed(ActionEvent e) {
		new AboutWindow(parent).setVisible(true);
	}

}
