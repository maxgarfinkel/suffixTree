package com.maxgarfinkel.suffixTree;

import java.util.HashMap;
import java.util.Map;

public class WordCache {

	Map<Word,Word> cache = new HashMap<Word, Word>();
	
	Word get(Word word){
		if(cache.containsKey(word))
			return cache.get(word);
		else{
			cache.put(word, word);
			return word;
		}
	}
	
}
