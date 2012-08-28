package com.maxgarfinkel.suffixTree;

public class Utils {

	static Object[] addLeafToSequence(Object[] sequence, Leaf leaf){
		Object[] newSequence = new Object[sequence.length+1];
		int i = 0;
		for(;i < sequence.length; i++)
			newSequence[i] = sequence[i];
		newSequence[i] = leaf;
		return newSequence;
	}
}
