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

import java.util.HashMap;
import java.util.Map;

/**
 * Class that allows simple String template handling.
 * Leaves much space for performance improvements.
 * @author Christian Lins (cli@openoffice.org)
 */
public class StringTemplate 
{
  private String              str               = null;
  private String              templateDelimiter = "%";
  private Map<String, String> templateValues    = new HashMap<String, String>();
  private String              templatedString   = null;
  private boolean             updateRequired    = true;
  
  public StringTemplate(String str, String templateDelimiter)
  {
    this.str               = str;
    this.templateDelimiter = templateDelimiter;
  }
  
  public StringTemplate(String str)
  {
    this(str, "%");
  }
  
  public void reset()
  {
    this.templateValues.clear();
    this.updateRequired = true;
  }
  
  public void set(String template, String value)
  {
    this.templateValues.put(template, value);
    this.updateRequired = true;
  }
  
  public void set(String template, long value)
  {
    set(template, Long.toString(value));
  }
  
  public void set(String template, double value)
  {
    set(template, Double.toString(value));
  }
  
  public void set(String template, Object obj)
  {
    set(template, obj.toString());
  }
  
  @Override
  public String toString()
  {
    if(this.updateRequired)
    {
      String ret = new String(str);

      for(String key : this.templateValues.keySet())
      {
        String value = this.templateValues.get(key);
        ret = ret.replace(templateDelimiter + key, value);
      }
      this.templatedString = ret;
      this.updateRequired  = false;
    }
    return this.templatedString;
  }
}
