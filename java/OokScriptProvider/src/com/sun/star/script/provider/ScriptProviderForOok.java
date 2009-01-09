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

package com.sun.star.script.provider;

import com.sun.star.script.ook.OokScript;
import com.sun.star.script.ook.editor.OokScriptEditor;
import com.sun.star.comp.loader.FactoryHelper;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.uno.XComponentContext;
import com.sun.star.lang.XSingleServiceFactory;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.script.browse.XBrowseNode;
import com.sun.star.script.framework.container.ScriptMetaData;
import com.sun.star.script.framework.provider.ScriptEditor;
import com.sun.star.script.framework.provider.ScriptProvider;

public final class ScriptProviderForOok 
{
  
  private static final String[] services =
  {
    "com.sun.star.script.browse.BrowseNode",
    "com.sun.star.script.provider.ScriptProvider",
    "com.sun.star.script.provider.LanguageScriptProvider",
    "com.sun.star.script.provider.ScriptProviderForOok",
    "com.sun.star.script.framework.provider.ScriptEditor"
  };
  
  public static class _ScriptProviderForOok 
    extends ScriptProvider
    implements XBrowseNode
  {
    public _ScriptProviderForOok(XComponentContext context) 
    {
      super(context, "Ook");
      m_xContext = context;
    }
    
    public ScriptEditor getScriptEditor()
    {
      return new OokScriptEditor();
    }

    public boolean hasScriptEditor()
    {
      return true;
    }

    /**
     * A factory method for the creation of XScript implementations.
     * @param scriptURI
     * @return an object implementing XScript representing the script
     * @throws com.sun.star.script.provider.ScriptFrameworkErrorException
     */
    public com.sun.star.script.provider.XScript getScript(String scriptURI)
            throws com.sun.star.script.provider.ScriptFrameworkErrorException 
    {
      OokScript script = null;

      try 
      {
        ScriptMetaData scriptMetaData = getScriptData(scriptURI);
        XScriptContext xScriptContext = getScriptingContext();
        script = new OokScript(xScriptContext, scriptMetaData);
        System.out.println("\tScript " + script + " created!");
      } 
      catch (com.sun.star.uno.Exception ex) 
      {
        ex.printStackTrace();
        System.out.println("Failed to get script: " + scriptURI);
      }
      return script;
    }
  }
  
  public static XSingleServiceFactory __getServiceFactory(String implName,
          XMultiServiceFactory multiFactory,
          XRegistryKey regKey) 
  {
    System.out.println("ScriptProviderForOok::__getServiceFactory(\"" + implName + "\")");
    XSingleServiceFactory xSingleServiceFactory = null;

    if (implName.equals(ScriptProviderForOok._ScriptProviderForOok.class.getName())) 
    {
      xSingleServiceFactory = FactoryHelper.getServiceFactory(
              ScriptProviderForOok._ScriptProviderForOok.class,
              "com.sun.star.script.provider.ScriptProviderForOok",
              multiFactory,
              regKey);
    }
    
    System.out.println("\treturn: " + xSingleServiceFactory);
    return xSingleServiceFactory;
  }

  public static boolean __writeRegistryServiceInfo(XRegistryKey regKey) 
  {
    System.out.println("ScriptProviderForOok::__writeRegistryServiceInfo");
    String impl = ScriptProviderForOok._ScriptProviderForOok.class.getName();

    for(String service : services)
    {
      if(!FactoryHelper.writeRegistryServiceInfo(impl, service, regKey))
      {
        System.out.println("\tFailed registering: " + service);
        return false;
      }
    }
    
    return true;
  }
}
