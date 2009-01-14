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

import java.awt.BorderLayout;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultEditorKit;
import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.syntaxkits.JavaSyntaxKit;
import org.openoffice.inspector.codegen.CodeGenerator;
import org.openoffice.inspector.codegen.CodeUpdateEvent;
import org.openoffice.inspector.codegen.CodeUpdateListener;
import org.openoffice.inspector.model.UnoTreeModel;

/**
 * Pane that contains a non-editable text component containing
 * generated sourcecode.
 * @author Christian Lins (cli@openoffice.org)
 */
public class CodePane 
  extends JPanel
  implements CodeUpdateListener
{
  
  private JEditorPane  code      = null;
  private UnoTreeModel treeModel = null;
  
  public CodePane(UnoTreeModel model)
  {
    DefaultSyntaxKit.initKit();
    
    this.treeModel = model;
    this.code = new JEditorPane();
    setLayout(new BorderLayout());
    add(new JLabel("Generated Codefragment:"), BorderLayout.NORTH);
    add(new JScrollPane(this.code), BorderLayout.CENTER);
    
    this.code.setEditable(false);
    this.code.setFont(new java.awt.Font("Monospaced", 0, 13));
    this.code.setEditorKit(new JavaSyntaxKit());
    //this.code.setContentType("text/java");
    this.code.setComponentPopupMenu(new CodeContextMenu(this));
    System.out.println(this.code.getEditorKit());
    
    // Register at CodeGenerators
    try
    {
      CodeGenerator[] codeGens = CodeGenerator.getInstances(
        model.getUnoRoot());
      
      for(CodeGenerator codeGen : codeGens)
      {
        if(codeGen != null)
          codeGen.addCodeUpdateListener(this);
      }
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  /**
   * Is called when a CodeGenerator changes something in the code.
   * @param event
   */
  public void codeUpdated(CodeUpdateEvent event)
  {
    // Update code
    setCode(event.getSourceCode());
    
    // Scroll to last changed line
    int caret = 0;
    while(caret < event.getFirstUpdatedLine() && caret >= 0)
      caret = event.getSourceCode().indexOf("\n", caret + 1);
    this.code.select(caret, 10);
  }
  
  public String getCode()
  {
    return this.code.getText();
  }
  
  protected void setCode(String code)
  {
    this.code.setText(code);
  }
}
