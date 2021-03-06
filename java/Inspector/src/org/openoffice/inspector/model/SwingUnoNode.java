/*************************************************************************
 *
 *  The Contents of this file are made available subject to the terms of
 *  the BSD license.
 *  
 *  Copyright (c) 2003, 2009 by Sun Microsystems, Inc.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of Sun Microsystems, Inc. nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 *  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *  COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 *  OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 *  TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 *  USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *     
 *************************************************************************/

package org.openoffice.inspector.model;

import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Root node of the tree.
 * @author Christian Lins (cli@openoffice.org)
 */
public class SwingUnoNode
  extends HideableMutableTreeNode
{

  public SwingUnoNode(UnoNode unoNode)
  {
    super(unoNode);
  }
  
  /** Creates a new instance of SwingUnoNode */
  public SwingUnoNode(Object userObject)
  {
    super(new UnoNode(userObject));
    
    add(new DummyNode());
  }

  /**
   * Looks for child nodes and adds them to itself.
   * It does not look for duplicates so a call of removeAllChilds()
   * is recommended.
   */
  public void reintrospectChildren()
  {
    // Ask the capsulated UnoNode for children
    UnoNode unoNode = getUnoNode();
    
    // Add all services implemented by the object
    DefaultMutableTreeNode servicesNode = new HideableMutableTreeNode("Services");
    List<UnoServiceNode> services = unoNode.getSupportedServices();
    if(services != null)
    {
      add(servicesNode);
      for(UnoServiceNode service : services)
        servicesNode.add(new SwingUnoServiceNode(service));
    }
    
    // Add interfaces of userObject to the interfaces subnode
    DefaultMutableTreeNode interfacesNode = new HideableMutableTreeNode("Interfaces");
    List<UnoInterfaceNode> interfaces     = unoNode.getInterfaces();
    if(interfaces != null)
    {
      add(interfacesNode);
      for(UnoInterfaceNode inf : interfaces)
        interfacesNode.add(new SwingUnoInterfaceNode(inf));
    }
    
    // Add methods of userObject to the methods subnode
    DefaultMutableTreeNode methodsNode = new HideableMutableTreeNode("Methods");
    List<UnoMethodNode> methods = unoNode.getMethods();
    if(methods != null)
    {
      add(methodsNode);
      for(UnoMethodNode method : methods)
        methodsNode.add(new SwingUnoMethodNode(method));
    }
    
    // Read out properties of the userObject
    DefaultMutableTreeNode propsNode  = new HideableMutableTreeNode("Properties");
    List<UnoPropertyNode>  properties = unoNode.getProperties();
    if(properties != null)
    {
      add(propsNode);
      for(UnoPropertyNode prop : properties)
        propsNode.add(new SwingUnoPropertyNode(prop));
    }
  }

  public Object getUnoObject()
  {
    return getUnoNode().getUnoObject();
  }
  
  public UnoNode getUnoNode()
  {
    return (UnoNode)getUserObject();
  }

}
