package com.g4mesoft.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AtomDictionary {

	public final Map<String, Atom> atoms;
	
	public AtomDictionary() {
		atoms = new HashMap<String, Atom>();
	}
	
	public void parseFile(String path) throws IOException {
		if (path == null)
			throw new IllegalArgumentException("path is null!");
	
		InputStream is = AtomDictionary.class.getResourceAsStream(path);
		BufferedReader bis = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
		
		String line;
		while ((line = bis.readLine()) != null) {
			String[] entry = line.split(" ");
			if (entry.length != 2)
				continue;
			
			String name = entry[0];
			double value;
			try {
				value = Double.parseDouble(entry[1]);
			} catch(NumberFormatException e) {
				continue;
			}
			
			addEntry(new Atom(name, value));
		}
		
		bis.close();
	}
	
	public void addEntry(Atom atom) {
		atoms.put(atom.name, atom);
	}

	public Atom getEntry(String atomName) {
		return atoms.get(atomName);
	}
}
