package com.g4mesoft.data;

import java.util.ArrayList;
import java.util.List;

import com.g4mesoft.display.SymbolManager;

public class Reaction {

	private final List<MoleculeEntry> reactants;
	private final List<MoleculeEntry> products;
	
	private final List<String> illegalNames;
	
	private String seperator;

	public Reaction() {
		reactants = new ArrayList<MoleculeEntry>();
		products = new ArrayList<MoleculeEntry>();
	
		illegalNames = new ArrayList<String>();
		
		seperator = SymbolManager.getAlias(SymbolManager.getSymbol("ra"));
	}
	
	public List<MoleculeEntry> getReactants() {
		return reactants;
	}

	public List<MoleculeEntry> getProducts() {
		return products;
	}
	
	public List<String> getIllegalNames() {
		return illegalNames;
	}
	
	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}
	
	public String getSeperator() {
		return seperator;
	}
	
	public void addReactant(Molecule molecule, int amount) {
		reactants.add(new MoleculeEntry(molecule, amount));
	}

	public void addProduct(Molecule molecule, int amount) {
		products.add(new MoleculeEntry(molecule, amount));
	}
	
	public void addIllegalName(String atomName) {
		illegalNames.add(atomName);
	}
	
	public static class MoleculeEntry {
	
		public final Molecule molecule;
		public final int amount;
		
		public MoleculeEntry(Molecule molecule, int amount) {
			this.molecule = molecule;
			this.amount = amount;
		}
		
		@Override
		public String toString() {
			return amount > 1 ? String.format("%d%s", amount, molecule) : molecule.toString();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		for (int i = 0, len = reactants.size(); i < len; i++) {
			if (i != 0)
				res.append(' ').append('+').append(' ');
			res.append(reactants.get(i).toString());
		}
		res.append(' ').append(seperator).append(' ');
		for (int i = 0, len = products.size(); i < len; i++) {
			if (i != 0)
				res.append(' ').append('+').append(' ');
			res.append(products.get(i).toString());
		}
		return res.toString();
	}
}
