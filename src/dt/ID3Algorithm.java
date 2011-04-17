/**
 *
 */

package dt;

import java.util.*;

public class ID3Algorithm implements Algorithm {
  private List<Example> examples;

  public ID3Algorithm(List<Example> examples) {
    this.examples = examples;
  }

  public Attribute nextAttribute(Map<String, String> chosenAttributes) {
    // consider remainingAttributes
    //
    // for each remaining attribute, determine the information gain of using it
    // to choose among the examples selected by the chosenAttributes
    // if none give any information gain, return a classification attribute,
    // otherwise return the found attribute with the corresponding decisions
    // attached.
    return new Attribute(""); 
  }

  private Set<String> remainingAttributes(Map<String, String> chosenAttributes) {
    return new HashSet<String>();
  }
}
