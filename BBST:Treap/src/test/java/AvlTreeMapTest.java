package hw6;

import hw6.bst.AvlTreeMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * In addition to the tests in BinarySearchTreeMapTest (and in OrderedMapTest & MapTest),
 * we add tests specific to AVL Tree.
 */
@SuppressWarnings("All")
public class AvlTreeMapTest extends BinarySearchTreeMapTest {

  @Override
  protected Map<String, String> createMap() {
    return new AvlTreeMap<>();
  }

  @Test
  @DisplayName("Testing Left Rotation on a simple tree of 3 consecutive ascending numbers being added")
  public void insertLeftRotation() {
    map.insert("1", "a");
    // System.out.println(avl.toString());
    // must print
    /*
        1:a
     */

    map.insert("2", "b");
    // System.out.println(avl.toString());
    // must print
    /*
        1:a,
        null 2:b
     */

    map.insert("3", "c"); // it must do a left rotation here!
    // System.out.println(avl.toString());
    // must print
    /*
        2:b,
        1:a 3:c
     */

    String[] expected = new String[]{
        "2:b",
        "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing Right Rotation on a simple tree of 3 consecutive descending numbers being added")
  public void insertRightRotation() {
    map.insert("3", "c");
    map.insert("2", "b");
    map.insert("1", "a");
    String[] expected = new String[]{
            "2:b",
            "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing Right-Left Rotation on a simple tree of 3 numbers being added in the order of smallest, largest, middle")
  public void insertRightLeftRotation() {
    map.insert("1", "a");
    map.insert("3", "c");
    map.insert("2", "b");
    String[] expected = new String[]{
            "2:b",
            "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing Left-Right Rotation on a simple tree of 3 numbers being added in the order of largest, smallest, middle")
  public void insertLeftRightRotation() {
    map.insert("3", "c");
    map.insert("1", "a");
    map.insert("2", "b");
    String[] expected = new String[]{
            "2:b",
            "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }
  @Test
  @DisplayName("Testing Left Rotation on a simple tree of 3 numbers after the fourth has been removed")
  public void removeLeftRotation() {
    map.insert("2", "b");
    map.insert("1", "a");
    map.insert("3", "c");
    map.insert("4", "d");
    String[] base = new String[]{
            "2:b",
            "1:a 3:c",
            "null null null 4:d"

    };
    assertEquals((String.join("\n", base) + "\n"), map.toString());
    map.remove("1");
    String[] expected = new String[]{
            "3:c",
            "2:b 4:d"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing Right Rotation on a simple tree of 3 numbers after the fourth has been removed")
  public void removeRightRotation() {
    map.insert("3", "c");
    map.insert("2", "b");
    map.insert("4", "d");
    map.insert("1", "a");
    String[] base = new String[]{
            "3:c",
            "2:b 4:d",
            "1:a null null null"

    };
    assertEquals((String.join("\n", base) + "\n"), map.toString());
    map.remove("4");
    String[] expected = new String[]{
            "2:b",
            "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }




  @Test
  @DisplayName("Testing Left-Right Rotation on a simple tree of 3 numbers after the fourth has been removed")
  public void removeLeftRightRotation() {
    map.insert("3", "c");
    map.insert("1", "a");
    map.insert("4", "d");
    map.insert("2", "b");
    String[] base = new String[]{
            "3:c",
            "1:a 4:d",
            "null 2:b null null"

    };
    assertEquals((String.join("\n", base) + "\n"), map.toString());
    map.remove("4");
    String[] expected = new String[]{
            "2:b",
            "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing Right Rotation on a simple tree of 3 numbers after the fourth has been removed")
  public void removeRightLeftRotation() {
    map.insert("2", "b");
    map.insert("4", "d");
    map.insert("1", "a");
    map.insert("3", "c");
    String[] base = new String[]{
            "2:b",
            "1:a 4:d",
            "null null 3:c null"

    };
    assertEquals((String.join("\n", base) + "\n"), map.toString());
    map.remove("1");
    String[] expected = new String[]{
            "3:c",
            "2:b 4:d"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Inserting null element")
  public void insertNull() {
    try {
      map.insert(null, null);
      fail("No/incorrect exception thrown");
    } catch (IllegalArgumentException ex) {
      System.out.println("Correct exception thrown");
    }
  }

  @Test
  @DisplayName("Removing nonexistant element")
  public void removeDoesNotExist() {
    map.insert("1", "a");
    map.insert("2", "b");
    map.insert("3", "c");
    try {
      map.remove("4");
      fail("No/incorrect exception thrown");
    } catch (IllegalArgumentException ex) {
      System.out.println("Correct exception thrown");
    }
  }

  @Test
  @DisplayName("Inserting a duplicate key")
  public void insertDuplicate() {
    map.insert("1", "a");
    try {
      map.insert("1", "b");
      fail("No/incorrect exception thrown");
    } catch (IllegalArgumentException ex) {
      System.out.println("Correct exception thrown");
    }
  }

  @Test
  @DisplayName("Inserting elements that do not cause rotation or any other structural changes other than the added node")
  public void insertWithoutRotation() {
    map.insert("2", "b");
    String[] expected1 = new String[]{
            "2:b"
    };
    assertEquals((String.join("\n", expected1) + "\n"), map.toString());
    map.insert("3", "c");
    String[] expected2 = new String[]{
            "2:b",
            "null 3:c"
    };
    assertEquals((String.join("\n", expected2) + "\n"), map.toString());
    map.insert("1", "a");
    String[] expected3 = new String[]{
            "2:b",
            "1:a 3:c"
    };
    assertEquals((String.join("\n", expected3) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Removing elements that do not cause rotation or any other structural changes other than the removed node")
  public void removeWithoutRotation() {
    map.insert("2", "b");
    map.insert("1", "a");
    map.insert("3", "c");
    String[] expected1 = new String[]{
            "2:b",
            "1:a 3:c"
    };
    assertEquals((String.join("\n", expected1) + "\n"), map.toString());
    map.remove("3");
    String[] expected2 = new String[]{
            "2:b",
            "1:a null"
    };
    assertEquals((String.join("\n", expected2) + "\n"), map.toString());
    map.remove("1");

    String[] expected3 = new String[]{
            "2:b",
    };
    assertEquals((String.join("\n", expected3) + "\n"), map.toString());
  }
}
