package com.maxgarfinkel.suffixTree;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class GeneralisedSuffixTreeTests {

	private static Logger LOGGER = Logger.getLogger(GeneralisedSuffixTreeTests.class);
	
	@Before
	public void setUp() throws Exception {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		LOGGER.setLevel(Level.DEBUG);
	}

	@Test
	public void twoSimpleStringsWithSharedCharactersProcessedCorrectly(){
		LOGGER.debug("two Simple Strings With Shared Characters Processed Correctly");
		Word word1 = new Word("abc");
		Word word2 = new Word("acb");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word1);
		tree.add(word2);
		
		LOGGER.debug(tree);
		TestUtils.everySuffixReachableFromRoot(word1, tree);
		TestUtils.everySuffixReachableFromRoot(word2, tree);
		
		Node<Character, Word> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(5));
		
		Edge<Character, Word> edgeA1 = root.getEdgeStarting('a');
		Edge<Character, Word> edgeB1 = root.getEdgeStarting('b');
		Edge<Character, Word> edgeC1 = root.getEdgeStarting('c');
		
		assertThat(edgeA1.getLength(), is(1));
		assertThat(edgeB1.getLength(), is(1));
		assertThat(edgeC1.getLength(), is(1));
		
		assertThat(edgeA1.getTerminal().getEdgeCount(), is(2));
		assertThat(edgeB1.getTerminal().getEdgeCount(), is(2));
		assertThat(edgeC1.getTerminal().getEdgeCount(), is(2));
		
	}
	
	@Test
	public void repeatedSequencesRequiringCanonizationAreHandled(){
		LOGGER.debug("Test simple custom types are accepted");
		//Word word1 = new Word("mississippi");
		//Word word2 = new Word("ppippimipimim");
		Word word1 = new Word("mis");
		Word word2 = new Word("mim");
		SuffixTree<Character,Word> tree = new SuffixTree<Character, Word>();
		tree.add(word1);
		tree.add(word2);
		
		LOGGER.debug(Utils.printTreeForGraphViz(tree, false));
		TestUtils.everySuffixReachableFromRoot(word1, tree);
		TestUtils.everySuffixReachableFromRoot(word2, tree);
		
		Node<Character, Word> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(5));

		Edge<Character, Word> edgeM = root.getEdgeStarting('m');
		assertThat(edgeM, is(notNullValue()));
		int edgeMChildCount = edgeM.getTerminal().getEdgeCount();
		assertThat(edgeMChildCount, is(2));
		Edge<Character, Word> edgeI = edgeM.getTerminal().getEdgeStarting('i');
		assertThat(edgeI.isTerminating(), is(true));
		assertThat(edgeI.getTerminal().getEdgeCount(), is(2));
		
	}
	
	@Test
	public void repeatedLettersDontGetDuplicated(){
		LOGGER.debug("Repeated Letters Dont Get Duplicated");
		Word word1 = new Word("mis");
		Word word2 = new Word("m");
		Word word3 = new Word("im");
		Word word4 = new Word("mipimim");
		SuffixTree<Character,Word> tree = new SuffixTree<Character, Word>();
		tree.add(word1);
		tree.add(word2);
		tree.add(word3);
		tree.add(word4);
		
		LOGGER.debug(Utils.printTreeForGraphViz(tree, false));
		TestUtils.everySuffixReachableFromRoot(word1, tree);
		TestUtils.everySuffixReachableFromRoot(word2, tree);
		TestUtils.everySuffixReachableFromRoot(word3, tree);
		TestUtils.everySuffixReachableFromRoot(word4, tree);
		
		Node<Character, Word> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(8));
		
		Edge<Character, Word> edgeM = root.getEdgeStarting('m');
		assertThat(edgeM, is(notNullValue()));
		int edgeMChildCount = edgeM.getTerminal().getEdgeCount();
		assertThat(edgeMChildCount, is(4));
		assertThat(edgeM.getLength(), is(1));
		
	}
	
	@Test
	public void reamindersAreHandledWhenActiveLengthIsZero(){
		LOGGER.debug("reaminders Are Handled When Active Length Is Zero");
		Word word1 = new Word("im");
		Word word2 = new Word("imim");
		Word word3 = new Word("imimi");
		//Word word4 = new Word("imim");
		SuffixTree<Character,Word> tree = new SuffixTree<Character, Word>();
		tree.add(word1);
		tree.add(word2);
		tree.add(word3);
		LOGGER.debug(Utils.printTreeForGraphViz(tree, false));
		TestUtils.everySuffixReachableFromRoot(word1, tree);
		TestUtils.everySuffixReachableFromRoot(word2, tree);
		TestUtils.everySuffixReachableFromRoot(word3, tree);
		Node<Character, Word> root = tree.getRoot();
		Edge<Character, Word> edgeM = root.getEdgeStarting('m');
		int edgeMChildCount = edgeM.getTerminal().getEdgeCount();
		assertThat(edgeMChildCount, is(3));
		assertThat(edgeM.getLength(), is(1));
	}
	
	
	@Test
	public void insertsAllSuffixesWhenThereAreMulitpleRepetitions(){
		LOGGER.debug("Test simple custom types are accepted");
		Word word1 = new Word("mis");
		Word word2 = new Word("sim");
		Word word3 = new Word("isim");
		Word word4 = new Word("imim");
		SuffixTree<Character,Word> tree = new SuffixTree<Character, Word>();
		
		tree.add(word1);
		LOGGER.debug(Utils.printTreeForGraphViz(tree, false));
		tree.add(word2);
		LOGGER.debug(Utils.printTreeForGraphViz(tree, false));
		Node<Character, Word> root = tree.getRoot();
		Edge<Character, Word> edgeM = root.getEdgeStarting('m');
		int edgeMChildCount = edgeM.getTerminal().getEdgeCount();
		assertThat(edgeMChildCount, is(2));
		assertThat(edgeM.getLength(), is(1));
		
		tree.add(word3);
		LOGGER.debug(Utils.printTreeForGraphViz(tree, false));
		edgeM = root.getEdgeStarting('m');
		edgeMChildCount = edgeM.getTerminal().getEdgeCount();
		assertThat(edgeMChildCount, is(3));
		assertThat(edgeM.getLength(), is(1));
		
		tree.add(word4);
		LOGGER.debug(Utils.printTreeForGraphViz(tree, false));
		root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(7));
		
		edgeM = root.getEdgeStarting('m');
		edgeMChildCount = edgeM.getTerminal().getEdgeCount();
		assertThat(edgeMChildCount, is(4));
		assertThat(edgeM.getLength(), is(1));
		
		TestUtils.everySuffixReachableFromRoot(word1, tree);
		TestUtils.everySuffixReachableFromRoot(word2, tree);
		TestUtils.everySuffixReachableFromRoot(word3, tree);
		TestUtils.everySuffixReachableFromRoot(word4, tree);
	}
	
	@Test
	public void simpleCustomTypesAreAccepted(){
		LOGGER.debug("Test simple custom types are accepted");
		Word word1 = new Word("mississippi");
		Word word2 = new Word("ippississim");
		Word word3 = new Word("hippyippyisim");
		Word word4 = new Word("ppippimipimim");
		SuffixTree<Character,Word> tree = new SuffixTree<Character, Word>();
		tree.add(word1);
		tree.add(word2);
		tree.add(word3);
		tree.add(word4);
		
		LOGGER.debug(Utils.printTreeForGraphViz(tree, true));
		TestUtils.everySuffixReachableFromRoot(word1, tree);
		TestUtils.everySuffixReachableFromRoot(word2, tree);
		TestUtils.everySuffixReachableFromRoot(word3, tree);
		TestUtils.everySuffixReachableFromRoot(word4, tree);
		Node<Character, Word> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(10));
		
		Edge<Character, Word> nullEdge = root.getEdgeStarting(null);
		assertThat(nullEdge, is(nullValue()));
		
		LOGGER.debug("All of roots edges.");
		for(Edge<Character, Word> edge : root){
			LOGGER.debug(edge);
		}
		
		Edge<Character, Word> edgeStartingH = root.getEdgeStarting('h');

		Edge<Character, Word> edgeM = root.getEdgeStarting('m');
		assertThat(edgeM, is(notNullValue()));
		int edgeMChildCount = edgeM.getTerminal().getEdgeCount();
		assertThat(edgeMChildCount, is(4));
		
	}

}
