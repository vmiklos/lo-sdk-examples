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

package org.openoffice.inspector.gui;

import org.openoffice.inspector.model.SwingUnoNode;
import org.openoffice.inspector.model.UnoTreeModel;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class InspectionTree 
  extends JTree 
{

  public InspectionTree(UnoTreeModel model)
  {
    setModel(model);
    setCellRenderer(new UnoTreeRenderer());
    model.setTree(this);
    
    InspectionTreeListener itl = new InspectionTreeListener(model, this);
    addTreeWillExpandListener(itl);
    addMouseListener(itl);
    
    // Ensure the visibility of the root node
    this.collapseRow(0);
    this.expandRow(0);
  }

  public SwingUnoNode getSelectedNode()
  {
    SwingUnoNode oUnoNode = null;
    TreePath aTreePath = getSelectionPath();
    Object oNode = aTreePath.getLastPathComponent();
    if (oNode instanceof SwingUnoNode)
    {
      oUnoNode = (SwingUnoNode) oNode;
    }
    return oUnoNode;
  }

}
