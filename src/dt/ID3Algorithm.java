/**
 *
 */

package dt;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ID3Algorithm implements Algorithm {
  final Logger logger = LoggerFactory.getLogger(ID3Algorithm.class);
  private Examples examples;

  public ID3Algorithm(Examples examples) {
    this.examples = examples;
  }

  /**
   * Returns the next attribute to be chosen.
   *
   * chosenAttributes represents the decision path from the root attribute
   * to the node under consideration. usedAttributes is the set of all
   * attributes that have been incorporated into the tree prior to this
   * call to nextAttribute(), even if the attributes were not used in the path
   * to the node under consideration.
   *
   * Results are undefined if examples.count() == 0.
   */
  public Attribute nextAttribute(Map<String, String> chosenAttributes, Set<String> usedAttributes) {
    double currentGain = 0.0, bestGain = 0.0;
    String bestAttribute = "";

    /*
     * If there are no positive examples for the already chosen attributes,
     * then return a false classifier leaf. If no negative examples,
     * then return a true classifier leaf.
     */
    if ( examples.countPositive(chosenAttributes) == 0 )
      return new Attribute(false);
    else if ( examples.countNegative(chosenAttributes) == 0 )
      return new Attribute(true);

    logger.debug("Choosing attribute out of {} remaining attributes.",
                 remainingAttributes(usedAttributes).size());
    logger.debug("Already chosen attributes/decisions are {}.", chosenAttributes);

    for ( String attribute : remainingAttributes(usedAttributes) ) {
      // for each remaining attribute, determine the information gain of using it
      // to choose among the examples selected by the chosenAttributes
      // if none give any information gain, return a leaf attribute,
      // otherwise return the found attribute as a non-leaf attribute
      currentGain = informationGain(attribute, chosenAttributes);
      logger.debug("Evaluating attribute {}, information gain is {}",
                  attribute, currentGain);
      if ( currentGain > bestGain ) {
        bestAttribute = attribute;
        bestGain = currentGain;
      }
    }

    // If no attribute gives information gain, generate leaf attribute.
    // Leaf is true if there are any true classifiers.
    // If there is at least one negative example, then the information gain
    // would be greater than 0.
    if ( bestGain == 0.0 ) {
      boolean classifier = examples.countPositive(chosenAttributes) > 0;
      logger.debug("Creating new leaf attribute with classifier {}.", classifier);
      return new Attribute(classifier);
    } else {
      logger.debug("Creating new non-leaf attribute {}.", bestAttribute);
      return new Attribute(bestAttribute);
    }
  }

  private Set<String> remainingAttributes(Set<String> usedAttributes) {
    Set<String> result = examples.extractAttributes();
    result.removeAll(usedAttributes);
    return result;
  }

  private double entropy(Map<String, String> specifiedAttributes) {
    double totalExamples = examples.count();
    double positiveExamples = examples.countPositive(specifiedAttributes);
    double negativeExamples = examples.countNegative(specifiedAttributes);

    return -nlog2(positiveExamples / totalExamples) - 
            nlog2(negativeExamples / totalExamples);
  }

  private double entropy(String attribute, String decision, Map<String, String> specifiedAttributes) {
    double totalExamples = examples.count(attribute, decision, specifiedAttributes);
    double positiveExamples = examples.countPositive(attribute, decision, specifiedAttributes);
    double negativeExamples = examples.countNegative(attribute, decision, specifiedAttributes);

    return -nlog2(positiveExamples / totalExamples) - 
            nlog2(negativeExamples / totalExamples);
  }

  private double informationGain(String attribute, Map<String, String> specifiedAttributes) {
    double sum = entropy(specifiedAttributes);
    double examplesCount = examples.count(specifiedAttributes);

    if ( examplesCount == 0 )
      return sum;

    Map<String, Set<String> > decisions = examples.extractDecisions();

    for ( String decision : decisions.get(attribute) ) {
      double entropyPart = entropy(attribute, decision, specifiedAttributes);
      double decisionCount = examples.countDecisions(attribute, decision);

      sum += -(decisionCount / examplesCount) * entropyPart;
    }

    return sum;
  }

  private double nlog2(double value) {
    if ( value == 0 )
      return 0;

    return value * Math.log(value) / Math.log(2);
  }
}
