package com.maxgarfinkel.suffixTree;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;



public class SuffixTreeTest {

	Logger logger = Logger.getRootLogger();
	
	@Before
	public void setUp() throws Exception {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		logger.setLevel(Level.DEBUG);
	}

	@Test
	public void rootNotNull() throws Exception{
		logger.debug("testRootNotNull");
		String[] sequence = new String[]{"a"};
		SuffixTree<String> tree = new SuffixTree<String>(sequence);
		assertThat(tree.getRoot(), is(notNullValue()));
	}
}
