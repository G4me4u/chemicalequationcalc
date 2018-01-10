package com.g4mesoft.data;

public class Atom {

	public final String name;
	public final double value;
	
	public Atom(String name, double value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return String.format("%s: %f", name, value);
	}
}
