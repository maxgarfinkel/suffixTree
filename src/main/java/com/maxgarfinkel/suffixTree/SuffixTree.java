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
		this.sequence = Utils.addTerminalToSequence(sequence, SequenceTerminal.getInstance());
		root = new Node<T>(null,this.sequence,this);
		activePoint = new ActivePoint<T>(root);
		suffix = new Suffix<T>(0, 1, this.sequence);
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
	
	/**
	 * Inserts the given suffix into this tree.
	 * @param suffix The suffix to insert. 
	 */
	void insert(Suffix<T> suffix){
		if(activePoint.isNode()){
			Node<T> node = activePoint.getNode();
			node.insert(suffix, activePoint);
		}else if(activePoint.isEdge()){
			Edge<T> edge = activePoint.getEdge();
			edge.insert(suffix, activePoint);
		}
	}
	
	/**
	 * Retrieves the point in the sequence for which all proceeding
	 * item have been inserted into the tree.
	 * @return The index of the current end point of tree.
	 */
	int getCurrentEnd(){
		return currentEnd;
	}
	
	/**
	 * Retrieves the root node for this tree.
	 * @return The root node of the tree.
	 */
	Node<T> getRoot() {
		return root;
	}

	/**
	 * Increments the inserts counter for this step.
	 */
	void incrementInsertCount() {
		insertsThisStep++;
	}
	
	/**
	 * Indecates if there have been inserts during the current step.
	 * @return
	 */
	boolean isNotFirstInsert(){
		return insertsThisStep > 0;
	}

	/**
	 * Retrieves the last node to be inserted, null if none has.
	 * @return The last node inserted or null.
	 */
	Node<T> getLastNodeInserted() {
		return lastNodeInserted;
	}
	
	/**
	 * Sets the last node inserted to the supplied node.
	 * @param node The node representing the last node inserted.
	 */
	void setLastNodeInserted(Node<T> node){
		lastNodeInserted = node;
	}
	
	/**
	 * Sets the suffix link of the last inserted node to
	 * point to the supplied node. This method checks the state
	 * of the step and only applies the suffix link if there is
	 * a previous node inserted during this step. This method also
	 * set the last node inserted to the supplied node after applying
	 * any suffix linking.
	 * @param node The node to which the last node inserteds suffix link should point to.
	 */
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