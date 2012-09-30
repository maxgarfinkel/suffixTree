package com.maxgarfinkel.suffixTree;

import java.util.Arrays;
import java.util.Iterator;

public class Word implements Iterable<Character> {

	char[] word;
	
	Word(String word){
		this.word = word.toCharArray();
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(word);
		return result;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (!Arrays.equals(word, other.word))
			return false;
		return true;
	}

	public String toString(){
		return new String(word);
	}

	public Iterator<Character> iterator() {
		return new Iterator<Character>(){

			private int position = 0;
			
			public boolean hasNext() {
				return position < word.length;
			}

			public Character next() {
				return word[position++];
			}

			public void remove() {
				
			}
			
		};
	}

	char[] getCharArray(){
		return word;
	}
}
