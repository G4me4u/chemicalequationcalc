package com.g4mesoft.word;

public class WordEquationSymbol extends WordEquationElement {

	private final String symbols;
	
	public WordEquationSymbol(String symbols) {
		this.symbols = symbols;
	}
	
	@Override
	public String compile(WordPasteTemplate template) {
		return String.format(template.wordEqSymTemplate, symbols);
	}
	
	@Override
	public String toString() {
		return symbols;
	}
}
