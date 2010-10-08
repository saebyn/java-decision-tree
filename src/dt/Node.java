/**
 *
 */

package dt;

import java.util.*;

public class Node {
  protected Node parent;
  protected int depth;

  private Node falseChild;
  private Node trueChild;

  private boolean leaf;
  private boolean decision;
  private boolean set;
  private String value;

  public Node() {
    leaf = true;
    depth = 0;
    set = false;
  }

  public boolean isSet() {
    return set;
  }

  public void addTrueChild(Node child) {
    setChildParent(child);
    
    trueChild = child;
  }

  public void addFalseChild(Node child) {
    setChildParent(child);

    falseChild = child;
  }

  public Map<String, Boolean> getPath() {
    // collect the attributes and their value from the root to this node.
    Map<String, Boolean> path = new HashMap<String, Boolean>();
    Node node = this;

    while ( node.hasParent() ) {
      Node child = node;
      node = node.getParent();

      try {
        if ( node.navigate(true) == child )
          path.put(node.getValue(), true);
        else
          path.put(node.getValue(), false);
      } catch ( NoSuchFieldException e ) {
        // it's not possible because it can only happen when
        // calling getValue() on a leaf node. Since the first thing
        // we do is skip to the node's parent, it can't happen.
        System.err.println("Node.getPath(): produced a NoSuchFieldException" +
                           " even though it's not possible.");
      }
    }
  
    return path;
  }

  public Node navigate(boolean choice) {
    if ( choice == true )
      return trueChild;
    else
      return falseChild;
  }

  public Node getParent() {
    return parent;
  }

  public boolean hasParent() {
    if ( depth == 0 )
      return false;
    else
      return true;
  }

  public boolean isLeaf() {
    return leaf;
  }

  public void setValue(String value) {
    leaf = false;
    set = true;
    this.value = value;
  }

  public String getValue() throws NoSuchFieldException {
    if ( !isLeaf() )
      return value;
    else
      throw new NoSuchFieldException();
  }

  public void setDecision(boolean decision) {
    set = true;
    this.decision = decision;
  }

  public boolean getDecision() throws NoSuchFieldException {
    if ( isLeaf() )
      return decision;
    else
      throw new NoSuchFieldException();
  }

  private void setChildParent(Node child) {
    child.parent = this;
    child.depth = depth + 1;

    leaf = false;
    set = true;
  }

}
