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
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CodeGenerator
{
  
  private static volatile Map<Language, Class<?>> CodeGenerators 
    = new HashMap<Language, Class<?>>();
  private static volatile Map<Object, CodeGenerator[]> CodeGenCache
    = new HashMap<Object, CodeGenerator[]>();
  
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
  
  /**
   * @param obj
   * @return An array of CodeGenerators. Some array entries can be null.
   * @throws java.lang.IllegalAccessException
   * @throws java.lang.InstantiationException
   */
  public static CodeGenerator[] getInstances(Object obj)
    throws IllegalAccessException, InstantiationException
  {
    if(!CodeGenCache.containsKey(obj))
      getInstance(null, obj);
      
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
   * @return CodeGenerator associated with given Language and Object. Can
   *         be null.
   * @throws java.lang.InstantiationException
   * @throws java.lang.IllegalAccessException
   */
  public static synchronized CodeGenerator getInstance(Language lang, Object rootObject)
    throws InstantiationException, IllegalAccessException
  {
    CodeGenerator[] codeGens = CodeGenCache.get(rootObject);
    if (codeGens == null)
    {
      Language[] langs = Language.values();
      codeGens = new CodeGenerator[langs.length];
      for (int n = 0; n < langs.length; n++)
      {
        Class<?> clazz = CodeGenerators.get(langs[n]);
        if(clazz != null)
        {
          codeGens[n] = (CodeGenerator)clazz.newInstance();
          codeGens[n].setRootObject(rootObject);
        }
      }
      CodeGenCache.put(rootObject, codeGens);
    }

    for (CodeGenerator codeGen : codeGens)
    {
      if(codeGen != null && codeGen.getLanguage().equals(lang))
        return codeGen;
    }

    // else
    System.err.println("No CodeGenerator for language " + lang + " found!");
    return null;
  }
  
  /**
   * Returns the line number of the first different line in the two
   * strings or 0 if there is no difference.
   * @param strA
   * @param strB
   * @return
   */
  protected static int firstDifferentLine(String strA, String strB)
  {
    try
    {
      LineNumberReader readerA = new LineNumberReader(new StringReader(strA));
      LineNumberReader readerB = new LineNumberReader(new StringReader(strB));

      String lineA = readerA.readLine();
      String lineB = readerB.readLine();

      while(lineA != null && lineB != null)
      {
        if(!lineA.equals(lineB))
          return readerA.getLineNumber();

        lineA = readerA.readLine();
        lineB = readerB.readLine();
      }

      return readerA.getLineNumber();
    }
    catch(IOException ex)
    {
      // Probably never thrown as we read from strings not files
      ex.printStackTrace();
      return 0;
    }
  }
  
  private List<CodeUpdateListener> codeUpdateListeners = new ArrayList<CodeUpdateListener>();
  
  /** This variable indicates if the source code needs to be regenerated */
  protected volatile boolean codeUpdateRequired = true;
  
  /** Root UNO object for which code is created */
  protected Object rootObject = null;
  
  /** The last created source code of this generator */
  protected String sourceCode = new String();

  protected CodeGenerator()
  {
  }
  
  public void addCodeUpdateListener(CodeUpdateListener listener)
  {
    this.codeUpdateListeners.add(listener);
    System.out.println(listener + " registered as CodeUpdateListener");
  }
  
  public void removeCodeUpdateListener(CodeUpdateListener listener)
  {
    this.codeUpdateListeners.remove(listener);
  }
  
  protected void fireCodeUpdateEvent(CodeUpdateEvent event)
  {
    for(CodeUpdateListener listener : this.codeUpdateListeners)
    {
      listener.codeUpdated(event);
    }
  }
  
  public abstract void addAccessorCodeFor(String property);
  public abstract void addInvokeCodeFor(XIdlMethod method);
  public abstract void addQueryCodeFor(String iface);
  
  public abstract Language  getLanguage();
  public abstract String    getSourceCode();
  
  protected void setRootObject(Object unoObject)
  {
    this.rootObject = unoObject;
  }

}
