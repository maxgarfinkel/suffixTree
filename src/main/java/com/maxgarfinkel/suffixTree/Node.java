package com.maxgarfinkel.suffixTree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Node<T> implements Iterable<Edge<T>>{
	private final Map<T,Edge<T>> edges = new HashMap<T,Edge<T>>();
	private final Edge<T> incomingEdge;
	private final Object[] sequence;
	private final SuffixTree<T> tree;
	private Node<T> link = null;
	
	Node(Edge<T> incomingEdge, Object[] sequence, SuffixTree<T> tree){
		this.incomingEdge = incomingEdge;
		this.sequence = sequence;
		this.tree = tree;
	}
	
	boolean insert(Suffix<T> suffix, ActivePoint<T> activePoint){
		if(edges.containsKey(suffix.getEndItem())){
			Edge<T> edge = edges.get(suffix.getEndItem());
			activePoint.setPosition(this, edge, 1);
			return false;
		}else{
			Edge<T> edge = new Edge<T>(suffix.getEndPosition(), this, sequence, tree);
			edges.put(edge.getStartItem(),edge);
			return true;
		}
	}
	
	Edge<T> getEdgeStarting(Object item){
		return edges.get(item);
	}
	
	boolean hasSuffixLink(){
		return link != null;
	}

	int getEdgeCount(){
		return edges.size();
		
	}

	public Iterator<Edge<T>> iterator() {
		return edges.values().iterator();
	}
}
