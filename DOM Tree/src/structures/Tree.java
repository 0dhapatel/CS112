package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		if (sc == null) {
			return;
		}
		Stack<TagNode> nodeStack = new Stack<TagNode>();
		root = new TagNode("html", null, null);
		nodeStack.push(root); 
		String curr = sc.nextLine();
		while (sc.hasNextLine()) {
			curr = sc.nextLine();
			if (curr.contains("<") && curr.contains(">") && curr.contains("/")) {
				nodeStack.pop();
			} else if (curr.contains("<") && curr.contains(">") && !curr.contains("/")) {
				if (nodeStack.peek().firstChild != null) {
					TagNode currNode = nodeStack.peek().firstChild;
					while (currNode.sibling != null) {
						currNode = currNode.sibling;
					}
					TagNode node = new TagNode(curr.replace("<", "").replace(">", ""), null, null);
					currNode.sibling = node;
					nodeStack.push(node);
				} else { 
					TagNode node = new TagNode(curr.replace("<", "").replace(">", ""), null, null);
					nodeStack.peek().firstChild = node;
					nodeStack.push(node);
				}
			} else {
				if (nodeStack.peek().firstChild == null) {
					nodeStack.peek().firstChild = new TagNode(curr, null, null);
				} else {
					TagNode currNode = nodeStack.peek().firstChild;
					while (currNode.sibling != null)
						currNode = currNode.sibling;
					currNode.sibling = new TagNode(curr, null, null);
				}
			} 
		}
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		if (root == null || oldTag.isEmpty() || newTag.isEmpty()) {
			return; 
		} else {
			replaceHelp(oldTag, newTag, root.firstChild);
		}
	}
	
	private void replaceHelp(String oldTag1, String newTag1, TagNode node) {
		if (node == null) {
			return;
		} else if (node.tag.compareTo(oldTag1) == 0) {
			node.tag = newTag1;
		}

		replaceHelp(oldTag1, newTag1, node.firstChild);
		replaceHelp(oldTag1, newTag1, node.sibling);
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		if(row <= 0) {
			return;
		} else {
			boldHelp(row,0,root,root.firstChild);
		}
	}
	
	private void boldHelp(int row, int counter,TagNode node1, TagNode node2) {
		if(node2 == null) {
			return;
		} else if(node2.tag.equals("tr")) {
			counter++;
		} if(counter == row && node2.firstChild == null) {
			node1.firstChild = new TagNode("b", node2, null);
		} 
		boldHelp(row, counter, node2, node2.firstChild); 
		boldHelp(row, counter, node2, node2.sibling);
	}
	
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		if (root == null || tag.isEmpty()) {
			return;
		} else {
			removeHelp(null, root, tag);
		}
	}
	
	private void removeHelp(TagNode prevNode, TagNode currNode, String tag1) {
		if (currNode == null) {
			return;
		}
		if (currNode.tag.equals("b") || currNode.tag.equals("em") || currNode.tag.equals("p")) {
			if (currNode.tag.equals(tag1)) {
				if (prevNode.firstChild != null && prevNode.firstChild.tag.equals(currNode.tag)) {
					if (currNode.sibling != null) {
						if (currNode.firstChild.sibling != null) {
							TagNode tempNode = currNode.firstChild;
							prevNode.sibling = tempNode;
							while (tempNode.sibling != null) {
								tempNode = tempNode.sibling;
							}
							tempNode.sibling = currNode.sibling;
							currNode.firstChild = null;
							currNode.sibling = null;
						} else {
							currNode.firstChild.sibling = currNode.sibling;
							prevNode.firstChild = currNode.firstChild;
						}
					} else {
						prevNode.firstChild = currNode.firstChild;
					}
				} else if (prevNode.sibling != null) {
					if (currNode.sibling != null) {
						if (currNode.firstChild.sibling != null) {
							TagNode tempNode = currNode.firstChild;
							prevNode.sibling = tempNode;
							while (tempNode.sibling != null) {
								tempNode = tempNode.sibling;
							}
							tempNode.sibling = currNode.sibling;
							currNode.firstChild = null;
							currNode.sibling = null;
						}

						else {
							currNode.firstChild.sibling = currNode.sibling;
							prevNode.sibling = currNode.firstChild;
						}
					} else {
						prevNode.sibling = currNode.firstChild;
					}
				}
			}
		} else if (currNode.tag.equals("ol") || currNode.tag.equals("ul")) {
			if (currNode.tag.equals(tag1)) {
				if (prevNode.firstChild != null && prevNode.firstChild.tag.equals(currNode.tag)) {
					if (currNode.sibling != null) {
						if (currNode.firstChild.sibling != null) {
							TagNode tempNode = currNode.firstChild;
							while (tempNode.sibling != null) {
								if (tempNode.tag.equals("li"))
									tempNode.tag = "p";
								tempNode = tempNode.sibling;
							}
							if (tempNode.tag.equals("li"))
								tempNode.tag = "p";
							tempNode.sibling = currNode.sibling;
							prevNode.firstChild = currNode.firstChild;
						} else {
							if (currNode.firstChild.tag.equals("li"))
								currNode.firstChild.tag = "p";
							currNode.firstChild.sibling = currNode.sibling;
							prevNode.firstChild = currNode.firstChild;
						}
					} else {
						if (currNode.firstChild.sibling != null) {
							TagNode tempNode = currNode.firstChild;
							while (tempNode.sibling != null) {
								if (tempNode.tag.equals("li"))
									tempNode.tag = "p";
								tempNode = tempNode.sibling;
							}
							if (tempNode.tag.equals("li"))
								tempNode.tag = "p";
							prevNode.firstChild = currNode.firstChild;
						} else {
							if (currNode.firstChild.tag.equals("li"))
								currNode.firstChild.tag = "p";
							prevNode.firstChild = currNode.firstChild;
						}
					}
				} else if (prevNode.sibling != null) {
					if (currNode.sibling != null) {
						if (currNode.firstChild.tag.equals("li"))
							currNode.firstChild.tag = "p";
						if (currNode.firstChild.sibling != null) {
							TagNode tempNode = currNode.firstChild;
							prevNode.sibling = tempNode;
							while (tempNode.sibling != null) {
								if (tempNode.tag.equals("li"))
									tempNode.tag = "p";
								tempNode = tempNode.sibling;
							}
							if (tempNode.tag.equals("li"))
								tempNode.tag = "p";
							tempNode.sibling = currNode.sibling;
							currNode.firstChild = null;
							currNode.sibling = null;
						} else {
							currNode.firstChild.sibling = currNode.sibling;
							prevNode.sibling = currNode.firstChild;
						}
					} else {
						if (currNode.firstChild.sibling != null) {
							TagNode tempNode = currNode.firstChild;
							while (tempNode.sibling != null) {
								if (tempNode.tag.equals("li"))
									tempNode.tag = "p";
								tempNode = tempNode.sibling;
							}
							if (tempNode.tag.equals("li"))
								tempNode.tag = "p";
							prevNode.sibling = currNode.firstChild;
						} else {
							if (currNode.firstChild.tag.equals("li"))
								currNode.firstChild.tag = "p";
							prevNode.sibling = currNode.firstChild;
						}
					}
				}
			}
		}

		removeHelp(currNode, currNode.firstChild, tag1);
		removeHelp(currNode, currNode.sibling, tag1);
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		if (root == null || word.isEmpty() || tag.isEmpty()) {
			return;
		} else if (tag.compareTo("em")==0 || tag.compareTo("b")==0 ) {  
			addHelp(word, tag, root);
		} else {
			return;
		}
	}
	
	private void addHelp(String word, String tag, TagNode node){
		for (TagNode ptr=node; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				String checkCase=ptr.tag.toLowerCase(), checkWord=word.toLowerCase();
					if(checkCase.contains(checkWord)){
						//three cases beginning of string, middle of string, end of string
						char[] charArray=checkCase.toCharArray();
						int i=0;
						int d=0;
						for(int p=0;p<charArray.length;p++){
							if(charArray[p]==checkWord.charAt(i)){
								i++;
							}
							else{
								d+=i;
								i=0;
								d++;
								
							}
							if(p+1>=word.length()&&i==word.length()&&!checkCase.equals(checkWord)){
								
								if(p+1==word.length()){
									if(charArray[p+1]!=' '&&charArray[p+1]!='.'&&charArray[p+1]!=','&&charArray[p+1]!='?'&&charArray[p+1]!='!'&&charArray[p+1]!=':'&&charArray[p+1]!=';'){
										d+=i;
										i=0;
									}
								}
								else if(charArray[p-word.length()]!=' '){
									d+=i;
									i=0;
								}
							}
							
							if(i==word.length()){
								if((p+1==charArray.length)||(p+1<charArray.length&&charArray[p+1]==' ')){
									if(word.length()-1-p==0){
										String word1=ptr.tag.substring(0,p+1);
										if(p+1!=charArray.length){
											String word2=ptr.tag.substring(p+1);
											TagNode sib=new TagNode(word2,null,ptr.sibling);
											ptr.sibling=sib;
										}
										ptr.tag=tag;
										ptr.firstChild=new TagNode(word1,null,null);
										ptr=ptr.sibling;
										
									}
									else if(p+1==charArray.length){
										String word1=ptr.tag.substring(0,d),word2=ptr.tag.substring(d);
										TagNode tag2=new TagNode(word2,null,null);
										TagNode node3=new TagNode(tag,tag2,ptr.sibling);
										ptr.tag=word1;
										ptr.sibling=node3;
										ptr=ptr.sibling.sibling;
										
									}
									else{
										String word1=ptr.tag.substring(0,d),word2=ptr.tag.substring(d,d+word.length()),word3=ptr.tag.substring(d+word.length());
										TagNode tag2=new TagNode(word2,null,null);
										TagNode tag3=new TagNode(word3,null,ptr.sibling);
										TagNode node3=new TagNode(tag,tag2,tag3);
										ptr.tag=word1;
										ptr.sibling=node3;
										ptr=ptr.sibling.sibling;
										
									}
									d=0;
								}
								else if(((p+2==charArray.length)||(p+2<charArray.length&&charArray[p+2]==' '))&&(charArray[p+1]=='.'||charArray[p+1]==','||charArray[p+1]=='?'||charArray[p+1]=='!'||charArray[p+1]==':'||charArray[p+1]==';')){
									
									if(word.length()-1-p==0){
										String word1=ptr.tag.substring(0,p+2);
										if(p+2!=charArray.length){
											String word2=ptr.tag.substring(p+2);
											TagNode sib=new TagNode(word2,null,ptr.sibling);
											ptr.sibling=sib;
										}
										ptr.tag=tag;
										ptr.firstChild=new TagNode(word1,null,null);
										ptr=ptr.sibling;
										d=-1;
										
									}
									else if(p+2==charArray.length){
										String word1=ptr.tag.substring(0,d),word2=ptr.tag.substring(d);
										TagNode node2=ptr;
										TagNode tag2=new TagNode(word2,null,null);
										TagNode node3=new TagNode(tag,tag2,ptr.sibling);
										ptr.tag=word1;
										node2.sibling=node3;
										ptr=ptr.sibling.sibling;
										d=-1;
									}
									else{
										String word1=ptr.tag.substring(0,d),word2=ptr.tag.substring(d,d+word.length()+1),word3=ptr.tag.substring(d+word.length()+1);
										
										TagNode tag2=new TagNode(word2,null,null);
										TagNode tag3=new TagNode(word3,null,ptr.sibling);
										TagNode node3=new TagNode(tag,tag2,tag3);
										ptr.tag=word1;
										ptr.sibling=node3;
										ptr=ptr.sibling.sibling;
										d=-1;
									}
								}
								else
									d+=i;
								i=0;
							}
						}
					}
			}
			if(ptr==null){
				break;
			}
				addHelp(word,tag,ptr.firstChild);	
		}	
	}

	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|---- ");
			} else {
				System.out.print("      ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
