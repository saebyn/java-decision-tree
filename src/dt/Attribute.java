/**
 *
 */

package dt;

import java.util.*;


class Attribute {
  /**
   * Indicates if this attribute yields a classification (true) or has child 
   * decisions that point to further attributes (false).
   */
  private boolean leaf;

  private String attributeName;
  private Decisions decisions;
  private boolean classification;

  public Attribute(String name) {
    leaf = true;
    classification = false;
    decisions = new Decisions();
    attributeName = name;
  }

  public boolean isLeaf() {
    return leaf;
  }

  public void setClassification(boolean classification) {
    if ( !leaf ) {
      decisions.clear();
      leaf = true;
    }
    this.classification = classification;
  }

  /**
   * Returns the classification of the followed decision.
   *
   * Undefined if isLeaf() returns false.
   */
  public boolean getClassification() {
    return classification;
  }

  public boolean apply(Map<String, String> data) throws Decisions.BadDecision {
    if ( isLeaf() )
      return getClassification();

    Attribute nextAttribute = decisions.apply(data.get(attributeName));
    return nextAttribute.apply(data);
  }

  public void addDecision(String decision, Attribute attribute) {
    leaf = false;
    decisions.put(decision, attribute);
  }
}
