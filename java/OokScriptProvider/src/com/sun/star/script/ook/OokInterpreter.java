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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Ook Interpreter.
 * @author Christian Lins (christian.lins@web.de)
 */
public class OokInterpreter 
{
  public static final char[] ALLOWED_CHARS = {'O', 'o', 'k', '.', '?', '!'}; 
  public static final int    TOKEN_INC     = "Ook.Ook.".hashCode();
  public static final int    TOKEN_DEC     = "Ook!Ook!".hashCode();
  public static final int    TOKEN_RIGHT   = "Ook.Ook?".hashCode();
  public static final int    TOKEN_LEFT    = "Ook?Ook.".hashCode();
  public static final int    TOKEN_LSTART  = "Ook!Ook?".hashCode();
  public static final int    TOKEN_LEND    = "Ook?Ook!".hashCode();
  public static final int    TOKEN_OUT     = "Ook!Ook.".hashCode();
  public static final int    TOKEN_IN      = "Ook.Ook!".hashCode();
  
  private String source = null;
  private String output = "";
  private int    ptr    = 0;   // Parser pointer to next character
  private Map<Integer, Character> buffer = new HashMap<Integer, Character>();
  private int                     bufptr = 0;
  private List<Integer>           tokens = new ArrayList<Integer>();
  
  public OokInterpreter(String source)
  {
    this.source = source;
    
    String tokenStr = nextToken();
    while(tokenStr != null)
    {
      tokens.add(tokenStr.hashCode());
      tokenStr = nextToken();
    }
  }
  
  private boolean contains(char[] array, char key)
  {
    for(char c : array)
      if(c == key)
        return true;
    return false;
  }
  
  public String getOutput()
  {
    return this.output;
  }
  
  private String nextToken()
  {
    StringBuffer buf = new StringBuffer();
    
    do
    {
      if(buf.length() >= 8)
        return buf.toString();
      else if(contains(ALLOWED_CHARS, this.source.charAt(ptr)))
        buf.append(this.source.charAt(ptr));
    }
    while(++ptr < this.source.length());
    
    if(buf.length() < 0)
      System.out.println("Warning: incomplete token '" + buf.toString() + "'");
    
    return null;
  }
  
  public void run()
    throws IOException
  {
    int tokptr = 0;
    int token = tokens.get(tokptr);
    
    while(tokptr < tokens.size())
    {
      if(token == TOKEN_INC)
      {
        Character c = this.buffer.get(bufptr);
        if(c == null)
          c = (char)0;
        this.buffer.put(bufptr, ++c);
      }
      else if(token == TOKEN_DEC)
      {
        Character c = this.buffer.get(bufptr);
        if(c == null)
          c = (char)0;
        this.buffer.put(bufptr, --c);
      }
      else if(token == TOKEN_RIGHT)
      {
        bufptr++;
      }
      else if(token == TOKEN_LEFT)
      {
        bufptr--;
      }
      else if(token == TOKEN_LSTART)
      {
        Character c = this.buffer.get(bufptr);
        if(c == null)
          c = (char)0;
        
        if(c == 0)
        { 
          // Jump to end of loop
          while(tokens.get(tokptr) != TOKEN_LEND)
            tokptr++;
        }
      }
      else if(token == TOKEN_LEND)
      {
        Character c = this.buffer.get(bufptr);
        if(c == null)
          c = (char)0;
        
        if(c != 0)
        {
          // Jump to head of loop
          while(tokens.get(tokptr) != TOKEN_LSTART)
            tokptr--;
        }
      }
      else if(token == TOKEN_OUT)
      {
        output += this.buffer.get(bufptr);
      }
      else if(token == TOKEN_IN)
      {
        int in = System.in.read();
        this.buffer.put(bufptr, (char)in);
      }
      
      if(++tokptr < tokens.size())
        token = tokens.get(tokptr);
    }
  }
}
