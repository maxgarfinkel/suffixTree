package com.maxgarfinkel.suffixTree;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class SuffixTreePerformanceTest {

	private static final char[] symbols = new char[36];
	static {
		for (int idx = 0; idx < 10; ++idx)
			symbols[idx] = (char) ('0' + idx);
		for (int idx = 10; idx < 36; ++idx)
			symbols[idx] = (char) ('a' + idx - 10);
	}

	private static final int multiplier = 100000;
	Logger logger = Logger.getRootLogger();

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		SuffixTreePerformanceTest app = new SuffixTreePerformanceTest();
		app.testAgainstRealText();
	}

	private SuffixTreePerformanceTest() {
		logger.setLevel(Level.FATAL);
		Runtime runtime = Runtime.getRuntime();
		int stringSize = 10;
		System.out.println("Length (" + multiplier
				+ "'s), Time (s), Space Total (MB), Space Free(MB)");
		for (int i = 1; i < 11; i++) {

			RandomString rs = new RandomString(stringSize * (i * multiplier));
			Character[] s = rs.nextString();

			long allocatedMemoryStart = runtime.totalMemory()
					- runtime.freeMemory();

			long start = System.nanoTime();
			SuffixTree<Character,List<Character>> tree = new SuffixTree<Character,List<Character>>(Arrays.asList(s));
			long end = System.nanoTime() - start;

			long allocatedMemoryEnd = runtime.totalMemory()
					- runtime.freeMemory();

			long space = ((allocatedMemoryEnd - allocatedMemoryStart) / 1024) / 1024;
			double time = (((double) end / 1000d) / 1000d) / 1000d;

			System.out.println(stringSize * i + ", " + time + ", "
					+ (runtime.totalMemory() / 1024) / 1024 + ", "
					+ (runtime.freeMemory() / 1024) / 1024);

			tree = null;
		}
	}
	
public void testAgainstRealText() throws IOException{
		
		SuffixTree<Character, Word> tree = new SuffixTree<Character, Word>();
		
		
		//open file and read line by line
		URL fileUrl = this.getClass().getResource("summaTheologica.txt");
		FileInputStream fstream = new FileInputStream(fileUrl.getFile());

		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String strLine;

		//logger.setLevel(Level.INFO);
		int lineCount = 0;
		//Read File Line By Line
		
		long totalTime = System.nanoTime();
		long time = System.nanoTime();
		List<Long> timings = new ArrayList<Long>(80000);
		while ((strLine = br.readLine()) != null)   {
			lineCount++;
			time = System.nanoTime();
			for(String s : strLine.split(" ")){
			  Word word = new Word(strLine);
			  tree.add(word);	  
			}
			if(lineCount % 1000 == 0)
				timings.add(System.nanoTime());
		  
		}
		totalTime = System.nanoTime() - totalTime;
		System.out.print("time <- c(");
		for(Long i : timings){
			System.out.print(i + "," );
		}
		System.out.print(")\n");

		//Close the input stream
		in.close();
		logger.debug(lineCount + " lines imported in " + totalTime + " nano seconds.");
		//logger.info(tree.toString());
		
	}

	private void printMemoryUsage() {
		Runtime runtime = Runtime.getRuntime();
		NumberFormat format = NumberFormat.getInstance();
		StringBuilder sb = new StringBuilder();
		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		sb.append("free memory: " + format.format((freeMemory / 1024) / 1024)
				+ "MB\n");
		sb.append("allocated memory: "
				+ format.format((allocatedMemory / 1024) / 1024) + "MB\n");
		sb.append("max memory: " + format.format((maxMemory / 1024) / 1024)
				+ "MB\n");
		sb.append("total free memory: "
				+ format.format(((freeMemory + (maxMemory - allocatedMemory)) / 1024) / 1024)
				+ "MB\n");
		System.out.println(sb.toString());
	}

	private class RandomString {
		private final Random random = new Random();

		private final char[] buf;

		public RandomString(int length) {
			if (length < 1)
				throw new IllegalArgumentException("length < 1: " + length);
			buf = new char[length];
		}

		public Character[] nextString() {
			Character[] data = new Character[buf.length];
			for (int idx = 0; idx < buf.length; ++idx)
				data[idx] = symbols[random.nextInt(symbols.length)];
			return data;
		}
	}

}
