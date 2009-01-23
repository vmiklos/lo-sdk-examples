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

import com.sun.star.beans.XIntrospectionAccess;
import com.sun.star.beans.XPropertySet;
import com.sun.star.reflection.XIdlMethod;
import com.sun.star.script.XInvocation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openoffice.inspector.util.Resource;
import org.openoffice.inspector.util.StringTemplate;

/**
 * Generator for Java code.
 * @author Christian Lins (cli@openoffice.org)
 */
public class JavaCodeGenerator 
  extends CodeGenerator 
{
  
  private Set<String> properties      = new HashSet<String>();
  private Set<String> queryInterfaces = new HashSet<String>();
  private List<XIdlMethod> invokeMethods = new ArrayList<XIdlMethod>();
  
  private StringTemplate tmplInvoke = new StringTemplate(
    Resource.getAsString("org/openoffice/inspector/codegen/template/JavaInvoke.tmpl"));
  
  private StringTemplate tmplProgram = new StringTemplate(
    Resource.getAsString("org/openoffice/inspector/codegen/template/JavaProgramStub.tmpl"));

  private StringTemplate tmplProperty = new StringTemplate(
    Resource.getAsString("org/openoffice/inspector/codegen/template/JavaGetPropValue.tmpl"));
  
  private StringTemplate tmplQueryInterface = new StringTemplate(
    Resource.getAsString("org/openoffice/inspector/codegen/template/JavaQueryInterface.tmpl"));
  
  private int lastChangedLine = 0;
  
  /**
   * This constructor is protected agains direct instantiation.
   */
  protected JavaCodeGenerator()
  {
  }

  /**
   * Generates and returns the Java source code.
   */
  public String getSourceCode()
  {
    if(!codeUpdateRequired)
      return sourceCode;
    
    StringBuffer imports  = new StringBuffer();
    StringBuffer code     = new StringBuffer();
    
    // Query all interfaces
    for(String iface : queryInterfaces)
    {
      String iface_short = iface.substring(iface.lastIndexOf(".") + 1);
      String iface_var   = iface_short
        .replaceFirst(iface_short.substring(0, 1), 
                      iface_short.substring(0, 1).toLowerCase());
      tmplQueryInterface.set("interface", iface_short);
      tmplQueryInterface.set("variable", iface_var);
      tmplQueryInterface.set("unoobject", "xContext"); // or xDocModel
      code.append(tmplQueryInterface.toString());
      
      // Add import
      imports.append("import ");
      imports.append(iface);
      imports.append(";\n");
    }
    
    // Invoke all methods
    for(XIdlMethod method : this.invokeMethods)
    {
      tmplInvoke.set("methodname", method.getName());
      tmplInvoke.set("inoutparams", generateInOutParamsFor(method));

      code.append(tmplInvoke.toString());
      code.append('\n');
    }
    
    // Get value from properties
    if(this.properties.size() > 0)
    {
      imports.append("import com.sun.star.beans.Property;\n");
    }
    for(String property : this.properties)
    {
      tmplProperty.set("propname", property);
      
      code.append(tmplProperty.toString());
      code.append('\n');
    }
    
    tmplProgram.set("imports", imports.toString());
    tmplProgram.set("code", code.toString());
    
    String newSourceCode = tmplProgram.toString();
    this.lastChangedLine = firstDifferentLine(sourceCode, newSourceCode);
    this.sourceCode      = newSourceCode;
    return sourceCode;
  }
  
  /**
   * Generates a Java code fragment for an in/out parameter
   * declaration specific for a method invocation.
   * @param method
   * @return
   */
  private String generateInOutParamsFor(XIdlMethod method)
  {
    StringBuffer buf = new StringBuffer();
    
    buf.append("new Object[1][");
    buf.append(method.getParameterInfos().length);
    buf.append("]");
    
    return buf.toString();
  }
  
  public Language getLanguage()
  {
    return Language.Java;
  }
  
  @Override
  protected void setRootObject(Object obj)
  {
    super.setRootObject(obj);
  }

  @Override
  public void addAccessorCodeFor(String property)
  {
    if(!this.properties.contains(property))
    {
      this.properties.add(property);
      
      // We need interfaces to access the properties
      addQueryCodeFor(XIntrospectionAccess.class.getName());
      addQueryCodeFor(XPropertySet.class.getName());
      fireCodeUpdateEvent();
    }
  }

  /**
   * Generates code that shows the invocation of the given method
   * at the specified unoObject.
   * @param unoObject
   * @param method
   */
  @Override
  public void addInvokeCodeFor(XIdlMethod method)
  {
    // Add method for invoke code generation
    this.invokeMethods.add(method);
    
    // Make sure that the program queries a XInvocation interface
    addQueryCodeFor(XInvocation.class.getName());
    
    // Force the code regeneration and send update event
    fireCodeUpdateEvent();
  }
  
  @Override
  public void addQueryCodeFor(String iface)
  {
    if(!queryInterfaces.contains(iface))
    {
      this.queryInterfaces.add(iface);
      fireCodeUpdateEvent();
    }
  }

  protected void fireCodeUpdateEvent()
  {
    this.codeUpdateRequired = true;
    CodeUpdateEvent event = new CodeUpdateEvent(
      getSourceCode(), Language.Java, this.lastChangedLine);
    super.fireCodeUpdateEvent(event);
  }

}
