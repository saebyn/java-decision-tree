/**
 *
 */

package dt;

import java.util.*;


public class Attribute {
  /**
   * Indicates if this attribute yields a classification (true) or has child 
   * decisions that point to further attributes (false).
   */
  private boolean leaf;

  private String attributeName;
  private Decisions decisions;
  private boolean classification;

  public Attribute(boolean classification) {
    leaf = true;
    this.classification = classification;
    decisions = new Decisions();
    attributeName = null;
  }

  public Attribute(String name) {
    leaf = false;
    attributeName = name;
    decisions = new Decisions();
  }

  public String getName() {
    return attributeName;
  }

  public boolean isLeaf() {
    return leaf;
  }

  public void setClassification(boolean classification) {
    assert ( leaf );

    this.classification = classification;
  }

  /**
   * Returns the classification of the followed decision.
   *
   * Undefined if isLeaf() returns false.
   */
  public boolean getClassification() {
    assert ( leaf );

    return classification;
  }

  public boolean apply(Map<String, String> data) throws BadDecisionException {
    if ( isLeaf() )
      return getClassification();

    Attribute nextAttribute = decisions.apply(data.get(attributeName));
    return nextAttribute.apply(data);
  }

  public void addDecision(String decision, Attribute attribute) {
    assert ( !leaf );

    decisions.put(decision, attribute);
  }

  public String toString() {
    StringBuffer b = new StringBuffer();

    for ( Map.Entry<String, Attribute> e : decisions.getMap().entrySet() ) {
      b.append(getName());
      b.append(" -> ");
      if ( e.getValue().isLeaf() )
        b.append(e.getValue().getClassification());
      else
        b.append(e.getValue().getName());
      b.append(" [label=\"");
      b.append(e.getKey());
      b.append("\"]\n");

      b.append(e.getValue().toString());
    }

    return b.toString();
  }

  public Map<String, Attribute> getDecisions() {
    return decisions.getMap();
  }
}
