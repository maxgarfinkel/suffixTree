package com.maxgarfinkel.suffixTree;

/**
 * A suffix tree implementation using Ukkonen's algorithm.
 * 
 * @author Max Garfinkel
 *
 * @param <I> The type of the item within the sequence. 
 */
public class SuffixTree<T> {
	
	private final Node<T> root;
	private final Object[] sequence;
	private final Suffix<T> suffix;
	private final ActivePoint<T> activePoint;
	private int currentEnd = 0;
	private int insertsThisStep = 0;
	private Node<T> lastNodeInserted = null;
		
	
	/**
	 * Construct and represent a suffix tree representation of the given sequence using Ukkonen's
	 * algorithm.
	 *  
	 * @param sequence the array of items for which we are going to generate a suffix tree.
	 * @throws Exception 
	 */
	SuffixTree(T[] sequence) throws Exception{
		this.sequence = Utils.addLeafToSequence(sequence, Leaf.getInstance());
		root = new Node<T>(null,this.sequence,this);
		activePoint = new ActivePoint<T>(root);
		suffix = new Suffix(0, 1, this.sequence);
		buildTree();
	}
	
	private void buildTree(){
		for(@SuppressWarnings("unused") Object item : sequence){
			suffix.increment();
			insertsThisStep = 0;
			insert(suffix);
			currentEnd++;
		}
	}
	
	void insert(Suffix suffix){
		if(activePoint.isNode()){
			Node<T> node = activePoint.getNode();
			node.insert(suffix, activePoint);
		}else if(activePoint.isEdge()){
			Edge<T> edge = activePoint.getEdge();
			edge.insert(suffix, activePoint);
		}
	}
	
	int getCurrentEnd(){
		return currentEnd;
	}
	
	Node<T> getRoot() {
		return root;
	}

	void incrementInsertCount() {
		insertsThisStep++;
	}
	
	boolean isNotFirstInsert(){
		return insertsThisStep > 0;
	}

	Node<T> getLastNodeInserted() {
		return lastNodeInserted;
	}
	
	void setLastNodeInserted(Node<T> node){
		lastNodeInserted = node;
	}
	
	void setSuffixLink(Node<T> node){
		if(isNotFirstInsert()){
			lastNodeInserted.setSuffixLink(node);
		}
		lastNodeInserted = node;
	}
	
	@Override
	public String toString(){
		return Utils.printTreeForGraphViz(this);
	}
}