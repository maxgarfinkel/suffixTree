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
		StringBuilder sb = new StringBuilder("[");
		int end = getEndPosition();
		for(int i = start; i < end+1; i++){
			sb.append(sequence[i]).append(",");
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 
	 * @return The position in the master sequence of the end item in this suffix. 
	 * This value is inclusive, thus and end of 0 implies the suffix contains only
	 * the item at <code>sequence[0]</code>
	 */
	int getEndPosition(){
		return end-1;
	}
	
	Object getEndItem(){
		return sequence[end-1];
	}
	
	Object getStart(){
		if(start >= sequence.length)
			return null;
		return sequence[start];
	}
	
	void decrement(){
		start++;
	}
	
	void increment(){
		end++;
	}
	
	boolean isEmpty(){
		return start == end;
	}

	int getRemaining() {
		return (end-start)-1;
	}

	public Object getItemXFromEnd(int distanceFromEnd) {
		return sequence[end-distanceFromEnd];
	}
}
