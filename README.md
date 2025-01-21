# Differences from the base version:
1. Fixes [a bug](https://github.com/maxgarfinkel/suffixTree/issues/2) of incorrect suffix tree, which comes from wrong suffix links
2. Fixes incomingEdge variable after split

---

# Suffix Tree

A Java implementation of Ukkonen's suffix tree creation algorithm capable 
of creating a generalized suffix tree.

You will need to be familiar with Ukkonen's algorithm and suffix trees in
general. Most of this library is based on this [Stack Overflow answer](http://stackoverflow.com/questions/9452701/ukkonens-suffix-tree-algorithm-in-plain-english) 

This implementation is generic which allows the user to supply a custom
character and word type.

The tree should perform linearly with regard to the length of the string / 
strings being added to it.

## Design
### Main Classes
`SuffixTree`
*	Holds the root `Node`.
*	Holds the master `Sequence`
*	Most of the state of the whole tree.
 

## Notes
SuffixTree.toString return a .dot file with the tree structure represented 
within it. 
To view these files use [Graph Viz](http://www.graphviz.org/)