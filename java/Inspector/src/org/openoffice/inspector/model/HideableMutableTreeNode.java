package org.openoffice.inspector.model;

import javax.swing.tree.*;

/**
 * A TreeNode that can be set invisible (e.g. when using filters).
 * @author chris
 */
public class HideableMutableTreeNode extends DefaultMutableTreeNode
{

  public boolean isVisible = true;

  /**
   * Creates a tree node that has no parent and no children, but which 
   * allows children.
   */
  public HideableMutableTreeNode()
  {
  }

  /**
   * Creates a tree node with no parent, no children, but which allows 
   * children, and initializes it with the specified user object.
   * 
   * @param  userObject - an Object provided by the user that 
   *                      constitutes the node's data
   */
  public HideableMutableTreeNode(Object userObject)
  {
    super(userObject);
  }

  /**
   * Creates a tree node with no parent, no children, initialized with the 
   * specified user object, and that allows children only if specified.
   * 
   * @param  userObject     - an Object provided by the user that describes the node's data
   * @param  ballowsChildren - if true, the node is allowed to have childnodes -- otherwise, it is always a leaf node
   */
  public HideableMutableTreeNode(Object userObject, boolean allowsChildren)
  {
    super(userObject, allowsChildren);
  }

  /**
   * Checks if the node is visible. 
   * 
   * @return  true if the node is visible, else false
   */
  public boolean isVisible()
  {
    return this.isVisible;
  }

  /**
   * Sets if the node is visible. 
   * 
   * @param  returns true if the node is visible, else false
   */
  public void setVisible(boolean isVisible)
  {
    this.isVisible = isVisible;
  }

 /* public void addDummyNode()
  {
    removeDummyNode();
    DefaultMutableTreeNode oDefaultMutableTreeNode = new DefaultMutableTreeNode(SDUMMY);
    add(oDefaultMutableTreeNode);

  }

  public boolean removeDummyNode()
  {
    boolean breturn = false;
    if (getChildCount() == 1)
    {
      DefaultMutableTreeNode oDefaultMutableTreeNode = (DefaultMutableTreeNode) getChildAt(0);
      if (oDefaultMutableTreeNode != null)
      {
        if (oDefaultMutableTreeNode.getUserObject().equals(SDUMMY))
        {
          remove(0);
          breturn = true;
        }
      }
    }
    return breturn;
  }*/
}
