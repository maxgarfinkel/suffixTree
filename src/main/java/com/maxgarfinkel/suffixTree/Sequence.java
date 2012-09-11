package com.maxgarfinkel.suffixTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Represents a sequence of items. This plays the part of the string in a non
 * generic suffix tree implementation. This object automatically appends a
 * terminating item to the end of the instance which is included in all
 * operations. For example length provides the length of the sequence including
 * the terminating object.
 * 
 * @author Max Garfinkel
 * 
 * @param <I>
 */
public class Sequence<I, S extends Iterable<I>> implements Iterable<Object> {

	private Object[] masterSequence;
	Logger logger = Logger.getLogger(Sequence.class);
	//private final SequenceTerminal<S> sequenceTerminal;

	/**
	 * Initialize the sequence.
	 * 
	 * @param sequence
	 */
	Sequence(S sequence) {
		ArrayList<Object> list = new ArrayList<Object>();
		for(Object item : sequence)
			list.add(item);
		SequenceTerminal<S> sequenceTerminal = new SequenceTerminal<S>(sequence);
		list.add(sequenceTerminal);
		this.masterSequence = list.toArray();
		logger.debug("Consructed a sequence with length of " + masterSequence.length);
		if(this.masterSequence == null)
			throw new IllegalArgumentException("Sequence is null.");
		
	}
	
	/*
	SequenceTerminal<S> getTerminal(){
		return sequenceTerminal;
	}
*/
	/**
	 * Retrieve the item at the position specified by index.
	 * 
	 * @param index
	 * @return
	 */
	Object getItem(int index) {
		//if (index < sequence.length) {
			return masterSequence[index];
		//} else if (index == sequence.length)
			//return sequenceTerminal;
		//else
			//throw new IndexOutOfBoundsException("index " + index
				//	+ " greater than the length of this sequence. [" + sequence.length + "]");
	}

	/**
	 * Retrieves the length of the sequence, including the terminating instance.
	 * 
	 * @return
	 */
	/*int getLength() {
		return sequence.length + 1;
	}*/

	void add(S sequence){
		List<Object> newMasterSequence = new ArrayList<Object>(masterSequence.length);
		for(Object item : masterSequence)
			newMasterSequence.add(item);
		
		for(I item : sequence){
			newMasterSequence.add(item);
		}
		SequenceTerminal<S> terminal = new SequenceTerminal<S>(sequence);
		newMasterSequence.add(terminal);
		
		masterSequence = newMasterSequence.toArray();
		
		//Object[] newMasterSequence = new Object[this.masterSequence.length + newItems.size()];
		
		/*
		ArrayList<I> list = new ArrayList<I>();
		for(I item : sequence)
			list.add(item);
		
		Object[] items = new Object[this.sequence.length + 1 + list.size()];
			
		int start = this.sequence.length+1;
		int end = list.size()+start;
		int i = 0;
		for(; i < this.sequence.length; i++){
			items[i] = this.sequence[i];
		}
		
		items[i] = this.sequenceTerminal;
		
		for(i = start; i < end; i++)
			items[i] = list.get(i-start);
		
		this.sequence = items;
		*/
	}
	
	/**
	 * Retrieves an iterator for the sequence.
	 */
	public Iterator<Object> iterator() {
		return new Iterator<Object>() {

			int currentPosition = 0;

			public boolean hasNext() {
				return masterSequence.length + 1 > currentPosition;
			}

			public Object next() {
				if (currentPosition <= masterSequence.length)
					return masterSequence[currentPosition++];
				else {
					return null;
				}
			}

			public void remove() {
				throw new UnsupportedOperationException(
						"Remove is not supported.");

			}

		};
	}
	
	int getLength(){
		return masterSequence.length;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder("Sequence = [");
		for(Object i : masterSequence){
			sb.append(i).append(", ");
		}
		sb.append("]");
		return sb.toString();
	}
	
}
