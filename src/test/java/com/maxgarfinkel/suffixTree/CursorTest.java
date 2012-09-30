package com.maxgarfinkel.suffixTree;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsEmptyCollection.*;
import static org.hamcrest.collection.IsCollectionWithSize.*;
import static org.hamcrest.collection.IsIn.*;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class CursorTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void proceedToFindsCorrectEdgeFromRoot() {
		Word word = new Word("abc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		assertThat(cursor.proceedTo('a'), is(true));
	}
	
	@Test
	public void proceedToHandlesCharacterNotFoundAtRoot() {
		Word word = new Word("abc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		assertThat(cursor.proceedTo('d'), is(false));
	}
	
	@Test
	public void proceedToWalksDownEdge() {
		Word word = new Word("abc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		cursor.proceedTo('a');
		assertThat(cursor.proceedTo('b'), is(true));
	}
	
	@Test
	public void proceedToHandlesCharacterNotFoundOnEdge() {
		Word word = new Word("abc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		cursor.proceedTo('a');
		assertThat(cursor.proceedTo('d'), is(false));
	}
	
	@Test
	public void proceedToWalksEdgeOfLengthOneAndHandlesBranch() {
		Word word = new Word("ababc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		cursor.proceedTo('b');
		assertThat(cursor.proceedTo('c'), is(true));
	}
	
	@Test
	public void proceedToWalksEdgeOfLengthTwoAndHandlesBranch() {
		Word word = new Word("ababc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		cursor.proceedTo('a');
		cursor.proceedTo('b');
		assertThat(cursor.proceedTo('c'), is(true));
	}
	
	@Test
	public void proceedToWalksEdgeOfLengthOneAndHandlesBranchWhereItemNotFound() {
		Word word = new Word("ababc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		cursor.proceedTo('b');
		assertThat(cursor.proceedTo('b'), is(false));
	}
	
	@Test
	public void proceedToWalksEdgeOfLengthTwoAndHandlesBranchWhereItemNotFound() {
		Word word = new Word("ababc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		cursor.proceedTo('a');
		cursor.proceedTo('b');
		assertThat(cursor.proceedTo('b'), is(false));
	}
	
	@Test
	public void getSequenceTerminalsReturnsEmptyCollectionWhenOnInternalPoint() {
		Word word = new Word("ababc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		cursor.proceedTo('a');
		Collection<SequenceTerminal<Word>> termCollection = cursor.getSequenceTerminals();
		assertThat(termCollection, is(empty()));
	}
	
	@Test
	public void getSequenceTerminalsReturnsCorrectSequenceTerminalsWhenAtRoot() {
		Word word = new Word("ababc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		Collection<SequenceTerminal<Word>> termCollection = cursor.getSequenceTerminals();
		SequenceTerminal<Word> terminal = new SequenceTerminal<Word>(word);
		assertThat(terminal, isIn(termCollection));
		assertThat(termCollection, hasSize(1));
	}
	
	@Test
	public void getSequenceTerminalsReturnsCorrectSequenceTerminalsWhenOnEdge() {
		Word word = new Word("ababc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		cursor.proceedTo('c');
		Collection<SequenceTerminal<Word>> termCollection = cursor.getSequenceTerminals();
		SequenceTerminal<Word> terminal = new SequenceTerminal<Word>(word);
		assertThat(terminal, isIn(termCollection));
		assertThat(termCollection, hasSize(1));
	}
	
	@Test
	public void getSequenceTerminalsReturnsEmptyCollectionWhenOnInternalNode() {
		Word word = new Word("ababc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		cursor.proceedTo('b');
		Collection<SequenceTerminal<Word>> termCollection = cursor.getSequenceTerminals();
		assertThat(termCollection, is(empty()));
	}
	
	@Test
	public void getSequenceTerminalsReturnsEmptyCollectionWhenOnInternalEdge() {
		Word word = new Word("ababc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		cursor.proceedTo('a');
		Collection<SequenceTerminal<Word>> termCollection = cursor.getSequenceTerminals();
		assertThat(termCollection, is(empty()));
	}
	
	@Test
	public void getSequenceTerminalsReturnsEmptyCollectionWhenNotAtEndOfValidEdge() {
		Word word = new Word("ababc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		cursor.proceedTo('a');
		cursor.proceedTo('b');
		cursor.proceedTo('a');
		cursor.proceedTo('b');
		Collection<SequenceTerminal<Word>> termCollection = cursor.getSequenceTerminals();
		assertThat(termCollection, is(empty()));
	}
	
	@Test
	public void getSequenceTerminalsFindSequenceTerminalAfterBranchingWalk() {
		Word word = new Word("ababc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		cursor.proceedTo('a');
		cursor.proceedTo('b');
		cursor.proceedTo('a');
		cursor.proceedTo('b');
		cursor.proceedTo('c');
		Collection<SequenceTerminal<Word>> termCollection = cursor.getSequenceTerminals();
		SequenceTerminal<Word> terminal = new SequenceTerminal<Word>(word);
		assertThat(terminal, isIn(termCollection));
		assertThat(termCollection, hasSize(1));
	}
	
	@Test
	public void getSequenceTerminalsFindSequenceTerminalsAtRoot() {
		Word word1 = new Word("ababc");
		Word word2 = new Word("babc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word1);
		tree.add(word2);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		Collection<SequenceTerminal<Word>> seqTerminals = cursor.getSequenceTerminals();
		SequenceTerminal<Word> term1 = new SequenceTerminal<Word>(word1);
		SequenceTerminal<Word> term2 = new SequenceTerminal<Word>(word2);
		assertThat(seqTerminals, containsInAnyOrder(term1, term2));
	}
	
	@Test
	public void getSequenceTerminalsFindSequenceTerminalsAtEndOfEdge() {
		Word word1 = new Word("ababc");
		Word word2 = new Word("babc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word1);
		tree.add(word2);
		Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
		cursor.proceedTo('c');
		Collection<SequenceTerminal<Word>> seqTerminals = cursor.getSequenceTerminals();
		SequenceTerminal<Word> term1 = new SequenceTerminal<Word>(word1);
		SequenceTerminal<Word> term2 = new SequenceTerminal<Word>(word2);
		assertThat(seqTerminals, containsInAnyOrder(term1, term2));
	}
}
