/**
 *
 */

package dt;

import java.util.*;

public class ID3Algorithm implements Algorithm {
  private DecisionTree tree;

  public ID3Algorithm(DecisionTree decisiontree) {
    tree = decisiontree;
  }
  
  public String nextAttribute(Map<String, Boolean> path) throws DecisionLeafException {
    String bestAttribute = "";
    double bestInformationGain = 0;

    for ( Iterator<String> i = availableAttributes(path).iterator() ; 
          i.hasNext() ; ) {
      String attribute = i.next();

      double curInformationGain = informationGain(attribute, path);

      if ( curInformationGain > bestInformationGain ) {
        bestAttribute = attribute;
        bestInformationGain = curInformationGain;
      } else if ( bestInformationGain == 0 && bestAttribute.equals("") ) {
        bestAttribute = attribute;
      }
    }

    if ( bestInformationGain == 0 || bestAttribute.equals("") ) {
      if ( tree.countPositiveExamples(path) >=
           tree.countNegativeExamples(path) )
        throw new DecisionLeafException(true);
      else
        throw new DecisionLeafException(false);
    }

    return bestAttribute;
  }

  private List<String> availableAttributes(Map<String, Boolean> path) {
    List<String> attributes = tree.listAttributes();
    
    attributes.removeAll(path.keySet());

    return attributes;
  }

  private double nlog2(double value) {
    if ( value == 0 )
      return 0;

    return value * Math.log(value) / Math.log(2);
  }

  private double entropy(String attribute, Map<String, Boolean> specifiedAttributes) {
    double positiveExamples = tree.countPositiveExamples(attribute, specifiedAttributes);
    double negativeExamples = tree.countNegativeExamples(attribute, specifiedAttributes);

    return -nlog2(positiveExamples / tree.countExamples()) - 
            nlog2(negativeExamples / tree.countExamples());
  }

  private double entropy(Map<String, Boolean> specifiedAttributes) {
    double positiveExamples = tree.countPositiveExamples(specifiedAttributes);
    double negativeExamples = tree.countNegativeExamples(specifiedAttributes);

    return -nlog2(positiveExamples / tree.countExamples()) - 
            nlog2(negativeExamples / tree.countExamples());
  }

  private double informationGain(String attribute, Map<String, Boolean> specifiedAttributes) {
    double originalEntropy = entropy(specifiedAttributes);
    double newEntropy = entropy(attribute, specifiedAttributes);

    return originalEntropy - newEntropy;
  }
}
