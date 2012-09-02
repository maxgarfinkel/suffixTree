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
	
	boolean isStarting(Object item){
		return sequence[start].equals(item);
	}

	/**
	 * 
	 * @param suffix
	 * @param activePoint
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
	
	void split(Suffix<T> suffix, ActivePoint<T> activePoint){
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
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = start; i < getEnd(); i++){
			sb.append(sequence[i].toString()).append(", ");
		}
		return sb.toString();
	}

	public Iterator<T> iterator() {
		return new Iterator<T>(){
			private int currentPosition = start;
			public boolean hasNext() {
				return currentPosition < getEnd();
			}

			public T next() {
				return (T)sequence[currentPosition++];
			}

			public void remove() {
				throw new UnsupportedOperationException("The remove method is not supported.");
			}
		};
	}
}
