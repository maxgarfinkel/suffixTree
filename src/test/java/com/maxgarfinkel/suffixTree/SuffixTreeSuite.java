package com.maxgarfinkel.suffixTree;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SequenceTest.class, 
				SuffixTest.class,
				SuffixTreeTest.class, 
				GeneralisedSuffixTreeTests.class,
				CursorTest.class})

public class SuffixTreeSuite {

	private static Logger logger = Logger.getLogger(SuffixTreeSuite.class);
	
	@BeforeClass
	public static void setup() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		logger.setLevel(Level.DEBUG);
		logger.debug("Suffix Tree Test Suite.");
	}
	
}
