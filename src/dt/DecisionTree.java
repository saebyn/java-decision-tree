/**
 *
 */

package dt;

import java.util.*;

public class DecisionTree {
  /**
   * Contains the set of available attributes.
   */
  private LinkedHashSet<String> attributes;

  /**
   * Maps a attribute name to a set of possible decisions for that attribute.
   */
  private Map<String, Set<String> > decisions;
  private boolean decisionsSpecified;

  /**
   * Contains the examples to be processed into a decision tree.
   *
   * The 'attributes' and 'decisions' member variables should be updated
   * prior to adding examples that refer to new attributes or decisions.
   */
  private Examples examples;

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
    examples = new Examples();
    attributes = new LinkedHashSet<String>();
    decisions = new HashMap<String, Set<String> >();
    decisionsSpecified = false;
  }

  private void setDefaultAlgorithm() {
    if ( algorithm == null )
      setAlgorithm(new ID3Algorithm(examples));
  }

  public void setAlgorithm(Algorithm algorithm) {
    this.algorithm = algorithm;
  }

  /**
   * Saves the array of attribute names in an insertion ordered set.
   *
   * The ordering of attribute names is used when addExamples is called to
   * determine which values correspond with which names.
   *
   */
  public DecisionTree setAttributes(String[] attributeNames) {
    compiled = false;

    decisions.clear();
    decisionsSpecified = false;

    attributes.clear();

    for ( int i = 0 ; i < attributeNames.length ; i++ )
      attributes.add(attributeNames[i]);

    return this;
  }

  /**
   */
  public DecisionTree setDecisions(String attributeName, String[] decisions) {
    if ( !attributes.contains(attributeName) ) {
      // TODO some kind of warning or something
      return this;
    }

    compiled = false;
    decisionsSpecified = true;

    Set<String> decisionsSet = new HashSet<String>();
    for ( int i = 0 ; i < decisions.length ; i++ )
      decisionsSet.add(decisions[i]);

    this.decisions.put(attributeName, decisionsSet);

    return this;
  }

  /**
   */
  public DecisionTree addExample(String[] attributeValues, boolean classification) throws UnknownDecisionException {
    String[] attributes = this.attributes.toArray(new String[0]);

    if ( decisionsSpecified )
      for ( int i = 0 ; i < attributeValues.length ; i++ )
        if ( !decisions.get(attributes[i]).contains(attributeValues[i]) ) {
          throw new UnknownDecisionException(attributes[i], attributeValues[i]);
        }

    compiled = false;

    examples.add(attributes, attributeValues, classification);
    
    return this;
  }

  public DecisionTree addExample(Map<String, String> attributes, boolean classification) throws UnknownDecisionException {
    compiled = false;

    examples.add(attributes, classification);

    return this;
  }

  public boolean apply(Map<String, String> data) throws BadDecisionException {
    compile();

    return rootAttribute.apply(data);
  }

  private Attribute compileWalk(Attribute current, Map<String, String> chosenAttributes, Set<String> usedAttributes) {
    // if the current attribute is a leaf, then there are no decisions and thus no
    // further attributes to find.
    if ( current.isLeaf() )
      return current;

    // get decisions for the current attribute (from this.decisions)
    String attributeName = current.getName();

    // remove this attribute from all further consideration
    usedAttributes.add(attributeName);

    for ( String decisionName : decisions.get(attributeName) ) {
      // overwrite the attribute decision for each value considered
      chosenAttributes.put(attributeName, decisionName);

      // find the next attribute to choose for the considered decision
      // build the subtree from this new attribute, pre-order
      // insert the newly-built subtree into the open decision slot
      current.addDecision(decisionName, compileWalk(algorithm.nextAttribute(chosenAttributes, usedAttributes), chosenAttributes, usedAttributes));
    }

    // remove the attribute decision before we walk back up the tree.
    chosenAttributes.remove(attributeName);

    // return the subtree so that it can be inserted into the parent tree.
    return current;
  }

  public void compile() {
    // skip compilation if already done.
    if ( compiled )
      return;

    // if no algorithm is set beforehand, select the default one.
    setDefaultAlgorithm();

    Map<String, String> chosenAttributes = new HashMap<String, String>();
    Set<String> usedAttributes = new HashSet<String>();

    if ( !decisionsSpecified )
      decisions = examples.extractDecisions();

    // find the root attribute (either leaf or non)
    // walk the tree, adding attributes as needed under each decision
    // save the original attribute as the root attribute.
    rootAttribute = compileWalk(algorithm.nextAttribute(chosenAttributes, usedAttributes), chosenAttributes, usedAttributes);

    compiled = true;
  }

  public String toString() {
    compile();

    if ( rootAttribute != null )
      return rootAttribute.toString();
    else
      return "";
  }

  public Attribute getRoot() {
    return rootAttribute;
  }
}
