/**
 *
 */

package dt;

import java.util.*;


class Decisions {
  public class BadDecision extends Exception {
  }

  private Map<String, Attribute> decisions;

  public Decisions() {
    decisions = new HashMap<String, Attribute>();
  }

  public void put(String decision, Attribute attribute) {
    decisions.put(decision, attribute);
  }

  public void clear() {
    decisions.clear();
  }

  /**
   * Returns the attribute based on the decision matching the provided value.
   *
   * Throws BadDecision exception if no decision matches.
   */
  public Attribute apply(String value) throws BadDecision {
    Attribute result = decisions.get(value);

    if ( result == null )
      throw new BadDecision();

    return result;
  }
}
