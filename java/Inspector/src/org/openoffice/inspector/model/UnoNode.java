/*************************************************************************
 *
 *  $RCSfile: UnoNode.java,v $
 *
 *  $Revision: 1.4 $
 *
 *  last change: $Author: rt $ $Date: 2007/04/04 09:23:07 $
 *
 *  The Contents of this file are made available subject to the terms of
 *  the BSD license.
 *  
 *  Copyright (c) 2003 by Sun Microsystems, Inc.
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
package org.openoffice.inspector.model;

import com.sun.star.beans.Property;
import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.FrameSearchFlag;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XDispatch;
import com.sun.star.frame.XDispatchProvider;
import com.sun.star.frame.XFrame;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.reflection.TypeDescriptionSearchDepth;
import com.sun.star.reflection.XIdlMethod;
import com.sun.star.reflection.XServiceTypeDescription;
import com.sun.star.reflection.XTypeDescription;
import com.sun.star.reflection.XTypeDescriptionEnumeration;
import com.sun.star.reflection.XTypeDescriptionEnumerationAccess;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.Type;
import com.sun.star.uno.TypeClass;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.URL;
import com.sun.star.util.XURLTransformer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.openoffice.inspector.Inspector;
import org.openoffice.inspector.Introspector;

public class UnoNode
{

  protected Object unoObject;
  private XMultiComponentFactory m_xMultiComponentFactory;
  private XComponentContext m_xComponentContext;
  private Object[] m_oParamObjects = null;
  private UnoType unoType;
  private Type aType = null;
  private String sLabel = "";

  /** Creates a new instance of UnoNode */
  public UnoNode(Object _oUnoObject)
  {
    m_xComponentContext = Introspector.getIntrospector().getXComponentContext();
    m_xMultiComponentFactory = m_xComponentContext.getServiceManager();
    this.unoObject = _oUnoObject;
  }

  public UnoNode(Object _oUnoObject, Type _aType)
  {
    this(_oUnoObject);
    aType = _aType;
    unoType = UnoType.Interface;
  }

  /**
   * Introspects the capsulated Uno object and retrieves the
   * supported service names from it.
   * @return 
   */
  public List<UnoServiceNode> getSupportedServices()
  {
    Object                unoObject = getUnoObject();
    List<UnoServiceNode>  services  = new ArrayList<UnoServiceNode>();
    String[] serviceNames = Introspector.getSupportedServiceNames(unoObject);
    
    if(serviceNames != null)
    {
      for(String serviceName : serviceNames)
        services.add(new UnoServiceNode(serviceName, unoObject));
    }
    
    return services.size() > 0 ? services : null;
  }
  
  public List<UnoMethodNode> getMethods()
  {
    Object unoObject = getUnoObject();
    List<UnoMethodNode> methods = new ArrayList<UnoMethodNode>();
    XIdlMethod[] xidlmethods = 
      Introspector.getIntrospector().getMethods(unoObject);
    
    if(xidlmethods != null)
    {
      for(XIdlMethod method : xidlmethods)
        methods.add(new UnoMethodNode(method, unoObject));
    }
    
    return methods.size() > 0 ? methods : null;
  }
  
  public List<UnoInterfaceNode> getInterfaces()
  {
    Object unoObject = getUnoObject();
    List<UnoInterfaceNode> interfaces = new ArrayList<UnoInterfaceNode>();
    Type[] interfaceTypes = 
      Introspector.getIntrospector().getInterfaces(unoObject);
    
    if(interfaceTypes != null)
    {
      for(Type type : interfaceTypes)
        interfaces.add(new UnoInterfaceNode(type, unoObject));
    }
    
    return interfaces.size() > 0 ? interfaces : null;
  }
  
  public List<UnoPropertyNode> getProperties()
  {
    Object unoObject = getUnoObject();
    List<UnoPropertyNode> properties = new ArrayList<UnoPropertyNode>();
    
    Property[] props = Introspector.getIntrospector().getProperties(unoObject);
    if(props != null)
    {
      for(Property prop : props)
        properties.add(new UnoPropertyNode(prop, unoObject));
    }
    
    return properties.size() > 0 ? properties : null;
  }
  
  public Object getUnoObject()
  {
    return this.unoObject;
  }

  protected XComponentContext getXComponentContext()
  {
    return m_xComponentContext;
  }

  protected XMultiComponentFactory getXMultiComponentFactory()
  {
    return m_xMultiComponentFactory;
  }

  private static XTypeDescriptionEnumerationAccess getXTypeDescriptionEnumerationAccess()
  {
    return Introspector.getIntrospector().getXTypeDescriptionEnumerationAccess();
  }

  public String getAnchor()
  {
    return "";
  }

  public UnoType getNodeType()
  {
    return this.unoType;
  }

  public void setNodeType(UnoType unoType)
  {
    this.unoType = unoType;
  }

  public String getClassName()
  {
    String sClassName = "";
    if (this.unoType == UnoType.Interface)
    {
      sClassName = aType.getTypeName();
    }
    else if (this.unoType == UnoType.Service)
    {
      sClassName = sLabel;
    }
    return sClassName;
  }

  public Type getUnoType()
  {
    return aType;
  }

  protected void setLabel(String _sLabel)
  {
    sLabel = _sLabel;
  }

  /*public void openIdlDescription(String _sIDLUrl, String _sClassName, String _sAnchor)
  {
    try
    {
      String sIDLUrl = _sIDLUrl;
      String sAnchor = ""; // TODO find out how the Anchor may be set at the html file;  //_sAnchor;
      boolean bExists = Introspector.getIntrospector().getXSimpleFileAccess().exists(sIDLUrl);
      if (sIDLUrl.equals("") || (!bExists))
      {
        sIDLUrl = "http://api.openoffice.org/" + Inspector.sIDLDOCUMENTSUBFOLDER;
      }
      if (!sIDLUrl.endsWith("/"))
      {
        sIDLUrl += "/";
      }
      if (_sClassName.equals(""))
      {
        sIDLUrl += "com/sun/star/module-ix";
        sAnchor = "";
      }
      else
      {
        sIDLUrl += _sClassName.replace('.', '/');
      }
      if (sAnchor != null)
      {
        if (!sAnchor.equals(""))
        {
          sIDLUrl += "#" + sAnchor;
        }
      }
      sIDLUrl += ".html";
      URL openHyperlink = getDispatchURL(".uno:OpenHyperlink");
      PropertyValue pv = new PropertyValue();
      pv.Name = "URL";
      pv.Value = sIDLUrl;
      getXDispatcher(openHyperlink).dispatch(openHyperlink, new PropertyValue[]{pv});
    }
    catch (Exception exception)
    {
      exception.printStackTrace(System.out);
    }
  }*/

  private com.sun.star.util.URL getDispatchURL(String _sURL)
  {
    try
    {
      Object oTransformer = getXMultiComponentFactory().createInstanceWithContext("com.sun.star.util.URLTransformer", getXComponentContext());
      XURLTransformer xTransformer = (XURLTransformer) UnoRuntime.queryInterface(XURLTransformer.class, oTransformer);
      com.sun.star.util.URL[] oURL = new com.sun.star.util.URL[1];
      oURL[0] = new com.sun.star.util.URL();
      oURL[0].Complete = _sURL;
      xTransformer.parseStrict(oURL);
      return oURL[0];
    }
    catch (Exception e)
    {
      e.printStackTrace(System.out);
    }
    return null;
  }

  private XFrame getCurrentFrame()
  {
    try
    {
      Object oDesktop = getXMultiComponentFactory().createInstanceWithContext("com.sun.star.frame.Desktop", getXComponentContext());
      XDesktop xDesktop = (XDesktop) UnoRuntime.queryInterface(XDesktop.class, oDesktop);
      return xDesktop.getCurrentFrame();
    }
    catch (Exception e)
    {
      e.printStackTrace(System.out);
      return null;
    }
  }

  private XDispatch getXDispatcher(com.sun.star.util.URL oURL)
  {
    try
    {
      com.sun.star.util.URL[] oURLArray = new com.sun.star.util.URL[1];
      oURLArray[0] = oURL;
      XDispatchProvider xDispatchProvider = (XDispatchProvider) UnoRuntime.queryInterface(XDispatchProvider.class, getCurrentFrame());
      XDispatch xDispatch = xDispatchProvider.queryDispatch(oURLArray[0], "_top", FrameSearchFlag.ALL); // "_self"
      return xDispatch;
    }
    catch (Exception e)
    {
      e.printStackTrace(System.out);
      return null;
    }
  }

  private PropertyValue[] loadArgs(String url)
  {
    PropertyValue pv = new PropertyValue();
    pv.Name = "URL";
    pv.Value = url;
    return new PropertyValue[]{pv};
  }

  public boolean isFilterApplicable(String _sFilter, String _sName)
  {
    boolean bFilterDoesApply = true;
    if (_sFilter.length() > 0)
    {
      if (_sName.indexOf(_sFilter) == -1)
      {
        bFilterDoesApply = false;
      }
    }
    return bFilterDoesApply;
  }

