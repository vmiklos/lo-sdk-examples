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

package org.openoffice.inspector.codegen;

import com.sun.star.reflection.XIdlMethod;
import java.util.HashSet;
import java.util.Set;
import org.openoffice.inspector.util.Resource;
import org.openoffice.inspector.util.StringTemplate;

public class BasicCodeGenerator 
  extends CodeGenerator
{

  private Set<String>     interfaces = new HashSet<String>();
  private Set<String>     properties = new HashSet<String>();
  private Set<XIdlMethod> methods    = new HashSet<XIdlMethod>();
  
  private StringTemplate tmplProgram = new StringTemplate(
    Resource.getAsString("org/openoffice/inspector/codegen/template/BasicProgramStub.tmpl"));

  private StringTemplate tmplProperty = new StringTemplate(
    Resource.getAsString("org/openoffice/inspector/codegen/template/BasicGetPropValue.tmpl"));
  
  protected BasicCodeGenerator()
  {
  }
  
  public Language getLanguage()
  {
    return Language.StarBasic;
  }
  
  public String getSourceCode()
  {
    StringBuffer code = new StringBuffer();
    
    // Loop over all interfaces, although there is now query necessary
    // we should print out an informational message
    for(String iface : this.interfaces)
    {
      code.append("REM You need not query for interface ");
      code.append(iface);
      code.append("\nREM Just call the method you want");
    }
    
    // Loop over all methods
    for(XIdlMethod method : this.methods)
    {
      code.append("  ");
      code.append(generateMethodInvocationCode(method));
      code.append('\n');
    }
    
    // Loop over all property
    for(String property : this.properties)
    {
      tmplProperty.set("propname", property);
      code.append(tmplProperty.toString());
    }
    
    tmplProgram.set("code", code.toString());
    
    return tmplProgram.toString();
  }
  
  private String generateMethodInvocationCode(XIdlMethod method)
  {
    StringBuffer buf = new StringBuffer();
    int numParams = method.getParameterInfos().length;
    
    buf.append("retval");
    buf.append(method.getName());
    buf.append(" = doc.");
    buf.append(method.getName());
    buf.append("(");
    
    for(int n = 0; n < numParams; n++)
    {
      buf.append("param");
      buf.append(n);
      if(n + 1 < numParams)
      {
        buf.append(", ");
      }
    }
    
    buf.append(")");
    
    return buf.toString();
  }
  
  @Override
  public void addAccessorCodeFor(String property)
  {
    if(!this.properties.contains(property))
    {
      this.properties.add(property);
      fireCodeUpdateEvent();
    }
  }

  @Override
  public void addInvokeCodeFor(XIdlMethod method)
  {
    if(!this.methods.contains(method))
    {
      this.methods.add(method);
      fireCodeUpdateEvent();     
    }
  }
  
  @Override
  public void addQueryCodeFor(String iface)
  {
    if(!this.interfaces.contains(iface))
    {
      this.interfaces.add(iface);
      fireCodeUpdateEvent();
    }
  }
  
  protected void fireCodeUpdateEvent()
  {
    CodeUpdateEvent event = new CodeUpdateEvent(
      getSourceCode(), Language.StarBasic, 0);
    super.fireCodeUpdateEvent(event); 
  }

}
