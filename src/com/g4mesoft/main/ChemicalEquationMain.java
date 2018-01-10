package com.g4mesoft.main;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;

import com.g4mesoft.data.AtomDictionary;
import com.g4mesoft.data.Reaction;
import com.g4mesoft.data.ReactionParser;
import com.g4mesoft.display.Display;
import com.g4mesoft.display.FontManager;
import com.g4mesoft.display.ReactionListener;
import com.g4mesoft.word.WordOutline;
import com.g4mesoft.word.WordPasteManager;

public class ChemicalEquationMain implements ReactionListener {

	// Dictionary files
	public static final String ATOM_DICT_FILE = "/atoms.txt";
	
	// Font files
	public static final String CAMBRIA_FONT_PATH = "/CAMBRIA.TTC";
	
	// Display values
	public static final String TITLE = "Chemical Equation Calculator v0.1";

	public static final int WIDTH = 575;
	public static final int HEIGHT = 325;

	public Reaction currentReaction;
	
	public final AtomDictionary dict;
	public final ReactionParser parser;

	public final FontManager fontManager;
	private final Display display;


	private final WordPasteManager pasteManager;
	
	public ChemicalEquationMain() throws IOException {
		dict = new AtomDictionary();
		dict.parseFile(ATOM_DICT_FILE);

		parser = new ReactionParser(dict);

		fontManager = new FontManager();
		fontManager.registerFont("cambria", CAMBRIA_FONT_PATH);
		
		display = new Display(this);
		display.create();
		
		display.reactionPane.setReactionListener(this);
		
		pasteManager = new WordPasteManager();
		pasteManager.load();
	
		updateReactionInput("");
	}

	public void compileToClipboard(WordOutline outline) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(pasteManager.compile(outline), null);
	}
	
	@Override
	public void updateReactionInput(String input) {
		currentReaction = parser.parseReaction(input);
		display.reactionTable.updateTable(currentReaction);
	}
	
	public static void main(String[] args) {
		try {
			new ChemicalEquationMain();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
