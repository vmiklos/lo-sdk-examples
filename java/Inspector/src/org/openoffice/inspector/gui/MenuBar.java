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

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import org.openoffice.inspector.Inspector;
import org.openoffice.inspector.codegen.CodeGenerator;
import org.openoffice.inspector.codegen.Language;
import org.openoffice.inspector.model.SwingUnoNode;
import org.openoffice.inspector.model.UnoTreeModel;

/**
 * Menu bar of the InspectionFrame.
 * @author Christian Lins (cli@openoffice.org)
 */
public class MenuBar extends JMenuBar
{
  
  private Language             currentLang = Language.Java;
  private InspectorFrame       frame;
  private JMenu                mnuOptions          = new JMenu("Options");
  private JRadioButtonMenuItem mnuOptionsJava      = null;
  private JRadioButtonMenuItem mnuOptionsCPlusPlus = null;
  private JRadioButtonMenuItem mnuOptionsBasic     = null;
  private JRadioButtonMenuItem mnuOptionsPython    = null;
  
  public MenuBar(InspectorFrame frame)
  {
    this.frame = frame;
    insertMenus();
  }
  
  private void insertMenus()
  {
    addFileMenu(this);
    addInspectMenu(this);
    addOptionsMenu(this);
    addHelpMenu(this);
  }
  
  private void addHelpMenu(JMenuBar inspectMenuBar)
  {
    JMenu mnuHelp = new JMenu("Help");
    mnuHelp.add(getHelpMenuItem("Idl-Help"));
    inspectMenuBar.add(mnuHelp);
  }
    
  private JMenuItem getHelpMenuItem(String menuTitle)
  {
    JMenuItem mnuHelpItem = new JMenuItem(menuTitle);
    mnuHelpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
    mnuHelpItem.setMnemonic('H');
    mnuHelpItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        //m_oInspector.openIdlFileforSelectedNode();
      }
    });
    return mnuHelpItem;
  }
  
  private void addFileMenu(JMenuBar inspectMenuBar)
  {
    JMenu mnuFile = new JMenu("File");
    JMenuItem mnuItemRemoveInspector = new JMenuItem("Close tab");
    mnuItemRemoveInspector.addActionListener(new ActionListener()
    {

      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        frame.removeSelectedTabPane();
      }
    });
    mnuFile.add(mnuItemRemoveInspector);
    mnuFile.addSeparator();
    JMenuItem mnuItemExit = new JMenuItem("Exit");
    mnuItemExit.addActionListener(new ActionListener()
    {

      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        frame.setVisible(false);
      }
    });
    mnuFile.add(mnuItemExit);
    inspectMenuBar.add(mnuFile);
  }
    
  private void addInspectMenu(JMenuBar inspectMenuBar)
  {
    JMenu mnuInspect = new JMenu("Inspect");
    addApplicationDocumentMenu(mnuInspect);
    mnuInspect.addSeparator();
    addGlobalServiceManagerMenu(mnuInspect);
    mnuInspect.addSeparator();
    addOpenDocumentMenu(mnuInspect);
    inspectMenuBar.add(mnuInspect);
  }
  
  private void addOpenDocumentMenu(JMenu menu)
  {
    ActionListener oActionListener = new ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        String sTDocUrl = evt.getActionCommand();
        Inspector.getInstance().inspectOpenDocument(sTDocUrl);
      }
    };
    String[] sTDocUrls = Inspector.getInstance().getTDocUrls();
    String[] sTDocTitles = Inspector.getInstance().getTDocTitles(sTDocUrls);
    for (int i = 0; i < sTDocUrls.length; i++)
    {
      JMenuItem item = new JMenuItem(sTDocTitles[i]);
      item.setActionCommand(sTDocUrls[i]);
      item.addActionListener(oActionListener);
      menu.add(item);
    }
  }
  
  private void addGlobalServiceManagerMenu(JMenu mnuRoot)
  {
    JMenuItem mnuGlobalServiceManager = new JMenuItem("Global Service Manager");
    mnuGlobalServiceManager.addActionListener(new ActionListener()
    {

      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        Inspector.getInstance().inspect(
          Inspector.getInstance().getXComponentContext().getServiceManager(), 
          "Global ServiceManager");
      }
    });
    mnuRoot.add(mnuGlobalServiceManager);
  }
  
  private void addApplicationDocumentMenu(JMenu menu)
  {
    ActionListener oActionListener = new ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        String applicationDocUrl = evt.getActionCommand();
        Inspector.getInstance().inspectOpenEmptyDocument(applicationDocUrl);
      }
    };
    String[][] sApplUrls = Inspector.getInstance().getApplicationUrls();
    for (int i = 0; i < sApplUrls.length; i++)
    {
      JMenuItem item = new JMenuItem(sApplUrls[i][1]);
      item.setActionCommand(sApplUrls[i][0]);
      item.addActionListener(oActionListener);
      menu.add(item);
    }
  }
  
  private JRadioButtonMenuItem addLanguageMenuItem(
    ButtonGroup group, String title, boolean select, char mnemonic, final Language lang)
  {
    JRadioButtonMenuItem item = new JRadioButtonMenuItem(title, select);
    item.setMnemonic(mnemonic);
    group.add(item);
    item.addActionListener(new ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        try
        {
          InspectorPane pane = (InspectorPane)InspectorFrame.getInstance().getTabbedPane().getSelectedComponent();
          UnoTreeModel model = (UnoTreeModel)pane.getInspectionTree().getModel();
          CodeGenerator codeGen =
            CodeGenerator.getInstance(lang, ((SwingUnoNode)model.getRoot()).getUnoObject());
          String code      = codeGen.getSourceCode();
          pane.getCodePane().setCode(code);
          currentLang = lang;
        }
        catch(Exception ex)
        {
          ex.printStackTrace();
        }
      }
    });
    return item;
  }  
    
  private void addOptionsMenu(JMenuBar inspectMenuBar)
  {
    ButtonGroup oButtonGroup = new ButtonGroup();
    this.mnuOptionsJava = addLanguageMenuItem(
      oButtonGroup, "Generate Java Sourcecode", true, 'J', Language.Java);
    this.mnuOptions.add(this.mnuOptionsJava);
    this.mnuOptionsCPlusPlus = addLanguageMenuItem(
      oButtonGroup, "Generate C++ Sourcecode", false, 'C', Language.CPlusPlus);
    this.mnuOptions.add(this.mnuOptionsCPlusPlus);
    this.mnuOptionsBasic = addLanguageMenuItem(
      oButtonGroup, "Generate OpenOffice.org Basic Sourcecode", false, 'B', Language.StarBasic);
    this.mnuOptions.add(this.mnuOptionsBasic);
    this.mnuOptionsPython = addLanguageMenuItem(
      oButtonGroup, "Generate Python Sourcecode", false, 'P', Language.Python);
    this.mnuOptions.add(this.mnuOptionsPython);
    inspectMenuBar.add(this.mnuOptions);
  }
  
  public Language getSelectedLanguage()
  {
    return this.currentLang;
  }
}
