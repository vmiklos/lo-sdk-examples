/*************************************************************************
 *
 *  The Contents of this file are made available subject to the terms of
 *  the BSD license.
 *  
 *  Copyright (c) 2003, 2008 by Sun Microsystems, Inc.
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

import org.openoffice.inspector.model.SwingTreePathProvider;
import org.openoffice.inspector.model.SwingUnoPropertyNode;
import org.openoffice.inspector.model.UnoPropertyNode;
import org.openoffice.inspector.model.SwingUnoNode;
import org.openoffice.inspector.model.UnoTreeModel;
import com.sun.star.beans.Property;
import com.sun.star.beans.PropertyValue;
import com.sun.star.lang.NullPointerException;
import com.sun.star.reflection.XConstantTypeDescription;
import com.sun.star.reflection.XIdlMethod;
import com.sun.star.uno.Type;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.openoffice.inspector.Introspector;
import org.openoffice.inspector.model.SwingUnoMethodNode;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class InspectionTree 
  extends JTree 
{

  public InspectionTree(UnoTreeModel model)
  {
    setModel(model);
    setCellRenderer(new UnoTreeRenderer());
    
    InspectionTreeListener itl = new InspectionTreeListener(model, this);
    addTreeWillExpandListener(itl);
    addMouseListener(itl);
    
    // Ensure the visibility of the root node
    this.collapseRow(0);
    this.expandRow(0);
  }

  /*private void ComponentSelector(Object _oRootObject, String _sRootTreeNodeName)
  {
    String sTreeNodeName = _sRootTreeNodeName;
    //oRootNode = new SwingUnoNode(_oRootObject);
    oRootNode.setLabel(_sRootTreeNodeName);
//    treeModel = new HideableTreeModel(oRootNode);
    setModel(treeModel);
    setRootVisible(true);
    setVisible(true);
    oRootNode.setFoldable(true);
    enableFilterElements(null);
  }*/

  /** Inspect the given object for methods, properties, interfaces, and
   * services.
   * @param a The object to inspect
   * @throws RuntimeException If
   */
 /* private Object inspect(java.lang.Object _oUserDefinedObject, String _sTitle) throws com.sun.star.uno.RuntimeException
  {
    JPanel jPnlContainer = new javax.swing.JPanel(new BorderLayout(10, 10));
    try
    {
      javax.swing.JPanel jPnlCenter = new javax.swing.JPanel();
      bIsUserDefined = (_oUserDefinedObject != null);
      if (bIsUserDefined)
      {
        oUserDefinedObject = _oUserDefinedObject;
        m_oInspectorPane.setTitle(_sTitle);
      }
      javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
      TreeSelectionModel tsm = new DefaultTreeSelectionModel();
      tsm.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
      setSelectionModel(tsm);
      setVisible(false);
      jPnlCenter.setLayout(new java.awt.BorderLayout(10, 10));
      jPnlCenter.getAccessibleContext().setAccessibleName("inspection tab view");
      insertTopPanel(jPnlCenter);
      jScrollPane1.setViewportView(this);
      jScrollPane1.setPreferredSize(new java.awt.Dimension(600, 600));
      jScrollPane1.getAccessibleContext().setAccessibleName("API view scroll pane");

      JScrollBar jHScrollBar = jScrollPane1.createHorizontalScrollBar();
      jHScrollBar.getAccessibleContext().setAccessibleName("API view horizontal scroll bar");
      jScrollPane1.setHorizontalScrollBar(jHScrollBar);

      JScrollBar jVScrollBar = jScrollPane1.createVerticalScrollBar();
      jVScrollBar.getAccessibleContext().setAccessibleName("API view vertical scroll bar");
      jScrollPane1.setVerticalScrollBar(jVScrollBar);

      JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
      jSplitPane.setTopComponent(jScrollPane1);
      jPnlCenter.add(jSplitPane, java.awt.BorderLayout.CENTER);
      jSplitPane.setDividerLocation(500);
      insertBottomPanel(jSplitPane);
      UnoTreeRenderer oUnoTreeRenderer = new UnoTreeRenderer();
      setCellRenderer(oUnoTreeRenderer);
      addTreeSelectionListener(
        new TreeSelectionListener()
        {

          public void valueChanged(TreeSelectionEvent event)
          {
            TreePath tp = event.getNewLeadSelectionPath();
            if (tp != null)
            {
              SwingUnoNode oUnoNode = getSelectedNode();
              String sFilter = enableFilterElements(oUnoNode);
            }
          }
        });
      //  Add KeyListener for help
      addKeyListener(new java.awt.event.KeyAdapter()
      {

        public void keyReleased(java.awt.event.KeyEvent event)
        {
          if (event.getKeyCode() == KeyEvent.VK_F1)
          {
            //  function key F1 pressed
            TreePath aTreePath = getSelectionPath();
            SwingUnoNode oUnoNode = (SwingUnoNode) aTreePath.getLastPathComponent();
       //     oUnoNode.openIdlDescription(m_xDialogProvider.getIDLPath());
          }
        }
      });
      
      addMouseListener(new MouseAdapter()
      {

        public void mousePressed(MouseEvent e)
        {
          if (e.isPopupTrigger())
          {
            m_oInspectorPane.showPopUpMenu(e.getComponent(), e.getX(), e.getY());
          }
          //unfortunately under Windows the method "isPopupTrigger" always returns false
          else
          {
            if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK)
            {
              m_oInspectorPane.showPopUpMenu(e.getComponent(), e.getX(), e.getY());
            }
          }
        }
      });
      jPnlContainer.add(jPnlCenter, java.awt.BorderLayout.CENTER);
      insertBorderPanes(jPnlContainer);
      if (this.bIsUserDefined)
      {
        ComponentSelector(oUserDefinedObject, m_oInspectorPane.getTitle());
      }
     // m_xDialogProvider.addInspectorPage(m_oInspectorPane.getTitle(), jPnlContainer);
//      addTreeExpandListener();
    }
    catch (Exception exception)
    {
      exception.printStackTrace(System.out);
    }
    return jPnlContainer;
  } */
