package jbookreader.ui.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
class AboutWindow extends JDialog {
	AboutWindow(Frame parent) {
		super(parent);
		setTitle("About...");
		final JComponent pane = new JOptionPane(createMessage(),
				JOptionPane.INFORMATION_MESSAGE);
		pane.setComponentOrientation(parent.getComponentOrientation());

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(pane, BorderLayout.CENTER);
		setLocationRelativeTo(parent);

        pane.addPropertyChangeListener(new PropertyChangeListener() {
        	private final JComponent optionPane = pane;
            public void propertyChange(PropertyChangeEvent event) {
                // Let the defaultCloseOperation handle the closing
                // if the user closed the window without selecting a button
                // (newValue = null in that case).  Otherwise, close the dialog.
                if (AboutWindow.this.isVisible() && event.getSource() == optionPane &&
                  (event.getPropertyName().equals(JOptionPane.VALUE_PROPERTY)) &&
                  event.getNewValue() != null &&
                  event.getNewValue() != JOptionPane.UNINITIALIZED_VALUE) {
                	AboutWindow.this.setVisible(false);
                }
            }
        });

		pack();

	}

	JComponent createMessage() {
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("JBookReader " + Values.getProjectVersion(), SwingConstants.CENTER);
//        label.setFont(new Font("Sans", Font.BOLD, 16));
//      label.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        pane.add(label);

        pane.add(new JLabel("(C) 2006 Dmitry Baryshkov (dbaryshkov@gmail.com)"));
        pane.add(new JLabel(""));
        pane.add(new JLabel("JBookReader comes with ABSOLUTELY NO WARRANTY."));
        pane.add(new JLabel("This is free software, and you are welcome"));
        pane.add(new JLabel("to redistribute it under conditions of GPL."));

        return pane;
	}
}
