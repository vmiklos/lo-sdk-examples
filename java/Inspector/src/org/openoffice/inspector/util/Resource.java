/*************************************************************************
 *
 *  The Contents of this file are made available subject to the terms of
 *  the BSD license.
 *  
 *  Copyright (c) 2009 by Sun Microsystems, Inc.
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

package org.openoffice.inspector.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * Stellt statische Methoden zum Laden von
 * einzelnen Resourcen zur Verfuegung.
 */
public class Resource
{
  public static byte[] getBytes(File file)
  {
    try
    {
      FileInputStream in = new FileInputStream(file);
      byte[] buffer = new byte[(int)file.length()];
      
      in.read(buffer);
      
      return buffer;
    }
    catch(IOException ex)
    {
      ex.printStackTrace();
      return null;
    }
  }
  
  public static String getAsString(String resource)
  {
    try
    {
      StringBuffer sbuf = new StringBuffer();
      InputStream  in   = getAsStream(resource);
      
      byte[] buffer = new byte[128];
      
      for(;;)
      {
        int read = in.read(buffer);
        if(read < 0)
          break;
        else
          sbuf.append(new String(buffer, 0, read));
      }
      
      return sbuf.toString();
    }
    catch(IOException ex)
    {
      ex.printStackTrace();
      return null;
    }
  }
  
  /**
   * Laedt eine Bilddatei von einer lokalen Resource.
   * @param name
   * @return Gibt null zurueck, falls das Bild nicht
   * gefunden oder geladen werden konnte.
   */
  public static ImageIcon getImage(String name)
  {
    URL url = getAsURL(name);
    
    if(url == null)
    {
      Image img = Toolkit.getDefaultToolkit().createImage(name);
      return new ImageIcon(img);
    }
    
    return new ImageIcon(url);
  }
  
  /**
   * Laedt eine Resource und gibt einen Verweis auf sie als
   * URL zurueck.
   * @return
   */
  public static URL getAsURL(String name)
  {
    return Resource.class.getClassLoader().getResource(name);
  }
  
  /**
   * Laedt eine Resource und gibt einen InputStream darauf
   * zurueck.
   * @param name
   * @return
   */
  public static InputStream getAsStream(String name)
  {
    try
    {
      URL url = getAsURL(name);
      return url.openStream();
    }
    catch(IOException ex)
    {
      ex.printStackTrace();
      return null;
    }
  }
}
