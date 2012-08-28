package com.maxgarfinkel.suffixTree;

class Edge<T> {
	private final int start;
	private int end = -1;
	private final Node<T> parentNode;
	private final Object[] sequence;
	private Node<T> terminal = null;
	private SuffixTree<T> tree = null;
	
	/**
	 * Create a new <code>Edge</code> object.
	 * @param start The position in the master sequence of the first item in this suffix.
	 * @param parent The parent {@link Node}
	 * @param sequence The master sequence which the {@link SuffixTree} indexes.  
	 * @param tree The master {@link SuffixTree} containing the root element which this edge is a child of.
	 */
	Edge(int start, Node<T> parent, Object[] sequence, SuffixTree<T> tree){
		this.start = start;
		this.parentNode = parent;
		this.sequence = sequence;
		this.tree = tree;
	}
	
	boolean isStarting(Object item){
		return sequence[start].equals(item);
	}

	/**
	 * 
	 * @param suffix
	 * @param activePoint
	 * @return
	 */
	boolean insert(Suffix<T> suffix, ActivePoint<T> activePoint) {
		// TODO Auto-generated method stub
		return false;
	}

	int getEnd(){
		return end != -1 ? end : tree.getCurrentEnd();
	}

	boolean isTerminating(){
		return terminal != null;
	}
	
	int getLength() {
		int realEnd = getEnd();
		return realEnd - start;
	}
	
	Node<T> getTerminal(){
		return terminal;
	}
	
	T getItemAt(int position){
		return (T) sequence[start+position];
	}
	
	T getStartItem(){
		return (T) sequence[start];
	}
}
