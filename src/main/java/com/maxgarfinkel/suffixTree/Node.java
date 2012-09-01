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
	
	void insert(Suffix<T> suffix, ActivePoint<T> activePoint){
		Object item = suffix.getEndItem();
		if(edges.containsKey(item)){
			activePoint.setEdge(edges.get(item));
			activePoint.incrementLength();
			System.out.println("Active Point " + activePoint.toString());
			System.out.println("Suffix " + suffix.toString());
		}else{
			Edge<T> newEdge = new Edge<T>(suffix.getEndPosition(), this, sequence, tree);
			edges.put((T)suffix.getEndItem(), newEdge);
			suffix.decrement();
			activePoint.updateAfterInsert(suffix);
			System.out.println("Active Point " + activePoint.toString());
			System.out.println("Suffix " + suffix.toString());
		}
	}
	
	void insert(Edge<T> edge){
		if(edges.containsKey(edge.getStartItem()))
			throw new IllegalArgumentException("Item " + edge.getStartItem() + " already exists in node " + toString());
		edges.put(edge.getStartItem(), edge);
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

	Node<T> getSuffixLink() {
		return link;
	}
	
	void setSuffixLink(Node<T> node){
		link = node;
	}
	
	@Override
	public String toString(){
		if(incomingEdge == null)
			return "root";
		else{
			return "end of edge [" + incomingEdge.toString()+"]";
		}
	}
}
