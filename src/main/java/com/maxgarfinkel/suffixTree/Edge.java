package com.maxgarfinkel.suffixTree;

import java.util.Iterator;

class Edge<T> implements Iterable<T>{
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
	
	/**
	 * Checks to see if the edge starts with the given item.
	 * @param item The possible start item.
	 * @return True if this edge starts with item. False if not.
	 */
	boolean isStarting(Object item){
		return sequence[start].equals(item);
	}

	/**
	 * Insert the given suffix at the supplied active point.
	 * @param suffix The suffix to insert.
	 * @param activePoint The active point to insert it at.
	 * @return 
	 */
	void insert(Suffix<T> suffix, ActivePoint<T> activePoint) {
		Object item = suffix.getEndItem();
		Object nextItem = getItemAt(activePoint.getLength());
		if(item.equals(nextItem)){
			activePoint.incrementLength();
		}else{
			split(suffix, activePoint);
			suffix.decrement();
			activePoint.updateAfterInsert(suffix);
			
			if(suffix.isEmpty())
				return;
			else
				tree.insert(suffix);
		}
	}
	
	/**
	 * Splits the edge to enable the insertion of supplied suffix at the supplied active point.
	 * @param suffix The suffix to insert.
	 * @param activePoint The active point to insert it at.
	 */
	private void split(Suffix<T> suffix, ActivePoint<T> activePoint){
		Node<T> breakNode = new Node<T>(this, sequence, tree);
		Edge<T> newEdge = new Edge<T>(suffix.getEndPosition(), breakNode, sequence, tree);
		breakNode.insert(newEdge);
		Edge<T> oldEdge = new Edge<T>(start+activePoint.getLength(), breakNode, sequence, tree);
		oldEdge.end = end;
		oldEdge.terminal = this.terminal;
		breakNode.insert(oldEdge);
		this.terminal = breakNode;
		end = start+activePoint.getLength();
		tree.setSuffixLink(breakNode);
		tree.incrementInsertCount();
	}

	/**
	 * Gets the index of the true end of the edge.
	 * @return The index of the end item, of this edge, in the original sequence.
	 */
	int getEnd(){
		return end != -1 ? end : tree.getCurrentEnd();
	}

	/**
	 * Tests if this edge is terminates at a node.
	 * @return True if this edge ends at a node. False if not.
	 */
	boolean isTerminating(){
		return terminal != null;
	}
	
	/**
	 * Retrieves the length of this edge. 
	 * @return
	 */
	int getLength() {
		int realEnd = getEnd();
		return realEnd - start;
	}
	
	/**
	 * Retrieves the terminating node of this edge if it has any, null if not.
	 * @return The terminating node if any exists, null otherwise.
	 */
	Node<T> getTerminal(){
		return terminal;
	}
	
	/**
	 * Retrieves the item at given position within the current edge.
	 * @param position The index of the item to retrieve relative to the start of edge.
	 * @return The item at position.
	 * @throws IllegalArgumentException when the position exceeds the length of the current edge. 
	 */
	@SuppressWarnings("unchecked")
	T getItemAt(int position){
		if(position > getLength())
			throw new IllegalArgumentException("Index " + position + " is greater than " + getLength() + " - the length of this edge.");
		return (T) sequence[start+position];
	}
	
	/**
	 * Retrieves the starting item of this edge.
	 * @return The item at index 0 of this edge.
	 */
	@SuppressWarnings("unchecked")
	T getStartItem(){
		return (T) sequence[start];
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = start; i < getEnd(); i++){
			sb.append(sequence[i].toString()).append(", ");
		}
		return sb.toString();
	}

	/**
	 * Retrieves an iterator that steps over the items in this edge.
	 * @return An iterator that walks this edge up to the end or terminating node.
	 */
	public Iterator<T> iterator() {
		return new Iterator<T>(){
			private int currentPosition = start;
			public boolean hasNext() {
				return currentPosition < getEnd();
			}

			@SuppressWarnings("unchecked")
			public T next() {
				return (T)sequence[currentPosition++];
			}

			public void remove() {
				throw new UnsupportedOperationException("The remove method is not supported.");
			}
		};
	}
}
