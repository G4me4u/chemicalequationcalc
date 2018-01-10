package com.g4mesoft.word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WordPasteManager {

	private static final String WORD_OUTLINE_PATH = "/wordOutline.txt";
	
	private static final String WORD_EQUATION_PATH = "/wordEquation.txt";
	private static final String WORD_EQUATION_SYM_PATH = "/wordEquationSymbol.txt";
	private static final String WORD_EQUATION_SUB_PATH = "/wordEquationSubscript.txt";
	
	private WordPasteTemplate template;

	public WordTransferable compile(WordOutline wordOutline) {
		String plain = String.valueOf(wordOutline);
		String html = wordOutline.compile(template);
		return new WordTransferable(plain, html);
	}
	
	public void load() throws IOException {
		String wordOutline = loadFileString(WORD_OUTLINE_PATH);

		String wordEq = loadFileString(WORD_EQUATION_PATH);
		String wordEqSym = loadFileString(WORD_EQUATION_SYM_PATH);
		String wordEqSub = loadFileString(WORD_EQUATION_SUB_PATH);
		
		template = new WordPasteTemplate(wordOutline, wordEq, wordEqSym, wordEqSub);
	}
	
	private String loadFileString(String path) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStream is = WordPasteManager.class.getResourceAsStream(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = br.readLine()) != null)
			sb.append(line);
		br.close();
		return sb.toString();
	}
}
