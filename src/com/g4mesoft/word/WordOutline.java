package com.g4mesoft.word;

import java.util.ArrayList;
import java.util.List;

public class WordOutline {

	private List<WordEquation> equations;
	
	public WordOutline() {
		equations = new ArrayList<WordEquation>();
	}
	
	public void addEquation(WordEquation equation) {
		equations.add(equation);
	}
	
	public String compile(WordPasteTemplate template) {
		StringBuilder sb = new StringBuilder();
		for (WordEquation equation : equations)
			sb.append(equation.compile(template));
		return String.format(template.wordOutlineTemplate, sb.toString());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (WordEquation equation : equations)
			sb.append(equation);
		return sb.toString();
	}
}
