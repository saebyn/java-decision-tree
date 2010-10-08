/**
 *
 */

package dt;

public class DecisionLeafException extends Exception {
  private boolean decision;

  public DecisionLeafException(boolean decision) {
    this.decision = decision;
  }

  public boolean getDecision() {
    return decision;
  }
}
