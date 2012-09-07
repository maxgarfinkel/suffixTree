package com.maxgarfinkel.suffixTree;

public class Algorithms {

	/**
	 * Tests the supplied tree to see if contains the supplied suffix.
	 * 
	 * @param suffix
	 *            The suffix which may exist in the supplied tree.
	 * @param tree
	 *            The tree which may contain the supplied suffix.
	 * @return True if the suffix is contained within the supplied tree. False
	 *         otherwise.
	 */
	public <T> boolean containsSuffix(Object[] suffix, SuffixTree<T> tree) {
		suffix = Utils.addTerminalToSequence(suffix,
				SequenceTerminal.getInstance());
		Node<T> node = tree.getRoot();
		Edge<T> edge = null;
		int position = 0;
		for (Object item : suffix) {
			position++;
			if (edge == null) {
				Edge<T> e = node.getEdgeStarting(item);
				if (e != null) {
					edge = e;
					position = 0;
				} else {
					return false;
				}
			} else {
				int length = edge.getLength();
				if (position == length && edge.isTerminating()) {
					node = edge.getTerminal();
					Edge<T> e = node.getEdgeStarting(item);
					if (e != null) {
						edge = e;
						position = 0;
					}
				} else if (!edge.getItemAt(position).equals(item))
					return false;
			}
		}

		if (edge == null || edge.getItemAt(position) == null)
			return false;
		else if (edge.isTerminating())
			return false;
		else
			return true;
	}
}
