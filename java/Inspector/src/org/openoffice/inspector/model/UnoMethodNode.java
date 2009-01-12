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

import com.sun.star.reflection.ParamInfo;
import com.sun.star.reflection.ParamMode;
import com.sun.star.reflection.XIdlClass;
import com.sun.star.reflection.XIdlMethod;
import com.sun.star.uno.TypeClass;
import java.util.Vector;
import org.openoffice.inspector.Introspector;

public class UnoMethodNode extends UnoNode
{

  XIdlMethod xIdlMethod      = null;
  Object[]   paramObjects    = null;
  Object     unoReturnObject = null;
  boolean    isInvoked       = false;

  /** 
   * Creates a new instance of UnoMethodNode 
   */
  public UnoMethodNode(XIdlMethod xIdlMethod, Object unoObject)
  {
    super(unoObject);
    this.xIdlMethod   = xIdlMethod;
    this.paramObjects = new Object[xIdlMethod.getParameterInfos().length];
  }

  protected boolean isInvokable()
  {
    boolean bisFoldable = true;
    XIdlClass[] xIdlClasses = xIdlMethod.getParameterTypes();
    for (int i = 0; i < xIdlClasses.length; i++)
    {
      bisFoldable = Introspector.isPrimitive(xIdlClasses[i].getTypeClass());
      if (!bisFoldable)
      {
        return false;
      }
    }
    return bisFoldable;
  }

  public XIdlMethod getXIdlMethod()
  {
    return xIdlMethod;
  }

  @Override
  public String getAnchor()
  {
    return getXIdlMethod().getName();
  }

  public String getName()
  {
    return getXIdlMethod().getName();
  }

  @Override
  public String getNodeDescription()
  {
    String sNodeDescription = "";
    String sParameters = getParameterDescription();
    if (xIdlMethod.getParameterInfos().length > 0)
    {
      sNodeDescription = getStandardMethodDescription();
    }
    else
    {
      TypeClass typeClass = getTypeClass();
      if (typeClass != TypeClass.VOID)
      {
        sNodeDescription = getStandardMethodDescription();
      }
      else
      {
        sNodeDescription = getStandardMethodDescription();
      }
    }
    return sNodeDescription;
  }

  public String getStandardMethodDescription()
  {
    String sNodeDescription = xIdlMethod.getReturnType().getName() + " " + 
      xIdlMethod.getName() + " (" + getParameterDescription() + " )";
    return sNodeDescription;
  }

  public boolean hasParameters()
  {
    return (xIdlMethod.getParameterInfos().length > 0);
  }

  public Object[] getLastParameterObjects()
  {
    return this.paramObjects;
  }

  public String getParameterDescription()
  {
    ParamInfo[] paramInfo = xIdlMethod.getParameterInfos();
    String sParameters = "";
    String sStandardMethodDisplayText = xIdlMethod.getReturnType().getName() 
      + " " + xIdlMethod.getName() + " (" + sParameters + " )";
    if (Introspector.isValid(paramInfo))
    {
      //  get all parameters with type and mode
      for (int i = 0; i < paramInfo.length; i++)
      {
        XIdlClass xIdlClass = paramInfo[i].aType;
        if (i == 0)
        {
          //  the first parameter has no leading comma
          sParameters += "[" + getParamMode(paramInfo[i].aMode) + "] " + xIdlClass.getName();
        }
        else
        {
          //  all other parameters are separated with comma
          sParameters += ", [" + getParamMode(paramInfo[i].aMode) + "] " + xIdlClass.getName();
        }
      }
    }
    return sParameters;
  }

  //  return the parameter mode (IN, OUT, INOUT)
  private static String getParamMode(ParamMode paramMode)
  {
    String toReturn = "";
    if (paramMode == ParamMode.IN)
    {
      toReturn = "IN";
    }
    if (paramMode == ParamMode.OUT)
    {
      toReturn = "OUT";
    }
    if (paramMode == ParamMode.INOUT)
    {
      toReturn = "INOUT";
    }
    return (toReturn);
  }

  public TypeClass getTypeClass()
  {
    XIdlClass xIdlClass = xIdlMethod.getReturnType();
    return xIdlClass.getTypeClass();
  }

}
