package com.maxgarfinkel.suffixTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * A suffix tree implementation using Ukkonen's algorithm.
 * 
 * @author Max Garfinkel
 *
 * @param <I> The type of the item within the sequence. 
 */
public class SuffixTree<T> {
	
	private final Node<T> root;
	private Object[] sequence;
	
	private int currentEnd = 0;
	private int remainder = 0;
	private int insertsThisStep = 0;
	private Node<T> lastNodeInserted = null;
	
	private Logger logger = Logger.getLogger(SuffixTree.class);
	
	
	/**
	 * Construct a suffix tree representation of the given sequence.
	 * @param sequence the array of items for which we are going to generate a suffix tree.
	 * @throws Exception 
	 */
	SuffixTree(T[] sequence) throws Exception{
		
		this.sequence = Utils.addLeafToSequence(sequence, Leaf.getInstance());
		root = new Node<T>(null,this.sequence,this);
		buildTree();
	}
	
	private void buildTree(){
		ActivePoint<T> activePoint = new ActivePoint<T>(root);
		for(Object item : sequence){
			remainder++;
			Suffix<T> suffix = new Suffix<T>(currentEnd, remainder, sequence);
			activePoint.insertSuffix(suffix);
			currentEnd++;
			insertsThisStep = 0;
		}
	}
	
	int getCurrentEnd(){
		return currentEnd;
	}
	
	public Node<T> getRoot() {
		return root;
	}
}
