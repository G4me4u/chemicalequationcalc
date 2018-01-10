package com.g4mesoft.word;

public class WordPasteTemplate {

	public final String wordOutlineTemplate;
	
	public final String wordEqTemplate;
	public final String wordEqSymTemplate;
	public final String wordEqSubTemplate;
	
	public WordPasteTemplate(String wordOutline, String wordEq, String wordEqSym, String wordEqSub) {
		this.wordOutlineTemplate = wordOutline;
		
		this.wordEqTemplate = wordEq;
		this.wordEqSymTemplate = wordEqSym;
		this.wordEqSubTemplate = wordEqSub;
	}
}
