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
import com.sun.star.beans.PropertyValue;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.reflection.XConstantTypeDescription;
import com.sun.star.reflection.XPropertyTypeDescription;
import com.sun.star.uno.UnoRuntime;
import org.openoffice.inspector.Introspector;

public class UnoPropertyNode extends UnoNode
{
  
  private Property      property;
  private PropertyValue propertyValue;
  private Object        unoReturnObject;
  private String        label = "";

  /** Creates a new instance of UnoPropertyNode */
  public UnoPropertyNode(Property property, Object unoObject)
  {
    super(unoObject);
    this.property  = property;
  }

  public UnoPropertyNode(Property property)
  {
    super(null);
    this.property = property;
  }

  public String getPropertyName()
  {
    return this.property.Name;
  }

  public String getName()
  {
    return getPropertyName();
  }

  public String getClassName()
  {
    String sClassName = "";
    if (this.unoObject != null)
    {
      XServiceInfo xServiceInfo = (XServiceInfo) UnoRuntime.queryInterface(XServiceInfo.class, this.unoObject);
      if (xServiceInfo != null)
      {
        String[] sServiceNames = xServiceInfo.getSupportedServiceNames();
        for (int i = 0; i < sServiceNames.length; i++)
        {
          if (doesServiceSupportProperty(sServiceNames[i], property.Name))
          {
            sClassName = sServiceNames[i];
            break;
          }
        }
      }
    }
    else
    {
      sClassName = "com.sun.star.beans.Property";
    }
    return sClassName;
  }

  protected boolean doesServiceSupportProperty(String _sServiceName, String _sPropertyName)
  {
    try
    {
      XPropertyTypeDescription[] xPropertyTypeDescriptions = Introspector.getIntrospector().getPropertyDescriptionsOfService(_sServiceName);
      for (int i = 0; i < xPropertyTypeDescriptions.length; i++)
      {
        if (xPropertyTypeDescriptions[i].getName().equals(_sServiceName + "." + _sPropertyName))
        {
          return true;
        }
      }
    }
    catch (java.lang.Exception e)
    {
      System.out.println(System.out);
    }
    return false;
  }

  public Property getProperty()
  {
    return this.property;
  }

  public static String getStandardPropertyValueDescription(PropertyValue _aPropertyValue)
  {
    if (!Introspector.isObjectPrimitive(_aPropertyValue.Value))
    {
      return _aPropertyValue.Name;
    }
    else
    {
      return _aPropertyValue.Name + " : " + UnoNode.getDisplayValueOfPrimitiveType(_aPropertyValue.Value);
    }
  }
}


