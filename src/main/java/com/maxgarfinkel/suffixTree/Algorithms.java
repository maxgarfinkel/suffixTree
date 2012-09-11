package com.maxgarfinkel.suffixTree;

import java.util.Collection;
import java.util.HashSet;

/**
 * 
 * @author max garfinkel
 *
 * @param <I> The item type
 * @param <S> The sequence of item type
 */
public class Algorithms<I,S extends Iterable<I>> {
	
	private final SuffixTree<I,S> tree;
	private  Node<I,S> node;// = tree.getRoot();
	private Edge<I,S> edge = null;
	private Integer position = 0;
	
	Algorithms(SuffixTree<I,S> tree){
		this.tree = tree;
		node = tree.getRoot();
	}
	
	@SuppressWarnings("unchecked")
	public Collection<S> getSequencesContainingSuffix(S sequence){
		resetToRoot();
		Collection<S> containedSequences = new HashSet<S>();
		
		if(containsSubSequence(sequence) == true){
			position++;
			if(edge != null && edge.getLength() > position){
				I item = edge.getItemAt(position);
				if(item.getClass().equals(SequenceTerminal.class)){
					containedSequences.add(((SequenceTerminal<S>)item).getSequence());
				}
			}else if(edge != null && edge.getLength() == position){
				if(edge.isTerminating()){
					for(SequenceTerminal<S> term : edge.getTerminal().getSuffixTerminals()){
						containedSequences.add(term.getSequence());
					}
				}
			}else if(edge == null){
				if(node.getSuffixTerminals() != null){
					for(SequenceTerminal<S> seq : node.getSuffixTerminals()){
						containedSequences.add(seq.getSequence());
					}
				}
			}
		}
		resetToRoot();
		return containedSequences;
	}
	
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
	public  boolean containsSuffix(S sequence){
		return !getSequencesContainingSuffix(sequence).isEmpty();
		
	}
	
	private boolean containsSubSequence(S sequence){
		for (Object item : sequence) {
			position++;
			if (edge == null) {
				Edge<I,S> outBoundEdge = node.getEdgeStarting(item);
				if (outBoundEdge != null) {
					edge = outBoundEdge;
					position = 0;
					if(!outBoundEdge.getStartItem().equals(item))
						return false;
				} else {
					return false;
				}
			} else {
				int length = edge.getLength();
				if (position == length && edge.isTerminating()) {
					node = edge.getTerminal();
					Edge<I,S> e = node.getEdgeStarting(item);
					if (e != null) {
						edge = e;
						position = 0;
					}
				} else if (!edge.getItemAt(position).equals(item))
					return false;
			}
		}
		return true;
	}
	
	public void resetToRoot(){
		node = tree.getRoot();
		edge = null;
		position = 0;
	}
	
}
