package com.maxgarfinkel.suffixTree;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class AlgorithmsTest {

	Logger logger = Logger.getRootLogger();
	
	@Before
	public void setUp(){
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		logger.setLevel(Level.DEBUG);
	}

	@Test
	public void containsSuffix(){
		logger.debug("testContainsAllSuffixs");
		String[] sequence = new String[]{"a","a","b","a","a","c"};	
		SuffixTree<String> tree = new SuffixTree<String>(sequence);
		
		logger.debug(Utils.printTreeForGraphViz(tree));
		
		Algorithms algo = new Algorithms();
		logger.debug("Tree contains suffixes:");
		for(int i = 0; i < sequence.length; i++){
			String[] suffix = Arrays.copyOfRange(sequence, i, sequence.length);
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix, tree), is(true));
		}
		logger.debug("Tree does not contains suffixs:");
		for(int i = sequence.length-1; i > 0; --i){
			String[] suffix = Arrays.copyOfRange(sequence, 0, i);
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix, tree), is(false));
		}
	}
	
	@Test
	public void containsSuffix2(){
		logger.debug("testContainsAllSuffixs");
		String[] sequence = new String[]{"a", "b", "a", "b", "c", "a", "b", "a", "d"};	
		SuffixTree<String> tree = new SuffixTree<String>(sequence);
		
		logger.debug(Utils.printTreeForGraphViz(tree));
		
		Algorithms algo = new Algorithms();
		logger.debug("Tree contains suffixes:");
		for(int i = 0; i < sequence.length; i++){
			String[] suffix = Arrays.copyOfRange(sequence, i, sequence.length);
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix, tree), is(true));
		}
		logger.debug("Tree does not contains suffixs:");
		for(int i = sequence.length-1; i > 0; --i){
			String[] suffix = Arrays.copyOfRange(sequence, 0, i);
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix, tree), is(false));
		}
	}
	
	@Test
	public void containsSuffix3(){
		logger.debug("testContainsAllSuffixs");
		String[] sequence = new String[]{"m","i","s","s","i","s","s","i","p","p","i"};	
		SuffixTree<String> tree = new SuffixTree<String>(sequence);
		
		logger.debug(Utils.printTreeForGraphViz(tree));
		
		Algorithms algo = new Algorithms();
		logger.debug("Tree contains suffixes:");
		for(int i = 0; i < sequence.length; i++){
			String[] suffix = Arrays.copyOfRange(sequence, i, sequence.length);
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix, tree), is(true));
		}
		logger.debug("Tree does not contains suffixs:");
		for(int i = sequence.length-1; i > 0; --i){
			String[] suffix = Arrays.copyOfRange(sequence, 0, i);
			logger.debug(printArray(suffix));
			assertThat(algo.containsSuffix(suffix, tree), is(false));
		}
	}

	private String printArray(Object[] array){
		StringBuilder sb = new StringBuilder("[");
		for(Object item : array){
			sb.append(item).append(",");
		}
		sb.replace(sb.length()-1, sb.length(), "]");
		return sb.toString();
	}
}
