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

import com.sun.star.uno.XComponentContext;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.openoffice.inspector.Inspector;

/**
 * Main frame of the Object Inspector.
 * @author Christian Lins (cli@openoffice.org)
 */
public class InspectorFrame extends JFrame
{

  private static InspectorFrame instance  = null;
  
  public static InspectorFrame getInstance()
  {
    return instance;
  }
  
  private XComponentContext xComponentContext;
  private Inspector._Inspector inspector;
  private JTabbedPane tabbedPane = new JTabbedPane();
  private MenuBar     menuBar    = new MenuBar(this);
  
  public InspectorFrame(Inspector._Inspector inspector, String title)
  {
    instance = this;
    
    this.inspector         = inspector;
    this.xComponentContext = inspector.getXComponentContext();

    setLayout(new BorderLayout());
    
    this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    setTitle(title);
    
    getContentPane().add(this.tabbedPane, BorderLayout.CENTER);
    setDefaultCloseOperation(HIDE_ON_CLOSE);
    setJMenuBar(this.menuBar);
    setSize(950, 750);
  }
  
  protected MenuBar getTheMenuBar()
  {
    return this.menuBar;
  }
  
  public JTabbedPane getTabbedPane()
  {
    return this.tabbedPane;
  }

  public void removeSelectedTabPane()
  {
    int idx = getTabbedPane().getSelectedIndex();
    getTabbedPane().remove(idx);
  }

  public void addInspectorPage(String title, JPanel pane) 
  {
    this.tabbedPane.addTab(title, pane);
  }

}
