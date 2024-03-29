package jbookreader.ui.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
class MainWindow extends JFrame {
	private static final int BORDER_WIDTH = 15;
	private final JScrollPane pane;

	public MainWindow() {
		this(null);
	}

	public MainWindow(GraphicsConfiguration gc) {
		super(gc);

		setTitle("JBookReader");

		setJMenuBar(createMenuBar());
		pane = new JScrollPane(
						ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(pane);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setPreferredSize(new Dimension(800, 600));

		pack();
		setVisible(true);
	}

	private JMenuBar createMenuBar() {
		JMenuBar mainmenu = new JMenuBar();

		mainmenu.add(createFileMenu());
		mainmenu.add(createHelpMenu());

		return mainmenu;
	}

	private Component createFileMenu() {
		JMenu menu = new JMenu();

		menu.setText("File");
		menu.setToolTipText("File operations");
		menu.setMnemonic(KeyEvent.VK_F);

		return menu;
	}

	private JMenu createHelpMenu() {
		JMenu menu = new JMenu();

		menu.setText("Help");
		menu.setToolTipText("Help");
		menu.setMnemonic(KeyEvent.VK_H);

		JMenuItem menuItem;

		menuItem = new JMenuItem(new AboutAction(this));
		menu.add(menuItem);

		return menu;
	}

	public void setMainComponent(JComponent component) {
		component.setBorder(BorderFactory.createEmptyBorder(
				BORDER_WIDTH, BORDER_WIDTH,
				BORDER_WIDTH, BORDER_WIDTH));
//		component.setBorder(BorderFactory.createLineBorder(java.awt.Color.RED, BORDER_WIDTH));

		pane.setViewportView(component);

	}
}
