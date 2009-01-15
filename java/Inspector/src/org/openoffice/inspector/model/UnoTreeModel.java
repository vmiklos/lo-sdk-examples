/*************************************************************************
 *
 *  The Contents of this file are made available subject to the terms of
 *  the BSD license.
 *  
 *  Copyright (c) 2009 by Sun Microsystems, Inc.
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

import com.sun.star.uno.XComponentContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.openoffice.inspector.Introspector;
import org.openoffice.inspector.gui.InspectionTree;

/**
 * Model for the UNO Tree.
 * @author Christian Lins (cli@openoffice.org)
 */
public class UnoTreeModel 
  extends DefaultTreeModel
{
  
  private InspectionTree    inspectionTree    = null;
  private Introspector      introspector      = null;
  private XComponentContext xComponentContext = null;
  
  public UnoTreeModel(HideableMutableTreeNode root, XComponentContext xComponentContext)
  {
    super(root);
    root.setModel(this);
    
    this.introspector      = Introspector.getIntrospector(xComponentContext);
    this.xComponentContext = xComponentContext;
  }

  public XComponentContext getXComponentContext()
  {
    return this.xComponentContext;
  }
  
  public void nodeStructureChanged(DefaultMutableTreeNode node)
  {
    super.nodeStructureChanged(node);
    this.inspectionTree.expandPath(new TreePath(node.getPath()));
  }
  
  /**
   * @return Root UNO object
   */
  public Object getUnoRoot()
  {
    UnoNode rootCapsulate = (UnoNode)((HideableMutableTreeNode)getRoot()).getUserObject();
    return rootCapsulate.getUnoObject();
  }
  
  public void setFilter(String filter)
  {
    ((HideableMutableTreeNode)getRoot()).setFilter(filter);
  }
  
  public void setTree(InspectionTree tree)
  {
    this.inspectionTree = tree;
  }
  
}
