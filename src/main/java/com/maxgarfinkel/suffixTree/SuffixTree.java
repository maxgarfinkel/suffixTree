package com.maxgarfinkel.suffixTree;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * A suffix tree implementation using Ukkonen's algorithm.
 * 
 * @author Max Garfinkel
 * 
 * @param <I>
 *            The type of the item within the sequence.
 */
public class SuffixTree<I,S extends Iterable<I>> {

	private final Node<I,S> root;
	private final Sequence<I,S> sequence;
	
	//private Map<Integer, Sequence<I,S>> seqLengthToSequenceMap = new TreeMap<Integer, Sequence<I,S>>();
	
	private Suffix<I,S> suffix;
	private final ActivePoint<I,S> activePoint;
	private int currentEnd = 0;
	private int insertsThisStep = 0;
	private Node<I,S> lastNodeInserted = null;
	
	private Logger logger = Logger.getLogger(SuffixTree.class);

	/*
	SuffixTree(){
		//sequence = new Sequence<I,S>();
		root = new Node<I,S>(null, this.sequence, this);
		activePoint = new ActivePoint<I,S>(root);
		suffix = new Suffix<I, S>(0, 1, this.sequence);
	}
	*/
	
	
	
	/**
	 * Construct and represent a suffix tree representation of the given
	 * sequence using Ukkonen's algorithm.
	 * 
	 * @param sequence
	 *            the array of items for which we are going to generate a suffix
	 *            tree.
	 * @throws Exception
	 */
	SuffixTree(S sequenceArray) {
		sequence = new Sequence<I, S>(sequenceArray);
		root = new Node<I,S>(null, this.sequence, this);
		activePoint = new ActivePoint<I,S>(root);
		suffix = new Suffix<I, S>(0, 1, this.sequence);
		extendTree(0,sequence.getLength());
	}
	
	public void add(S sequence){
		
		logger.debug("The active point is: " + activePoint.toString());
		logger.debug("fixing all open end points to " + currentEnd);
		fixEndPoints();
		int start = currentEnd;
		//currentEnd += 1;
		this.sequence.add(sequence);
		int end  = currentEnd;
		
		for(I i : sequence){
			end++;
		}
		suffix = new Suffix<I,S>(currentEnd,1,this.sequence);
		
		extendTree(start, end+1);
	}
/*
	SequenceTerminal<S> getTerminal(S sequence){
		return this.sequence.getTerminal();
	}
	*/
	private void extendTree(int from, int to) {
		logger.debug("extending tree from: " + from + " to: " + to);
		logger.debug("starting with suffix: " + suffix);
		for (int i = from; i < to; i++){
			suffix.increment();
			insertsThisStep = 0;
			insert(suffix);
			currentEnd++;
		}
	}
	/*
	private void addSequenceToTree() {
		for (@SuppressWarnings("unused")
		Object item : sequence) {
			suffix.increment();
			insertsThisStep = 0;
			insert(suffix);
			currentEnd++;
		}
	}
	*/
	private void fixEndPoints(){
		//walk tree setting each end node that doesn't have an
		//explicit end to have an explicit end equal to the 
		//current end.
		
		LinkedList<Edge<I,S>> stack = new LinkedList<Edge<I,S>>();
		stack.addAll(root.getEdges());
		while(!stack.isEmpty()){
			Edge<I,S> edge = stack.removeFirst();
			if(edge.isTerminating()){
				stack.addAll(edge.getTerminal().getEdges());
			}else{
				edge.fixEnd();
			}
		}
	}
	
	

	/**
	 * Inserts the given suffix into this tree.
	 * 
	 * @param suffix
	 *            The suffix to insert.
	 */
	void insert(Suffix<I, S> suffix) {
		if (activePoint.isNode()) {
			Node<I, S> node = activePoint.getNode();
			node.insert(suffix, activePoint);
		} else if (activePoint.isEdge()) {
			Edge<I,S> edge = activePoint.getEdge();
			edge.insert(suffix, activePoint);
		}
	}

	/**
	 * Retrieves the point in the sequence for which all proceeding item have
	 * been inserted into the tree.
	 * 
	 * @return The index of the current end point of tree.
	 */
	int getCurrentEnd() {
		return currentEnd;
	}

	/**
	 * Retrieves the root node for this tree.
	 * 
	 * @return The root node of the tree.
	 */
	Node<I,S> getRoot() {
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
	 * 
	 * @return
	 */
	boolean isNotFirstInsert() {
		return insertsThisStep > 0;
	}

	/**
	 * Retrieves the last node to be inserted, null if none has.
	 * 
	 * @return The last node inserted or null.
	 */
	Node<I,S> getLastNodeInserted() {
		return lastNodeInserted;
	}

	/**
	 * Sets the last node inserted to the supplied node.
	 * 
	 * @param node
	 *            The node representing the last node inserted.
	 */
	void setLastNodeInserted(Node<I,S> node) {
		lastNodeInserted = node;
	}

	/**
	 * Sets the suffix link of the last inserted node to point to the supplied
	 * node. This method checks the state of the step and only applies the
	 * suffix link if there is a previous node inserted during this step. This
	 * method also set the last node inserted to the supplied node after
	 * applying any suffix linking.
	 * 
	 * @param node
	 *            The node to which the last node inserted's suffix link should
	 *            point to.
	 */
	void setSuffixLink(Node<I,S> node) {
		if (isNotFirstInsert()) {
			lastNodeInserted.setSuffixLink(node);
		}
		lastNodeInserted = node;
	}

	@Override
	public String toString() {
		return Utils.printTreeForGraphViz(this);
	}
	
	Sequence<I,S> getSequence(){
		return sequence;
	}
}