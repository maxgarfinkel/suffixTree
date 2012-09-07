package com.maxgarfinkel.suffixTree;

import java.util.Iterator;

/**
 * Represents a sequence of items. This plays the part of the string in a non
 * generic suffix tree implementation. This object automatically appends a
 * terminating item to the end of the instance which is included in all
 * operations. For example length provides the length of the sequence including
 * the terminating object.
 * 
 * @author Max Garfinkel
 * 
 * @param <T>
 */
public class Sequence<T> implements Iterable<Object> {

	private final T[] sequence;

	/**
	 * Initialize the sequence.
	 * 
	 * @param sequence
	 */
	Sequence(T[] sequence) {
		this.sequence = sequence;
	}

	/**
	 * Retrieve the item at the position specified by index.
	 * 
	 * @param index
	 * @return
	 */
	Object getItem(int index) {
		if (index < sequence.length) {
			return sequence[index];
		} else if (index == sequence.length)
			return SequenceTerminal.getInstance();
		else
			throw new IndexOutOfBoundsException("index " + index
					+ " greater than the length of this sequence.");
	}

	/**
	 * Retrieves the length of the sequence, including the terminating instance.
	 * 
	 * @return
	 */
	int getLength() {
		return sequence.length + 1;
	}

	/**
	 * Retrieves an iterator for the sequence.
	 */
	public Iterator<Object> iterator() {
		return new Iterator<Object>() {

			int currentPosition = 0;

			public boolean hasNext() {
				return sequence.length + 1 > currentPosition;
			}

			public Object next() {
				if (currentPosition < sequence.length)
					return sequence[currentPosition++];
				else if (currentPosition == sequence.length) {
					currentPosition++;
					return SequenceTerminal.getInstance();
				} else {
					return null;
				}
			}

			public void remove() {
				throw new UnsupportedOperationException(
						"Remove is not supported.");

			}

		};
	}

}
