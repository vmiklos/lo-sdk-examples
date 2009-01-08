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

import java.util.HashMap;
import java.util.Map;

public abstract class CodeGenerator
{
  
  private static Map<Language, Class<?>> CodeGenerators 
    = new HashMap<Language, Class<?>>();
  private static Map<Object, CodeGenerator> CodeGenCache
    = new HashMap<Object, CodeGenerator>();
  
  static
  {
    CodeGenerators.put(Language.CPlusPlus, CPlusPlusCodeGenerator.class);
    CodeGenerators.put(Language.Java, JavaCodeGenerator.class);
    CodeGenerators.put(Language.StarBasic, BasicCodeGenerator.class);
  }
  
  /**
   * Deletes the CodeGenerator instance of the given UNO object.
   * This method is used to reset the code generation of a specific
   * UNO object.
   * @param obj
   */
  public static void deleteInstanceOf(Object obj)
  {
    CodeGenCache.remove(obj);
  }
  
  public static CodeGenerator getInstance(Object obj)
  {
    return CodeGenCache.get(obj);
  }
  
  /**
   * Creates a new CodeGenerator for the given language. The root object
   * is attached to the CodeGenerator and basic imports and a program
   * stub of the target language are created.
   * Once the CodeGenerator is created for a specific object, the same
   * instance is returned until it is manually resetted using deleteInstanceOf().
   * @param lang
   * @param rootObject
   * @return
   * @throws java.lang.InstantiationException
   * @throws java.lang.IllegalAccessException
   */
  public static CodeGenerator getInstance(Language lang, Object rootObject)
    throws InstantiationException, IllegalAccessException
  {
    Class<?> clazz = CodeGenerators.get(lang);
    if(clazz != null)
    {
      CodeGenerator codeGen = CodeGenCache.get(rootObject);
      if(codeGen == null || codeGen.getLanguage() != lang)
      {
        codeGen = (CodeGenerator)clazz.newInstance();
        codeGen.setRootObject(rootObject);
        CodeGenCache.put(rootObject, codeGen);
      }
      return codeGen;
    }
    else
    {
      System.err.println("No CodeGenerator for language " + lang + " found!");
      return null;
    }
  }
  
  /** Root UNO object for which code is created */
  protected Object rootObject = null;

  protected CodeGenerator()
  {
  }
  
  public abstract Language  getLanguage();
  public abstract String    getSourceCode();
  
  protected void setRootObject(Object unoObject)
  {
    this.rootObject = unoObject;
  }

}
