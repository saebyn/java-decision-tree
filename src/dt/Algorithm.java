/**
 *  
 */


package dt;

import java.util.Map;

public interface Algorithm {

  /**
   * nextAttribute
   *
   * Finds the next attribute to add to the tree given the already determined
   * set of decisions represented by `path`.
   *
   * The `path` argument is a mapping of attributes given by the path from the
   * decision tree root to the current (temporary) leaf, including the leaf itself.
   *
   * Throws a YesDecisionLeafException or a NoDecisionLeafException when no further
   * leaves need to be added for the given path. Otherwise, returns an attribute
   * id string for the next decision node.
   *
   */
  public String nextAttribute(Map<String, Boolean> path) throws DecisionLeafException;
}
