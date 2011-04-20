/**
 *
 */

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.*;

import dt.*;

public class DecisionTreeTest {
  private DecisionTree makeOne() {
    return new DecisionTree();
  }

  private DecisionTree makeOutlookTree() {
    try {
      // example data from http://www.cise.ufl.edu/~ddd/cap6635/Fall-97/Short-papers/2.htm
      return makeOne().setAttributes(new String[]{"Outlook",  "Temperature", "Humidity", "Wind"})
                      .addExample(   new String[]{"Sunny",    "Hot",         "High",     "Weak"}, false)
                      .addExample(   new String[]{"Sunny",    "Hot",         "High",     "Strong"}, false)
                      .addExample(   new String[]{"Overcast", "Hot",         "High",     "Weak"}, true)
                      .addExample(   new String[]{"Rain",     "Mild",        "High",     "Weak"}, true)
                      .addExample(   new String[]{"Rain",     "Cool",        "Normal",   "Weak"}, true)
                      .addExample(   new String[]{"Rain",     "Cool",        "Normal",   "Strong"}, false)
                      .addExample(   new String[]{"Overcast", "Cool",        "Normal",   "Strong"}, true)
                      .addExample(   new String[]{"Sunny",    "Mild",        "High",     "Weak"}, false)
                      .addExample(   new String[]{"Sunny",    "Cool",        "Normal",   "Weak"}, true)
                      .addExample(   new String[]{"Rain",     "Mild",        "Normal",   "Weak"}, true)
                      .addExample(   new String[]{"Sunny",    "Mild",        "Normal",   "Strong"}, true)
                      .addExample(   new String[]{"Overcast", "Mild",        "High",     "Strong"}, true)
                      .addExample(   new String[]{"Overcast", "Hot",         "Normal",   "Weak"}, true)
                      .addExample(   new String[]{"Rain",     "Mild",        "High",     "Strong"}, false);
    } catch ( UnknownDecisionException e ) {
      fail();
      return makeOne(); // this is here to shut up compiler.
    }
  }

  @Test (expected=UnknownDecisionException.class) public void testUnknownDecisionThrowsException() throws UnknownDecisionException {
    DecisionTree tree = makeOne().setAttributes(new String[]{"Outlook"})
                                 .setDecisions("Outlook", new String[]{"Sunny", "Overcast"});

    // this causes exception
    tree.addExample(new String[]{"Rain"}, false);
  }

  @Test public void testOutlookOvercastApplyReturnsTrue() throws BadDecisionException {
    Map<String, String> case1 = new HashMap<String, String>();
    case1.put("Outlook", "Overcast");
    case1.put("Temperature", "Hot");
    case1.put("Humidity", "High");
    case1.put("Wind", "Strong");
    assertTrue(makeOutlookTree().apply(case1));
  }

  @Test (expected=BadDecisionException.class) public void testOutlookRainInsufficientDataThrowsException() throws BadDecisionException {
    Map<String, String> case1 = new HashMap<String, String>();
    case1.put("Outlook", "Rain");
    case1.put("Temperature", "Mild");
    makeOutlookTree().apply(case1);
  }

  public void attributeIsUsedOnlyOnceInTree(Attribute node, List<String> attributes) {
    for ( Attribute child : node.getDecisions().values() ) {
      if ( !child.isLeaf() ) {
        assertFalse( attributes.contains(child.getName()) );
        attributes.add(child.getName());
        attributeIsUsedOnlyOnceInTree(child, attributes);
      }
    }
  }

  @Test public void testAttributeIsUsedOnlyOnceInTree() {
    DecisionTree tree = makeOutlookTree();
    tree.compile();

    List<String> attributeList = new LinkedList<String>();
    attributeList.add(tree.getRoot().getName());
    attributeIsUsedOnlyOnceInTree(tree.getRoot(), attributeList);
  }


  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("DecisionTreeTest");
  }
}
