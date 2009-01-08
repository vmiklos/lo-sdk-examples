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
import org.openoffice.inspector.model.SwingTreePathProvider;
import org.openoffice.inspector.model.SwingUnoMethodNode;
import org.openoffice.inspector.model.SwingUnoNode;

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
  private FilterPane     filterPane = new FilterPane();
  private String sTitle = "";

  /** The constructor of the inner class has a XMultiServiceFactory parameter.
   * @param xMultiServiceFactory XMultiServiceFactory
   */
  public InspectorPane(InspectionTree tree)
    throws InstantiationException, IllegalAccessException
  {
    //this.xComponentContext        = xComponentContext;
    this.inspectionTreeComponent = tree;

    //this.sourceCodeGenerator      = CodeGenerator.createInstance(lang);

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

  /*private Object invoke(SwingUnoMethodNode _oUnoMethodNode)
  {
    try
    {
      String sParamValueDescription = "";
      Object oUnoReturnObject = _oUnoMethodNode.invoke();
      boolean bHasParameters = _oUnoMethodNode.hasParameters();
      boolean bIsPrimitive = _oUnoMethodNode.isPrimitive();
      if (bHasParameters)
      {
//        sParamValueDescription = " (" + sourceCodeGenerator.getMethodParameterValueDescription(_oUnoMethodNode, _oUnoMethodNode.getLastParameterObjects(), true) + ")";
      }
      if (oUnoReturnObject != null)
      {
        String sNodeDescription = "";
        SwingUnoNode oUnoNode = null;
        if (_oUnoMethodNode.getXIdlMethod().getReturnType().getTypeClass().getValue() == TypeClass.VOID_value)
        {
          sNodeDescription += _oUnoMethodNode.getXIdlMethod().getReturnType().getName() + " " + _oUnoMethodNode.getXIdlMethod().getName() + sParamValueDescription;
          _oUnoMethodNode.setLabel(sNodeDescription);
          this.inspectionTreeComponent.nodeChanged(_oUnoMethodNode);
        }
        else
        {
          if (bHasParameters || !bIsPrimitive)
          {
            if (bIsPrimitive)
            {
              sNodeDescription += sourceCodeGenerator.getStringValueOfObject(oUnoReturnObject, _oUnoMethodNode.getTypeClass());
            }
            else
            {
              Any aReturnObject = Any.complete(oUnoReturnObject);
              String sShortClassName = Introspector.getShortClassName(aReturnObject.getType().getTypeName());
              sNodeDescription += sourceCodeGenerator.getVariableNameforUnoObject(sShortClassName);
            }
            if (Introspector.isArray(oUnoReturnObject))
            {
              if (Introspector.isUnoTypeObject(oUnoReturnObject))
              {
              //oUnoNode = addUnoFacetteNode(_oUnoMethodNode, XUnoFacetteNode.SINTERFACEDESCRIPTION, _oUnoMethodNode.getUnoObject());
              }
              else if (Introspector.isUnoPropertyTypeObject(oUnoReturnObject))
              {
              //oUnoNode = addUnoFacetteNode(_oUnoMethodNode, XUnoFacetteNode.SPROPERTYINFODESCRIPTION, oUnoReturnObject);
              }
              else if (Introspector.isUnoPropertyValueTypeObject(oUnoReturnObject))
              {
              //oUnoNode = addUnoFacetteNode(_oUnoMethodNode, XUnoFacetteNode.SPROPERTYVALUEDESCRIPTION, oUnoReturnObject);
              }
            }
            if (oUnoNode == null)
            {
              if (bHasParameters)
              {
                sNodeDescription += sParamValueDescription;
              }
              oUnoNode = addUnoNode(null, oUnoReturnObject, sNodeDescription);
              if (bHasParameters)
              {
                oUnoNode.setParameterObjects(_oUnoMethodNode.getLastParameterObjects());
              }
            }
            if (oUnoNode != null)
            {
              oUnoNode.setFoldable(!bIsPrimitive);
              _oUnoMethodNode.setFoldable(false);
//              _oUnoMethodNode.addChildNode(oUnoNode);
              this.inspectionTreeComponent.nodeInserted(_oUnoMethodNode, oUnoNode, _oUnoMethodNode.getChildCount() - 1);
            }
          }
          else
          {
            if (bIsPrimitive)
            {
              sNodeDescription = _oUnoMethodNode.getStandardMethodDescription() + "=" + UnoMethodNode.getDisplayValueOfPrimitiveType(oUnoReturnObject);
              _oUnoMethodNode.setLabel(sNodeDescription);
              this.inspectionTreeComponent.nodeChanged(_oUnoMethodNode);
            }
          }
        }
      }
      // ReturnObject of method node == null..
      else
      {
        if (!bHasParameters)
        {
          _oUnoMethodNode.setLabel(_oUnoMethodNode.getLabel() + " = null");
        }
        else
        {
          _oUnoMethodNode.setLabel(_oUnoMethodNode.getXIdlMethod().getName() + sParamValueDescription + " = null");
        }
        this.inspectionTreeComponent.nodeChanged(_oUnoMethodNode);
      }
      return oUnoReturnObject;
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      return null;
    }
  }*/
/*
  public void invokeSelectedMethodNode()
  {
    SwingTreePathProvider xTreePathProvider = this.inspectionTreeComponent.getSelectedPath();
    SwingUnoNode oUnoNode = xTreePathProvider.getLastPathComponent();
    if (oUnoNode instanceof SwingUnoMethodNode)
    {
      invoke((SwingUnoMethodNode) oUnoNode);
      this.inspectionTreeComponent.expandPath(xTreePathProvider);
    }
  }*/
/*
  public void addSourceCodeOfSelectedNode()
  {
    SwingTreePathProvider oTreePathProvider = inspectionTreeComponent.getSelectedPath();
    SwingUnoNode oUnoNode = oTreePathProvider.getLastPathComponent();
    if (oUnoNode instanceof SwingUnoMethodNode)
    {
      SwingUnoMethodNode oUnoMethodNode = (SwingUnoMethodNode) oUnoNode;
      if (!oUnoMethodNode.isInvoked() && oUnoMethodNode.isInvokable())
      {
        invoke(oUnoMethodNode);
      }
    }
  //String sSourceCode = sourceCodeGenerator.addSourceCodeOfUnoObject(oTreePathProvider, true, true, true);
//    inspectionTreeComponent.setSourceCode(sSourceCode);
  }*/

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

  private void addInterfacesToTreeNode(SwingUnoNode _oGrandParentNode, Object _oUnoParentObject, Type[] _aTypes)
  {
    try
    {
      if (_oUnoParentObject != null)
      {
        for (int m = 0; m < _aTypes.length; m++)
        {
          addUnoNode(_oGrandParentNode, _oUnoParentObject, _aTypes[m]);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace(System.out);
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

  private void addPropertiesAndInterfacesOfService(SwingUnoNode _oUnoServiceNode)
  {
    String sServiceName = _oUnoServiceNode.getClassName();
    Object oUnoObject = _oUnoServiceNode.getUserObject();
    addInterfacesToTreeNode(_oUnoServiceNode, oUnoObject, introspector.getInterfaces(oUnoObject, sServiceName));
  //addPropertiesToTreeNode(_oUnoServiceNode, oUnoObject, introspector.getProperties(oUnoObject, sServiceName));
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

  private SwingUnoNode addUnoNode(SwingUnoNode _oParentNode, Object _oUnoObject, Type _aType)
  {
    SwingUnoNode oUnoNode = this.inspectionTreeComponent.addUnoNode(_oUnoObject, _aType);
    if (_oParentNode != null)
    {
//      _oParentNode.addChildNode(oUnoNode);
    }
    setNodeFoldable(oUnoNode, _oUnoObject);
    return oUnoNode;
  }

  /*private void addPropertySetInfoNodesToTreeNode(XUnoFacetteNode _oUnoFacetteNode, Object _oUnoObject)
  {
  if (_oUnoObject.getClass().isArray())
  {
  Object[] object = (Object[]) _oUnoObject;
  for (int i = 0; i < object.length; i++)
  {
  Property aProperty = (Property) object[i];
  XUnoPropertyNode oUnoPropertyNode = inspectionTreeComponent.addUnoPropertyNode(_oUnoObject, aProperty);
  oUnoPropertyNode.setLabel(XUnoFacetteNode.SPROPERTYINFODESCRIPTION + " (" + aProperty.Name + ")");
  oUnoPropertyNode.setFoldable(true);
  _oUnoFacetteNode.addChildNode(oUnoPropertyNode);
  }
  }
  }*/
  private void addPrimitiveValuesToTreeNode(SwingUnoNode _oUnoNode, Object _oUnoObject)
  {
    if (_oUnoObject.getClass().isArray())
    {
      if (!_oUnoObject.getClass().getComponentType().isPrimitive())
      {
        Object[] object = (Object[]) _oUnoObject;
        for (int i = 0; i < object.length; i++)
        {
          if (Introspector.isObjectPrimitive(object[i]))
          {
            SwingUnoNode oChildNode = addUnoNode(_oUnoNode, null, UnoNode.getNodeDescription(object[i]));
          }
        }
      }
      else
      {
        String[] sDisplayValues = UnoNode.getDisplayValuesOfPrimitiveArray(_oUnoObject);
        for (int i = 0; i < sDisplayValues.length; i++)
        {
          SwingUnoNode oUnoNode = addUnoNode(_oUnoNode, null, sDisplayValues[i]);
        }
      }
    }
  }

  /*private void addPropertySetInfoSubNodes(SwingUnoPropertyNode _oUnoPropertyNode)
  {
  Property aProperty = _oUnoPropertyNode.getProperty();
  _oUnoPropertyNode.addChildNode(inspectionTreeComponent.addUnoPropertyNodeWithName(aProperty));
  _oUnoPropertyNode.addChildNode(inspectionTreeComponent.addUnoPropertyNodeWithType(aProperty));
  _oUnoPropertyNode.addChildNode(inspectionTreeComponent.addUnoPropertyNodeWithHandle(aProperty));
  _oUnoPropertyNode.addChildNode(inspectionTreeComponent.addUnoPropertyNodeWithAttributesDescription(aProperty));
  }*/

  /*private void addPropertyValueSubNodes(XUnoFacetteNode _oUnoFacetteNode, Object _oUnoObject)
  {
  if (Introspector.isUnoPropertyValueTypeObject(_oUnoObject))
  {
  Object[] object = (Object[]) _oUnoObject;
  for (int i = 0; i < object.length; i++)
  {
  String sObjectClassName = object[i].getClass().getName();
  if (sObjectClassName.equals("com.sun.star.beans.PropertyValue"))
  {
  XUnoNode oChildNode = null;
  PropertyValue aPropertyValue = (PropertyValue) object[i];
  if (!Introspector.isObjectPrimitive(aPropertyValue.Value))
  {
  oChildNode = inspectionTreeComponent.addUnoPropertyNode(_oUnoObject, aPropertyValue, _oUnoObject);
  }
  else
  {
  oChildNode = addUnoNode(null, aPropertyValue.Value, UnoPropertyNode.getStandardPropertyValueDescription(aPropertyValue));
  }
  if (oChildNode != null)
  {
  _oUnoFacetteNode.addChildNode(oChildNode);
  }
  }
  }
  }
  }*/
 /* public boolean expandNode(SwingUnoNode node, SwingTreePathProvider _xTreePathProvider)
  {
    if (node != null)
    {
      node.setFoldable(false);
      if (node instanceof SwingUnoMethodNode)
      {
        SwingUnoMethodNode oUnoMethodNode = (SwingUnoMethodNode) node;
        if (!oUnoMethodNode.isInvoked())
        {
          Object oUnoReturnObject = invoke((SwingUnoMethodNode) node);
          if (oUnoReturnObject == null)
          {
            node.setFoldable(true);
            return false;
          }
        }
      }
      else
      {
        if (node instanceof SwingUnoPropertyNode)
        {
          SwingUnoPropertyNode oUnoPropertyNode = (SwingUnoPropertyNode) node;
          Object oUnoObject = oUnoPropertyNode.getUnoReturnObject();
          if (oUnoPropertyNode.getPropertyNodeType() == 0)//SwingUnoPropertyNode.nPROPERTYSETINFOTYPE)
          {
//            addPropertySetInfoSubNodes(oUnoPropertyNode);
          }
          else
          {
            //TOOD this code is redundant!!!
            if (oUnoObject.getClass().isArray())
            {
              // TOODO probably we should provid a possibility to insert also non-primitive nodes
              addPrimitiveValuesToTreeNode(oUnoPropertyNode, oUnoObject);
            }
            else
            {
              addFacetteNodesToTreeNode(oUnoPropertyNode, oUnoObject);
            }
          }
        }
        else
        {
        /*  if (oUnoFacetteNode.isServiceNode())
        {
        addServicesToTreeNode(oUnoFacetteNode, oUnoObject);
        }
        if (oUnoFacetteNode.isInterfaceNode())
        {
        addInterfacesToTreeNode(oUnoFacetteNode, oUnoObject, getTypes(oUnoObject));
        }
        if (oUnoFacetteNode.isContainerNode())
        {
        addContainerElementsToTreeNode(oUnoFacetteNode, oUnoObject);
        }
        }
        else
        {
        if (_oUnoNode.getNodeType() == XUnoNode.nSERVICE)
        {
        addPropertiesAndInterfacesOfService(_oUnoNode);
        }
        else
        {
        if (_oUnoNode.getNodeType() == XUnoNode.nINTERFACE)
        {
        Type aType = _oUnoNode.getUnoType();
        if (aType != null)
        {
        addMethodsToTreeNode(_oUnoNode, _oUnoNode.getUnoObject(), introspector.getMethodsOfInterface(aType));
        }
        }
        else
        {
        if (_oUnoNode.getNodeType() == XUnoNode.nOTHERS)
        {
        Object oUnoObject = _oUnoNode.getUnoObject();
        if (oUnoObject.getClass().isArray())
        {
        // TOODO probably we should provid a possibility to insert also non-primitive nodes
        addPrimitiveValuesToTreeNode(_oUnoNode, oUnoObject);
        }
        else
        {
        addFacetteNodesToTreeNode(_oUnoNode, oUnoObject);
        }
        }
        }
        }
        }
        }
      }
    }
    return true;
  }
*/
  /* public void applyFilter(XUnoFacetteNode _oUnoFacetteNode, String _sFilter)
  {
  for (int i = 0; i < _oUnoFacetteNode.getChildCount(); i++)
  {
  XUnoNode oUnoNode = _oUnoFacetteNode.getChild(i);
  boolean bIsVisible = oUnoNode.isFilterApplicable(_sFilter);
  inspectionTreeComponent.setNodeVisible(oUnoNode, bIsVisible);
  }
  _oUnoFacetteNode.setFilter(_sFilter);
  }*/
  /** In opposition to 'getUnoObjectOfTreeNode' this method inspects the passed node if it represents a Uno object
   *  If not it returns null
   *
   */
  private Object getUnoObjectOfExplicitTreeNode(SwingTreePathProvider _xTreePathProvider)
  {
    SwingUnoNode oUnoNode = _xTreePathProvider.getLastPathComponent();
    if (oUnoNode != null)
    {
      return oUnoNode.getUserObject();//getUnoObject();
    }
    return null;
  }

  public String getFilter(SwingUnoNode _oUnoNode)
  {
    String sFilter = "";
    if (_oUnoNode != null)
    {
      SwingUnoNode oUnoNode = _oUnoNode;
      boolean bleaveLoop = false;
      do
      {
        /*if (inspectionTreeComponent.isFacetteNode(oUnoNode))
        {
        sFilter = ((XUnoFacetteNode) oUnoNode).getFilter();
        bleaveLoop = true;
        }
        else*/
        {
//          if (oUnoNode.getParentNode() != null)
          {
  //          oUnoNode = oUnoNode.getParentNode();
          }
    //      else
          {
            bleaveLoop = true;
          }
        }
      }
      while (!bleaveLoop);
    }
    return sFilter;
  }

  /** In opposition to 'getUnoObjectOfexplictTreeNode' this method inspects the passed node if it represents a Uno object
   *  if not it keeps checking all ancestors until it finds a Uno representation
   */
  private Object getUnoObjectOfTreeNode(SwingTreePathProvider _xTreePathProvider)
  {
    SwingTreePathProvider xTreePathProvider = _xTreePathProvider;
    HideableMutableTreeNode oNode = null;
    Object oUnoObject = null;
    while (xTreePathProvider != null)
    {
      oUnoObject = getUnoObjectOfExplicitTreeNode(xTreePathProvider);
      if (oUnoObject != null)
      {
        if (oUnoObject instanceof String)
        {
        }
        else
        {
          if (!Introspector.isUnoTypeObject(oUnoObject))
          {
            return oUnoObject;
          }
        }
      }
      xTreePathProvider = xTreePathProvider.getParentPath();
    }
    return null;
  }

}
    