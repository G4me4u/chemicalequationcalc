package com.g4mesoft.display;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager {

	private final Map<String, Font> fonts;
	
	public FontManager() {
		this.fonts = new HashMap<String, Font>();
	}
	
	public void registerFont(String name, String path) {
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			InputStream is = FontManager.class.getResourceAsStream(path);
			Font font = Font.createFont(Font.TRUETYPE_FONT, is);
			ge.registerFont(font);
			is.close();
			
			fonts.put(name, font);
		} catch (IOException | FontFormatException e) {
			throw new RuntimeException("Unable to load font: " + name + " - " + path, e);
		}
	}
	
	public String getFontFamily(String name) {
		return fonts.get(name).getFamily();
	}
}
