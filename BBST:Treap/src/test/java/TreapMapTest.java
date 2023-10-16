package hw6;

import hw6.bst.TreapMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * In addition to the tests in BinarySearchTreeMapTest (and in OrderedMapTest & MapTest),
 * we add tests specific to Treap.
 */
@SuppressWarnings("All")
public class TreapMapTest extends BinarySearchTreeMapTest {

  final int SEED = 13;

  @Override
  protected Map<String, String> createMap() {
    return new TreapMap<>(SEED);
  }

  /**
   * random numbers in order with seed 13
   *-1160486312
   * 1412442200
   * 1909600750
   * 88367997
   * 220262727
   * -1020255876
   */

  @Test
  @DisplayName("Testing single right rotation with insert")
  public void singleRightRotationInsert() {
    map.insert("3", "t");
    //       3
    map.insert("2", "t");
    //       3
    //      /
    //     2
    map.remove("2");
    //       3
    map.insert("2", "t");
    //       3
    //      /
    //     2
    map.insert("1", "t");
    //       3
    //      /
    //     2
    //    /
    //   1
    //rotates to
    //       3
    //      /
    //     1
    //      \
    //       2
    String[] expected = new String[]{
            "3:t:-1160486312",
            "1:t:88367997 null",
            "null 2:t:1909600750 null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing single left rotation with insert")
  public void singleLeftRotationInsert() {
    map.insert("1", "t");
    map.insert("2", "t");
    map.remove("2");
    map.insert("2", "t");
    map.insert("3", "t");
    String[] expected = new String[]{
            "1:t:-1160486312",
            "null 3:t:88367997",
            "null null 2:t:1909600750 null"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Double left rotation after insertion 4 times")
  public void doubleLeftRotationAfterInsertion() {
    map.insert("1", "t");
    //       1
    map.insert("2", "t");
    //       1
    //        \
    //          2
    map.insert("3", "t");
    //       1
    //        \
    //          2
    //           \
    //            3
    map.insert("4", "t");
    //       1
    //        \
    //          2
    //           \
    //            3
    //             \
    //              4
    // after first rotation:
    //       1
    //        \
    //          2
    //           \
    //            4
    //           /
    //          3
    // after second rotation:
    //       1
    //        \
    //          4
    //          /
    //         2
    //          \
    //           3
    String[] expected = new String[]{
            "1:t:-1160486312",
            "null 4:t:88367997",
            "null null 2:t:1412442200 null",
            "null null null null null 3:t:1909600750 null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Double right rotation after insertion 4 times")
  public void doubleRightRotationAfterInsertion() {
    map.insert("4", "t");
    map.insert("3", "t");
    map.insert("2", "t");
    map.insert("1", "t");

    String[] expected = new String[]{
            "4:t:-1160486312",
            "1:t:88367997 null",
            "null 3:t:1412442200 null null",
            "null null 2:t:1909600750 null null null null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing for single downward rotation with a left rotation")
  public void sinkWithOneChildLeftRotationRemove() {
    map.insert("1", "t");
    //       1
    map.insert("2", "t");
    //       1
    //        \
    //         2
    map.insert("3", "t");
    //       1
    //        \
    //         2
    //          \
    //           3
    map.insert("4", "t");
    //       1
    //        \
    //         4
    //        /
    //       2
    //        \
    //         3
    map.remove("2");
    //       1
    //        \
    //         4
    //        /
    //       3
    //      /
    //     2
    //which balances to
    //       1
    //        \
    //         4
    //        /
    //       3
    String[] expected = new String[]{
            "1:t:-1160486312",
            "null 4:t:88367997",
            "null null 3:t:1909600750 null",
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing for single downward rotation with a right rotation")
  public void sinkWithOneChildRightRotationRemove() {
    map.insert("4", "t");
    map.insert("3", "t");
    map.insert("2", "t");
    map.insert("1", "t");
    map.remove("3");
    String[] expected = new String[]{
            "4:t:-1160486312",
            "1:t:88367997 null",
            "null 2:t:1909600750 null null",
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Sinking root with right rotations")
  public void sinkingRootRightRotate() {
    map.insert("4", "t");
    map.insert("3", "t");
    map.insert("2", "t");
    map.insert("1", "t");
    map.remove("4");

    String[] expected = new String[]{
            "1:t:88367997",
            "null 3:t:1412442200",
            "null null 2:t:1909600750 null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Sinking root with left rotations")
  public void sinkingRootLeftRotate() {
    map.insert("1", "t");
    map.insert("2", "t");
    map.insert("3", "t");
    map.insert("4", "t");
    map.remove("1");

    String[] expected = new String[]{
            "4:t:88367997",
            "2:t:1412442200 null",
            "null 3:t:1909600750 null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Sink/remove node with 2 children then sink it to the bottom")
  public void sinkAndRemoveWithTwoChildrenThenBringToBottom() {
    map.insert("1", "t");
    map.insert("2", "t");
    map.insert("3", "t");
    map.insert("4", "t");
    map.insert("5", "t");
    map.remove("4");
    String[] expected = new String[]{
            "1:t:-1160486312",
            "null 5:t:220262727",
            "null null 2:t:1412442200 null",
            "null null null null null 3:t:1909600750 null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Inserting with no structural changes")
  public void insertLeafNoStructuralChange() {
    map.insert("1", "t");
    map.insert("2", "t");
    String[] expected = new String[]{
            "1:t:-1160486312",
            "null 2:t:1412442200"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Removing with no structural changes")
  public void removeLeafNoStructuralChange() {
    map.insert("1", "t");
    map.insert("2", "t");
    map.remove("2");
    String[] expected = new String[]{
            "1:t:-1160486312",
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }
}