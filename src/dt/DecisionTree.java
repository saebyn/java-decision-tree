/**
 *  
 */

package dt;

import java.util.*;

public class DecisionTree {
  private Node treeRoot;
  private Algorithm algorithm;
  
  private class Outcome {
    public boolean success;
    public List<String> attributes;
  }

  private List<Outcome> outcomes;

  private boolean someOutcomesNotInTree;

  public DecisionTree() {
    outcomes = new Vector<Outcome>();
    treeRoot = new Node();
    someOutcomesNotInTree = false;
  }

  public void setAlgorithm(Algorithm alg) {
    algorithm = alg;
  }

  public void addExample(boolean success, List<String> attributes) {
    Outcome outcome = new Outcome();
    outcome.success = success;
    outcome.attributes = attributes;

    outcomes.add(outcome);
    someOutcomesNotInTree = true;
  }

  public boolean apply(List<String> attributes) throws InsufficientAttributesException {
    if ( someOutcomesNotInTree )
      compile();

    Node node = treeRoot;

    try {
      while ( !node.isLeaf() ) {
        // Because `attributes` contains only the attributes that are true,
        // we only need to follow the `true` path.
        node = node.navigate(attributes.contains(node.getValue()));

        if ( node.isLeaf() )
          return node.getDecision();
      }
    } catch ( NoSuchFieldException ex ) {
      // this can't happen, because we check the only conditions which
      // could cause this (that the root node is a leaf and that any other
      // node is a leaf before we call getValue() on it).
      System.err.println("Despite checking all conditions, we tried to" +
                         " get the value of a leaf node that doesn't have" +
                         " a value.");
    } catch ( NullPointerException ex2 ) {
      // this happens when the tree isn't properly built
      System.err.println("Tree wasn't built properly. Found and tried to" +
                         " follow a internal node that had a null pointer" +
                         " for a child.");
    }

    System.err.println("Tree wasn't built properly. Didn't find a leaf node.");
    return false;
  }

  public List<String> listAttributes() {
    Set<String> attributes = new HashSet<String>();

    for ( Iterator<Outcome> i = outcomes.iterator() ; i.hasNext() ; ) {
      for ( Iterator<String> j = i.next().attributes.iterator() ; j.hasNext() ; ) {
        attributes.add(j.next());
      }
    }

    return new ArrayList<String>(attributes);
  }

  private void compile() {
    treeRoot = new Node();

    Node node = treeRoot;

    do {
      if ( !node.isSet() ) {
        try {
          // find the next best attribute
          String attribute = algorithm.nextAttribute(node.getPath());

          node.setValue(attribute);
        } catch ( DecisionLeafException ex ) {
          boolean decision = ex.getDecision();
          node.setDecision(decision);
        }
      }

      if ( node.isLeaf() ) {
        node = node.getParent();
      } else if ( node.navigate(false) == null ) {
        Node child = new Node();
        node.addFalseChild(child);
        node = child;
      } else if ( node.navigate(true) == null ) {
        Node child = new Node();
        node.addTrueChild(child);
        node = child;
      } else {
        node = node.getParent();
      }
    } while ( node != null ); // we've tried to go up from the root node, so stop.

    someOutcomesNotInTree = false;
  }

  public int countExamples() {
    return outcomes.size();
  }

  private int countExamples(boolean success, Map<String, Boolean> where) {
    int count = 0;

    for ( Iterator<Outcome> i = outcomes.iterator() ; i.hasNext() ; ) {
      Outcome outcome = i.next();
      if ( outcome.success != success )
        continue;

      boolean incomplete = false;
      for ( Iterator<String> j = where.keySet().iterator() ; j.hasNext() ; ) {
        String attr = j.next();

        boolean needs = where.get(attr);
        boolean has = outcome.attributes.contains(attr);

        if ( !(needs && has || !needs && !has) ) {
          incomplete = true;
          break;
        }
      }

      if ( !incomplete )
        count++;
    }

    return count;
  }

  /**
   * countPositiveExamples
   *
   * Count the number of positive examples added to the decision tree.
   *
   * The `where` argument only includes outcomes where the given attributes
   * have the given boolean value.
   */
  public int countPositiveExamples(Map<String, Boolean> where) {
    return countExamples(true, where);
  }

  /**
   * countPositiveExamples
   *
   * Count the number of examples that include the attribute `attribute` with
   * a true value.
   *
   * The `where` argument only includes outcomes where the given attributes
   * have the given boolean value.
   */
  public int countPositiveExamples(String attribute, Map<String, Boolean> where) {
    where.put(attribute, true);
    return countPositiveExamples(where);
  }

  public int countPositiveExamples(String attribute) {
    return countPositiveExamples(attribute, new HashMap<String, Boolean>());
  }

  public int countNegativeExamples(Map<String, Boolean> where) {
    return countExamples(false, where);
  }

  public int countNegativeExamples(String attribute, Map<String, Boolean> where) {
    where.put(attribute, true);
    return countNegativeExamples(where);
  }

  public int countNegativeExamples(String attribute) {
    return countNegativeExamples(attribute, new HashMap<String, Boolean>());
  }
}
