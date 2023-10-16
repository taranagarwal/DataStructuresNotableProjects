package hw6.bst;

import hw6.OrderedMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

/**
 * Map implemented as a Treap.
 *
 * @param <K> Type for keys.
 * @param <V> Type for values.
 */
public class TreapMap<K extends Comparable<K>, V> implements OrderedMap<K, V> {

  /*** Do not change variable name of 'rand'. ***/
  private static Random rand;
  /*** Do not change variable name of 'root'. ***/
  private Node<K, V> root;

  private int size;

  /**
   * Make a TreapMap.
   */
  public TreapMap() {
    rand = new Random();
  }

  /**
   * Make a TreapMap with seed value for testing purposes.
   * @param seed the seed value we want to input
   */
  public TreapMap(int seed) {
    rand = new Random(seed);
  }

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

    int comp = k.compareTo(node.key);
    if (comp < 0) {
      node.left = insert(node.left, k, v);
    } else if (comp > 0) {
      node.right = insert(node.right, k, v);
    } else {
      throw new IllegalArgumentException("duplicate key " + k);
    }
    return balance(node);
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    Node<K, V> nodeToRemove = findForSure(k);
    V value = nodeToRemove.value;
    root = remove(nodeToRemove, root);
    size--;
    return value;
  }

  private Node<K, V> remove(Node<K, V> target, Node<K, V> current) {
    if (target == null || current == null) {
      return null;
    }
    int comp = target.key.compareTo(current.key);
    if (comp > 0) {
      current.right = remove(target, current.right);
    } else if (comp < 0) {
      current.left = remove(target, current.left);
    } else {
      current = sink(target);
    }
    return current;
  }

  private Node<K, V> sink(Node<K,  V> nodeToRemove) {
    if (nodeToRemove.right == null && nodeToRemove.left == null) {
      nodeToRemove = null;
    } else if (nodeToRemove.right == null) {
      nodeToRemove = rotateRight(nodeToRemove);
      nodeToRemove.right = sink(nodeToRemove.right);
    } else if (nodeToRemove.left == null) {
      nodeToRemove = rotateLeft(nodeToRemove);
      nodeToRemove.left = sink(nodeToRemove.left);
    } else if (nodeToRemove.right.priority < nodeToRemove.left.priority) {
      nodeToRemove = rotateLeft(nodeToRemove);
      nodeToRemove.left = sink(nodeToRemove.left);
    } else {
      nodeToRemove = rotateRight(nodeToRemove);
      nodeToRemove.right = sink(nodeToRemove.right);
    }
    return nodeToRemove;
  }


  private Node<K, V> balance(Node<K, V> node) {
    if (node == null) {
      return node;
    }
    Node<K, V> newRoot;
    if (node.left != null && node.left.priority < node.priority) {
      newRoot = rotateRight(node);
      return balance(newRoot);
    } else if (node.right != null && node.right.priority < node.priority) {
      newRoot = rotateLeft(node);
      return balance(newRoot);
    }
    return node;
  }

  private Node<K, V> rotateRight(Node<K, V> node) {
    Node<K, V> newRoot = node.left;
    node.left = newRoot.right;
    newRoot.right = node;
    return newRoot;
  }

  private Node<K, V> rotateLeft(Node<K, V> node) {
    Node<K, V> newRoot = node.right;
    node.right = newRoot.left;
    newRoot.left = node;
    return newRoot;
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
  public int size() {
    return size;
  }

  @Override
  public Iterator<K> iterator() {
    return new InorderIterator();
  }

  private class InorderIterator implements Iterator<K> {
    private final Stack<Node<K, V>> stack;

    private Node<K, V> curr;

    InorderIterator() {
      this.stack = new Stack<>();
      this.curr = root;
    }

    @Override
    public boolean hasNext() {
      return curr != null || !stack.isEmpty();
    }

    @Override
    public K next() {
      while (curr != null) {
        stack.push(curr);
        curr = curr.left;
      }
      Node<K, V> top = stack.pop();
      curr = top.right;
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
   * Inner node class, each holds a key (which is what we sort the
   * BST by) as well as a value. We don't need a parent pointer as
   * long as we use recursive insert/remove helpers. Since this is
   * a node class for a Treap we also include a priority field.
   **/
  private static class Node<K, V> implements BinaryTreeNode {
    Node<K, V> left;
    Node<K, V> right;
    K key;
    V value;
    int priority;

    // Constructor to make node creation easier to read.
    Node(K k, V v) {
      // left and right default to null
      key = k;
      value = v;
      priority = generateRandomInteger();
    }

    // Use this function to generate random values
    // to use as node priorities as you insert new
    // nodes into your TreapMap.
    private int generateRandomInteger() {
      // Note: do not change this function!
      return rand.nextInt();
    }

    @Override
    public String toString() {
      return key + ":" + value + ":" + priority;
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
