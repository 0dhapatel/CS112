package apps;

import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Vertex;


public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    		
    		/* COMPLETE THIS METHOD */
    	
    	if (size == 0) {
    		throw new NoSuchElementException("Not Found");
    	}
   	Node temp = rear.next;
    	rear.next = temp.next;
    	if (size == 1)
    		rear = null;
   		size--;
    	return temp.tree;
    	
    }

   
    private boolean checkRemove(PartialTree tree, Vertex ver) {
        Vertex parent = ver;
        while(parent.parent != parent) {
          parent = parent.parent;
        }
        return parent == tree.getRoot();
      }
      
      private void removeNode(Node remove) {
          Node prev;
          prev = remove;
          while(prev.next != remove) {
        	  prev = prev.next;
          }
          Node front = remove.next;
          if(front == remove && prev == remove) {
            rear = null;
            size--;
          }
          else if(front == prev) {
            if(remove == rear) {
              rear = rear.next;
            }
            remove.next.next = remove.next;
            size--;
          }else {
            if(remove == rear) {
              rear = prev;
            }
            prev.next = front;
            size--;
          }
          remove.next = null;
      }
      /**
       * Removes the tree in this list that contains a given vertex.
       * 
       * @param vertex Vertex whose tree is to be removed
       * @return The tree that is removed
       * @throws NoSuchElementException If there is no matching tree
       */
      
      public PartialTree removeTreeContaining(Vertex vertex) 
      throws NoSuchElementException {
          /* COMPLETE THIS METHOD */
          PartialTree remove = null;
          if(rear == null) {
            throw new NoSuchElementException("Not Found");
          }
          Node ptr = rear;
          do {
            PartialTree tree = ptr.tree;
            boolean ver = checkRemove(tree, vertex);
            if(ver) {
            	remove = tree;
              removeNode(ptr);
              break;
            }
            ptr = ptr.next;
          }while(ptr != rear);
          if(remove == null) {
            return null;
          }else {
            return remove;
          }
      }
    
    
    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}


