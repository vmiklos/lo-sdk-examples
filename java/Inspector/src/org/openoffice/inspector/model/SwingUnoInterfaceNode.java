/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openoffice.inspector.model;

import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author chris
 */
public class SwingUnoInterfaceNode extends SwingUnoNode
{
  public SwingUnoInterfaceNode(UnoInterfaceNode node)
  {
    super(node);
    
    add(new DummyNode());
  }
  
  @Override
  public void reintrospectChildren()
  {
    UnoNode unoNode = getUnoNode();
    List<UnoMethodNode> methods = unoNode.getMethods();
    
    DefaultMutableTreeNode methodsNode = new HideableMutableTreeNode("Methods");
    for(UnoMethodNode method : methods)
      methodsNode.add(new SwingUnoMethodNode(method));
    
    add(methodsNode);
  }
}
