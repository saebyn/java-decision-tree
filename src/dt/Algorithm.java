/**
 *  
 */

package dt;

import java.util.*;


public interface Algorithm {
  /**
   * Find the next attribute.
   *
   * For the initial attribute, pass an empty
   * chosenAttributes and use the returned attribute as the rootAttribute.
   * Then, walk the decision tree pre-order. At each decision, call this method
   * with the attribute/decision pairs that led to that node in 
   * chosenAttributes. Attach the returned Attribute to the decision.
   *
   */
  abstract public Attribute nextAttribute(Map<String, String> chosenAttributes, Set<String> usedAttributes);
}
