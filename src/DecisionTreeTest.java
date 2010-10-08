/**
 *
 */

import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;

import dt.*;

public class DecisionTreeTest {
  private DecisionTree tree;

  @Before public void setUp() {
    tree = new DecisionTree();
    ID3Algorithm id3 = new ID3Algorithm(tree);

    tree.setAlgorithm(id3);
  }

  @Test public void applyWorksOnTrueExample() throws Exception {
    List<String> attributes1 = new Vector<String>();
    List<String> attributes2 = new Vector<String>();

    attributes1.add("attribute1");
    attributes1.add("attribute2");

    tree.addExample(true, attributes1);

    attributes2.add("attribute1");
    attributes1.add("attribute3");

    tree.addExample(false, attributes2);

    assertTrue( tree.apply(attributes1) ); 
  }

  @Test public void applyWorksOnFalseExample() throws Exception {
    List<String> attributes1 = new Vector<String>();
    List<String> attributes2 = new Vector<String>();

    attributes1.add("attribute1");
    attributes1.add("attribute2");

    tree.addExample(true, attributes1);

    attributes2.add("attribute1");
    attributes1.add("attribute3");

    tree.addExample(false, attributes2);

    assertFalse( tree.apply(attributes2) ); 
  }
}
