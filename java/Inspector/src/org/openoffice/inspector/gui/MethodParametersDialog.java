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

import com.sun.star.reflection.InvocationTargetException;
import com.sun.star.reflection.XIdlMethod;
import com.sun.star.script.CannotConvertException;
import com.sun.star.script.XInvocation;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.openoffice.inspector.Inspector;
import org.openoffice.inspector.Introspector;
import org.openoffice.inspector.model.UnoMethodNode;

/**
 * @author bc93774
 * @author Christian Lins (cli@openoffice.org)
 */
public class MethodParametersDialog 
  extends JDialog
  implements ActionListener
{

  private XIdlMethod    xIdlMethod   = null;
  private JButton       helpButton   = new JButton("Help");
  private JButton       invokeButton = new JButton("Invoke");
  private MethodParametersPanel methParamPanel;
  private UnoMethodNode unoMethodNode= null;

  public MethodParametersDialog(UnoMethodNode unoMethodNode)
  {
    this.xIdlMethod     = unoMethodNode.getXIdlMethod();
    this.unoMethodNode  = unoMethodNode;
    
    super.setModal(true);

    JPanel headerPanel = new JPanel(new BorderLayout());
    JLabel lblHeader = new JLabel();
    lblHeader.setText("Please insert the values for the given Parameters of the method '" + xIdlMethod.getName() + "'");
    headerPanel.add(lblHeader, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.add(this.helpButton);
    buttonPanel.add(this.invokeButton);
    
    this.methParamPanel = new MethodParametersPanel(unoMethodNode);
    
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(headerPanel, BorderLayout.NORTH);
    getContentPane().add(methParamPanel, BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    
    pack();
    setLocation(350, 350);
    setTitle("Object Inspector - Parameter Values of '" + xIdlMethod.getName() + "'");
    super.setFocusable(true);
    super.setFocusableWindowState(true);
    super.requestFocus();
    
    this.invokeButton.addActionListener(this);
    this.helpButton.addActionListener(this);
  }

  public void actionPerformed(ActionEvent event)
  {
    if(event.getSource().equals(this.invokeButton))
    {
      try
      {
        int numin  = this.unoMethodNode.getXIdlMethod().getParameterInfos().length;
        Object[][] inout = new Object[1][numin];
        for(int n = 0; n < numin; n++)
        {
          inout[0][n] = this.methParamPanel.getMethodParameterComponent(n).getParamValue();
        }
        
        // Wohoo go on invoking...
        Object ret = this.unoMethodNode.getXIdlMethod().invoke(
          this.unoMethodNode.getUnoObject(), 
          inout);
        
        if(Introspector.isObjectPrimitive(ret))
        {
          String[] msg = {"The Invocation returned:", ret.toString()};
          JOptionPane.showMessageDialog(null, msg);
        }
        else
        {
          Inspector.getInstance().inspect(
            ret, 
            this.unoMethodNode.getName() + " Result");
        }
        setVisible(false);
      }
      catch(Exception ex)
      {
        String[] msg =
        {
          "The Invocation caused an exception:",
          ex.getClass().getName(),
          ex.getLocalizedMessage()
        };
        JOptionPane.showMessageDialog(null, msg);
      }
    }
  }
  
}