/*
  public void addTreeExpandListener()
  {
    addTreeWillExpandListener(
      new TreeWillExpandListener()
      {

        public void treeWillExpand(javax.swing.event.TreeExpansionEvent event) throws javax.swing.tree.ExpandVetoException
        {
          SwingTreePathProvider oSwingTreePathProvider = new SwingTreePathProvider(event.getPath());
          SwingUnoNode oUnoNode = oSwingTreePathProvider.getLastPathComponent();
          if (!m_oInspectorPane.expandNode(oUnoNode, oSwingTreePathProvider))
          {
            throw new ExpandVetoException(event);
          }
        }

        public void treeWillCollapse(javax.swing.event.TreeExpansionEvent evt) throws javax.swing.tree.ExpandVetoException
        {
        }
      });
  }
*/

  public SwingTreePathProvider getSelectedPath()
  {
    return new SwingTreePathProvider(getSelectionPath());
  }

  public void expandPath(SwingTreePathProvider _xTreePathProvider) throws ClassCastException
  {
    SwingTreePathProvider oSwingTreePathProvider = (SwingTreePathProvider) _xTreePathProvider;
    expandPath(oSwingTreePathProvider.getSwingTreePath());
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

  public void nodeInserted(SwingUnoNode _oParentNode, SwingUnoNode _oChildNode, int index)
  {
    //getModel().nodeInserted(_oParentNode, _oChildNode, _oParentNode.getChildCount() - 1);
  }

  public void nodeChanged(SwingUnoNode _oNode)
  {
    //getModel().nodeChanged(_oNode);
  }

  public boolean setNodeVisible(Object node, boolean v)
  {
    return false; //getModel().setNodeVisible(node, v);
  }

  public SwingUnoNode addUnoNode(Object _oUnoObject)
  {
    return null;//return new SwingUnoNode(_oUnoObject);
  }

  public SwingUnoNode addUnoNode(Object _oUnoObject, Type _aType)
  {
    throw new NotImplementedException();
    //return new SwingUnoNode(_oUnoObject, _aType);
  }

  public SwingUnoMethodNode addMethodNode(Object _objectElement, XIdlMethod _xIdlMethod)
  {
//    SwingUnoMethodNode oSwingUnoMethodNode = new SwingUnoMethodNode(_xIdlMethod, _objectElement, m_xDialogProvider);
    return null; //oSwingUnoMethodNode;
  }

  /*public SwingUnoPropertyNode addUnoPropertyNodeWithName(Property _aProperty)
  {
    SwingUnoPropertyNode oUnoPropertyNode = new SwingUnoPropertyNode(_aProperty);
    oUnoPropertyNode.setLabel("Name: " + _aProperty.Name);
    return oUnoPropertyNode;
  }*/

  /*public SwingUnoPropertyNode addUnoPropertyNodeWithHandle(Property _aProperty)
  {
    SwingUnoPropertyNode oUnoPropertyNode = new SwingUnoPropertyNode(_aProperty);
    oUnoPropertyNode.setLabel("Handle: " + _aProperty.Handle);
    return oUnoPropertyNode;
  }*/

  /*public SwingUnoPropertyNode addUnoPropertyNodeWithType(Property _aProperty)
  {
    SwingUnoPropertyNode oUnoPropertyNode = new SwingUnoPropertyNode(_aProperty);
    oUnoPropertyNode.setLabel("Type: " + _aProperty.Type.getTypeName());
    return oUnoPropertyNode;
  }*/

  /*public SwingUnoPropertyNode addUnoPropertyNodeWithAttributesDescription(Property _aProperty)
  {
    SwingUnoPropertyNode oUnoPropertyNode = new SwingUnoPropertyNode(_aProperty);
    XConstantTypeDescription[] xPropertyAttributesTypeDescriptions = Introspector.getIntrospector().getFieldsOfConstantGroup("com.sun.star.beans.PropertyAttribute");
    String sDisplay = Introspector.getIntrospector().getConstantDisplayString((int) _aProperty.Attributes, xPropertyAttributesTypeDescriptions, "Attributes: ");
    oUnoPropertyNode.setLabel(sDisplay);
    return oUnoPropertyNode;
  }*/

  /*public SwingUnoPropertyNode addUnoPropertyNode(Object _oUnoObject, Property _aProperty)
  {
    SwingUnoPropertyNode oUnoPropertyNode = new SwingUnoPropertyNode(_aProperty, _oUnoObject, null);
    oUnoPropertyNode.setPropertyNodeType(0 );//SwingUnoPropertyNode.nPROPERTYSETINFOTYPE);
    oUnoPropertyNode.setLabel(UnoPropertyNode.getStandardPropertyDescription(_aProperty, null));
    return oUnoPropertyNode;
  }*/

 /* public SwingUnoPropertyNode addUnoPropertyNode(Object _oUnoObject, Property _aProperty, Object _oUnoReturnObject)
  {
    SwingUnoPropertyNode oUnoPropertyNode = new SwingUnoPropertyNode(_aProperty, _oUnoObject, _oUnoReturnObject);
    oUnoPropertyNode.setLabel(UnoPropertyNode.getStandardPropertyDescription(_aProperty, _oUnoReturnObject));
    return oUnoPropertyNode;
  }*/

  /*public SwingUnoPropertyNode addUnoPropertyNode(Object _oUnoObject, PropertyValue _aPropertyValue, Object _oReturnObject)
  {
    SwingUnoPropertyNode oUnoPropertyNode = new SwingUnoPropertyNode(_aPropertyValue, _oUnoObject, _oReturnObject);
    oUnoPropertyNode.setLabel(UnoPropertyNode.getStandardPropertyValueDescription(_aPropertyValue));
    return oUnoPropertyNode;
  }*/
}
