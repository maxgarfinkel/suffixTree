package com.maxgarfinkel.suffixTree;

import static org.junit.Assert.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
public class SuffixTest {

	private static Logger LOGGER = Logger.getLogger(SuffixTest.class);
	
	
	@Before
	public void setUp() throws Exception {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		LOGGER.setLevel(Level.DEBUG);
	}

	@Test
	public void suffixWithStartZeroEndZeroIsEmpty() {
		Word word = new Word("Suffix Test");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		Suffix<Character, Word> suffix = new Suffix<Character, Word>(0,0,sequence);
		assertThat(suffix.isEmpty(), describedAs("suffix.isEmpty <true>",is(true)));
		assertThat(suffix.getStart(), is(nullValue()));
		assertThat(suffix.getEndItem(), is(nullValue()));
		assertThat(suffix.getRemaining(), is(0));
	}
	
	@Test
	public void incrementMovesHeadPointer() {
		Word word = new Word("Suffix Test");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		Suffix<Character, Word> suffix = new Suffix<Character, Word>(0,0,sequence);
		suffix.increment();
		assertThat(suffix.isEmpty(), describedAs("suffix.isEmpty <false>",is(false)));
		assertThat((Character)suffix.getStart(), is('S'));
		assertThat((Character)suffix.getEndItem(), is('S'));
		assertThat(suffix.getRemaining(), is(1));
		suffix.increment();
		assertThat(suffix.isEmpty(), describedAs("suffix.isEmpty <false>",is(false)));
		assertThat((Character)suffix.getStart(), is('S'));
		assertThat((Character)suffix.getEndItem(), is('u'));
		assertThat(suffix.getRemaining(), is(2));
	}
	
	@Test
	public void decrementMovesTailPointer() {
		Word word = new Word("abcdefg");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		Suffix<Character, Word> suffix = new Suffix<Character, Word>(0,0,sequence);
		suffix.increment();
		suffix.increment();
		suffix.increment();
		
		assertThat(suffix.isEmpty(), describedAs("suffix.isEmpty <false>",is(false)));
		assertThat((Character)suffix.getStart(), is('a'));
		assertThat((Character)suffix.getEndItem(), is('c'));
		assertThat(suffix.getRemaining(), is(3));
		suffix.decrement();
		assertThat(suffix.isEmpty(), describedAs("suffix.isEmpty <false>",is(false)));
		assertThat((Character)suffix.getStart(), is('b'));
		assertThat((Character)suffix.getEndItem(), is('c'));
		assertThat(suffix.getRemaining(), is(2));
	}

	@Test
	public void tailPointerCanPushUpHeadPointer() {
		Word word = new Word("Suffix Test");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		Suffix<Character, Word> suffix = new Suffix<Character, Word>(0,0,sequence);
		//takes us to:
		//          Suffix Test
		//			^
		
		suffix.decrement();
		suffix.decrement();
		suffix.decrement();
		suffix.decrement();
		//takes us to 
		//			Suffix Test
		//    		    ^
		assertThat(suffix.isEmpty(), describedAs("suffix.isEmpty <true>",is(true)));
		assertThat(suffix.getEndItem(), is(nullValue()));
		assertThat(suffix.getStart(), is(nullValue()));
		assertThat(suffix.getRemaining(), is(0));
		assertThat(suffix.getEndPosition(), is(4));
		
		suffix.increment();
		suffix.increment();
		suffix.increment();
		suffix.increment();
		//takes us to 
		//			Suffix Test
		//    		    [   |
		assertThat(suffix.isEmpty(), describedAs("suffix.isEmpty <false>",is(false)));
		assertThat((Character)suffix.getEndItem(), is('T'));
		assertThat((Character)suffix.getStart(), is('i'));
		assertThat(suffix.getRemaining(), is(4));
		assertThat(suffix.getEndPosition(), is(8));
		
		suffix.increment();
		//takes us to 
		//			Suffix Test
		//              [    |
		assertThat(suffix.isEmpty(), describedAs("suffix.isEmpty <fasle>",is(false)));
		assertThat((Character)suffix.getEndItem(), is('e'));
		assertThat((Character)suffix.getStart(), is('i'));
		assertThat(suffix.getRemaining(), is(5));
		assertThat(suffix.getEndPosition(), is(9));
	}
	
