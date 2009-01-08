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

package org.openoffice.inspector;

import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.uno.UnoRuntime;

import javax.swing.JOptionPane;

public class InspectorApplication
{

  public static void main(String args[])
  {
    com.sun.star.uno.XComponentContext xContext = null;
    try
    {
      // Get the remote office component context
      xContext = Bootstrap.bootstrap();
      if (xContext != null)
      {
        System.out.println("Connected to a running office ...");
      }
    }
    catch (BootstrapException ex)
    {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(null, "Could not bootstrap soffice executable!");
      System.exit(1);
    }

    try
    {
      com.sun.star.lang.XMultiComponentFactory xMCF = xContext.getServiceManager();
      // Creating an instance of the instance inspector with arguments

      XSingleComponentFactory xFactory = Inspector.__getComponentFactory(Inspector._Inspector.class.getName());
      Object obj = null;
      if (xFactory != null)
      {
        obj = xFactory.createInstanceWithContext(xContext);
      }
      org.openoffice.XInstanceInspector xInstInspector = null;
      if (obj != null)
      {
        xInstInspector = (org.openoffice.XInstanceInspector) UnoRuntime.queryInterface(org.openoffice.XInstanceInspector.class, obj);
      }

      /* A desktop environment contains tasks with one or more
      frames in which components can be loaded. Desktop is the
      environment for components which can instanciate within
      frames. */
      com.sun.star.frame.XComponentLoader xCmpLoader = (com.sun.star.frame.XComponentLoader) UnoRuntime.queryInterface(com.sun.star.frame.XComponentLoader.class,
        xMCF.createInstanceWithContext("com.sun.star.frame.Desktop", xContext));

      // Load a new spreadsheet document, which will be automaticly
      // displayed and is used for inspection
      com.sun.star.lang.XComponent xComp = xCmpLoader.loadComponentFromURL("private:factory/scalc", "_blank", 0, new com.sun.star.beans.PropertyValue[0]);
      xInstInspector.inspect(xCmpLoader, "");
      System.out.println("You can now inspect the new spreadsheet " + "document ...\n");
    }
    catch (Exception e)
    {
      System.err.println(e + e.getMessage());
      e.printStackTrace();
    }
//        System.exit( 0 );
  }
}
