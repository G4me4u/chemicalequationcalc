package com.g4mesoft.word;

public class WordEquationSubscript extends WordEquationElement {

	public final WordEquationSymbol primary;
	public final WordEquationSymbol subscript;
	
	public WordEquationSubscript(WordEquationSymbol primary, WordEquationSymbol subscript) {
		this.primary = primary;
		this.subscript = subscript;
	}

	@Override
	public String compile(WordPasteTemplate template) {
		return String.format(template.wordEqSubTemplate, primary.compile(template), subscript.compile(template));
	}
	
	@Override
	public String toString() {
		return String.valueOf(primary) + String.valueOf(subscript);
	}
}