	@Test
	public void canRetrieveTerminal() {
		Word word = new Word("Suffix Test");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		Suffix<Character, Word> suffix = new Suffix<Character, Word>(0,0,sequence);
		//takes us to:
		//          Suffix Test$
		//			^
		for(int pos = 0; pos < 11; pos++)
			suffix.decrement();
		//takes us to:
		//          Suffix Test$
		//			           ^
		
		assertThat(suffix.isEmpty(), describedAs("suffix.isEmpty <true>",is(true)));
		
		suffix.increment();
		//takes us to:
		//          Suffix Test$
		//			           [|
		//assertThat(suffix.getEndItem(), isA(SequenceTerminal.class));
		assertThat(suffix.getEndItem(), is(instanceOf(SequenceTerminal.class)));
		assertThat(suffix.getRemaining(), is(1));
	}
	
	@Test
	public void gettingEndOfEmptySuffixReturnsNull() {
		Word word = new Word("Suffix Test");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		Suffix<Character, Word> suffix = new Suffix<Character, Word>(0,0,sequence);
		assertThat(suffix.getEndItem(), is(nullValue()));
		suffix.increment();
		assertThat(suffix.getEndItem(), is(notNullValue()));
	}
	
	@Test
	public void gettingEndWhenEndIsAtZeroReturnsNull() {
		Word word = new Word("Suffix Test");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		Suffix<Character, Word> suffix = new Suffix<Character, Word>(0,0,sequence);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void incrementingPastEndThrowsOutOfBoundsException(){
		Word word = new Word("Suffix Test");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		Suffix<Character, Word> suffix = new Suffix<Character, Word>(0,0,sequence);
		for(int i = 0; i < word.word.length; i++)
			suffix.increment();
		suffix.increment();
		suffix.increment();
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void deccrementingPastEndThrowsOutOfBoundsException(){
		Word word = new Word("Suffix Test");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		Suffix<Character, Word> suffix = new Suffix<Character, Word>(0,0,sequence);
		for(int i = 0; i < word.word.length; i++)
			suffix.decrement();
		suffix.decrement();
		suffix.decrement();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void cannotInitializeSuffixWithNegativeValues(){
		Word word = new Word("Suffix Test");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		Suffix<Character, Word> suffix = new Suffix<Character, Word>(-1,-1,sequence);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void cannotInitializeSuffixWithEndLessThatStart(){
		Word word = new Word("Suffix Test");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		Suffix<Character, Word> suffix = new Suffix<Character, Word>(1,0,sequence);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void cannotInitializeSuffixWithEndBeyondSequenceLength(){
		Word word = new Word("Suffix Test");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		Suffix<Character, Word> suffix = new Suffix<Character, Word>(1,13,sequence);
	}
	
	@Test
	public void getItemXFromEndWorksWithLegalValues(){
		Word word = new Word("Suffix Test");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		Suffix<Character, Word> suffix = new Suffix<Character, Word>(0,12,sequence);
		assertThat((Character)suffix.getItemXFromEnd(12), is('S'));
		assertThat((Character)suffix.getItemXFromEnd(2), is('t'));
		assertThat(suffix.getItemXFromEnd(1), is(instanceOf(SequenceTerminal.class)));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getItemXFromEndFailsWhenDistanceFromEndOverlapsStart(){
		Word word = new Word("Suffix Test");
		Sequence<Character, Word> sequence = new Sequence<Character, Word>(word);
		Suffix<Character, Word> suffix = new Suffix<Character, Word>(1,12,sequence);
		assertThat((Character)suffix.getItemXFromEnd(12), is('S'));
	}
}
