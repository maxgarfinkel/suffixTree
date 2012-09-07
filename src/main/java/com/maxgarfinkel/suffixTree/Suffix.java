package com.maxgarfinkel.suffixTree;

/**
 * Represents the remaining suffix to be inserted during suffix tree
 * construction.
 * 
 * @author maxgarfinkel
 * 
 * @param <T>
 */
class Suffix<T> {
	private int start;
	private int end;
	private Sequence<T> sequence;

	/**
	 * Construct a subsequence of sequence. The subsequence will be a suffix of
	 * the sequence UP TO the point in the sequence we have reached whilst
	 * running Ukonnen's algorithm. In this sense it is not a true suffix of the
	 * sequence but only a suffix of the portion of the sequence we have so far
	 * parsed.
	 * 
	 * @param currentEnd
	 *            The current end point (becomes the end of this suffix).
	 * @param remainder
	 *            The remaining items to be inserted into the tree (becomes the
	 *            length of this suffix).
	 * @param sequence
	 *            The sequence to which this suffix belongs.
	 */
	public Suffix(int currentEnd, int remainder, Sequence<T> sequence) {
		start = currentEnd - (remainder - 1);
		end = currentEnd;
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		int end = getEndPosition();
		for (int i = start; i < end + 1; i++) {
			sb.append(sequence.getItem(i)).append(",");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 
	 * @return The position in the master sequence of the end item in this
	 *         suffix. This value is inclusive, thus and end of 0 implies the
	 *         suffix contains only the item at <code>sequence[0]</code>
	 */
	int getEndPosition() {
		return end - 1;
	}

	/**
	 * Get the end item of this suffix.
	 * 
	 * @return The end item of sequence
	 */
	Object getEndItem() {
		return sequence.getItem(end - 1);
	}

	/**
	 * Get the start of this suffix.
	 * 
	 * @return
	 */
	Object getStart() {
		if (start >= sequence.getLength())
			return null;
		return sequence.getItem(start);
	}

	/**
	 * Decrement the length of this suffix. This is done by incrementing the
	 * start position. This is reducing its length from the back.
	 */
	void decrement() {
		start++;
	}

	/**
	 * Increments the length of the suffix by incrementing the end position. The
	 * effectivly moves the suffix forward, along the master sequence.
	 */
	void increment() {
		end++;
	}

	/**
	 * Indicates if the suffix is empty.
	 * 
	 * @return
	 */
	boolean isEmpty() {
		return start == end;
	}

	/**
	 * Retrieves the count of remaining items in the suffix.
	 * 
	 * @return The number of items in the suffix.
	 */
	int getRemaining() {
		return (end - start) - 1;
	}

	/**
	 * Retrieves the item the given distance from the end of the suffix.
	 * 
	 * @param distanceFromEnd
	 *            The distance from the end.
	 * @return The item the given distance from the end.
	 * @throws IllegalArgumentException
	 *             if the distance from end is greater than the length of the
	 *             suffix.
	 */
	public Object getItemXFromEnd(int distanceFromEnd) {
		if ((end - distanceFromEnd) < start)
			throw new IllegalArgumentException(distanceFromEnd
					+ " extends before the start of this suffix: " + this);
		return sequence.getItem(end - distanceFromEnd);
	}
}
