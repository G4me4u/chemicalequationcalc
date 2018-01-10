package com.g4mesoft.data;

import com.g4mesoft.display.SymbolManager;

public class ReactionParser {

	private final AtomDictionary dict;
	
	public ReactionParser(AtomDictionary dict) {
		this.dict = dict;
	}
	
	public Reaction parseReaction(String input) {
		Reaction reaction = new Reaction();

		int len = input.length();
		if (len <= 0)
			return reaction;

		boolean prod = false;
		
		int moleculeStart = -1;
		for (int i = 0; i < len; i++) {
			char c = input.charAt(i);
			String alias = SymbolManager.getAlias(c);
			if (alias == null && (Character.isAlphabetic(c) || Character.isDigit(c))) {
				if (moleculeStart == -1)
					moleculeStart = i;
			} else {
				if (alias != null) {
					reaction.setSeperator(alias);
					prod = true;
				}
				
				if (moleculeStart != -1) {
					parseMolecule(reaction, prod, input, moleculeStart, i);
					moleculeStart = -1;
				}
			}
		}
		
		if (moleculeStart != -1)
			parseMolecule(reaction, prod, input, moleculeStart, len);
		
		return reaction;
	}
	
	private void parseMolecule(Reaction reaction, boolean prod, String input, int start, int end) {
		int i = start;
		
		while (i < end && Character.isDigit(input.charAt(i))) 
			i++;
		int moleculeAmount = parseAmount(input, start, i);
		
		Molecule molecule = new Molecule();

		while (true) {
			if ((i = lookForUpperCase(input, i, end)) == -1)
				break;
			
			start = i;
			while (++i < end && Character.isLowerCase(input.charAt(i)));
			
			String atomName = input.substring(start, i);
			
			start = i;
			while (i < end && Character.isDigit(input.charAt(i)))
				i++;
			int atomAmount = parseAmount(input, start, i);
			
			Atom atom = dict.getEntry(atomName);
			
			if (atom == null) {
				reaction.addIllegalName(atomName);
				continue;
			}
			molecule.addAtom(atom, atomAmount);
		}
		
		if (molecule.getAtoms().size() > 0) {
			if (prod) {
				reaction.addProduct(molecule, moleculeAmount);
			} else {
				reaction.addReactant(molecule, moleculeAmount);
			}
		}
	}
	
	private int parseAmount(String input, int start, int i) {
		if (i != start) {
			try {
				return Integer.parseInt(input.substring(start, i));
			} catch (NumberFormatException e) {
			}
			
			// If parsing fails, it's because of an
			// integer of greater size than max int
			return Integer.MAX_VALUE;
		}
		
		return 1;
	}
	
	private int lookForUpperCase(String input, int start, int end) {
		while (start < end && !Character.isUpperCase(input.charAt(start)))
			start++;
		return (start >= end) ? -1 : start;
	}
}
