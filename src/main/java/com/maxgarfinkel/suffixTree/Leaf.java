package com.maxgarfinkel.suffixTree;

class Leaf {

	private static Leaf leafInstance = null;
	
	static Leaf getInstance(){
		if(leafInstance == null)
			leafInstance = new Leaf();
		return leafInstance;
	}
	
	@Override
	public boolean equals(Object o){
		return Leaf.class.isInstance(o);
	}
	
	@Override
	public String toString(){
		return "$";
	}

}
