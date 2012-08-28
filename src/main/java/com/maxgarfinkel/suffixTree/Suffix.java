package com.maxgarfinkel.suffixTree;

class Suffix<T> {
	private int start;
	private int end;
	private Object[] sequence;
	
	public Suffix(int currentEnd, int remainder, Object[] sequence) {
		start = currentEnd-(remainder-1);
		end = currentEnd;
		this.sequence = sequence;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		int end = getEndPosition();
		for(int i = start; i < end; i++){
			sb.append(sequence[i]).append(",");
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @return The position in the master sequence of the end item in this suffix. 
	 * This value is inclusive, thus and end of 0 implies the suffix contains only
	 * the item at <code>sequence[0]</code>
	 */
	int getEndPosition(){
		return end;
	}
	
	Object getEndItem(){
		return sequence[end];
	}
}
