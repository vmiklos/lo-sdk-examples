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

package org.openoffice.inspector.model;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * A TreeNode that can be set invisible (e.g. when using filters).
 * @author Christian Lins (cli@openoffice.org)
 */
public class HideableMutableTreeNode extends DefaultMutableTreeNode
{

  protected String  filter    = "";
  protected boolean isVisible = true;

  /**
   * Creates a tree node that has no parent and no children, but which 
   * allows children.
   */
  public HideableMutableTreeNode()
  {
  }

  /**
   * Creates a tree node with no parent, no children, but which allows 
   * children, and initializes it with the specified user object.
   * 
   * @param  userObject - an Object provided by the user that 
   *                      constitutes the node's data
   */
  public HideableMutableTreeNode(Object userObject)
  {
    super(userObject);
  }

  /**
   * Creates a tree node with no parent, no children, initialized with the 
   * specified user object, and that allows children only if specified.
   * 
   * @param  userObject     - an Object provided by the user that describes the node's data
   * @param  ballowsChildren - if true, the node is allowed to have childnodes -- otherwise, it is always a leaf node
   */
  public HideableMutableTreeNode(Object userObject, boolean allowsChildren)
  {
    super(userObject, allowsChildren);
  }

  /**
   * Subclasses should override this method to support
   * customized filtering.
   * @return true if the node is visible, else false
   */
  public boolean isVisible()
  {
    return this.isVisible;
  }

  public void setFilter(String filter)
  {
    this.filter = filter;
    if(isVisible())
    {
      // Give the filter down to all appropriate children
      for(int n = 0; n < getChildCount(); n++)
      {
        TreeNode node = getChildAt(n);
        if(node instanceof HideableMutableTreeNode)
        {
          HideableMutableTreeNode hnode = (HideableMutableTreeNode)node;
          hnode.setFilter(filter);
        }
      }
    }
  }
  
  /**
   * Sets if the node is visible. 
   * 
   * @param  returns true if the node is visible, else false
   */
  public void setVisible(boolean isVisible)
  {
    this.isVisible = isVisible;
  }

}
