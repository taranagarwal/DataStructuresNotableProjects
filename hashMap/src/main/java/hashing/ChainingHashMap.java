package hw7.hashing;

import hw7.Map;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ChainingHashMap<K, V> implements Map<K, V> {

  private Entry<K, V>[] hashArray;
  private int size;
  private int depthIntoPrimes;
  private final int[] primes = {5, 11, 23, 47, 97, 197, 397, 797, 1597, 3203, 6421, 12853, 25717, 51437,102877,
      205759, 411527, 823117, 1646237,3292489, 6584983, 13169977
  };
  private int capacity;


  /**
   * Implementation of Map with a hash map
   * using chaining to handle collisions.
   */
  public ChainingHashMap() {
    capacity = 3;
    size = 0;
    hashArray = (Entry<K,V>[]) new Entry[capacity];
  }

  private void rehash() {
    if (getLoad() >= 0.90) {
      if (depthIntoPrimes < primes.length) {
        capacity = primes[depthIntoPrimes];
        ++depthIntoPrimes;
      } else {
        capacity *= 2;
      }
      Entry<K, V>[] oldArray = hashArray;
      hashArray = (Entry<K,V>[]) new Entry[capacity];
      size = 0;
      for (Entry<K, V> entry : oldArray) {
        Entry<K, V> cur = entry;
        while (cur != null) {
          insert(cur.key, cur.value);
          cur = cur.next;
        }
      }
    }
  }

  private double getLoad() {
    return (double) size / (double) capacity;
  }

  private int getIndex(K k) {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int x = k.hashCode() & 0x7fffffff;
    return x % capacity;
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    rehash();
    int index = getIndex(k);
    if (hashArray[index] == null) {
      hashArray[index] = new Entry<>(k, v);
    } else {
      Entry<K, V> cur = hashArray[index];
      while (cur.next != null) {
        isInsertExistingKey(cur, k);
        cur = cur.next;
      }
      isInsertExistingKey(cur, k);
      cur.next = new Entry<>(k, v);
      cur.next.prev = cur;
    }
    size++;
  }

  private void isInsertExistingKey(Entry<K, V> cur, K k) throws IllegalArgumentException {
    if (cur.key.equals(k)) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    int index = getIndex(k);
    Entry<K, V> target = find(k);
    if (target == null) {
      throw new IllegalArgumentException();
    }
    if (target.prev != null) {
      hashArray[index] = target.prev;
    } else {
      hashArray[index] = target.next;
    }
    removeNode(target);
    V val = target.value;
    size--;
    return val;
  }

  private void removeNode(Entry<K,V> target) {
    Entry<K, V> prevNode = target.prev;
    Entry<K, V> nextNode = target.next;
    if (prevNode != null) {
      prevNode.next = nextNode;
    }
    if (nextNode != null) {
      nextNode.prev = prevNode;
    }
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    Entry<K, V> target = find(k);
    if (target == null) {
      throw new IllegalArgumentException();
    } else {
      target.value = v;
    }
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    Entry<K, V> target = find(k);
    if (target == null) {
      throw new IllegalArgumentException();
    } else {
      return target.value;
    }
  }

  @Override
  public boolean has(K k) {
    if (k == null) {
      return false;
    }
    Entry<K, V> target = find(k);
    return target != null;
  }

  private Entry<K, V> find(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int index = getIndex(k);
    if (hashArray[index] != null) {
      Entry<K, V> cur = hashArray[index];
      while (cur != null) {
        if (cur.key.equals(k)) {
          return cur;
        }
        cur = cur.next;
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
    return new ChainingHashMapIterator();
  }

  private class ChainingHashMapIterator implements Iterator<K> {

    private int index;
    private Entry<K, V> current;

    ChainingHashMapIterator() {
      index = 0;
      current = null;
    }

    @Override
    public boolean hasNext() {
      if (current != null && current.next != null) {
        return true;
      }
      for (int i = index; i < capacity; i++) {
        if (hashArray[i] != null) {
          return true;
        }
      }
      return false;
    }

    @Override
    public K next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      if (current == null || current.next == null) {
        do {
          current = hashArray[index++];
        } while (current == null);
      } else {
        current = current.next;
      }
      return current.key;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private static class Entry<K,V> {
    K key;
    V value;
    Entry<K, V> next;
    Entry<K, V> prev;

    Entry(K k, V v) {
      this.key = k;
      this.value = v;
    }
  }
}
