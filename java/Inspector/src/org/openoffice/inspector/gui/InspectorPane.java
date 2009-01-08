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

import org.openoffice.inspector.model.UnoNode;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.Type;
import com.sun.star.uno.XComponentContext;
import com.sun.star.beans.XIntrospectionAccess;
import com.sun.star.beans.Property;
import com.sun.star.beans.XPropertySet;
import com.sun.star.reflection.XIdlMethod;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import org.openoffice.inspector.model.HideableMutableTreeNode;
import org.openoffice.inspector.Introspector;
import org.openoffice.inspector.model.SwingUnoMethodNode;
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

  private void setMaximumOfProgressBar(Object _oUnoObject)
  {
    int nMaxValue = 0;
    this.idlMethods = this.introspector.getMethods(_oUnoObject);
  //this.properties = this.introspector.getProperties(_oUnoObject);
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

  private Type[] getTypes(Object _oUnoObject)
  {
    if (Introspector.isArray(_oUnoObject))
    {
      return (Type[]) _oUnoObject;
    }
    else
    {
      return this.introspector.getInterfaces(_oUnoObject);
    }
  }

  public InspectionTree getInspectionTree()
  {
    return this.inspectionTreeComponent;
  }
  
  public SwingUnoNode getSelectedNode()
  {
    return inspectionTreeComponent.getSelectedNode();
  }

  private SwingUnoMethodNode addMethodNode(Object _objectElement, XIdlMethod _xIdlMethod)
  {// Node MUST NOT be added to the JTree VIEW
    return inspectionTreeComponent.addMethodNode(_objectElement, _xIdlMethod);
  }

  public void addMethodsToTreeNode(SwingUnoNode _oGrandParentNode, Object _oUnoParentObject, XIdlMethod[] _xIdlMethods)
  {// Node MUST NOT be added to the JTree VIEW
    if (Introspector.isValid(_xIdlMethods))
    {
      for (int n = 0; n < _xIdlMethods.length; n++)
      {
        XIdlMethod xIdlMethod = _xIdlMethods[n];
        if (!xIdlMethod.getDeclaringClass().getName().equals("com.sun.star.uno.XInterface"))
        {
          SwingUnoMethodNode oChildNode = addMethodNode(_oUnoParentObject, xIdlMethod);
          if (oChildNode != null)
          {
//            _oGrandParentNode.addChildNode(oChildNode);
          }
        }
      }
    }
  }

  private void addFacetteNodesToTreeNode(SwingUnoNode _oParentNode, Object _oUnoObject)
  {// TODO What about this useless local variables?
    if (this.introspector.hasMethods(_oUnoObject))
    {
    //XUnoFacetteNode oUnoFacetteNode = addUnoFacetteNode(_oParentNode, XUnoFacetteNode.SMETHODDESCRIPTION, _oUnoObject);
    }
    if (this.introspector.hasProperties(_oUnoObject))
    {
    //XUnoFacetteNode oUnoFacetteNode = addUnoFacetteNode(_oParentNode, XUnoFacetteNode.SPROPERTYDESCRIPTION, _oUnoObject);
    }
    if (this.introspector.hasInterfaces(_oUnoObject))
    {
    //XUnoFacetteNode oUnoFacetteNode = addUnoFacetteNode(_oParentNode, XUnoFacetteNode.SINTERFACEDESCRIPTION, _oUnoObject);
    }
    if (this.introspector.isContainer(_oUnoObject))
    {
    //XUnoFacetteNode oUnoFacetteNode = addUnoFacetteNode(_oParentNode, XUnoFacetteNode.SCONTAINERDESCRIPTION, _oUnoObject);
    }
    if (this.introspector.hasSupportedServices(_oUnoObject))
    {
    //XUnoFacetteNode oUnoFacetteNode = addUnoFacetteNode(_oParentNode, XUnoFacetteNode.SSERVICEDESCRIPTION, _oUnoObject);
    }
  }

  //  add all services for the given object to the tree under the node parent
  private void addServicesToTreeNode(SwingUnoNode _oGrandParentNode, Object _oUnoObject)
  {
    try
    {
      XServiceInfo xServiceInfo = (XServiceInfo) UnoRuntime.queryInterface(XServiceInfo.class, _oUnoObject);
      if (xServiceInfo != null)
      {
        String[] sSupportedServiceNames = xServiceInfo.getSupportedServiceNames();
        for (int m = 0; m < sSupportedServiceNames.length; m++)
        {
          String sServiceName = sSupportedServiceNames[m];
          if (sServiceName.length() > 0)
          {
            SwingUnoNode oUnoNode = addUnoNode(_oGrandParentNode, _oUnoObject, sSupportedServiceNames[m]);
            oUnoNode.setNodeType(0); //XUnoNode.nSERVICE);
          }
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace(System.out);
    }
  }

  private void addPropertiesToTreeNode(SwingUnoNode _oParentNode, Object _oUnoParentObject, Property[] _aProperties)
  {
    try
    {

      for (int n = 0; n < _aProperties.length; n++)
      {
        Property aProperty = _aProperties[n];
        XIntrospectionAccess xIntrospectionAccess = introspector.getXIntrospectionAccess(_oUnoParentObject);
        XPropertySet xPropertySet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xIntrospectionAccess.queryAdapter(new Type(XPropertySet.class)));
        if (xPropertySet != null)
        {
          if (xPropertySet.getPropertySetInfo().hasPropertyByName(aProperty.Name))
          {
            Object objectElement = xPropertySet.getPropertyValue(aProperty.Name);
            if (objectElement != null)
            {
              SwingUnoNode oChildNode = null; // this.inspectionTreeComponent.addUnoPropertyNode(_oUnoParentObject, aProperty, objectElement);
              if (oChildNode != null)
              {
//                _oParentNode.addChildNode(oChildNode);
              }
            }
          }
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
  }

  public void addContainerElementsToTreeNode(SwingUnoNode _oParentNode, Object _oUnoParentObject)
  {
    Object[] oUnoContainerElements = introspector.getUnoObjectsOfContainer(_oUnoParentObject);
    if (Introspector.isValid(oUnoContainerElements))
    {
      if (oUnoContainerElements.length > 0)
      {
        for (int i = 0; i < oUnoContainerElements.length; i++)
        {
          SwingUnoNode oChildNode = addUnoNode(_oParentNode, oUnoContainerElements[i], UnoNode.getNodeDescription(oUnoContainerElements[i]));
          inspectionTreeComponent.nodeInserted(_oParentNode, oChildNode, _oParentNode.getChildCount() - 1);
        }
      }
    }
  }

  private SwingUnoNode addUnoNode(SwingUnoNode _oParentNode, Object _oUnoObject, String _sLabel)
  {
    SwingUnoNode oUnoNode = this.inspectionTreeComponent.addUnoNode(_oUnoObject);
    oUnoNode.setLabel(_sLabel);
    if (_oParentNode != null)
    {
//      _oParentNode.addChildNode(oUnoNode);
    }
    setNodeFoldable(oUnoNode, _oUnoObject);
    return oUnoNode;
  }

  private void setNodeFoldable(SwingUnoNode _oUnoNode, Object _oUnoObject)
  {
    if (_oUnoObject != null)
    {
      if (!Introspector.isObjectPrimitive(_oUnoObject))
      {
        _oUnoNode.setFoldable(true);
      }
    }
  }

}
    