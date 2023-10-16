package hw6.bst;

import hw6.OrderedMap;
import java.util.Iterator;
import java.util.Stack;

/**
 * Map implemented as an AVL Tree.
 *
 * @param <K> Type for keys.
 * @param <V> Type for values.
 */
public class AvlTreeMap<K extends Comparable<K>, V> implements OrderedMap<K, V> {

  /*** Do not change variable name of 'root'. ***/
  private Node<K, V> root;
  private int size;

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }

    root = insert(root, k, v);
    size++;
  }

  private Node<K, V> insert(Node<K, V> node, K k, V v) {
    if (node == null) {
      return new Node<>(k, v);
    }

    int cmp = k.compareTo(node.key);
    if (cmp < 0) {
      node.left = insert(node.left, k, v);
    } else if (cmp > 0) {
      node.right = insert(node.right, k, v);
    } else {
      throw new IllegalArgumentException("duplicate key " + k);
    }
    updateHeight(node);
    int balanceFactor = getBalanceFactor(node);
    return balance(node, balanceFactor);
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    Node<K, V> node = findForSure(k);
    V val = node.value;
    root = remove(root, node);
    size--;
    return val;
  }

  private Node<K, V> remove(Node<K, V> subtreeRoot, Node<K, V> toRemove) {
    int cmp = subtreeRoot.key.compareTo(toRemove.key);
    if (cmp == 0) {
      return remove(subtreeRoot);
    } else if (cmp > 0) {
      subtreeRoot.left = remove(subtreeRoot.left, toRemove);
    } else {
      subtreeRoot.right = remove(subtreeRoot.right, toRemove);
    }
    updateHeight(subtreeRoot);
    int balanceFactor = getBalanceFactor(subtreeRoot);
    return balance(subtreeRoot, balanceFactor);
  }

  private Node<K, V> remove(Node<K, V> node) {
    // Easy if the node has 0 or 1 child.
    if (node.right == null) {
      return node.left;
    } else if (node.left == null) {
      return node.right;
    }

    // If it has two children, find the predecessor (max in left subtree),
    Node<K, V> toReplaceWith = max(node);
    // then copy its data to the given node (value change),
    node.key = toReplaceWith.key;
    node.value = toReplaceWith.value;
    // then remove the predecessor node (structural change).
    node.left = remove(node.left, toReplaceWith);

    updateHeight(node);
    int balanceFactor = getBalanceFactor(node);
    return balance(node, balanceFactor);
  }

  private void updateHeight(Node<K, V> node) {
    int l;
    int r;
    if (node.left == null) {
      l = -1;
    } else {
      l = node.left.height;
    }
    if (node.right == null) {
      r = -1;
    } else {
      r = node.right.height;
    }
    node.height = 1 + Math.max(l, r);
  }

  private int getBalanceFactor(Node<K, V> node) {
    if (node.left == null && node.right == null) {
      return 0;
    } else if (node.left == null) {
      return -1 - node.right.height;
    } else if (node.right == null) {
      return node.left.height - (-1);
    } else {
      return node.left.height - node.right.height;
    }
  }

  private Node<K, V> balance(Node<K, V> node, int balanceFactor) {
    if (balanceFactor >= 2) {
      return rightHeavy(node);
    } else if (balanceFactor <= -2) {
      return leftHeavy(node);
    } else {
      return node;
    }
  }

  private Node<K, V> rightHeavy(Node<K, V> node) {
    if (getBalanceFactor(node.left) <= -1) {
      node.left = rotateLeft(node.left);
    }
    return rotateRight(node);
  }

  private Node<K, V> leftHeavy(Node<K, V> node) {
    if (getBalanceFactor(node.right) >= 1) {
      node.right = rotateRight(node.right);
    }
    return rotateLeft(node);
  }

  private Node<K, V> rotateRight(Node<K, V> node) {
    Node<K, V> newRoot = node.left;
    node.left = newRoot.right;
    newRoot.right = node;
    updateHeight(node);
    updateHeight(newRoot);
    return newRoot;
  }

  private Node<K, V> rotateLeft(Node<K, V> node) {
    Node<K, V> newRoot = node.right;
    node.right = newRoot.left;
    newRoot.left = node;
    updateHeight(node);
    updateHeight(newRoot);
    return newRoot;
  }



  private Node<K, V> max(Node<K, V> node) {
    Node<K, V> curr = node.left;
    while (curr.right != null) {
      curr = curr.right;
    }
    return curr;
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    Node<K, V> n = findForSure(k);
    n.value = v;
  }

  private Node<K, V> findForSure(K k) {
    Node<K, V> n = find(k);
    if (n == null) {
      throw new IllegalArgumentException("cannot find key " + k);
    }
    return n;
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    Node<K, V> n = findForSure(k);
    return n.value;
  }

  @Override
  public boolean has(K k) {
    if (k == null) {
      return false;
    }
    return find(k) != null;
  }

  @Override
  public int size() {
    return size;
  }

  private Node<K, V> find(K k) {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    Node<K, V> n = root;
    while (n != null) {
      int cmp = k.compareTo(n.key);
      if (cmp < 0) {
        n = n.left;
      } else if (cmp > 0) {
        n = n.right;
      } else {
        return n;
      }
    }
    return null;
  }

  @Override
  public Iterator<K> iterator() {
    return new InorderIterator();
  }

  private class InorderIterator implements Iterator<K> {
    private final Stack<Node<K, V>> stack;

    InorderIterator() {
      stack = new Stack<>();
      pushLeft(root);
    }

    private void pushLeft(Node<K, V> curr) {
      while (curr != null) {
        stack.push(curr);
        curr = curr.left;
      }
    }

    @Override
    public boolean hasNext() {
      return !stack.isEmpty();
    }

    @Override
    public K next() {
      Node<K, V> top = stack.pop();
      pushLeft(top.right);
      return top.key;
    }
  }

  /*** Do not change this function's name or modify its code. ***/
  @Override
  public String toString() {
    return BinaryTreePrinter.printBinaryTree(root);
  }

  /**
   * Feel free to add whatever you want to the Node class (e.g. new fields).
   * Just avoid changing any existing names, deleting any existing variables,
   * or modifying the overriding methods.
   *
   * <p>Inner node class, each holds a key (which is what we sort the
   * BST by) as well as a value. We don't need a parent pointer as
   * long as we use recursive insert/remove helpers.</p>
   **/
  private static class Node<K, V> implements BinaryTreeNode {
    Node<K, V> left;
    Node<K, V> right;
    K key;
    V value;
    int height;

    // Constructor to make node creation easier to read.
    Node(K k, V v) {
      // left and right default to null
      key = k;
      value = v;
    }

    @Override
    public String toString() {
      return key + ":" + value;
    }

    @Override
    public BinaryTreeNode getLeftChild() {
      return left;
    }

    @Override
    public BinaryTreeNode getRightChild() {
      return right;
    }
  }
}
