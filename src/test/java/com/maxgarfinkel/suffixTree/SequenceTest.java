package com.maxgarfinkel.suffixTree;

import static org.junit.Assert.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

public class SequenceTest {

	private static Logger LOGGER = Logger.getLogger(SequenceTest.class);
	
	@Before
	public void setUp() throws Exception {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		LOGGER.setLevel(Level.DEBUG);
	}

	@Test
	public void emptySequenceIsNotNull() {
		Sequence<Character, Word> sequence = new Sequence<Character, Word>();
		assertThat(sequence, is(notNullValue()));
	}
	
	@Test
	public void emptySequenceHasZeroLength() {
		Sequence<Character, Word> sequence = new Sequence<Character, Word>();
		assertThat(sequence.getLength(), is(0));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void emptySequenceThrowsExceptionOnGetItemAt() {
		Sequence<Character, Word> sequence = new Sequence<Character, Word>();
		sequence.getItem(0);
	}

	@Test
	public void sequenceWithWordIsOneLongerThanWord() {
		Word word = new Word("Test word");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		assertThat(sequence.getLength(), is(10));
	}
	
	@Test
	public void sequenceWithWordHasWordAsTerminatingElement() {
		Word word = new Word("Test word");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		@SuppressWarnings("unchecked")
		SequenceTerminal<Word> terminal = (SequenceTerminal<Word>)sequence.getItem(9);
		assertThat(terminal.getSequence(), is(word));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void sequenceWithWordItteratesOverItems() {
		Word word = new Word("Test word");
		char[] characters = new char[]{'T','e','s','t',' ','w','o','r','d'};
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		
		int position = 0;
		for(Object ch : sequence){
			if(position < 9)
				assertThat(characters[position], is((Character)ch));
			else
				assertThat(word, is(((SequenceTerminal<Word>)ch).getSequence()));
			position++;
		}
		assertThat(position, is(10));
	}

	@Test
	public void getItemRetrievesCorrectItem() {
		Word word = new Word("Test word");
		char[] characters = new char[]{'T','e','s','t',' ','w','o','r','d'};
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		
		for(int i = 0; i < sequence.getLength()-1; i++){
			assertThat((Character)sequence.getItem(i), is(characters[i]));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void canAddItemToEmptySequence() {
		Sequence<Character, Word> sequence = new Sequence<Character, Word>();
		Word word = new Word("Test word");
		sequence.add(word);
		char[] characters = new char[]{'T','e','s','t',' ','w','o','r','d'};
		@SuppressWarnings("unchecked")
		SequenceTerminal<Word> terminal = (SequenceTerminal<Word>)sequence.getItem(9);
		assertThat(terminal.getSequence(), is(word));
		int position = 0;
		for(Object ch : sequence){
			if(position < 9)
				assertThat(characters[position], is((Character)ch));
			else
				assertThat(word, is(((SequenceTerminal<Word>)ch).getSequence()));
			position++;
		}
		assertThat(position, is(10));
		for(int i = 0; i < sequence.getLength()-1; i++){
			assertThat((Character)sequence.getItem(i), is(characters[i]));
		}
	}
	
	@Test
	public void canAddMultipleItemsToEmptySequenceAndLengthStaysCorrect() {
		Sequence<Character, Word> sequence = new Sequence<Character, Word>();
		Word word1 = new Word("word one");
		Word word2 = new Word("word two");
		Word word3 = new Word("word three");
		
		sequence.add(word1);
		sequence.add(word2);
		sequence.add(word3);
		
		assertThat(sequence.getLength(), is(9+9+11));
	}
	
	@Test
	public void sequenceTerminalsAreCorrectlyPositionedInWordSequence() {
		Sequence<Character, Word> sequence = new Sequence<Character, Word>();
		Word word1 = new Word("word one");
		Word word2 = new Word("word two");
		Word word3 = new Word("word three");
		
		sequence.add(word1);
		sequence.add(word2);
		sequence.add(word3);
		
		@SuppressWarnings("unchecked")
		SequenceTerminal<Word> terminal1 = (SequenceTerminal<Word>)sequence.getItem(8);
		assertThat(terminal1.getSequence(), is(word1));
		
		@SuppressWarnings("unchecked")
		SequenceTerminal<Word> terminal2 = (SequenceTerminal<Word>)sequence.getItem(17);
		assertThat(terminal2.getSequence(), is(word2));
		
		@SuppressWarnings("unchecked")
		SequenceTerminal<Word> terminal3 = (SequenceTerminal<Word>)sequence.getItem(28);
		assertThat(terminal3.getSequence(), is(word3));
	}
	
	@Test(expected=NullPointerException.class)
	public void sequenceThrowsExceptionWhenConstructedWithNullArgument(){
		@SuppressWarnings("unused")
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(null);
	}
	
	@Test(expected=NullPointerException.class)
	public void sequenceThrowsExceptionWhenAddingANullItem(){
		Sequence<Character, Word> sequence = new Sequence<Character, Word>();
		sequence.add(null);
	}

}
