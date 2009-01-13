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
import com.sun.star.script.XInvocation;
import java.util.ArrayList;
import java.util.List;
import org.openoffice.inspector.util.Resource;
import org.openoffice.inspector.util.StringTemplate;

/**
 * Generator for Java code.
 * @author Christian Lins (cli@openoffice.org)
 */
public class JavaCodeGenerator 
  extends CodeGenerator 
{
  
  private List<Class<?>> queryInterfaces = new ArrayList<Class<?>>();
  
  private List<XIdlMethod> invokeMethods = new ArrayList<XIdlMethod>();
  
  private StringTemplate tmplInvoke = new StringTemplate(
    Resource.getAsString("org/openoffice/inspector/codegen/template/JavaInvoke.tmpl"));
  
  private StringTemplate tmplProgram = new StringTemplate(
    Resource.getAsString("org/openoffice/inspector/codegen/template/JavaProgramStub.tmpl"));

  private StringTemplate tmplQueryInterface = new StringTemplate(
    Resource.getAsString("org/openoffice/inspector/codegen/template/JavaQueryInterface.tmpl"));
  
  /**
   * This constructor is protected agains direct instantiation.
   */
  protected JavaCodeGenerator()
  {
  }

  public String getSourceCode()
  {
    if(!codeUpdateRequired)
      return sourceCode;
    
    StringBuffer imports  = new StringBuffer();
    StringBuffer code     = new StringBuffer();
    
    // Query all interfaces
    for(Class<?> iface : queryInterfaces)
    {
      tmplQueryInterface.set("interface", iface.getSimpleName());
      tmplQueryInterface.set("variable", "myVar");
      tmplQueryInterface.set("unoobject", "xContext"); // or xDocModel
      code.append(tmplQueryInterface.toString());
      
      // Add import
      imports.append("import ");
      imports.append(iface.getName());
      imports.append(";\n");
    }
    
    // Invoke all methods
    for(XIdlMethod method : this.invokeMethods)
    {
      tmplInvoke.set("methodname", method.getName());

      code.append(tmplInvoke.toString());
      code.append('\n');
    }
    
    tmplProgram.set("imports", imports.toString());
    tmplProgram.set("code", code.toString());
    
    sourceCode = tmplProgram.toString();
    return sourceCode;
  }
  
  public Language getLanguage()
  {
    return Language.Java;
  }
  
  @Override
  protected void setRootObject(Object obj)
  {
    super.setRootObject(obj);
    
    this.queryInterfaces.add(obj.getClass());
  }

  @Override
  public void addAccessorCodeFor(Object unoObject)
  {
    codeUpdateRequired = true;
    
    CodeUpdateEvent event = new CodeUpdateEvent(getSourceCode(), Language.Java, 0);
    fireCodeUpdateEvent(event);
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
    codeUpdateRequired = true;
    
    CodeUpdateEvent event = new CodeUpdateEvent(getSourceCode(), Language.Java, 0);
    fireCodeUpdateEvent(event);
    
    // Add method for invoke code generation
    this.invokeMethods.add(method);
    
    // Make sure that the program queries a XInvocation interface
    this.queryInterfaces.add(XInvocation.class);
  }

}
