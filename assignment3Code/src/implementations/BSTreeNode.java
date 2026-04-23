package implementations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a single node in a binary search tree.
 *
 * @param <E> the type of element stored in this node
 */

public class BSTreeNode<E> implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** The element stored in this node. */
	private E element;
	
	private BSTreeNode<E> left, right;
	
	private HashMap<String, ArrayList<Integer>> locations;
	
	/**
     * Constructs a node with the given element and no children.
     *
     * @param element the element to store in the node
     */
	 public BSTreeNode(E element) {
	        this.element = element;
			this.left = null;
			this.right = null;
			this.locations = new HashMap<>();
	    }

	/**
     * Constructs a node with the given element and child references.
     *
     * @param element the element to store in the node
     * @param left the left child node
     * @param right the right child node
     */
	public BSTreeNode (E element, BSTreeNode <E> left, BSTreeNode <E> right) {
		this.element = element;
		this.left = left;
		this.right = right;
		this.locations = new HashMap<>();
	}
	
	/**
     * Returns the element stored in this node.
     *
     * @return the element in this node
     */
    public E getElement() {
        return element;
    }

    /**
     * Updates the element stored in this node.
     *
     * @param element the new element to store
     */
    public void setElement(E element) {
        this.element = element;
    }

    /**
     * Returns the left child of this node.
     *
     * @return the left child node, or null if none exists
     */
    public BSTreeNode<E> getLeft() {
        return left;
    }

    /**
     * Sets the left child of this node.
     *
     * @param left the node to assign as the left child
     */
    public void setLeft(BSTreeNode<E> left) {
        this.left = left;
    }

    /**
     * Returns the right child of this node.
     *
     * @return the right child node, or null if none exists
     */
    public BSTreeNode<E> getRight() {
        return right;
    }

    /**
     * Sets the right child of this node.
     *
     * @param right the node to assign as the right child
     */
    public void setRight(BSTreeNode<E> right) {
        this.right = right;
    }
    
    /**
     * Determines whether this node has a left child.
     *
     * @return true if the left child exists; false otherwise
     */
    public boolean hasLeft() {
        return left != null;
    }

    /**
     * Determines whether this node has a right child.
     *
     * @return true if the right child exists; false otherwise
     */
    public boolean hasRight() {
        return right != null;
    }
    
    /**
     * Determines whether this node is a leaf node.
     *
     * @return true if this node has no children; false otherwise
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }
     
    /**
     * Returns the total number of nodes in the subtree rooted at this node,
     * including this node itself.
     *
     * @return the total number of nodes in this subtree
     */
    public int getNumberOfNodes() {
        int leftCount = (left == null) ? 0 : left.getNumberOfNodes();
        int rightCount = (right == null) ? 0 : right.getNumberOfNodes();
        return 1 + leftCount + rightCount;
    }
    
    /**
     * Returns the height of the subtree rooted at this node.
     *
     * @return the height of this node's subtree
     */
    public int getHeight() {
        return getHeight(this);
    }

    /**
     * Returns the height of the subtree rooted at the given node.
     *
     * @param node the root node of the subtree
     * @return the height of the subtree, or 0 if the node is null
     */
    public int getHeight(BSTreeNode<E> node) {
        if (node == null) {
            return 0;
        }

        int leftHeight = getHeight(node.getLeft());
        int rightHeight = getHeight(node.getRight());

        return 1 + Math.max(leftHeight, rightHeight);
    }
    
    /**
     * Adds location information (file name and line number) to the node.
     *
     * @param fileName the file name the word is located in
     * @param lineNumber the line the word is located in within the corresponding file
     */
    public void addLocation(String fileName, int lineNumber) {
        // If file already exists in map, just add the line number
        if (locations.containsKey(fileName)) {
            locations.get(fileName).add(lineNumber);
        } else {
            // Otherwise create a new entry for this file
            ArrayList<Integer> lines = new ArrayList<>();
            lines.add(lineNumber);
            locations.put(fileName, lines);
        }
    }
    
    /**
     * Returns the location information (file names and line numbers) of the word.
     *
     * @return the location information of the word
     */
    public HashMap<String, ArrayList<Integer>> getLocations() {
        return locations;
    }
}
