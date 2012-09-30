package com.maxgarfinkel.suffixTree;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.apache.log4j.Logger;

public class TestUtils {

	static Logger LOGGER = Logger.getLogger(TestUtils.class);
	
	public static void everySuffixReachableFromRoot(Word word, SuffixTree<Character, Word> tree){
		char[] charArray = word.getCharArray();
		
		SequenceTerminal<Word> terminal = new SequenceTerminal<Word>(word);
		
		for(int i = charArray.length; i >= 0; i--){
			
			StringBuilder sb = new StringBuilder();
			Cursor<Character, Word> cursor = new Cursor<Character, Word>(tree);
			
			for(int j = i;j < charArray.length; j++){
				if(cursor.proceedTo(charArray[j]))
					sb.append(charArray[j]);
				else
					fail("could not find item " + charArray[j] + " in tree");
			}
			LOGGER.debug(sb.toString());
			Collection<SequenceTerminal<Word>> seqTermCollection = cursor.getSequenceTerminals();
			LOGGER.debug("sequenceTerm collection size" + seqTermCollection.size());
			assertThat(seqTermCollection, hasItem(terminal));
		}
	}
}
