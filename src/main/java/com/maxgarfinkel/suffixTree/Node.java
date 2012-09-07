package com.maxgarfinkel.suffixTree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Node<T> implements Iterable<Edge<T>> {
	private final Map<T, Edge<T>> edges = new HashMap<T, Edge<T>>();
	private final Edge<T> incomingEdge;
	private final Sequence<T> sequence;
	private final SuffixTree<T> tree;
	private Node<T> link = null;

	/**
	 * Create a new node, for the supplied tree and sequence.
	 * 
	 * @param incomingEdge
	 *            The parent edge, unless this is a root node.
	 * @param sequence
	 *            The sequence this tree is indexing.
	 * @param tree
	 *            The tree to which this node belongs.
	 */
	Node(Edge<T> incomingEdge, Sequence<T> sequence, SuffixTree<T> tree) {
		this.incomingEdge = incomingEdge;
		this.sequence = sequence;
		this.tree = tree;
	}

	/**
	 * Inserts the suffix at the given active point.
	 * 
	 * @param suffix
	 *            The suffix to insert.
	 * @param activePoint
	 *            The active point to insert it at.
	 */
	@SuppressWarnings("unchecked")
	void insert(Suffix<T> suffix, ActivePoint<T> activePoint) {
		Object item = suffix.getEndItem();
		if (edges.containsKey(item)) {
			if (tree.isNotFirstInsert()
					&& activePoint.getNode() != tree.getRoot())
				tree.setSuffixLink(activePoint.getNode());
			activePoint.setEdge(edges.get(item));
			activePoint.incrementLength();
		} else {
			Edge<T> newEdge = new Edge<T>(suffix.getEndPosition(), this,
					sequence, tree);
			edges.put((T) suffix.getEndItem(), newEdge);
			suffix.decrement();
			activePoint.updateAfterInsert(suffix);
			if (suffix.isEmpty())
				return;
			else
				tree.insert(suffix);
		}
	}

	/**
	 * Inserts the given edge as a child of this node. The edge must not already
	 * exist as child or an IllegalArgumentException will be thrown.
	 * 
	 * @param edge
	 *            The edge to be inserted.
	 * @throws IllegalArgumentException
	 *             This is thrown when the edge already exists as an out bound
	 *             edge of this node.
	 */
	void insert(Edge<T> edge) {
		if (edges.containsKey(edge.getStartItem()))
			throw new IllegalArgumentException("Item " + edge.getStartItem()
					+ " already exists in node " + toString());
		edges.put(edge.getStartItem(), edge);
	}

	/**
	 * Retrieves the edge starting with item or null if none exists.
	 * 
	 * @param item
	 * @return The edge extending from this node starting with item.
	 */
	Edge<T> getEdgeStarting(Object item) {
		return edges.get(item);
	}

	/**
	 * True if the node has a suffix link extending from it.
	 * 
	 * @return True if node has suffix link. False if not.
	 */
	boolean hasSuffixLink() {
		return link != null;
	}

	/**
	 * Gets the number of edges extending from this node.
	 * 
	 * @return The count of the number edges extending from this node.
	 */
	int getEdgeCount() {
		return edges.size();

	}

	/**
	 * @return An iterator which iterates over the child edges. No order is
	 *         guaranteed.
	 */
	public Iterator<Edge<T>> iterator() {
		return edges.values().iterator();
	}

	/**
	 * 
	 * @return The node that this nodes suffix link points to if it has one.
	 *         Null if not.
	 */
	Node<T> getSuffixLink() {
		return link;
	}

	/**
	 * Sets the suffix link of this node to point to the supplied node.
	 * 
	 * @param node
	 *            The node this suffix link should point to.
	 */
	void setSuffixLink(Node<T> node) {
		link = node;
	}

	@Override
	public String toString() {
		if (incomingEdge == null)
			return "root";
		else {
			return "end of edge [" + incomingEdge.toString() + "]";
		}
	}
}
