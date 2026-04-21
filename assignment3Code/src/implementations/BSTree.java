package implementations;

import java.util.ArrayList;
import utilities.BSTreeADT;
import utilities.Iterator;

public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E> {
	private static final long serialVersionUID = 1L;
	private BSTreeNode<E> root;
	private int size;
	

	/**
	 * Returns the root of the Binary Search Tree.
	 * 
	 * @return the node stored at the root of the tree
	 * @throws NullPointerException if the tree is empty
	 */
	@Override
	public BSTreeNode<E> getRoot() throws NullPointerException {
		if (isEmpty()) {
			throw new NullPointerException();
		} else {
			return root;
		}
	}

	/**
	 * Determines the row height of the tree and returns that value.
	 * 
	 * @return the height of the tree.
	 */
	@Override
	public int getHeight() {
		 return getHeightRecursive(root);
	}
	
	// Recursively calculates the height of the subtree rooted at current.
	// Returns 0 for null nodes, otherwise 1 + the height of the taller subtree.
	private int getHeightRecursive(BSTreeNode<E> current) {
		if ( current == null ) {
			return 0;
		} else {
			int leftHeight = getHeightRecursive(current.getLeft());
			int rightHeight = getHeightRecursive(current.getRight());
			return 1 + Math.max(leftHeight, rightHeight);
		}
	}
	
	/**
	 * Counts the number of elements currently stored in the tree and returns the value.
	 * 
	 * @return number of elements currently stored in the tree.
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Checks if the tree is currently empty.
	 * 
	 * @return true if the tree is empty, false otherwise
	 */
	@Override
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * Clears all elements currently stored in the tree.
	 */
	@Override
	public void clear() {
		root = null;
		size = 0;
	}

	/**
	 * Checks if the specified element exists in the tree.
	 * 
	 * @param entry the element to find in the tree
	 * @return returns boolean true if element is currently in the tree and false if the
	 * 		   element is not found in the tree
	 * @throws NullPointerException if the element being passed in is null
	 */
	@Override
	public boolean contains(E entry) throws NullPointerException {
		if ( entry == null ) {
			throw new NullPointerException();
		}
		return search(entry) != null;
	}

	/**
	 * Retrieves a node from the tree given the object to search for.
	 * 
	 * @param entry element object being searched
	 * @return the node with the element located in tree, null if not found
	 * @throws NullPointerException if the element being passed in is null
	 */
	@Override
	public BSTreeNode<E> search(E entry) throws NullPointerException {
		if ( entry == null ) {
			throw new NullPointerException();
		}
		
		return searchRecursive(root, entry);
	}
	
	// Recursively searches for entry in the subtree rooted at current. 
	// Returns the matching node, or null if not found.
	private BSTreeNode<E> searchRecursive(BSTreeNode<E> current, E newEntry) {
		if ( current == null ) {
			return null;
		}
		
		if ( newEntry.compareTo(current.getElement()) < 0 ) {
			return searchRecursive(current.getLeft(), newEntry);
		} else if ( newEntry.compareTo(current.getElement()) > 0 ) {
			return searchRecursive(current.getRight(), newEntry);
		} else {
			return current;
		}
	}
	
	/**
	 * Adds a new element to the tree according to the natural ordering established
	 * by the Comparable implementation.
	 * 
	 * @param newEntry the element being added to the tree
	 * @return a boolean true if the element was added successfully, otherwise false
	 * @throws NullPointerException if the element being passed in is null
	 */
	@Override
	public boolean add(E newEntry) throws NullPointerException {
		if ( newEntry == null ) {
			throw new NullPointerException();
		}
		
		root = addRecursive(root, newEntry);
		size++;
		return true;
	}
	
	// Recursively finds the correct position and inserts newEntry into the subtree
	// rooted at current. Returns the updated subtree root.
	private BSTreeNode<E> addRecursive(BSTreeNode<E> current, E newEntry) {
		if ( current == null ) {
			return new BSTreeNode<>(newEntry);
		} 
		
		if ( newEntry.compareTo(current.getElement()) < 0 ) {
			current.setLeft(addRecursive(current.getLeft(), newEntry));
		} else if ( newEntry.compareTo(current.getElement()) > 0 ) {
			current.setRight(addRecursive(current.getRight(), newEntry));
		}
		
		return current;
	}

	/**
	 * Removes the smallest element in the tree according to the ordering established
	 * by the Comparable implementation.
	 * 
	 * @return the removed element or null if the tree is empty
	 */
	@Override
	public BSTreeNode<E> removeMin() {
		if ( size == 0) {
			return null;
		} else {
			BSTreeNode<E> min = root;
			while (min.getLeft() != null) {
				min = min.getLeft();
			}
			
		root = removeMinRecursive(root);
		size--;
		return min;
		}
	}
	
	// Recursively finds and removes the leftmost node in the subtree rooted at current.
	// If the minimum node has a right child, that child takes its place.
	private BSTreeNode<E> removeMinRecursive(BSTreeNode<E> current) {
		if ( current == null ) {
			return null;
		} 
		
		if ( current.getLeft() == null ) {
			return current.getRight();
		} else {
			current.setLeft(removeMinRecursive(current.getLeft()));
			return current;
		}
	}
	
	/**
	 * Removes the largest element in the tree according to the ordering established
	 * by the Comparable implementation.
	 * 
	 * @return the removed element or null if the tree is empty
	 */
	@Override
	public BSTreeNode<E> removeMax() {
		if ( size == 0 ) {
			return null;
		} else {
			BSTreeNode<E> max = root;
			while (max.getRight() != null) {
				max = max.getRight();
			}
			
		root = removeMaxRecursive(root);
		size--;
		return max;
		}
	}
	
	// Recursively finds and removes the rightmost node in the subtree rooted at current.
	// If the maximum node has a left child, that child takes its place.
	private BSTreeNode<E> removeMaxRecursive(BSTreeNode<E> current) {
		if ( current == null ) {
			return null;
		} 
		
		if ( current.getRight() == null ) {
			return current.getLeft();
		} else {
			current.setRight(removeMaxRecursive(current.getRight()));
			return current;
		}
	}

	/**
	 * Iterates over all elements in the tree and presents them in their natural order.
	 * 
	 * @return an iterator with the elements in the natural order
	 */
	@Override
	public Iterator<E> inorderIterator() {
		java.util.ArrayList<E> list = new java.util.ArrayList<>();
		inorderTraversal(root, list);
		int[] index = new int[]{0};
		
		return new Iterator<E>() {
			
			@Override
			public boolean hasNext() {
				return index[0] < list.size();
			}
			
			@Override
			public E next() {
				return list.get(index[0]++);
			}
		};
	}
	
	// Recursively traverses the tree in-order (left → node → right),
	// adding each element to the list.
	private void inorderTraversal(BSTreeNode<E> current, java.util.ArrayList<E> list) {
		if ( current == null ) {
			return;
		} 
		inorderTraversal(current.getLeft(), list);
		list.add(current.getElement());
		inorderTraversal(current.getRight(), list);
	}

	/**
	 * Iterates over all elements in the tree and presents them so that they are ordered
	 * in such a way that the root element is first.
	 * 
	 * @return an iterator with the elements in a root element first order
	 */
	@Override
	public Iterator<E> preorderIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Iterates over all elements in the tree and presents them so that they are ordered
	 * in such a way as that the root element is last.
	 * 
	 * @return an iterator with the elements in a root element last order
	 */
	@Override
	public Iterator<E> postorderIterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
