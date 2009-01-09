/*************************************************************************
 *  The Contents of this file are made available subject to the terms of
 *  the BSD license.
 *  
 *  Copyright (c) 2008 by Sun Microsystems, Inc.
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

package com.sun.star.script.ook;

import com.sun.star.script.provider.*;
import com.sun.star.frame.XModel;
import com.sun.star.reflection.InvocationTargetException;
import com.sun.star.script.framework.container.ScriptMetaData;
import com.sun.star.text.XText;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.uno.Any;
import com.sun.star.uno.Type;
import com.sun.star.uno.UnoRuntime;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Christian Lins (christian.lins@web.de)
 */
public class OokScript
  implements com.sun.star.script.provider.XScript
{
  private XScriptContext context;
  private ScriptMetaData metaData;
  private String         source;
  
  public OokScript(XScriptContext context, ScriptMetaData metaData)
  {
    this.context  = context;
    this.metaData = metaData;
    
    // Retrieve macro source code from meta data
    try
    {
      BufferedReader in = new BufferedReader(
          new InputStreamReader(metaData.getSourceURL().openStream()));
      StringBuffer buf = new StringBuffer();
      for(;;)
      {
        String line = in.readLine();
        if(line == null)
          break;
        buf.append(line);
        buf.append('\n');
      }
      this.source = buf.toString();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public Object invoke(Object[] arg0, short[][] arg1, Object[][] arg2) 
    throws ScriptFrameworkErrorException, InvocationTargetException 
  {
    try
    {
      System.out.println("OokScript::invoke()");
      OokInterpreter ook = new OokInterpreter(this.source);
      ook.run();

      System.out.println(ook.getOutput());

      // Write out to Office
      XModel xDocModel = context.getDocument();

      // getting the text document object
      XTextDocument xtextdocument = (XTextDocument) UnoRuntime.queryInterface(
                  XTextDocument.class, xDocModel);

      XText xText = xtextdocument.getText();
      XTextRange xTextRange = xText.getEnd();
      xTextRange.setString(ook.getOutput());

      return new Any(new Type(), null);
    }
    catch(IOException ex)
    {
      throw new ScriptFrameworkErrorException();
    }
  }
}
