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
package org.openoffice.inspector.model;

import com.sun.star.beans.Property;
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
import java.util.ArrayList;
import java.util.List;
import org.openoffice.inspector.Introspector;

public class UnoNode
{

  protected Object unoObject;

  private XMultiComponentFactory xMultiComponentFactory;
  private XComponentContext      xComponentContext;

  /** Creates a new instance of UnoNode */
  public UnoNode(Object unoObject)
  {
    this.xComponentContext      = Introspector.getIntrospector().getXComponentContext();
    this.xMultiComponentFactory = xComponentContext.getServiceManager();
    this.unoObject = unoObject;
  }

  /**
   * Introspects the capsulated Uno object and retrieves the
   * supported service names from it.
   * @return A list of UnoServiceNodes
   */
  public List<UnoServiceNode> getSupportedServices()
  {
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
    return this.xComponentContext;
  }

  protected XMultiComponentFactory getXMultiComponentFactory()
  {
    return this.xMultiComponentFactory;
  }

  private static XTypeDescriptionEnumerationAccess getXTypeDescriptionEnumerationAccess()
  {
    return Introspector.getIntrospector().getXTypeDescriptionEnumerationAccess();
  }

  public String getAnchor()
  {
    return "";
  }

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

  public String getImplementationName()
  {
    XServiceInfo xServiceInfo = (XServiceInfo)UnoRuntime.queryInterface(XServiceInfo.class, unoObject);
    if (xServiceInfo != null)
    {
      return xServiceInfo.getImplementationName();
    }
    else
    {
      return null;
    }
  }
  
  public String getNodeDescription()
  {
    return getNodeDescription(getUnoObject());
  }
  
  protected static String getNodeDescription(Object unoObject)
  {
    if(unoObject == null)
      return "null";
    
    XServiceInfo xServiceInfo = (XServiceInfo)UnoRuntime.queryInterface(XServiceInfo.class, unoObject);
    if (xServiceInfo != null)
    {
      return xServiceInfo.getImplementationName();
    }
    else if (Introspector.isObjectPrimitive(unoObject))
    { 
      return unoObject.toString();
    }
    else
    {
      return unoObject.getClass().getName();
    }
  }
  
  protected void setUnoObject(Object unoObject)
  {    
    this.unoObject = unoObject;
  }
}
