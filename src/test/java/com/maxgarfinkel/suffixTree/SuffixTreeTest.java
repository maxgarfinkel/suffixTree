package com.maxgarfinkel.suffixTree;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
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
	public void rootNotNull() {
		logger.debug("Test Root Not Null");
		String[] sequence = new String[] { "a" };
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(Arrays.asList(sequence));
		assertThat(tree.getRoot(), is(notNullValue()));
	}

	@Test
	public void generatesSimplestTree() {
		logger.debug("Test Generates Simplest Tree");
		
		String[] sequence = new String[] { "a", "b", "c" };
		SequenceTerminal<List<String>> terminal = new SequenceTerminal<List<String>>(Arrays.asList(sequence));
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(Arrays.asList(sequence));
		logger.debug(tree.toString());
		assertThat(tree.getRoot().getEdgeCount(), is(4));

		Edge<String,List<String>> edgeA = tree.getRoot().getEdgeStarting("a");
		assertThat(edgeA.getLength(), is(4));
		assertThat(edgeA.isTerminating(), is(false));
		assertThat(edgeA.getItemAt(0), is("a"));
		assertThat(edgeA.getItemAt(1), is("b"));
		assertThat(edgeA.getItemAt(2), is("c"));
		assertThat((Object) edgeA.getItemAt(3),
				is((Object) terminal));

		Edge<String,List<String>> edgeB = tree.getRoot().getEdgeStarting("b");
		assertThat(edgeB.getLength(), is(3));
		assertThat(edgeB.isTerminating(), is(false));
		assertThat(edgeB.getItemAt(0), is("b"));
		assertThat(edgeB.getItemAt(1), is("c"));
		assertThat((Object) edgeB.getItemAt(2),
				is((Object) terminal));

		Edge<String,List<String>> edgeC = tree.getRoot().getEdgeStarting("c");
		assertThat(edgeC.getLength(), is(2));
		assertThat(edgeC.isTerminating(), is(false));
		assertThat(edgeC.getItemAt(0), is("c"));
		assertThat(edgeC.getItemAt(1),
				is((Object) terminal));

		Edge<String,List<String>> edgeLeaf = tree.getRoot().getEdgeStarting(
				terminal);
		assertThat(edgeLeaf.getLength(), is(1));
		assertThat(edgeLeaf.isTerminating(), is(false));
		assertThat(edgeLeaf.getItemAt(0),
				is((Object) terminal));
	}

	@Test
	public void simpleSplit() {
		logger.debug("Test Simple Split");
		String[] sequence = new String[] { "a", "a", "b" };
		SequenceTerminal<List<String>> terminal = new SequenceTerminal<List<String>>(Arrays.asList(sequence));
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(Arrays.asList(sequence));

		Node<String,List<String>> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(3));

		Edge<String,List<String>> edgeA1 = root.getEdgeStarting("a");
		assertThat(edgeA1.isTerminating(), is(true));
		assertThat(edgeA1.getLength(), is(1));

		Node<String,List<String>> nodeA1 = edgeA1.getTerminal();
		assertThat(nodeA1.getEdgeCount(), is(2));
		assertThat(nodeA1.getEdgeStarting("a"), is(notNullValue()));
		assertThat(nodeA1.getEdgeStarting("b"), is(notNullValue()));

		Edge<String,List<String>> edgeAB = nodeA1.getEdgeStarting("a");
		assertThat(edgeAB.getLength(), is(3));
		assertThat(edgeAB.isTerminating(), is(false));
		assertThat(edgeAB.getItemAt(0), is("a"));
		assertThat(edgeAB.getItemAt(1), is("b"));
		assertThat((Object) edgeAB.getItemAt(2),
				is((Object) terminal));

		Edge<String,List<String>> edgeB2 = nodeA1.getEdgeStarting("b");
		assertThat(edgeB2.getLength(), is(2));
		assertThat(edgeB2.isTerminating(), is(false));
		assertThat(edgeB2.getItemAt(0), is("b"));
		assertThat(edgeB2.getItemAt(1),
				is((Object) terminal));

		Edge<String,List<String>> edgeB = root.getEdgeStarting("b");
		assertThat(edgeB.getLength(), is(2));
		assertThat(edgeB.isTerminating(), is(false));
		assertThat(edgeB.getItemAt(0), is("b"));
		assertThat(edgeB.getItemAt(1),
				is((Object) terminal));

		Edge<String,List<String>> edgeLeaf = tree.getRoot().getEdgeStarting(
				terminal);
		assertThat(edgeLeaf.getLength(), is(1));
		assertThat(edgeLeaf.isTerminating(), is(false));
		assertThat(edgeLeaf.getItemAt(0),
				is((Object) terminal));
	}

	@Test
	public void simpleSuffixLinking() {
		logger.debug("Test Simple Suffix Linking");
		String[] sequence = new String[] { "a", "b", "a", "b", "c" };
		SequenceTerminal<List<String>> terminal = new SequenceTerminal<List<String>>(Arrays.asList(sequence));
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(Arrays.asList(sequence));

		Node<String,List<String>> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(4));

		Edge<String,List<String>> edgeRoot_AB = root.getEdgeStarting("a");
		assertThat(edgeRoot_AB.isTerminating(), is(true));
		assertThat(edgeRoot_AB.getLength(), is(2));

		Node<String,List<String>> node_AB = edgeRoot_AB.getTerminal();
		assertThat(node_AB.getEdgeCount(), is(2));

		Edge<String,List<String>> edgeB = root.getEdgeStarting("b");
		assertThat(edgeB.getLength(), is(1));
		assertThat(edgeB.isTerminating(), is(true));
		assertThat(node_AB.getSuffixLink(), is(edgeB.getTerminal()));

		Edge<String,List<String>> edgeABC = node_AB.getEdgeStarting("a");
		assertThat(edgeABC.getLength(), is(4));
		assertThat(edgeABC.isTerminating(), is(false));

		Edge<String,List<String>> edgeC = node_AB.getEdgeStarting("c");
		assertThat(edgeC.getLength(), is(2));
		assertThat(edgeC.isTerminating(), is(false));

		Node<String,List<String>> node_B = edgeB.getTerminal();
		assertThat(node_B.getEdgeCount(), is(2));

		edgeABC = node_B.getEdgeStarting("a");
		assertThat(edgeABC.getLength(), is(4));
		assertThat(edgeABC.isTerminating(), is(false));

		edgeC = node_B.getEdgeStarting("c");
		assertThat(edgeC.getLength(), is(2));
		assertThat(edgeC.isTerminating(), is(false));

		edgeC = root.getEdgeStarting("c");
		assertThat(edgeC.getLength(), is(2));
		assertThat(edgeC.isTerminating(), is(false));

		Edge<String,List<String>> edgeLeaf = root.getEdgeStarting(terminal);
		assertThat(edgeLeaf.getLength(), is(1));
		assertThat(edgeLeaf.isTerminating(), is(false));
	}

	@Test
	public void followSuffixLinks() {
		logger.debug("Test Follow Suffix Links");
		String[] sequence = new String[] { "a", "b", "a", "b", "c", "a", "b",
				"a", "d" };
		SequenceTerminal<List<String>> terminal = new SequenceTerminal<List<String>>(Arrays.asList(sequence));
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(Arrays.asList(sequence));
		// Should produce the following tree
		/*
		 * root |
		 * +-------------------------+-------+---------------+---------+--
		 * -------+ | -------- | | | | a <-/ -----\--------> b c d $ +----+----+
		 * / \ +------+------------+ a $ | | / \ | | b d b/ ---> \a c a $
		 * +------+--+ / +--+--------+ a d | | / | | b $ c a/ d b a a +--+--+ $
		 * c d b | | a $ a b d b d c $ a $ a d b $ a d $
		 */
		Node<String,List<String>> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(5));

		// all edges from root->a
		Edge<String,List<String>> edgeRoot_A = root.getEdgeStarting("a");
		assertThat(edgeRoot_A.isTerminating(), is(true));
		assertThat(edgeRoot_A.getLength(), is(1));

		Node<String,List<String>> node_A = edgeRoot_A.getTerminal();
		assertThat(node_A.getEdgeCount(), is(2));

		Edge<String,List<String>> edge_D = node_A.getEdgeStarting("d");
		assertThat(edge_D.getLength(), is(2));
		assertThat(edge_D.isTerminating(), is(false));

		Edge<String,List<String>> edge_B = node_A.getEdgeStarting("b");
		assertThat(edge_B.getLength(), is(1));
		assertThat(edge_B.isTerminating(), is(true));

		Node<String,List<String>> node_B = edge_B.getTerminal();
		assertThat(node_B.getEdgeCount(), is(2));

		Edge<String,List<String>> edge_CABAD = node_B.getEdgeStarting("c");
		assertThat(edge_CABAD.getLength(), is(6));
		assertThat(edge_CABAD.isTerminating(), is(false));

		Edge<String,List<String>> edge_A = node_B.getEdgeStarting("a");
		assertThat(edge_A.getLength(), is(1));
		assertThat(edge_A.isTerminating(), is(true));

		node_A = edge_A.getTerminal();
		assertThat(node_A.getEdgeCount(), is(2));

		Edge<String,List<String>> edge_BCABAD = node_A.getEdgeStarting("b");
		assertThat(edge_BCABAD.getLength(), is(7));
		assertThat(edge_BCABAD.isTerminating(), is(false));

		edge_D = node_A.getEdgeStarting("d");
		assertThat(edge_D.getLength(), is(2));
		assertThat(edge_D.isTerminating(), is(false));

		// all edges from root->b
		Edge<String,List<String>> edgeRoot_B = root.getEdgeStarting("b");
		assertThat(edgeRoot_B.isTerminating(), is(true));
		assertThat(edgeRoot_B.getLength(), is(1));

		node_B = edgeRoot_B.getTerminal();
		assertThat(node_B.getEdgeCount(), is(2));

		edge_A = node_B.getEdgeStarting("a");
		assertThat(edge_A.getLength(), is(1));
		assertThat(edge_A.isTerminating(), is(true));

		node_A = edge_A.getTerminal();
		assertThat(node_A.getEdgeCount(), is(2));

		edge_D = node_A.getEdgeStarting("d");
		assertThat(edge_D.getLength(), is(2));
		assertThat(edge_D.isTerminating(), is(false));

		edge_BCABAD = node_A.getEdgeStarting("b");
		assertThat(edge_BCABAD.getLength(), is(7));
		assertThat(edge_BCABAD.isTerminating(), is(false));

		edge_CABAD = node_B.getEdgeStarting("c");
		assertThat(edge_CABAD.getLength(), is(6));
		assertThat(edge_CABAD.isTerminating(), is(false));

		// all edges from root->c
		edge_CABAD = root.getEdgeStarting("c");
		assertThat(edge_CABAD.getLength(), is(6));
		assertThat(edge_CABAD.isTerminating(), is(false));

		// all edges from root->d
		edge_D = root.getEdgeStarting("d");
		assertThat(edge_D.getLength(), is(2));
		assertThat(edge_D.isTerminating(), is(false));

		// all edges from root->$
		Edge<String,List<String>> edge_leaf = root.getEdgeStarting(terminal);
		assertThat(edge_leaf.getLength(), is(1));
		assertThat(edge_leaf.isTerminating(), is(false));

		// suffix links
		Node<String,List<String>> linkA_to_A = root.getEdgeStarting("a").getTerminal()
				.getEdgeStarting("b").getTerminal().getEdgeStarting("a")
				.getTerminal().getSuffixLink();
		assertThat(linkA_to_A, is(root.getEdgeStarting("b").getTerminal()
				.getEdgeStarting("a").getTerminal()));

		Node<String,List<String>> linkB_to_B = root.getEdgeStarting("a").getTerminal()
				.getEdgeStarting("b").getTerminal().getSuffixLink();
		assertThat(linkB_to_B, is(root.getEdgeStarting("b").getTerminal()));

		Node<String,List<String>> linkA_to_A2 = root.getEdgeStarting("b").getTerminal()
				.getEdgeStarting("a").getTerminal().getSuffixLink();
		assertThat(linkA_to_A2, is(root.getEdgeStarting("a").getTerminal()));
	}

	@Test
	public void mississippi() {
		logger.debug("Test Mississippi");
		String[] sequence = new String[] { "m", "i", "s", "s", "i", "s", "s",
				"i", "p", "p", "i" };
		SequenceTerminal<List<String>> terminal = new SequenceTerminal<List<String>>(Arrays.asList(sequence));
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(Arrays.asList(sequence));
		logger.debug(Utils.printTreeForGraphViz(tree));

		Node<String,List<String>> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(5));

		Edge<String,List<String>> m = root.getEdgeStarting("m");
		Edge<String,List<String>> i = root.getEdgeStarting("i");
		Edge<String,List<String>> s = root.getEdgeStarting("s");
		Edge<String,List<String>> p = root.getEdgeStarting("p");
		Edge<String,List<String>> leaf = root
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
		String[] sequence = new String[] { "d", "e", "d", "o", "d", "o", "d",
				"e", "e", "o", "d", "o" };
		SequenceTerminal<List<String>> terminal = new SequenceTerminal<List<String>>(Arrays.asList(sequence));
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(Arrays.asList(sequence));
		logger.debug(Utils.printTreeForGraphViz(tree));

		Node<String,List<String>> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(4));

		Edge<String,List<String>> root_e = root.getEdgeStarting("e");
		assertThat(root_e.getLength(), is(1));

		Edge<String,List<String>> root_d = root.getEdgeStarting("d");
		assertThat(root_d.getLength(), is(1));

		Edge<String,List<String>> root_o = root.getEdgeStarting("o");
		assertThat(root_o.getLength(), is(1));

		Edge<String,List<String>> root_leaf = root.getEdgeStarting(terminal);
		assertThat(root_leaf.getLength(), is(1));

		// check problem edge.
		Edge<String,List<String>> root_d_o = root_d.getTerminal().getEdgeStarting("o");
		assertThat(root_d_o.getLength(), is(1));
	}

	@Test
	public void checkAlmasamolmaz() {
		logger.debug("Test String Almasamolmaz");
		String[] sequence = new String[] { "a", "l", "m", "a", "s", "a", "m",
				"o", "l", "m", "a", "z" };
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(Arrays.asList(sequence));
		logger.debug(Utils.printTreeForGraphViz(tree));

		Node<String,List<String>> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(7));
	}

	@Test
	public void checkOoooooooo() {
		logger.debug("Test String Ooooooooo");
		String[] sequence = new String[] { "o", "o", "o", "o", "o", "o", "o",
				"o", "o" };
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(Arrays.asList(sequence));
		logger.debug(Utils.printTreeForGraphViz(tree));

		Node<String,List<String>> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(2));
	}

	@Test
	public void checkAbcadak() {
		logger.debug("Test String Abcadak");
		String[] sequence = new String[] { "a", "b", "c", "a", "d", "a", "k" };
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(Arrays.asList(sequence));
		logger.debug(Utils.printTreeForGraphViz(tree));

		Node<String,List<String>> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(6));
	}

	@Test
	public void checkAbcdefabxybcdmnabcdex() {
		logger.debug("Test String Abcadak");
		String[] sequence = new String[] { "a", "b", "c", "d", "e", "f", "a",
				"b", "x", "y", "b", "c", "d", "m", "n", "a", "b", "c", "d",
				"e", "x" };
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(Arrays.asList(sequence));
		logger.debug(Utils.printTreeForGraphViz(tree));

		Node<String,List<String>> root = tree.getRoot();
		assertThat(root.getEdgeCount(), is(11));
	}
}
