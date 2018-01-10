package com.g4mesoft.data;

import java.util.ArrayList;
import java.util.List;

public class Molecule {

	private final List<AtomEntry> atoms;
	
	public Molecule() {
		atoms = new ArrayList<AtomEntry>();
	}
	
	public double calcMolarMass() {
		double res = 0.0;
		for (AtomEntry entry : atoms)
			res += entry.amount * entry.atom.value;
		return res;
	}
	
	public void addAtom(Atom atom, int amount) {
		atoms.add(new AtomEntry(atom, amount));
	}

	public List<AtomEntry> getAtoms() {
		return atoms;
	}
	
	public static class AtomEntry {
		
		public final Atom atom;
		public final int amount;
		
		public AtomEntry(Atom atom, int amount) {
			this.atom = atom;
			this.amount = amount;
		}
		
		@Override
		public String toString() {
			return amount > 1 ? String.format("%s%d", atom.name, amount) : atom.name;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (AtomEntry entry : atoms)
			sb.append(entry.toString());
		return sb.toString();
	}
}
