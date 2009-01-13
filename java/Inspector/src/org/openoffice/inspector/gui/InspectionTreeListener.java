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

package org.openoffice.inspector.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import org.openoffice.inspector.model.DummyNode;
import org.openoffice.inspector.model.SwingUnoNode;
import org.openoffice.inspector.model.UnoTreeModel;

/**
 * This listener is notified when the user changes something
 * on the inspection tree.
 * With interpreting the events this listener is able to
 * change the model of the InspectionTree.
 * @author Christian Lins (cli@openoffice.org)
 */
class InspectionTreeListener 
  extends MouseAdapter
  implements TreeWillExpandListener
{

  private InspectionTree  tree;
  private UnoTreeModel    model;
  
  public InspectionTreeListener(UnoTreeModel model, InspectionTree tree)
  {
    this.model = model;
    this.tree  = tree;
  }
  
  public void treeWillCollapse(TreeExpansionEvent event) 
    throws ExpandVetoException
  {
    TreePath path    = event.getPath();
    Object   leafObj = path.getLastPathComponent();
    
    // When collapsing a SwingUnoNode we remove all sub nodes
    // from it and add a DummyNode.
    if(leafObj instanceof SwingUnoNode)
    {
      SwingUnoNode leaf = (SwingUnoNode)leafObj;
      leaf.removeAllChildren();
      leaf.add(new DummyNode());
      
      // Show changes in the tree by firing some events
      this.model.reload(leaf);
    }
  }

  public void treeWillExpand(TreeExpansionEvent event) 
    throws ExpandVetoException
  {
    TreePath path  = event.getPath();
    Object leafObj = path.getLastPathComponent();
    
    // If leaf is a SwingUnoNode we need to add all
    // necessary sub nodes to it before we can really
    // expand the whole thing
    if(leafObj instanceof SwingUnoNode)
    {
      SwingUnoNode leaf = (SwingUnoNode)leafObj;
      
      // Remove the dummy leaf
      leaf.removeAllChildren();
      
      // Introspect the node and add necessary children
      leaf.reintrospectChildren();
      
      // Show changes in the tree by firing some events
      this.model.reload(leaf);
    }
  }

  @Override
  public void mouseClicked(MouseEvent event)
  {
    if(SwingUtilities.isRightMouseButton(event))
    {
      int row = tree.getRowForLocation(event.getX(), event.getY());
      tree.setSelectionRow(row);
      SwingUnoNode node = tree.getSelectedNode();
      if(node != null)
      {
        new NodeContextMenu(node).show(tree, event.getX(), event.getY());
      }
    }
  }

}
