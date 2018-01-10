package com.g4mesoft.display;

import java.util.HashMap;
import java.util.Map;

public final class SymbolManager {

	private SymbolManager() {
	}
	
	public static final Map<String, Character> KEY_SYMBOL_TABLE = new HashMap<String, Character>();
	public static final Map<Character, String> SYMBOL_ALIAS_TABLE = new HashMap<Character, String>();
	public static final Map<String, Character> ALIAS_SYMBOL_TABLE = new HashMap<String, Character>();

	public static Character getSymbol(String key) {
		return KEY_SYMBOL_TABLE.get(key);
	}
	
	public static String getAlias(Character symbol) {
		return SYMBOL_ALIAS_TABLE.get(symbol);
	}
	
	public static Character fromAlias(String alias) {
		return ALIAS_SYMBOL_TABLE.get(alias);
	}
	
	public static boolean hasSymbol(Character symbol) {
		return KEY_SYMBOL_TABLE.containsValue(symbol);
	}
	
	static {
		KEY_SYMBOL_TABLE.put("ra", Character.valueOf('\u2192'));
		KEY_SYMBOL_TABLE.put("Ra", Character.valueOf('\u21D2'));
		
		KEY_SYMBOL_TABLE.put("la", Character.valueOf('\u2190'));
		KEY_SYMBOL_TABLE.put("La", Character.valueOf('\u21D0'));
		
		KEY_SYMBOL_TABLE.put("lra", Character.valueOf('\u2194'));
		KEY_SYMBOL_TABLE.put("Lra", Character.valueOf('\u21D4'));
	
		SYMBOL_ALIAS_TABLE.put(getSymbol("ra"), "->");
		SYMBOL_ALIAS_TABLE.put(getSymbol("Ra"), "=>");
		
		SYMBOL_ALIAS_TABLE.put(getSymbol("la"), "<-");
		SYMBOL_ALIAS_TABLE.put(getSymbol("La"), "<=");

		SYMBOL_ALIAS_TABLE.put(getSymbol("lra"), "<->");
		SYMBOL_ALIAS_TABLE.put(getSymbol("Lra"), "<=>");
		
		Character s;
		ALIAS_SYMBOL_TABLE.put(getAlias(s = getSymbol("ra")), s);
		ALIAS_SYMBOL_TABLE.put(getAlias(s = getSymbol("Ra")), s);
		
		ALIAS_SYMBOL_TABLE.put(getAlias(s = getSymbol("la")), s);
		ALIAS_SYMBOL_TABLE.put(getAlias(s = getSymbol("La")), s);

		ALIAS_SYMBOL_TABLE.put(getAlias(s = getSymbol("lra")), s);
		ALIAS_SYMBOL_TABLE.put(getAlias(s = getSymbol("Lra")), s);
	}
}
