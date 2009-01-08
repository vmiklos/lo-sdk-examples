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
 *
 * @author Christian Lins (cli@openoffice.org)
 */
class MenuBar extends JMenuBar
{
  
  private Language       currentLang = Language.Java;
  private InspectorFrame frame;
  private JMenu jMnuOptions = new JMenu("Options");
  private JRadioButtonMenuItem jJavaMenuItem = null;
  private JRadioButtonMenuItem jCPlusPlusMenuItem = null;
  private JRadioButtonMenuItem jBasicMenuItem = null;
  
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
  
  private void addHelpMenu(JMenuBar _jInspectMenuBar)
  {
    JMenu jMnuHelp = new JMenu("Help");
    jMnuHelp.add(getHelpMenuItem("Idl-Help"));
    _jInspectMenuBar.add(jMnuHelp);
  }
    
  private JMenuItem getHelpMenuItem(String _sMenuTitle)
  {
    JMenuItem jMnuHelpItem = new JMenuItem(_sMenuTitle);
    jMnuHelpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
    jMnuHelpItem.setMnemonic('H');
    jMnuHelpItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        //m_oInspector.openIdlFileforSelectedNode();
      }
    });
    return jMnuHelpItem;
  }
  
  private void addFileMenu(JMenuBar _jInspectMenuBar)
  {
    JMenu jMnuFile = new JMenu("File");
    JMenuItem jMnuItemRemoveInspector = new JMenuItem("Close tab");
    jMnuItemRemoveInspector.addActionListener(new ActionListener()
    {

      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        frame.removeSelectedTabPane();
      }
    });
    jMnuFile.add(jMnuItemRemoveInspector);
    jMnuFile.addSeparator();
    JMenuItem jMnuItemExit = new JMenuItem("Exit");
    jMnuItemExit.addActionListener(new ActionListener()
    {

      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        frame.setVisible(false);
      }
    });
    jMnuFile.add(jMnuItemExit);
    _jInspectMenuBar.add(jMnuFile);
  }
    
  private void addInspectMenu(JMenuBar _jInspectMenuBar)
  {
    JMenu jMnuInspect = new JMenu("Inspect");
    addApplicationDocumentMenu(jMnuInspect);
    jMnuInspect.addSeparator();
    addGlobalServiceManagerMenu(jMnuInspect);
    jMnuInspect.addSeparator();
    addOpenDocumentMenu(jMnuInspect);
    _jInspectMenuBar.add(jMnuInspect);
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
  
  private void addGlobalServiceManagerMenu(JMenu _jMnuRoot)
  {
    JMenuItem jMnuGlobalServiceManager = new JMenuItem("Global Service Manager");
    jMnuGlobalServiceManager.addActionListener(new ActionListener()
    {

      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        Inspector.getInstance().inspect(
          Inspector.getInstance().getXComponentContext().getServiceManager(), 
          "Global ServiceManager");
      }
    });
    _jMnuRoot.add(jMnuGlobalServiceManager);
  }
  
  private void addApplicationDocumentMenu(JMenu menu)
  {
    ActionListener oActionListener = new ActionListener()
    {

      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        String sApplicationDocUrl = evt.getActionCommand();
        Inspector.getInstance().inspectOpenEmptyDocument(sApplicationDocUrl);
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
    jJavaMenuItem = addLanguageMenuItem(
      oButtonGroup, "Generate Java Sourcecode", true, 'J', Language.Java);
    jMnuOptions.add(jJavaMenuItem);
    jCPlusPlusMenuItem = addLanguageMenuItem(
      oButtonGroup, "Generate C++ Sourcecode", false, 'C', Language.CPlusPlus);
    jMnuOptions.add(jCPlusPlusMenuItem);
    jBasicMenuItem = addLanguageMenuItem(
      oButtonGroup, "Generate OpenOffice.org Basic Sourcecode", false, 'B', Language.StarBasic);
    jMnuOptions.add(jBasicMenuItem);
    inspectMenuBar.add(jMnuOptions);
  }
  
  public Language getSelectedLanguage()
  {
    return this.currentLang;
  }
}
