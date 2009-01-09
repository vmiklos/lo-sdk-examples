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

import com.sun.star.uno.XComponentContext;
import com.sun.star.beans.Property;
import com.sun.star.reflection.XIdlMethod;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import org.openoffice.inspector.Introspector;
import org.openoffice.inspector.model.SwingUnoNode;
import org.openoffice.inspector.model.UnoTreeModel;

public class InspectorPane
  extends JPanel
{

  private XIdlMethod[] idlMethods;
  private Property[] properties;
  private boolean includeContent = false;
  private XComponentContext xComponentContext;
  //private InspectorFrame      inspectorFrame;
  private Introspector introspector = null;
  //private CodeGenerator sourceCodeGenerator;
  private InspectionTree inspectionTreeComponent;
  private CodePane       codePane   = new CodePane();
  private FilterPane     filterPane = null;
  private String sTitle = "";

  /** The constructor of the inner class has a XMultiServiceFactory parameter.
   * @param xMultiServiceFactory XMultiServiceFactory
   */
  public InspectorPane(InspectionTree tree)
    throws InstantiationException, IllegalAccessException
  {
    this.filterPane              = new FilterPane((UnoTreeModel)tree.getModel());
    this.inspectionTreeComponent = tree;

    setLayout(new BorderLayout());
    JSplitPane splitPane = new JSplitPane(
      JSplitPane.VERTICAL_SPLIT,
      new JScrollPane(tree), new JScrollPane(this.codePane));
    add(splitPane, BorderLayout.CENTER);
    add(this.filterPane, BorderLayout.NORTH);
  }
  
  public CodePane getCodePane()
  {
    return this.codePane;
  }

  public void setTitle(String title)
  {
    assert title != null && title.length() > 0;

    this.sTitle = title;
  }

  public String getTitle()
  {
    return sTitle;
  }

  public InspectionTree getInspectionTree()
  {
    return this.inspectionTreeComponent;
  }
  
  public SwingUnoNode getSelectedNode()
  {
    return inspectionTreeComponent.getSelectedNode();
  }

}
    