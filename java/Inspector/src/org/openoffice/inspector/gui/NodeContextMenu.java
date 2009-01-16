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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.openoffice.inspector.Inspector;
import org.openoffice.inspector.codegen.CodeGenerator;
import org.openoffice.inspector.model.HideableMutableTreeNode;
import org.openoffice.inspector.model.SwingUnoInterfaceNode;
import org.openoffice.inspector.model.SwingUnoMethodNode;
import org.openoffice.inspector.model.SwingUnoNode;
import org.openoffice.inspector.model.SwingUnoPropertyNode;
import org.openoffice.inspector.model.UnoInterfaceNode;
import org.openoffice.inspector.model.UnoMethodNode;
import org.openoffice.inspector.model.UnoNode;

/**
 * Context menu for all tree nodes.
 * @author Christian Lins (cli@openoffice.org)
 */
public class NodeContextMenu 
  extends JPopupMenu
{
  
  private JMenu         mnuCode        = new JMenu("Generate Code");
  private JMenuItem     mnuCodeInvoke  = new JMenuItem("Invoke");
  private JMenuItem     mnuCodeQuery   = new JMenuItem("Query");
  private JMenuItem     mnuCodeValue   = new JMenuItem("Get value");
  private JMenuItem     mnuInspectNode = new JMenuItem("Inspect");
  private JMenuItem     mnuInvokeNode  = new JMenuItem("Invoke...");
  private SwingUnoNode  node;
  
  public NodeContextMenu(final SwingUnoNode swingNode)
  {
    this.node = swingNode;
    
    if(swingNode instanceof SwingUnoMethodNode)
    {
      add(this.mnuInvokeNode);
    }
    else
    {
      add(this.mnuInspectNode);
    }
    
    add(this.mnuCode);
    this.mnuCode.add(this.mnuCodeInvoke);
    this.mnuCode.add(this.mnuCodeValue);
    this.mnuCode.add(this.mnuCodeQuery);
    
    this.mnuInspectNode.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent event)
      {
        Inspector.getInstance().inspect(
          node.getUnoObject(), 
          node.getUnoNode().getNodeDescription());
      }
    });
    
    this.mnuInvokeNode.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent event)
      {
        MethodParametersDialog mpdlg = 
          new MethodParametersDialog((UnoMethodNode)swingNode.getUnoNode());
        mpdlg.setVisible(true);
      }
    });
    
    this.mnuCodeValue.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent event)
      {
        if(swingNode instanceof SwingUnoMethodNode)
        {
          String[] msg = {"You cannot access a method in that way, sorry..."};
          JOptionPane.showMessageDialog(InspectorFrame.getInstance(), msg);
        }
        else if(swingNode instanceof SwingUnoPropertyNode)
        {
          try
          {
            Object obj = ((UnoNode)((HideableMutableTreeNode)node.getRoot())
              .getUserObject()).getUnoObject();
            CodeGenerator[] codeGens = CodeGenerator.getInstances(obj);
            for(CodeGenerator codeGen : codeGens)
            {
              if(codeGen != null)
              {
                codeGen.addAccessorCodeFor(
                  ((SwingUnoPropertyNode)swingNode).getProperty().Name);
              }
            }
          }
          catch(Exception ex)
          {
            ex.printStackTrace();
          }
        }
        else
        {
          String[] msg = {"You cannot access this node in that way, sorry..."};
          JOptionPane.showMessageDialog(InspectorFrame.getInstance(), msg);
        }
      }
    });
    
    this.mnuCodeQuery.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent event)
      {
        if(swingNode instanceof SwingUnoInterfaceNode)
        {
          try
          {
            // Create code for invoking this method
            Object obj = ((UnoNode)((HideableMutableTreeNode)node.getRoot())
              .getUserObject()).getUnoObject();
            CodeGenerator[] codeGens = CodeGenerator.getInstances(obj);
            for(CodeGenerator codeGen : codeGens)
            {
              if(codeGen != null)
              {
                codeGen.addQueryCodeFor(
                  swingNode.getUnoNode().getNodeDescription());
              }
            }
          }
          catch(Exception ex)
          {
            ex.printStackTrace();
          }
        }
        else
        {
          String[] msg = {"You cannot query this!"};
          JOptionPane.showMessageDialog(InspectorFrame.getInstance(), msg);
        }
      }
    });
    
    this.mnuCodeInvoke.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent event)
      {
        if(swingNode instanceof SwingUnoMethodNode)
        {
          try
          {
            // Create code for invoking this method
            Object obj = ((UnoNode)((HideableMutableTreeNode)node.getRoot())
              .getUserObject()).getUnoObject();
            CodeGenerator[] codeGens = CodeGenerator.getInstances(obj);
            for(CodeGenerator codeGen : codeGens)
            {
              if(codeGen != null)
              {
                codeGen.addInvokeCodeFor(
                  ((SwingUnoMethodNode)swingNode).getXIdlMethod());
              }
            }
          }
          catch(Exception ex)
          {
            ex.printStackTrace();
          }
        }
        else
        {
          String[] msg = {
            "The selected node is not callable!",
            "Please select a method instead."
            };
          JOptionPane.showMessageDialog(InspectorFrame.getInstance(), msg);
        }
      }
    });
  }
}
