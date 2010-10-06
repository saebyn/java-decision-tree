/**
 *
 */
package dt;

import org.junit.*;
import static org.junit.Assert.*;

public class DecisionTreeTest {
  private DecisionTree tree;

  @Before public void setUp() {
    tree = new DecisionTree();
    ID3Algorithm id3 = new ID3Algorithm();

    tree.setAlgorithm(id3);
  }

  @Test public void isWorking() {
    assertTrue(true);
  }
}
