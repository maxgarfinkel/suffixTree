package com.maxgarfinkel.suffixTree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Utils {

	/**
	 * Appends a SequenceTerminal element to a supplied array.
	 * 
	 * @param sequence
	 *            The sequence to which we are applying the terminating object.
	 * @param terminatingObject
	 *            The instance of the terminating object.
	 * @return A new sequence with an extra element at the end containing the
	 *         terminating object.
	 */
	static Object[] addTerminalToSequence(Object[] sequence,
			SequenceTerminal terminatingObject) {
		Object[] newSequence = new Object[sequence.length + 1];
		int i = 0;
		for (; i < sequence.length; i++)
			newSequence[i] = sequence[i];
		newSequence[i] = terminatingObject;
		return newSequence;
	}

	/**
	 * Generates a .dot format string for visualizing a suffix tree.
	 * 
	 * @param tree
	 *            The tree for which we are generating a dot file.
	 * @return A string containing the contents of a .dot representation of the
	 *         tree.
	 */
	static <T> String printTreeForGraphViz(SuffixTree<T> tree) {
		LinkedList<Node<T>> stack = new LinkedList<Node<T>>();
		stack.add(tree.getRoot());
		Map<Node<T>, Integer> nodeMap = new HashMap<Node<T>, Integer>();
		nodeMap.put(tree.getRoot(), 0);
		int nodeId = 1;

		StringBuilder sb = new StringBuilder(
				"\ndigraph suffixTree{\n node [shape=circle, label=\"\", fixedsize=true, width=0.1, height=0.1]\n");

		while (stack.size() > 0) {
			LinkedList<Node<T>> childNodes = new LinkedList<Node<T>>();
			for (Node<T> node : stack) {

				// List<Edge> edges = node.getEdges();
				for (Edge<T> edge : node) {
					int id = nodeId++;
					if (edge.isTerminating()) {
						childNodes.push(edge.getTerminal());
						nodeMap.put(edge.getTerminal(), id);
					}

					sb.append(nodeMap.get(node)).append(" -> ").append(id)
							.append(" [label=\"");
					int end = edge.getEnd();
					for (T item : edge) {
						sb.append(item.toString());
					}
					sb.append("\"];\n");
				}
			}
			stack = childNodes;
		}

		// loop again to find all suffix links.
		sb.append("edge [color=red]\n");
		for (Map.Entry<Node<T>, Integer> entry : nodeMap.entrySet()) {
			Node n1 = entry.getKey();
			int id1 = entry.getValue();

			if (n1.hasSuffixLink()) {
				Node n2 = n1.getSuffixLink();
				Integer id2 = nodeMap.get(n2);
				// if(id2 != null)
				sb.append(id1).append(" -> ").append(id2).append(" ;\n");
			}
		}

		sb.append("}");
		return (sb.toString());
	}
}
