package com.g4mesoft.display;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import com.g4mesoft.main.ChemicalEquationMain;
import com.g4mesoft.word.WordEquation;
import com.g4mesoft.word.WordOutline;

@SuppressWarnings("serial")
public class ReactionTextPane extends JTextPane implements DocumentListener {

	private StyledDocument doc;
	
	private Style regular;
	private Style italic;
	private Style subscript;
	private Style special;

	private boolean isFormatting;
	
	private ReactionListener listener;
	
	private final ChemicalEquationMain main;
	
	public ReactionTextPane(ChemicalEquationMain main) {
		this(main, 24);
	}

	public ReactionTextPane(ChemicalEquationMain main, int fontSize) {
		this.main = main;
		
		init(fontSize);
	}
	
	public void setReactionListener(ReactionListener listener) {
		this.listener = listener;
	}
	
	private void init(int fontSize) {
		setEditorKit(new CenterEditorKit());
		
		doc = getStyledDocument();
		doc.addDocumentListener(this);
		
		addStyles(fontSize);
	}
	
	private void addStyles(int fontSize) {
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		
		regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(regular, main.fontManager.getFontFamily("cambria"));
		StyleConstants.setFontSize(regular, fontSize);

		italic = doc.addStyle("italic", regular);
		StyleConstants.setItalic(italic, true);
		
		subscript = doc.addStyle("subscript", regular);
		StyleConstants.setSubscript(subscript, true);
		StyleConstants.setFontSize(subscript, fontSize - 4);
		
		special = doc.addStyle("special", regular);
		StyleConstants.setForeground(special, Color.RED);
		
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		setParagraphAttributes(center, true);
	}
	
	private void formatSpecialSymbols() {
		try {
			formatter : while (true) {
				String text = doc.getText(0, doc.getLength());
				
				String specialKey = "";
				int keyStart = -1;
				for (int i = 0; i < text.length(); i++) {
					char c = text.charAt(i);
					
					if (c == '\\') {
						keyStart = i;
						specialKey = "";
					} else if (keyStart != -1 && Character.isAlphabetic(c)) {
						specialKey += c;
						Character symbol = SymbolManager.getSymbol(specialKey);
						if (symbol != null) {
							doc.remove(keyStart, i - keyStart + 1);
							doc.insertString(keyStart, String.valueOf(symbol.charValue()), regular);
							continue formatter;
						}
					} else keyStart = -1;
				}
				
				break formatter;
			} 
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private void formatSpaces() {
		try {
			formatter : while (true) {
				String text = doc.getText(0, doc.getLength());
				
				int spaceStart = -1;
				for (int i = 0; i < text.length(); i++) {
					char c = text.charAt(i);

					if (c == '\n') {
						doc.remove(i, 1);
						continue formatter;
					}
					
					if (c == ' ') {
						if (spaceStart == -1)
							spaceStart = i;
					} else {
						if (spaceStart != -1 && i - spaceStart > 1) {
							doc.remove(spaceStart + 1, i - spaceStart - 1);
							continue formatter;
						} else if (!Character.isAlphabetic(c) && !Character.isDigit(c) && c != '\\') {
							boolean changed = false;
							if (text.length() > i + 1 && text.charAt(i + 1) != ' ') {
								doc.insertString(i + 1, " ", regular);
								changed = true;
							}
							
							if (spaceStart == -1) {
								doc.insertString(i, " ", regular);
								changed = true;
							}
							
							if (changed)
								continue formatter;
						}
						spaceStart = -1;
					}
				}
				break formatter;
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	protected void formatAndStyleReaction() {
		isFormatting = true;
		
		formatSpecialSymbols();
		formatSpaces();

		styleReaction();
		
		isFormatting = false;
	}
	
	protected void styleReaction() {
		try {
			String text = doc.getText(0, doc.getLength());

			boolean isSubscript = false;
			boolean isSpecial = false;
			Style style;
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				if (c == '\\') {
					style = special;
					isSpecial = true;
				} else if (Character.isAlphabetic(c)) {
					if (isSpecial) {
						style = special;
					} else {
						style = italic;
						isSubscript = true;
					}
				} else {
					isSpecial = false;
					
					isSubscript &= Character.isDigit(c);
					style = isSubscript ? subscript : regular;
				}

				doc.setCharacterAttributes(i, 1, style, true);
			}

			if (listener != null)
				listener.updateReactionInput(text);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertUpdate(final DocumentEvent e) {
		if (!isFormatting)
			SwingUtilities.invokeLater(() -> formatAndStyleReaction());
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if (!isFormatting)
			SwingUtilities.invokeLater(() -> formatAndStyleReaction());
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}
	
	@Override
	public void copy() {
		String text = getSelectedText();
		if (text.isEmpty())
			return;
		
		WordEquation equation = new WordEquation();

		String prim = "";
		String subs = "";
		
		boolean isSubscript = false;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			
			isSubscript &= Character.isDigit(c);
			if (isSubscript) {
				subs += c;
			} else {
				if (!subs.isEmpty()) {
					equation.addSubscript(prim, subs);
					prim = "";
					subs = "";
				} else if (!prim.isEmpty() && Character.isUpperCase(c)) {
					equation.addSymbol(prim);
					prim = "";
				}
				
				isSubscript = Character.isAlphabetic(c);

				prim += c;
			}
		}

		if (!subs.isEmpty()) {
			equation.addSubscript(prim, subs);
		} else if (!prim.isEmpty()) {
			equation.addSymbol(prim);
		}
		
		WordOutline outline = new WordOutline();
		outline.addEquation(equation);
		
		main.compileToClipboard(outline);
	}
	
	private static class CenterEditorKit extends StyledEditorKit {

		public ViewFactory getViewFactory() {
			return new StyledViewFactory();
		}
	 
		private static class StyledViewFactory implements ViewFactory {

			public View create(Element elem) {
				switch (elem.getName()) {
				case AbstractDocument.ContentElementName:
					return new LabelView(elem);
				case AbstractDocument.ParagraphElementName:
					return new ParagraphView(elem);
				case AbstractDocument.SectionElementName:
					return new CenteredBoxView(elem, View.Y_AXIS);
				case StyleConstants.ComponentElementName:
					return new ComponentView(elem);
				case StyleConstants.IconElementName:
					return new IconView(elem);
				default:
					return new LabelView(elem);
				}
			}
		}
	}
	 
	private static class CenteredBoxView extends BoxView {

		public CenteredBoxView(Element elem, int axis) {
			super(elem, axis);
		}

		protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {
			super.layoutMajorAxis(targetSpan, axis, offsets, spans);
			
			int textBlockHeight = spans.length > 0 ? spans[spans.length - 1] : 0;
			int offset = (targetSpan - textBlockHeight) / 2;

			for (int i = 0; i < offsets.length; i++)
				offsets[i] += offset;
		}
	}
}
