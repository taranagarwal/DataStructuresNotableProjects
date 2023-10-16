package hw7.hashing;

import hw7.Map;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OpenAddressingHashMap<K, V> implements Map<K, V> {

  private Entry<K, V>[] hashArray;
  private int size;

  private int capacity;


  /**
   * Implementation of Map with a hash map
   * using open addressing to handle collisions.
   */
  public OpenAddressingHashMap() {
    capacity = 3;
    size = 0;
    hashArray = (Entry<K,V>[]) new Entry[capacity];

  }

  private int getIndex(K k) {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int x = k.hashCode() & 0x7fffffff;
    return x % capacity;
  }

  private double getLoad() {
    return (double) size / (double) capacity;
  }

  private void rehash() {
    if (getLoad() >= 0.75) {
      capacity *= 2;
      Entry<K, V>[] temp = (Entry<K,V>[]) new Entry[capacity];
      int x = 0;
      for (Entry<K, V> entry : hashArray) {
        if (entry != null && !entry.tombstone) {
          int index = getIndex(entry.key);
          while (temp[index] != null || x >= capacity) {
            index = (index + 1) % capacity;
            x++;
          }
          temp[index] = entry;
        }
      }
      hashArray = temp;
    }

  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    rehash();
    int index = getIndex(k);
    if (hashArray[index] == null || hashArray[index].tombstone) {
      hashArray[index] = new Entry<>(k, v);
      size++;
    } else if (hashArray[index].key.equals(k)) {
      throw new IllegalArgumentException();
    } else {
      while (hashArray[index] != null) {
        if (hashArray[index].key.equals(k)) {
          throw new IllegalArgumentException();
        }
        index = (index + 1) % capacity;
      }
      hashArray[index] = new Entry<>(k, v);
      size++;
    }
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    Entry<K, V> target = find(k);
    if (target == null) {
      throw new IllegalArgumentException();
    } else {
      target.tombstone = true;
      target.key = null;
      V val = target.value;
      target.value = null;
      size--;
      return val;
    }
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    Entry<K, V> target = find(k);
    if (!(target == null)) {
      target.value = v;
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    Entry<K, V> temp = find(k);
    if (temp == null) {
      throw new IllegalArgumentException();
    } else {
      return temp.value;
    }
  }

  private Entry<K, V> find(K k) {
    if (hashArray[getIndex(k)] == null) {
      return null;
    } else if (!hashArray[getIndex(k)].tombstone && hashArray[getIndex(k)].key.equals(k)) {
      return hashArray[getIndex(k)];
    } else {
      int index = getIndex(k);
      int x = 0;
      while (hashArray[index] != null && x < capacity) {
        if (!hashArray[index].tombstone && hashArray[index].key.equals(k)) {
          return hashArray[index];
        }
        index = (index + 1) % capacity;
        x++;
      }
      return null;
    }
  }

  @Override
  public boolean has(K k) {
    if (k == null) {
      return false;
    }
    Entry<K, V> node = find(k);
    return node != null && !node.tombstone;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public Iterator<K> iterator() {
    return new OpenAddressingHashMapIterator();
  }

  private class OpenAddressingHashMapIterator implements Iterator<K> {
    private int currentIndex;

    @Override
    public boolean hasNext() {
      while (currentIndex < capacity && (hashArray[currentIndex] == null || hashArray[currentIndex].tombstone)) {
        currentIndex++;
      }
      return currentIndex < capacity;
    }

    @Override
    public K next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      K key = hashArray[currentIndex].key;
      currentIndex++;
      return key;
    }
  }

  private static class Entry<K,V> {
    K key;
    V value;
    boolean tombstone;

    Entry(K k, V v) {
      this.key = k;
      this.value = v;
    }
  }
}
