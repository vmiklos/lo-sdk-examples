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

package org.openoffice.inspector.gui;

import com.sun.star.reflection.ParamInfo;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openoffice.inspector.model.UnoMethodNode;

/**
 * Contains rows of input fields for method parameter.
 * @author Christian Lins
 */
class MethodParametersPanel extends JPanel
{
  
  private MethodParameterComponent[] pcomp = null;
  
  public MethodParametersPanel(UnoMethodNode unoMethodNode)
  {
    setLayout(new GridLayout(0, 1, 5, 5));
    
    ParamInfo[] paramInfo = unoMethodNode.getXIdlMethod().getParameterInfos();
    
    if(paramInfo == null || paramInfo.length == 0)
    {
      add(new JLabel("void"));
    }
    else
    {
      pcomp = new MethodParameterComponent[paramInfo.length];
      for(int n = 0; n < paramInfo.length; n++)
      {
        System.out.println("Parameter: " + paramInfo[n].aName);
        this.pcomp[n] = new MethodParameterComponent(paramInfo[n]);
        add(this.pcomp[n]);
      }
    }
  }
  
  public MethodParameterComponent getMethodParameterComponent(int idx)
  {
    return this.pcomp[idx];
  }
}
