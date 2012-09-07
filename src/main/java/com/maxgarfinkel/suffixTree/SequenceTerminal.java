package com.maxgarfinkel.suffixTree;

/**
 * Represents the terminating item of a sequence.
 * 
 * @author maxgarfinkel
 * 
 */
class SequenceTerminal {

	private static SequenceTerminal instance = null;

	static SequenceTerminal getInstance() {
		if (instance == null)
			instance = new SequenceTerminal();
		return instance;
	}

	@Override
	public boolean equals(Object o) {
		return SequenceTerminal.class.isInstance(o);
	}

	@Override
	public String toString() {
		return "$";
	}

}
