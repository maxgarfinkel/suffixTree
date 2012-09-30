package com.maxgarfinkel.suffixTree;

/**
 * Represents the terminating item of a sequence.
 * 
 * @author maxgarfinkel
 * 
 */
class SequenceTerminal<S> {

	private final S sequence;
	
	SequenceTerminal(S sequence){
		this.sequence = sequence;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if(o == null || o.getClass() != this.getClass())
			return false;
		return ((SequenceTerminal<S>)o).sequence.equals(this.sequence);
	}
	
	public int hashCode(){
		return sequence.hashCode();	
	}

	@Override
	public String toString() {
		return "$"+sequence.toString()+"$";
	}
	
	public S getSequence(){
		return sequence;
	}

}
