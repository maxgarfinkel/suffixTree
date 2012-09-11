package com.maxgarfinkel.suffixTree;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.*;
import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class AlgorithmsTest {

	Logger logger = Logger.getRootLogger();

	@Before
	public void setUp() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		logger.setLevel(Level.DEBUG);
	}

	@Test
	public void containsSuffix() {
		logger.debug("testContainsAllSuffixs");
		//String[] sequence = ;
		List<String> sequence = Arrays.asList(new String[] { "a", "a", "b", "a", "a", "c" });
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(sequence);

		logger.debug(Utils.printTreeForGraphViz(tree));

		Algorithms<String,List<String>> algo = new Algorithms<String,List<String>>(tree);
		logger.debug("Tree contains suffixes:");
		for (int i = 0; i < sequence.size(); i++) {
			List<String> suffix = sequence.subList(i, sequence.size());
					//Arrays.copyOfRange(sequence, i, sequence.size()));
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix), is(true));
			Collection<List<String>> sequences = algo.getSequencesContainingSuffix(suffix);
			assertThat(sequences.size(), is(1));
			assertThat(sequences, hasItem(sequence));
			algo.resetToRoot();
		}
		algo.resetToRoot();
		logger.debug("Tree does not contains suffixs:");
		for (int i = sequence.size() - 1; i > 0; --i) {
			List<String> suffix = sequence.subList(0, i);
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix), is(false));
			assertThat(algo.getSequencesContainingSuffix(suffix).isEmpty(), is(true));
			algo.resetToRoot();
		}
	}

	@Test
	public void containsSuffix2() {
		logger.debug("testContainsAllSuffixs");
		String[] sequence = new String[] { "a", "b", "a", "b", "c", "a", "b",
				"a", "d" };
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(Arrays.asList(sequence));

		logger.debug(Utils.printTreeForGraphViz(tree));

		Algorithms<String,List<String>> algo = new Algorithms<String,List<String>>(tree);
		logger.debug("Tree contains suffixes:");
		for (int i = 0; i < sequence.length; i++) {
			List<String> suffix = Arrays.asList(Arrays.copyOfRange(sequence, i, sequence.length));
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix), is(true));
		}
		logger.debug("Tree does not contains suffixs:");
		algo.resetToRoot();
		for (int i = sequence.length - 1; i > 0; --i) {
			List<String> suffix = Arrays.asList(Arrays.copyOfRange(sequence, 0, i));
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix), is(false));
		}
	}

	@Test
	public void containsSuffix3() {
		logger.debug("testContainsAllSuffixs");
		String[] sequence = new String[] { "m", "i", "s", "s", "i", "s", "s",
				"i", "p", "p", "i" };
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(Arrays.asList(sequence));

		logger.debug(Utils.printTreeForGraphViz(tree));

		Algorithms<String,List<String>> algo = new Algorithms<String,List<String>>(tree);
		logger.debug("Tree contains suffixes:");
		for (int i = 0; i < sequence.length; i++) {
			List<String> suffix = Arrays.asList(Arrays.copyOfRange(sequence, i, sequence.length));
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix), is(true));
		}
		logger.debug("Tree does not contains suffixs:");
		for (int i = sequence.length - 1; i > 0; --i) {
			List<String> suffix = Arrays.asList(Arrays.copyOfRange(sequence, 0, i));
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix), is(false));
		}
	}

	private String printArray(List<String> suffix) {
		StringBuilder sb = new StringBuilder("[");
		for (Object item : suffix) {
			sb.append(item).append(",");
		}
		sb.replace(sb.length() - 1, sb.length(), "]");
		return sb.toString();
	}
	
	@Test
	public void containsMultipleSequences() {
		logger.debug("Test Contains Multiple Sequences");
		
		List<String> sequence1 = Arrays.asList(new String[] { "a", "b","c" });
		List<String> sequence2 = Arrays.asList(new String[] { "p", "q", "r"});
		
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(sequence1);
		tree.add(sequence2);
		
		logger.debug(tree.getSequence());
		logger.debug(Utils.printTreeForGraphViz(tree));

		Algorithms<String,List<String>> algo = new Algorithms<String,List<String>>(tree);
		
		logger.debug("Tree contains suffixes of string 1:");
		for (int i = 0; i < sequence1.size(); i++) {
			List<String> suffix = sequence1.subList(i, sequence1.size());
			logger.debug(printArray(suffix));
			
			assertThat(algo.containsSuffix(suffix), is(true));
			Collection<List<String>> sequences = algo.getSequencesContainingSuffix(suffix);
			assertThat(sequences.size(), is(1));
			logger.debug("sequences:"+sequences);
			logger.debug("sequences2:"+sequences);
			assertThat(sequences, hasItem(sequence1));
			algo.resetToRoot();
		}
		algo.resetToRoot();
		logger.debug("Tree does not contains suffixs of string 1:");
		for (int i = sequence1.size() - 1; i > 0; --i) {
			List<String> suffix = sequence1.subList(0, i);
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix), is(false));
			assertThat(algo.getSequencesContainingSuffix(suffix).isEmpty(), is(true));
			algo.resetToRoot();
		}
		
		logger.debug("Tree contains suffixes of string 2:");
		for (int i = 0; i < sequence2.size(); i++) {
			List<String> suffix = sequence2.subList(i, sequence2.size());
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix), is(true));
			Collection<List<String>> sequences = algo.getSequencesContainingSuffix(suffix);
			assertThat(sequences.size(), is(1));
			logger.debug("sequences:"+sequences);
			logger.debug("sequences2:"+sequences);
			assertThat(sequences, hasItem(sequence2));
			algo.resetToRoot();
		}
		algo.resetToRoot();
		logger.debug("Tree does not contains suffixs of string 2:");
		for (int i = sequence2.size() - 1; i > 0; --i) {
			List<String> suffix = sequence2.subList(0, i);
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix), is(false));
			assertThat(algo.getSequencesContainingSuffix(suffix).isEmpty(), is(true));
			algo.resetToRoot();
		}
	}
	
	@Test
	public void containsMultipleSimilarSequences() {
		logger.debug("Test Contains Multiple Sequences");
		
		List<String> sequence1 = Arrays.asList(new String[] { "d", "e","d", "o", "d","o", "d","e", "e", "o","d" });
		List<String> sequence2 = Arrays.asList(new String[] { "d", "e","d", "o", "d","o", "d","e", "e", "o","o" });
		List<String> sequence3 = Arrays.asList(new String[] { "d", "e","d", "o", "d","o", "d","e", "e", "d","o" });
		List<String> sequence4 = Arrays.asList(new String[] { "d", "e","d", "o", "d","o", "d","e", "d", "e","d" });
		
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(sequence1);
		tree.add(sequence2);
		tree.add(sequence3);
		tree.add(sequence4);
		
		logger.debug(tree.getSequence());
		logger.debug(Utils.printTreeForGraphViz(tree));

		Algorithms<String,List<String>> algo = new Algorithms<String,List<String>>(tree);
		
		String[] pattern = new String[]{"d","e","d"};
		Collection<List<String>> result = algo.getSequencesContainingSuffix(Arrays.asList(pattern));
		assertThat(result.size(), is(1));
		for(List<String> item : result){
			int i = 0;
			for(String p : pattern){
				assertThat(item.get(i), is(p));
				i++;
			}
		}
		//String[] seq = result.toArray(new List<String>[]{});
		//assertThat(seq[0], is("d"));
		//assertThat(seq[1], is("e"));
		//assertThat(seq[2], is("d"));
		
		/*
		logger.debug("Tree contains suffixes of string 1:");
		for (int i = 0; i < sequence1.size(); i++) {
			List<String> suffix = sequence1.subList(i, sequence1.size());
			logger.debug(printArray(suffix));
			
			assertThat(algo.containsSuffix(suffix), is(true));
			Collection<List<String>> sequences = algo.getSequencesContainingSuffix(suffix);
			assertThat(sequences.size(), is(1));
			logger.debug("sequences:"+sequences);
			logger.debug("sequences2:"+sequences);
			assertThat(sequences, hasItem(sequence1));
			algo.resetToRoot();
		}
		algo.resetToRoot();
		logger.debug("Tree does not contains suffixs of string 1:");
		for (int i = sequence1.size() - 1; i > 0; --i) {
			List<String> suffix = sequence1.subList(0, i);
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix), is(false));
			assertThat(algo.getSequencesContainingSuffix(suffix).isEmpty(), is(true));
			algo.resetToRoot();
		}
		
		logger.debug("Tree contains suffixes of string 2:");
		for (int i = 0; i < sequence2.size(); i++) {
			List<String> suffix = sequence2.subList(i, sequence2.size());
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix), is(true));
			Collection<List<String>> sequences = algo.getSequencesContainingSuffix(suffix);
			assertThat(sequences.size(), is(1));
			logger.debug("sequences:"+sequences);
			logger.debug("sequences2:"+sequences);
			assertThat(sequences, hasItem(sequence2));
			algo.resetToRoot();
		}
		algo.resetToRoot();
		logger.debug("Tree does not contains suffixs of string 2:");
		for (int i = sequence2.size() - 1; i > 0; --i) {
			List<String> suffix = sequence2.subList(0, i);
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix), is(false));
			assertThat(algo.getSequencesContainingSuffix(suffix).isEmpty(), is(true));
			algo.resetToRoot();
		}
		*/
	}
	
	@Test
	public void containsMultipleSimilarSequences2() {
		logger.debug("Test Contains Multiple Sequences");
		
		List<String> sequence1 = Arrays.asList(new String[] { "g","o","o","d" });
		List<String> sequence2 = Arrays.asList(new String[] { "f","e","n","c","e","s" });
		List<String> sequence3 = Arrays.asList(new String[] { "m","a","k","e" });
		List<String> sequence4 = Arrays.asList(new String[] { "g","o","o","d" });
		List<String> sequence5 = Arrays.asList(new String[] { "n","e","i","g","h","b","o","r","s" });
		
		SuffixTree<String,List<String>> tree = new SuffixTree<String,List<String>>(sequence1);
		tree.add(sequence2);
		tree.add(sequence3);
		tree.add(sequence4);
		tree.add(sequence5);
		
		logger.debug(tree.getSequence());
		logger.debug(Utils.printTreeForGraphViz(tree));

		Algorithms<String,List<String>> algo = new Algorithms<String,List<String>>(tree);
	}
}
