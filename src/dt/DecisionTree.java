/**
 *
 */

package dt;

import java.util.*;

public class DecisionTree {
  /**
   * Contains the set of available attributes.
   */
  private Set<String> attributes;

  /**
   * Maps a attribute name to a set of possible decisions for that attribute.
   */
  private Map<String, Set<String> > decisions;

  /**
   * Contains the examples to be processed into a decision tree.
   *
   * The 'attributes' and 'decisions' member variables should be updated
   * prior to adding examples that refer to new attributes or decisions.
   */
  private List<Example> examples;

  /**
   * Indicates if the provided data has been processed into a decision tree.
   *
   * This value is initially false, and is reset any time additional data is
   * provided.
   */
  private boolean compiled;

  /**
   * Contains the top-most attribute of the decision tree.
   *
   * For a tree where the decision requires no attributes,
   * the rootAttribute yields a boolean classification.
   *
   */
  private Attribute rootAttribute;

  private Algorithm algorithm;

  public DecisionTree() {
    algorithm = null;
  }

  private void setDefaultAlgorithm() {
    if ( algorithm == null )
      setAlgorithm(new ID3Algorithm(examples));
  }

  public void setAlgorithm(Algorithm algorithm) {
    this.algorithm = algorithm;
  }

  /**
   * Modifies copy of this object and returns it.
   */
  public DecisionTree clear() {
    // don't forget to reset compiled var
    return this;
  }

  /**
   * Modifies copy of this object and returns it.
   */
  public DecisionTree setAttributes(String[] attributeNames) {
    // don't forget to reset compiled var
    return this;
  }

  /**
   * Modifies copy of this object and returns it.
   */
  public DecisionTree addExample(String[] attributeValues, boolean classification) {
    // don't forget to reset compiled var
    return this;
  }

  /**
   * Modifies copy of this object and returns it.
   */
  public DecisionTree setDecisions(String attributeName, String[] decisions) {
    // don't forget to reset compiled var

    return this;
  }

  public boolean apply(Map<String, String> data) throws Decisions.BadDecision {
    if ( !compiled )
      compile();

    // rootAttribute could be null, what to do, what to do...

    return rootAttribute.apply(data);
  }

  private void compile() {
    // move data from attributes, examples, and decisions into rootAttribute
    // by using algorithm.
    setDefaultAlgorithm();
    compiled = true;
  }
}
