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
  }

  @Test public void testEmptyEquals() {
    assertEquals(makeOne(), makeOne());
  }

  @Test public void testOutlookTreeEquals() {
    assertEquals(makeOutlookTree(), makeOutlookTree());
  }

  @Test public void testEmptyNotEqualOutlookTree() {
    assertThat(makeOutlookTree(), not(equalTo(makeOne())));
  }

  @Test public void testOutlookTreeClearedEqualsEmpty() {
    assertEquals(makeOne(), makeOutlookTree().clear());
  }

  @Test (expected=UnknownDecisionException.class) public void testUnknownDecisionThrowsException() {
    DecisionTree tree = makeOne().setAttributes(new String[]{"Outlook"})
                                 .setDecisions("Outlook", new String[]{"Sunny", "Overcast"});

    // this causes exception
    tree.addExample(new String[]{"Rain"}, false);
  }

  @Test public void testOutlookOvercastApplyReturnsTrue() {
    Map<String, String> case1 = new HashMap<String, String>();
    case1.put("Outlook", "Overcast");
    assertTrue(makeOutlookTree().apply(case1));
  }
}
