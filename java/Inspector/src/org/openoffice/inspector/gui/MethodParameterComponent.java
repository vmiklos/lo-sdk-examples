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
import com.sun.star.uno.TypeClass;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Christian Lins (cli@openoffice.org)
 */
public class MethodParameterComponent extends JPanel
{
  static final String[] TYPES =
  {
    "BOOLEAN",
    "BYTE",
    "CHAR",
    "DOUBLE",
    "ENUM",
    "FLOAT",
    "HYPER",
    "LONG",
    "SHORT",
    "STRING",
    "UNSIGNED HYPER",
    "UNSIGNED LONG",
    "UNSIGNED SHORT"
  };
  
  private JComboBox cmbType  = new JComboBox(TYPES);
  private JComboBox cmbValue = new JComboBox();
  
  public MethodParameterComponent(ParamInfo paramInfo)
  {
    setLayout(new GridLayout(1, 3, 5, 5));
    add(new JLabel(paramInfo.aName));
    add(this.cmbType);
    add(this.cmbValue);
    this.cmbValue.setEditable(true);
    
    switch (paramInfo.aType.getTypeClass().getValue())
    {
      case TypeClass.BOOLEAN_value:
      {
        this.cmbType.setSelectedItem("BOOLEAN");
        this.cmbType.setEnabled(false);
        
        this.cmbValue.addItem(Boolean.TRUE);
        this.cmbValue.addItem(Boolean.FALSE);
        break;
      }
      case TypeClass.BYTE_value:
      {
        this.cmbType.setSelectedItem("BYTE");
        this.cmbType.setEnabled(false);
        break;
      }
      case TypeClass.CHAR_value:
      {
        this.cmbType.setSelectedItem("CHAR");
        this.cmbType.setEnabled(false);
        break;
      }
      case TypeClass.DOUBLE_value:
      {
        this.cmbType.setSelectedItem("DOUBLE");
        this.cmbType.setEnabled(false);
        break;
      }
      case TypeClass.ENUM_value:
      {
        this.cmbType.setSelectedItem("ENUM");
        this.cmbType.setEnabled(false);
        break;
      }
      case TypeClass.FLOAT_value:
      {
        this.cmbType.setSelectedItem("FLOAT");
        this.cmbType.setEnabled(false);
        break;
      }
      case TypeClass.HYPER_value:
      {
        this.cmbType.setSelectedItem("HYPER");
        this.cmbType.setEnabled(false);
        break;
      }
      case TypeClass.LONG_value:
      {
        this.cmbType.setSelectedItem("LONG");
        this.cmbType.setEnabled(false);
        break;
      }
      case TypeClass.SHORT_value:
      {
        this.cmbType.setSelectedItem("SHORT");
        this.cmbType.setEnabled(false);
        break;
      }
      case TypeClass.STRING_value:
      {
        this.cmbType.setSelectedItem("STRING");
        this.cmbType.setEnabled(false);
        break;
      }
      case TypeClass.UNSIGNED_HYPER_value:
      {
        this.cmbType.setSelectedItem("UNSIGNED HYPER");
        this.cmbType.setEnabled(false);
        break;
      }
      case TypeClass.UNSIGNED_LONG_value:
      {
        this.cmbType.setSelectedItem("UNSIGNED LONG");
        this.cmbType.setEnabled(false);
        break;
      }
      case TypeClass.UNSIGNED_SHORT_value:
      {
        this.cmbType.setSelectedItem("UNSIGNED SHORT");
        this.cmbType.setEnabled(false);
        break;
      }
      default:
      {
        this.cmbType.addItem(paramInfo.aType.getName());
        this.cmbType.setSelectedItem(paramInfo.aType.getName());
        System.out.println("Type " + paramInfo.aType.getTypeClass().getValue() + " not yet defined in 'MethodParameterComponent'");
      }
    }
  }
  
  public Object getParamValue()
  {
    return this.cmbValue.getSelectedItem();
  }
}
