package com.maxgarfinkel.suffixTree;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class SuffixTreeTest {

	Logger logger = Logger.getRootLogger();

	@Before
	public void setUp() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		logger.setLevel(Level.DEBUG);
	}
	
	@Test
	public void suffixTreeDefaultConstructorOnlyRootNodeIsConstructed() {
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>();
		Node<Character, Word>  root = tree.getRoot();
		assertThat(root, is(notNullValue()));
		assertThat(root.getEdgeCount(), is(0));
		assertThat(root.toString(), is("root"));
	}

	@Test
	public void suffixTreeConstructorWithSequence() {
		
		/*	Tree should look like this:
		 * 					  root
		 * 				  		|
		 * 		+---------+-----+---+---------+
		 * 		a		  b	        c		  $
		 * 		b		  c			$
		 * 		c		  $
		 * 		$
		 */
		
		Word word = new Word("abc");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		logger.debug("suffix Tree Constructor With Sequence");
		logger.debug(tree);
		
		TestUtils.everySuffixReachableFromRoot(word, tree);
		
		Node<Character, Word> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(4));
		
		Edge<Character, Word> edgeA = root.getEdgeStarting('a');
		assertThat(edgeA.getLength(), is(4));
		assertThat(edgeA.isTerminating(), is(false));
		assertThat(edgeA.isStarting('a'), is(true));
		assertThat(edgeA.isStarting('b'), is(false));
		
		Edge<Character, Word> edgeB = root.getEdgeStarting('b');
		assertThat(edgeB.getLength(), is(3));
		assertThat(edgeB.isTerminating(), is(false));
		assertThat(edgeB.isStarting('b'), is(true));
		assertThat(edgeB.isStarting('a'), is(false));
		
		Edge<Character, Word> edgeC = root.getEdgeStarting('c');
		assertThat(edgeC.getLength(), is(2));
		assertThat(edgeC.isTerminating(), is(false));
		assertThat(edgeC.isStarting('c'), is(true));
		assertThat(edgeC.isStarting('b'), is(false));
		
		//Edge starting with a sequence terminal.... 
		//Not really sure we should be doing this...
		SequenceTerminal<Word> terminal = new SequenceTerminal<Word>(word);
		Edge<Character, Word> edgeWord = root.getEdgeStarting(terminal);
		assertThat(edgeWord.getLength(), is(1));
		assertThat(edgeWord.isTerminating(), is(false));
		
		SequenceTerminal<Word> end = new SequenceTerminal<Word>(word);
		assertThat(edgeWord.isStarting(end), is(true));
	}

	@Test
	public void rootNotNull() {
		logger.debug("Test Root Not Null");
		String[] sequence = new String[] { "a" };
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(Arrays.asList(sequence));
		assertThat(tree.getRoot(), is(notNullValue()));
	}

	@Test
	public void generatesSimplestTree() {
		logger.debug("Test Generates Simplest Tree");
		
		Word word = new Word("abc");
		SequenceTerminal<Word> terminal = new SequenceTerminal<Word>(word);
		SuffixTree<Character,Word> tree = new SuffixTree<Character,Word>(word);
		logger.debug(tree.toString());
		
		TestUtils.everySuffixReachableFromRoot(word, tree);
		
		assertThat(tree.getRoot().getEdgeCount(), is(4));

		Edge<Character, Word> edgeA = tree.getRoot().getEdgeStarting('a');
		assertThat(edgeA.getLength(), is(4));
		assertThat(edgeA.isTerminating(), is(false));
		assertThat(edgeA.getItemAt(0), is('a'));
		assertThat(edgeA.getItemAt(1), is('b'));
		assertThat(edgeA.getItemAt(2), is('c'));
		assertThat((Object) edgeA.getItemAt(3),
				is((Object) terminal));

		Edge<Character,Word> edgeB = tree.getRoot().getEdgeStarting('b');
		assertThat(edgeB.getLength(), is(3));
		assertThat(edgeB.isTerminating(), is(false));
		assertThat(edgeB.getItemAt(0), is('b'));
		assertThat(edgeB.getItemAt(1), is('c'));
		assertThat((Object) edgeB.getItemAt(2),
				is((Object) terminal));

		Edge<Character, Word> edgeC = tree.getRoot().getEdgeStarting('c');
		assertThat(edgeC.getLength(), is(2));
		assertThat(edgeC.isTerminating(), is(false));
		assertThat(edgeC.getItemAt(0), is('c'));
		assertThat(edgeC.getItemAt(1),
				is((Object) terminal));

		Edge<Character, Word> edgeLeaf = tree.getRoot().getEdgeStarting(
				terminal);
		assertThat(edgeLeaf.getLength(), is(1));
		assertThat(edgeLeaf.isTerminating(), is(false));
		assertThat(edgeLeaf.getItemAt(0),
				is((Object) terminal));
	}

	@Test
	public void simpleSplit() {
		/**
		 * Tree should look like:
		 * 
		 * 		   root
		 * 			|
		 * 		+---+---+
		 * 		a	b	$
		 * 	+---+	$
		 *  b	a
		 *  $	b
		 *  	$
		 */  
		logger.debug("Test Simple Split");
		Word word = new Word("aab");
		SequenceTerminal<Word> terminal = new SequenceTerminal<Word>(word);
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		logger.debug(tree);
		
		TestUtils.everySuffixReachableFromRoot(word, tree);
		
		Node<Character, Word> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(3));

		Edge<Character, Word> edgeA1 = root.getEdgeStarting('a');
		assertThat(edgeA1.isTerminating(), is(true));
		assertThat(edgeA1.getLength(), is(1));

		Node<Character, Word> nodeA1 = edgeA1.getTerminal();
		assertThat(nodeA1.getEdgeCount(), is(2));
		assertThat(nodeA1.getEdgeStarting('a'), is(notNullValue()));
		assertThat(nodeA1.getEdgeStarting('b'), is(notNullValue()));

		Edge<Character, Word> edgeAB = nodeA1.getEdgeStarting('a');
		assertThat(edgeAB.getLength(), is(3));
		assertThat(edgeAB.isTerminating(), is(false));
		assertThat(edgeAB.getItemAt(0), is('a'));
		assertThat(edgeAB.getItemAt(1), is('b'));
		assertThat((Object) edgeAB.getItemAt(2),
				is((Object) terminal));

		Edge<Character, Word> edgeB2 = nodeA1.getEdgeStarting('b');
		assertThat(edgeB2.getLength(), is(2));
		assertThat(edgeB2.isTerminating(), is(false));
		assertThat(edgeB2.getItemAt(0), is('b'));
		assertThat(edgeB2.getItemAt(1),
				is((Object) terminal));

		Edge<Character, Word> edgeB = root.getEdgeStarting('b');
		assertThat(edgeB.getLength(), is(2));
		assertThat(edgeB.isTerminating(), is(false));
		assertThat(edgeB.getItemAt(0), is('b'));
		assertThat(edgeB.getItemAt(1),
				is((Object) terminal));

		Edge<Character, Word> edgeLeaf = tree.getRoot().getEdgeStarting(
				terminal);
		assertThat(edgeLeaf.getLength(), is(1));
		assertThat(edgeLeaf.isTerminating(), is(false));
		assertThat(edgeLeaf.getItemAt(0),
				is((Object) terminal));
	}

	@Test
	public void simpleSuffixLinking() {
		logger.debug("Test Simple Suffix Linking");
		Word word = new Word("ababc");
		SequenceTerminal<Word> terminal = new SequenceTerminal<Word>(word);
		SuffixTree<Character,Word> tree = new SuffixTree<Character,Word>(word);
		TestUtils.everySuffixReachableFromRoot(word, tree);
		
		Node<Character,Word> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(4));

		Edge<Character,Word> edgeRoot_AB = root.getEdgeStarting('a');
		assertThat(edgeRoot_AB.isTerminating(), is(true));
		assertThat(edgeRoot_AB.getLength(), is(2));

		Node<Character,Word> node_AB = edgeRoot_AB.getTerminal();
		assertThat(node_AB.getEdgeCount(), is(2));

		Edge<Character,Word> edgeB = root.getEdgeStarting('b');
		assertThat(edgeB.getLength(), is(1));
		assertThat(edgeB.isTerminating(), is(true));
		assertThat(node_AB.getSuffixLink(), is(edgeB.getTerminal()));

		Edge<Character,Word> edgeABC = node_AB.getEdgeStarting('a');
		assertThat(edgeABC.getLength(), is(4));
		assertThat(edgeABC.isTerminating(), is(false));

		Edge<Character,Word> edgeC = node_AB.getEdgeStarting('c');
		assertThat(edgeC.getLength(), is(2));
		assertThat(edgeC.isTerminating(), is(false));

		Node<Character,Word> node_B = edgeB.getTerminal();
		assertThat(node_B.getEdgeCount(), is(2));

		edgeABC = node_B.getEdgeStarting('a');
		assertThat(edgeABC.getLength(), is(4));
		assertThat(edgeABC.isTerminating(), is(false));

		edgeC = node_B.getEdgeStarting('c');
		assertThat(edgeC.getLength(), is(2));
		assertThat(edgeC.isTerminating(), is(false));

		edgeC = root.getEdgeStarting('c');
		assertThat(edgeC.getLength(), is(2));
		assertThat(edgeC.isTerminating(), is(false));

		Edge<Character,Word> edgeLeaf = root.getEdgeStarting(terminal);
		assertThat(edgeLeaf.getLength(), is(1));
		assertThat(edgeLeaf.isTerminating(), is(false));
	}

	@Test
	public void followSuffixLinks() {
		logger.debug("Test Follow Suffix Links");
		Word word = new Word("ababcabad");
		SequenceTerminal<Word> terminal = new SequenceTerminal<Word>(word);
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		logger.debug("sequence = ababcabad");
		logger.debug(tree);
		
		TestUtils.everySuffixReachableFromRoot(word, tree);
		
		Node<Character, Word> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(5));

		// all edges from root->a
		Edge<Character, Word> edgeRoot_A = root.getEdgeStarting('a');
		assertThat(edgeRoot_A.isTerminating(), is(true));
		assertThat(edgeRoot_A.getLength(), is(1));

		Node<Character, Word> node_A = edgeRoot_A.getTerminal();
		assertThat(node_A.getEdgeCount(), is(2));

		Edge<Character, Word> edge_D = node_A.getEdgeStarting('d');
		assertThat(edge_D.getLength(), is(2));
		assertThat(edge_D.isTerminating(), is(false));

		Edge<Character, Word> edge_B = node_A.getEdgeStarting('b');
		assertThat(edge_B.getLength(), is(1));
		assertThat(edge_B.isTerminating(), is(true));

		Node<Character, Word> node_B = edge_B.getTerminal();
		assertThat(node_B.getEdgeCount(), is(2));

		Edge<Character, Word> edge_CABAD = node_B.getEdgeStarting('c');
		assertThat(edge_CABAD.getLength(), is(6));
		assertThat(edge_CABAD.isTerminating(), is(false));

		Edge<Character, Word> edge_A = node_B.getEdgeStarting('a');
		assertThat(edge_A.getLength(), is(1));
		assertThat(edge_A.isTerminating(), is(true));

		node_A = edge_A.getTerminal();
		assertThat(node_A.getEdgeCount(), is(2));

		Edge<Character, Word> edge_BCABAD = node_A.getEdgeStarting('b');
		assertThat(edge_BCABAD.getLength(), is(7));
		assertThat(edge_BCABAD.isTerminating(), is(false));

		edge_D = node_A.getEdgeStarting('d');
		assertThat(edge_D.getLength(), is(2));
		assertThat(edge_D.isTerminating(), is(false));

		// all edges from root->b
		Edge<Character, Word> edgeRoot_B = root.getEdgeStarting('b');
		assertThat(edgeRoot_B.isTerminating(), is(true));
		assertThat(edgeRoot_B.getLength(), is(1));

		node_B = edgeRoot_B.getTerminal();
		assertThat(node_B.getEdgeCount(), is(2));

		edge_A = node_B.getEdgeStarting('a');
		assertThat(edge_A.getLength(), is(1));
		assertThat(edge_A.isTerminating(), is(true));

		node_A = edge_A.getTerminal();
		assertThat(node_A.getEdgeCount(), is(2));

		edge_D = node_A.getEdgeStarting('d');
		assertThat(edge_D.getLength(), is(2));
		assertThat(edge_D.isTerminating(), is(false));

		edge_BCABAD = node_A.getEdgeStarting('b');
		assertThat(edge_BCABAD.getLength(), is(7));
		assertThat(edge_BCABAD.isTerminating(), is(false));

		edge_CABAD = node_B.getEdgeStarting('c');
		assertThat(edge_CABAD.getLength(), is(6));
		assertThat(edge_CABAD.isTerminating(), is(false));

		// all edges from root->c
		edge_CABAD = root.getEdgeStarting('c');
		assertThat(edge_CABAD.getLength(), is(6));
		assertThat(edge_CABAD.isTerminating(), is(false));

		// all edges from root->d
		edge_D = root.getEdgeStarting('d');
		assertThat(edge_D.getLength(), is(2));
		assertThat(edge_D.isTerminating(), is(false));

		// all edges from root->$
		Edge<Character, Word> edge_leaf = root.getEdgeStarting(terminal);
		assertThat(edge_leaf.getLength(), is(1));
		assertThat(edge_leaf.isTerminating(), is(false));

		// suffix links
		Node<Character, Word> linkA_to_A = root.getEdgeStarting('a').getTerminal()
				.getEdgeStarting('b').getTerminal().getEdgeStarting('a')
				.getTerminal().getSuffixLink();
		assertThat(linkA_to_A, is(root.getEdgeStarting('b').getTerminal()
				.getEdgeStarting('a').getTerminal()));

		Node<Character, Word> linkB_to_B = root.getEdgeStarting('a').getTerminal()
				.getEdgeStarting('b').getTerminal().getSuffixLink();
		assertThat(linkB_to_B, is(root.getEdgeStarting('b').getTerminal()));

		Node<Character, Word> linkA_to_A2 = root.getEdgeStarting('b').getTerminal()
				.getEdgeStarting('a').getTerminal().getSuffixLink();
		assertThat(linkA_to_A2, is(root.getEdgeStarting('a').getTerminal()));
	}

	@Test
	public void mississippi() {
		logger.debug("Test Mississippi");
		Word word = new Word("mississippi");
		SequenceTerminal<Word> terminal = new SequenceTerminal<Word>(word);
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		logger.debug("sequence = mississippi");
		logger.debug(Utils.printTreeForGraphViz(tree));

		TestUtils.everySuffixReachableFromRoot(word, tree);
		
		Node<Character, Word> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(5));

		Edge<Character, Word> m = root.getEdgeStarting('m');
		Edge<Character, Word> i = root.getEdgeStarting('i');
		Edge<Character, Word> s = root.getEdgeStarting('s');
		Edge<Character, Word> p = root.getEdgeStarting('p');
		Edge<Character, Word> leaf = root
				.getEdgeStarting(terminal);

		assertThat(m.getLength(), is(12));
		assertThat(i.getLength(), is(1));
		assertThat(s.getLength(), is(1));
		assertThat(p.getLength(), is(1));
		assertThat(leaf.getLength(), is(1));

	}

	/**
	 * Tests the case where a suffix link is followed but the active length is
	 * longer than the length of the active edge. In this case the system must
	 * traverse down the tree correcting the active point until it sits at the
	 * correct place.
	 * 
	 * In this case the suffix tree created should look like this : <img
	 * src="dedododeeodo.png" /> <strong>NB:</strong>Suffix links are not shown.
	 * 
	 * @throws Exception
	 */
	@Test
	public void followSuffixLinkWhereLengthIsWrong() {
		logger.debug("Test Follow Suffix Link Where Length is Wrong");
		Word word = new Word("dedododeeodo");
		SequenceTerminal<Word> terminal = new SequenceTerminal<Word>(word);
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		logger.debug("sequence = dedododeeodo");
		logger.debug(Utils.printTreeForGraphViz(tree));

		TestUtils.everySuffixReachableFromRoot(word, tree);
		
		Node<Character, Word> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(4));

		Edge<Character, Word> root_e = root.getEdgeStarting('e');
		assertThat(root_e.getLength(), is(1));

		Edge<Character, Word> root_d = root.getEdgeStarting('d');
		assertThat(root_d.getLength(), is(1));

		Edge<Character, Word> root_o = root.getEdgeStarting('o');
		assertThat(root_o.getLength(), is(1));

		Edge<Character, Word> root_leaf = root.getEdgeStarting(terminal);
		assertThat(root_leaf.getLength(), is(1));

		// check problem edge.
		Edge<Character, Word> root_d_o = root_d.getTerminal().getEdgeStarting('o');
		assertThat(root_d_o.getLength(), is(1));
	}

	@Test
	public void checkAlmasamolmaz() {
		logger.debug("Test String Almasamolmaz");
		Word word = new Word("almasamolmaz");
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>(word);
		logger.debug(Utils.printTreeForGraphViz(tree));
		Node<Character, Word> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(7));
		TestUtils.everySuffixReachableFromRoot(word, tree);
		
	}

	@Test
	public void checkOoooooooo() {
		logger.debug("Test String Ooooooooo");
		Word word = new Word("ooooooooo");
		SuffixTree<Character,Word> tree = new SuffixTree<Character, Word>(word);
		logger.debug(Utils.printTreeForGraphViz(tree));
		TestUtils.everySuffixReachableFromRoot(word, tree);
		Node<Character, Word> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(2));
	}

	@Test
	public void checkAbcadak() {
		logger.debug("Test String Abcadak");
		Word word = new Word( "abcadak");
		SuffixTree<Character,Word> tree = new SuffixTree<Character,Word>(word);
		logger.debug(Utils.printTreeForGraphViz(tree));
		TestUtils.everySuffixReachableFromRoot(word, tree);
		Node<Character,Word> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(6));
	}

	@Test
	public void checkAbcdefabxybcdmnabcdex() {
		logger.debug("Test String Abcdefabxybcdmnabcdex");
		Word word = new Word("abcdefabxybcdmnabcdex");
		SuffixTree<Character,Word> tree = new SuffixTree<Character,Word>(word);
		logger.debug(Utils.printTreeForGraphViz(tree));
		TestUtils.everySuffixReachableFromRoot(word, tree);
		Node<Character, Word> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(11));
	}
	
	@Test
	public void checkEmptyString() {
		logger.debug("Test Empty String");
		String[] sequence = new String[] {};
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(Arrays.asList(sequence));
		tree.add(new ArrayList<String>());
		logger.debug(Utils.printTreeForGraphViz(tree));

		Node<String,List<String>> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(1));
	}
	
	@Test
	public void emptyEdgeWithRemainingSuffixHandled(){
		logger.debug("reaminders Are Handled When Active Length Is Zero 2");
		Word word = new Word("abagbcbaebabagc");
		SuffixTree<Character,Word> tree = new SuffixTree<Character, Word>();
		tree.add(word);
		logger.debug(Utils.printTreeForGraphViz(tree, true));
		
		TestUtils.everySuffixReachableFromRoot(word, tree);
		
	}
}
