package com.g4mesoft.word;

import java.util.ArrayList;
import java.util.List;

public class WordEquation {

	private final List<WordEquationElement> elements;

	public WordEquation() {
		elements = new ArrayList<WordEquationElement>();
	}
	
	public void addElement(WordEquationElement element) {
		elements.add(element);
	}
	
	public void addSubscript(String primary, String subscript) {
		WordEquationSymbol primaryElement = new WordEquationSymbol(primary);
		WordEquationSymbol subscriptElement = new WordEquationSymbol(subscript);
		addElement(new WordEquationSubscript(primaryElement, subscriptElement));
	}

	public void addSymbol(String symbols) {
		addElement(new WordEquationSymbol(symbols));
	}
	
	public String compile(WordPasteTemplate template) {
		StringBuilder sb = new StringBuilder();
		for (WordEquationElement element : elements)
			sb.append(element.compile(template));
		return String.format(template.wordEqTemplate, sb.toString());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (WordEquationElement element : elements)
			sb.append(element);
		return sb.toString();
	}
}
