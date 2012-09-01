package com.maxgarfinkel.suffixTree;

class ActivePoint<T> {
	private Node<T> activeNode;
	private Edge<T> activeEdge;
	private int activeLength;
	private final Node<T> root;
	
	ActivePoint(Node<T> root){
		activeNode = root;
		activeEdge = null;
		activeLength = 0;
		this.root = root;
	}
	
	void setPosition(Node<T> node, Edge<T> edge, int length){
		activeNode = node;
		activeEdge = edge;
		activeLength = length;
	}
	
	void setEdge(Edge<T> edge){
		activeEdge = edge;
	}
	
	void incrementLength(){
		activeLength++;
		resetActivePointToTerminal();
	}
	
	void decrementLength(){
		if(activeLength > 0)
			activeLength--;
		resetActivePointToTerminal();
	}

	boolean isRootNode() {
		return activeNode.equals(root) && activeEdge == null && activeLength == 0;
	}

	boolean isNode() {
		return activeEdge == null && activeLength == 0;
	}

	Node<T> getNode() {
		return activeNode;
	}

	boolean isEdge() {
		return activeEdge != null;
	}

	Edge<T> getEdge() {
		return activeEdge;
	}
	
	int getLength(){
		return activeLength;
	}

	public void updateAfterInsert(Suffix<T> suffix) {
		if(activeNode == root && suffix.isEmpty()){
			activeNode = root;
			activeEdge = null;
			activeLength = 0;
		}else if(activeNode == root){
			Object item = suffix.getStart();
			activeEdge = root.getEdgeStarting(item);
			decrementLength();
		}else if(activeNode.hasSuffixLink()){
			activeNode = activeNode.getSuffixLink();
			findTrueActiveEdge();
			fixActiveEdgeAfterSuffixLink(suffix);
		}else{
			activeNode = root;
			findTrueActiveEdge();
		}
	}
	
	/**
	 * Deal with the case when we follow a suffix link but the active
	 * length is greater than the new active edge length. In this
	 * situation we must walk down the tree updating the entire active 
	 * point.
	 */
	private void fixActiveEdgeAfterSuffixLink(Suffix<T> suffix){
		while(activeLength > activeEdge.getLength()){			
			activeLength = activeLength - activeEdge.getLength();
			activeNode = activeEdge.getTerminal();
			Object item = suffix.getItemXFromEnd(activeLength);
			activeEdge = activeNode.getEdgeStarting(item);
		}
		resetActivePointToTerminal();
	}
	
	/**
	 * Finds the edge instance who's start item matches the current active
	 * edge start item but comes from the current active node.
	 */
	private void findTrueActiveEdge(){
		if(activeEdge != null){
			Object item = activeEdge.getStartItem();
			activeEdge = activeNode.getEdgeStarting(item);
		}
	}
	
	/**
	 * Resizes the active length in the case where we are sitting on a terminal.
	 * @return true if reset occurs false otherwise.
	 */
	private boolean resetActivePointToTerminal(){
		if(activeEdge !=null && activeEdge.getLength() == activeLength && activeEdge.isTerminating()){
			activeNode = activeEdge.getTerminal();
			activeEdge = null;
			activeLength = 0;
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public String toString(){
		return "{" + activeNode.toString() + ", " + activeEdge + ", " + activeLength+"}";
	}
}
