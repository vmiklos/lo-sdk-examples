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

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.openoffice.inspector.model.SwingUnoInterfaceNode;
import org.openoffice.inspector.model.SwingUnoMethodNode;
import org.openoffice.inspector.model.SwingUnoNode;
import org.openoffice.inspector.model.SwingUnoPropertyNode;
import org.openoffice.inspector.model.SwingUnoServiceNode;

public class UnoTreeRenderer extends DefaultTreeCellRenderer
{

  private Icon methodIcon;
  private Icon propertyIcon;
  private Icon containerIcon;
  private Icon contentIcon;
  private Icon serviceIcon;
  private Icon interfaceIcon;
  private Icon propertyValueIcon;
  
  private Map<Class<?>, Icon> iconCache = new HashMap<Class<?>, Icon>();

  /** Creates a new instance of UnoTreeRenderer */
  public UnoTreeRenderer()
  {
    try
    {
      final ClassLoader loader = getClass().getClassLoader();
      methodIcon        = new ImageIcon(loader.getResource("org/openoffice/inspector/images/methods_16.png"));
      propertyIcon      = new ImageIcon(loader.getResource("org/openoffice/inspector/images/properties_16.png"));
      propertyValueIcon = new ImageIcon(loader.getResource("org/openoffice/inspector/images/properties_16.png"));
      containerIcon     = new ImageIcon(loader.getResource("org/openoffice/inspector/images/containers_16.png"));
      serviceIcon       = new ImageIcon(loader.getResource("org/openoffice/inspector/images/services_16.png"));
      interfaceIcon     = new ImageIcon(loader.getResource("org/openoffice/inspector/images/interfaces_16.png"));
      contentIcon       = new ImageIcon(loader.getResource("org/openoffice/inspector/images/content_16.png"));
      
      // Warm up the cache
      this.iconCache.put(SwingUnoMethodNode.class, methodIcon);
      this.iconCache.put(SwingUnoInterfaceNode.class, interfaceIcon);
      this.iconCache.put(SwingUnoPropertyNode.class, propertyIcon);
      this.iconCache.put(SwingUnoServiceNode.class, serviceIcon);
    }
    catch (RuntimeException ex)
    {
      ex.printStackTrace();
      System.out.println("Sorry, could not locate resourecs, treecell icons will not be displayed.");
    }
  }

  @Override
  public synchronized Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
  {
    try
    {
      super.selected = sel;
      
      if(value instanceof SwingUnoNode)
      {
        SwingUnoNode swingUnoNode = (SwingUnoNode)value;
        Icon         icon = this.iconCache.get(value.getClass());
        if(icon == null)
          icon = this.containerIcon;
        
        setIcon(icon);
        setText(swingUnoNode.getUnoNode().getNodeDescription());
      
         if(value instanceof SwingUnoPropertyNode)
        {
          SwingUnoPropertyNode propNode = (SwingUnoPropertyNode)swingUnoNode;
          setText(propNode.getProperty().Name);
        }
      }
      else
      {
        return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return this;
  }

}


