/**
 *
 */

package dt;

import java.util.*;


class Example {
  // TODO
  public static double extractDataFromAListOfExamples() {
    return 0.0;
  }

  private Map<String, String> values;
  private boolean classification;

  public Example(String[] attributeNames, String[] attributeValues,
                 boolean classification) {
    assert(attributeNames.length == attributeValues.length);

    for ( int i = 0 ; i < attributeNames.length ; i++ ) {
      values.put(attributeNames[i], attributeValues[i]);
    }
    
    this.classification = classification;
  }
}
