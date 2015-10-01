package com.pearson.hackathon.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class NotePanel extends JPanel {
	private String newline = "\n";
	private JTextPane textPane;

	public NotePanel() {
		setLayout(new BorderLayout());

		// Create an editor pane.

		// Create a text pane.
		JTextPane textPane = createTextPane();
		JScrollPane paneScrollPane = new JScrollPane(textPane);
		paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paneScrollPane.setPreferredSize(new Dimension(250, 1550));
		paneScrollPane.setMinimumSize(new Dimension(10, 10));

		add(paneScrollPane, BorderLayout.CENTER);

	}

	private JTextPane createTextPane() {

		textPane = new JTextPane();
		StyledDocument doc = textPane.getStyledDocument();
		addStylesToDocument(doc);

		return textPane;
	}

	protected void addStylesToDocument(StyledDocument doc) {
		// Initialize some styles.
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "SansSerif");

		Style s = doc.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);

		s = doc.addStyle("bold", regular);
		StyleConstants.setBold(s, true);

		s = doc.addStyle("small", regular);
		StyleConstants.setFontSize(s, 10);

		s = doc.addStyle("large", regular);
		StyleConstants.setFontSize(s, 16);

		s = doc.addStyle("icon", regular);
		StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
		ImageIcon pigIcon = createImageIcon("images/Pig.gif", "a cute pig");
		if (pigIcon != null) {
			StyleConstants.setIcon(s, pigIcon);
		}

		s = doc.addStyle("button", regular);
		StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
		ImageIcon soundIcon = createImageIcon("images/sound.gif", "sound icon");
		JButton button = new JButton();
		if (soundIcon != null) {
			button.setIcon(soundIcon);
		} else {
			button.setText("BEEP");
		}
		button.setCursor(Cursor.getDefaultCursor());
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setActionCommand("placeholderButton");
		// button.addActionListener(this);
		StyleConstants.setComponent(s, button);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = NotePanel.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public void addNote(String style, String text) {
		StyledDocument doc = textPane.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), text, doc.getStyle(style));
		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert initial text into text pane.");
		}
	}

	public void addDemoData() {

		String[] initString = { "This is an editable JTextPane, ", // regular
				"another ", // italic
				"styled ", // bold
				"text ", // small
				"component, ", // large
				"which supports embedded components..." + newline, // regular
				" " + newline, // button
				"...and embedded icons..." + newline, // regular
				" ", // icon
				newline + "JTextPane is a subclass of JEditorPane that "
						+ "uses a StyledEditorKit and StyledDocument, and provides "
						+ "cover methods for interacting with those objects." };

		String[] initStyles = { "regular", "italic", "bold", "small", "large", "regular", "button", "regular", "icon",
				"regular" };

		for (int i = 0; i < initString.length; i++) {
			addNote(initStyles[i],initString[i]);
		}

	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 * 
	 * @return
	 */
	public static NotePanel createAndShowGUI() {
		NotePanel notePanel = new NotePanel();
		// Create and set up the window.
		JFrame frame = new JFrame("TextSamplerDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		frame.add(notePanel);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
		return notePanel;
	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				NotePanel notePanel = createAndShowGUI();
				notePanel.addDemoData();
			}
		});
	}
}
