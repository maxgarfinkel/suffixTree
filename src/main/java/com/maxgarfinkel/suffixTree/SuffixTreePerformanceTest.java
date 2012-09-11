package com.maxgarfinkel.suffixTree;

import java.text.NumberFormat;
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

	private static final int multiplier = 10000000;
	Logger logger = Logger.getRootLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		SuffixTreePerformanceTest app = new SuffixTreePerformanceTest();
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
