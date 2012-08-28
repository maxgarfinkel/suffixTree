package com.maxgarfinkel.suffixTree;

class ActivePoint<T> {
	private Node<T> activeNode;
	private Edge<T> activeEdge;
	private int activeLength;
	
	ActivePoint(Node<T> root){
		activeNode = root;
		activeEdge = null;
		activeLength = 0;
	}
	
	/**
	 * Inserts a suffix at the active point.
	 * @param suffix the suffix to be inserted at the active point
	 * @return true if suffix was explicitly inserted, false otherwise.
	 */
	boolean insertSuffix(Suffix suffix){
		boolean isExplicitInsert = false;
		if(activeEdge != null){
			//insert on the edge
			isExplicitInsert = activeEdge.insert(suffix, this);
		}else{
			//insert on the node
			isExplicitInsert = activeNode.insert(suffix, this);
		}
		return false;
	}
	
	void setPosition(Node<T> node, Edge<T> edge, int length){
		activeNode = node;
		activeEdge = edge;
		activeLength = length;
	}
}
