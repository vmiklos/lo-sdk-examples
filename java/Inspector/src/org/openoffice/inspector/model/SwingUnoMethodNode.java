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

package org.openoffice.inspector.model;

import com.sun.star.reflection.XIdlClass;
import com.sun.star.reflection.XIdlMethod;
import com.sun.star.uno.TypeClass;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * @author Christian Lins (cli@openoffice.org)
 */
public class SwingUnoMethodNode 
  extends SwingUnoNode 
  implements ActionListener
{

  public SwingUnoMethodNode(UnoMethodNode node)
  {
    super(node);
    
    add(new DummyNode());
  }

  public boolean isFoldable()
  {
    return ((UnoMethodNode)getUnoNode()).isFoldable();
  }
  
  @Override
  public boolean isVisible()
  {
    return true; //TODO
  }

  @Override
  public String getName()
  {
    return ((UnoMethodNode)getUnoNode()).getName();
  }

  @Override
  public String getClassName()
  {
    String sClassName = "";
    sClassName = getXIdlMethod().getDeclaringClass().getName();
    if (sClassName.equals(""))
    {
      sClassName = super.getClassName();
    }
    return sClassName;
  }

  @Override
  public String getAnchor()
  {
    return ((UnoMethodNode)getUnoNode()).getAnchor();
  }

  /**
   * Invokes the method for the given arguments.
   * @param _oUnoObject
   * @param _oParameters
   * @return
   * @throws com.sun.star.uno.Exception
   */
  public Object invoke(Object _oUnoObject, Object[] _oParameters) 
    throws com.sun.star.uno.Exception
  {
    return ((UnoMethodNode)getUnoNode()).invoke(_oUnoObject, _oParameters);
  }

  public Object[] getLastParameterObjects()
  {
    return ((UnoMethodNode)getUnoNode()).getLastParameterObjects();
  }

  public Object getLastUnoReturnObject()
  {
    return ((UnoMethodNode)getUnoNode()).getLastUnoReturnObject();
  }

  public TypeClass getTypeClass()
  {
    return ((UnoMethodNode)getUnoNode()).getTypeClass();
  }

  public XIdlMethod getXIdlMethod()
  {
    return ((UnoMethodNode)getUnoNode()).getXIdlMethod();
  }

  public boolean hasParameters()
  {
    return ((UnoMethodNode)getUnoNode()).hasParameters();
  }

  public Object invoke() throws com.sun.star.uno.Exception
  {
    return ((UnoMethodNode)getUnoNode()).invoke();
  }

  public boolean isInvoked()
  {
    return((UnoMethodNode)getUnoNode()).isInvoked();
  }

  public boolean isInvokable()
  {
    return ((UnoMethodNode)getUnoNode()).isInvokable();
  }

  public boolean isPrimitive()
  {
    return ((UnoMethodNode)getUnoNode()).isPrimitive();
  }

  public void actionPerformed(ActionEvent e)
  {
    //openIdlDescription(m_xDialogProvider.getIDLPath());
  }

  public String getParameterDescription()
  {
    return ((UnoMethodNode)getUnoNode()).getParameterDescription();
  }

  public String getStandardMethodDescription()
  {
    return ((UnoMethodNode)getUnoNode()).getStandardMethodDescription();
  }
  
  @Override
  public void reintrospectChildren()
  {
    XIdlClass ret = ((UnoMethodNode)getUnoNode()).getXIdlMethod().getReturnType();
    add(new HideableMutableTreeNode("Returns " + ret.getName()));
  }
}