//    public static String getServiceDescription(Object _oUnoObject){
//        String sClassName = "";
//        XServiceInfo xServiceInfo = (XServiceInfo) UnoRuntime.queryInterface(XServiceInfo.class, _oUnoObject);
//        if (xServiceInfo != null){
//            String[] sChildServiceNames = removeMandatoryServiceNames(xServiceInfo.getSupportedServiceNames());
//            if (sChildServiceNames.length > 0){
//                sClassName = sChildServiceNames[0];
//            }
//        }
//        return sClassName;
//    }
  private static String[] getMandatoryServiceNames(String _sServiceName)
  {
    String[] sMandatoryServiceNames = new String[]{};
    try
    {
      TypeClass[] eTypeClasses = new com.sun.star.uno.TypeClass[1];
      eTypeClasses[0] = com.sun.star.uno.TypeClass.SERVICE;
      XTypeDescriptionEnumeration xTDEnumeration = getXTypeDescriptionEnumerationAccess().createTypeDescriptionEnumeration(Introspector.getModuleName(_sServiceName), eTypeClasses, TypeDescriptionSearchDepth.INFINITE);
      while (xTDEnumeration.hasMoreElements())
      {
        XTypeDescription xTD = xTDEnumeration.nextTypeDescription();
        if (xTD.getName().equals(_sServiceName))
        {
          XServiceTypeDescription xServiceTypeDescription = (XServiceTypeDescription) UnoRuntime.queryInterface(XServiceTypeDescription.class, xTD);
          XServiceTypeDescription[] xMandatoryServiceTypeDescriptions = xServiceTypeDescription.getMandatoryServices();
          int nlength = xMandatoryServiceTypeDescriptions.length;
          sMandatoryServiceNames = new String[nlength];
          for (int i = 0; i < nlength; i++)
          {
            sMandatoryServiceNames[i] = xMandatoryServiceTypeDescriptions[i].getName();
          }

        }
      }
    }
    catch (java.lang.Exception e)
    {
      System.out.println(System.out);
    }
    return sMandatoryServiceNames;
  }

  private static String[] removeMandatoryServiceNames(String[] _sServiceNames)
  {
    try
    {
      List aList = java.util.Arrays.asList(_sServiceNames);
      Vector aVector = new Vector(aList);
      for (int n = 0; n < _sServiceNames.length; n++)
      {
        String[] sDelServiceNames = getMandatoryServiceNames(_sServiceNames[n]);
        for (int m = 0; m < sDelServiceNames.length; m++)
        {
          if (aVector.contains(sDelServiceNames[m]))
          {
            int nIndex = aVector.indexOf(sDelServiceNames[m]);
            aVector.remove(nIndex);
          }
        }
      }
      String[] sRetArray = new String[aVector.size()];
      aVector.toArray(sRetArray);
      return sRetArray;
    }
    catch (java.lang.Exception exception)
    {
      exception.printStackTrace(System.out);
    }
    return new String[]{};
  }

  public static String getDisplayValueOfPrimitiveType(Object _objectElement)
  {
    String sValue = "";
    try
    {
      if (AnyConverter.isString(_objectElement))
      {
        sValue = AnyConverter.toString(_objectElement);
      }
      else if (AnyConverter.isBoolean(_objectElement))
      {
        sValue += AnyConverter.toBoolean(_objectElement);
      }
      else if (AnyConverter.isByte(_objectElement))
      {
        sValue += AnyConverter.toByte(_objectElement);
      }
      else if (AnyConverter.isChar(_objectElement))
      {
        sValue += AnyConverter.toChar(_objectElement);
      }
      else if (AnyConverter.isDouble(_objectElement))
      {
        sValue += AnyConverter.toDouble(_objectElement);
      }
      else if (AnyConverter.isFloat(_objectElement))
      {
        sValue += AnyConverter.toFloat(_objectElement);
      }
      else if (AnyConverter.isInt(_objectElement))
      {
        sValue += AnyConverter.toInt(_objectElement);
      }
      else if (AnyConverter.isLong(_objectElement))
      {
        sValue += AnyConverter.toLong(_objectElement);
      }
      else if (AnyConverter.isShort(_objectElement))
      {
        sValue += AnyConverter.toShort(_objectElement);
      }
    }
    catch (Exception e)
    {
      System.err.println(e);
    }
    return sValue;
  }

  public static String[] getDisplayValuesOfPrimitiveArray(Object _oUnoObject)
  {
    String[] sDisplayValues = null;
    try
    {
      Type aType = AnyConverter.getType(_oUnoObject);
      TypeClass aTypeClass = aType.getTypeClass();
      int nTypeValue = aTypeClass.getValue();
      if (nTypeValue == TypeClass.SEQUENCE_value)
      {
        nTypeValue = (sequenceComponentType(aType)).getTypeClass().getValue();
      }
      switch (nTypeValue)
      {
        case TypeClass.BOOLEAN_value:
          boolean[] bBooleans = (boolean[]) AnyConverter.toArray(_oUnoObject);
          sDisplayValues = new String[bBooleans.length];
          for (int i = 0; i < bBooleans.length; i++)
          {
            sDisplayValues[i] = Boolean.toString(bBooleans[i]);
          }
          break;
        case TypeClass.BYTE_value:
          byte[] bBytes = (byte[]) AnyConverter.toArray(_oUnoObject);
          sDisplayValues = new String[bBytes.length];
          for (int i = 0; i < bBytes.length; i++)
          {
            sDisplayValues[i] = "" + bBytes[i];
          }
          break;
        case TypeClass.DOUBLE_value:
          double[] fdoubles = (double[]) AnyConverter.toArray(_oUnoObject);
          sDisplayValues = new String[fdoubles.length];
          for (int i = 0; i < fdoubles.length; i++)
          {
            sDisplayValues[i] = String.valueOf(fdoubles[i]);
          }
          break;
        case TypeClass.FLOAT_value:
          float[] ffloats = (float[]) AnyConverter.toArray(_oUnoObject);
          sDisplayValues = new String[ffloats.length];
          for (int i = 0; i < ffloats.length; i++)
          {
            sDisplayValues[i] = String.valueOf(ffloats[i]);
          }
          break;
        case TypeClass.LONG_value:
          int[] nints = (int[]) AnyConverter.toArray(_oUnoObject);
          sDisplayValues = new String[nints.length];
          for (int i = 0; i < nints.length; i++)
          {
            sDisplayValues[i] = String.valueOf(nints[i]);
          }
          break;
        case TypeClass.HYPER_value:
          long[] nlongs = (long[]) AnyConverter.toArray(_oUnoObject);
          sDisplayValues = new String[nlongs.length];
          for (int i = 0; i < nlongs.length; i++)
          {
            sDisplayValues[i] = String.valueOf(nlongs[i]);
          }
          break;
        case TypeClass.SHORT_value:
          short[] nShorts = (short[]) AnyConverter.toArray(_oUnoObject);
          sDisplayValues = new String[nShorts.length];
          for (int i = 0; i < nShorts.length; i++)
          {
            sDisplayValues[i] = "" + nShorts[i];
          }
          break;
        case TypeClass.CHAR_value:
          break;
        default:
          System.out.println("Value could not be retrieved: " + aType.getTypeClass().getClass().getName());
      }
      return sDisplayValues;
    }
    catch (Exception e)
    {
      System.err.println(e);
      return null;
    }
  }

  private static Type sequenceComponentType(Type sequenceType)
  {
//        assert sequenceType.getTypeClass() == TypeClass.SEQUENCE;
    String n = sequenceType.getTypeName();
    final String PREFIX = "[]";
//        assert n.startsWith(PREFIX);
    return new Type(n.substring(PREFIX.length()));
  }

  public String getNodeDescription()
  {
    return getNodeDescription(getUnoObject());
  }
  
  public static String getNodeDescription(Object unoObject)
  {
    if(unoObject == null)
      return "null";
    
    XServiceInfo xServiceInfo = (XServiceInfo)UnoRuntime.queryInterface(XServiceInfo.class, unoObject);
    if (xServiceInfo != null)
    {
      return xServiceInfo.getImplementationName();
    }
//    String sClassName = _oUnoObject.getClass().getName();
    if (Introspector.isObjectPrimitive(unoObject))
    {         //super.isO{sObjectClassName.equals("java.lang.String"))issClassName.equals("java.lang.String"))
      return unoObject.toString();
    }
    else
    {
      return unoObject.getClass().getName();
    }
  }

  public void setParameterObjects(Object[] _oParamObjects)
  {
    m_oParamObjects = _oParamObjects;
  }

  public Object[] getParameterObjects()
  {
    return m_oParamObjects;
  }
  
  protected void setUnoObject(Object unoObject)
  {    
    this.unoObject = unoObject;
  }
}
