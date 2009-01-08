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

import com.sun.star.uno.XComponentContext;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.openoffice.inspector.Introspector;

/**
 * Model for the UNO Tree.
 * @author Christian Lins (cli@openoffice.org)
 */
public class UnoTreeModel 
  extends DefaultTreeModel
{
  
  private Introspector      introspector      = null;
  private XComponentContext xComponentContext = null;
  
  public UnoTreeModel(HideableMutableTreeNode root, XComponentContext xComponentContext)
  {
    super(root);
    
    this.introspector      = Introspector.getIntrospector(xComponentContext);
    this.xComponentContext = xComponentContext;
  }

  public XComponentContext getXComponentContext()
  {
    return this.xComponentContext;
  }
  
  public Object[] getPathToRoot(Object node)
  {
    return getPathToRoot(node, 0);
  }

  private Object[] getPathToRoot(Object node, int i)
  {
    Object anode[];
    if (node == null)
    {
      if (i == 0)
      {
        return null;
      }
      anode = new Object[i];
    }
    else
    {
      i++;
      if (node == getRoot())
      {
        anode = new Object[i];
      }
      else
      {
        anode = getPathToRoot(getParent(node), i);
      }
      anode[anode.length - i] = node;
    }
    return anode;
  }

  public void reload()
  {
    reload(getRoot());
  }

  public void reload(Object node)
  {
    if (node != null)
    {
      TreePath tp = new TreePath(getPathToRoot(node));
      fireTreeStructureChanged(this, getPathToRoot(node), new int[]{1}, null);
    }
  }

  public void valueForPathChanged(TreePath path, Object newValue)
  {
    nodeChanged(path.getLastPathComponent());
  }

  public void nodeInserted(Object node, Object child)
  {
    nodeInserted(node, child, -1);
  }

  public void nodeInserted(Object node, Object child, int index)
  {
    if (index < 0)
    {
      index = getIndexOfChild(node, child);
    }
    if (node != null && child != null && index >= 0)
    {
      int[] ai = {index};
      Object[] ac = {child};
      fireTreeNodesInserted(this, getPathToRoot(node), ai, ac);
    }
  }

  public void nodeRemoved(Object node, Object child, int index)
  {
    assert (node != null && child != null && index >= 0);

    int[] ai = {index};
    Object[] ac = {child};
    fireTreeNodesRemoved(this, getPathToRoot(node), ai, ac);
  }

  public void nodeChanged(Object node)
  {
    assert node != null;

    fireTreeNodesChanged(this, getPathToRoot(node), null, null);
  }

  public List getExpandedPaths(JTree tree)
  {
    ArrayList expandedPaths = new ArrayList();
    addExpandedPaths(tree, tree.getPathForRow(0), expandedPaths);
    return expandedPaths;
  }

  private void addExpandedPaths(JTree tree, TreePath path, ArrayList pathlist)
  {
    Enumeration aEnum = tree.getExpandedDescendants(path);
    while (aEnum.hasMoreElements())
    {
      TreePath tp = (TreePath) aEnum.nextElement();
      pathlist.add(tp);
      addExpandedPaths(tree, tp, pathlist);
    }
  }

  public void expandPaths(JTree tree, ArrayList pathlist)
  {
    for (int i = 0; i < pathlist.size(); i++)
    {
      tree.expandPath((TreePath) pathlist.get(i));
    }
  }

  public boolean isLeaf(Object _oNode)
  {
    if (_oNode instanceof TreeNode)
    {
      return ((TreeNode) _oNode).isLeaf();
    }
    return true;
  }

  public Object getParent(Object node)
  {
    if (node != getRoot() && (node instanceof TreeNode))
    {
      return ((TreeNode) node).getParent();
    }
    return null;
  }

  public boolean isNodeVisible(Object node)
  {
    if (node != getRoot())
    {
      if (node instanceof HideableMutableTreeNode)
      {
        return ((HideableMutableTreeNode) node).isVisible();
      }
    }
    return true;
  }

  public boolean setNodeVisible(Object node, boolean v)
  {
    // can't hide root
    if (node != getRoot())
    {
      if (node instanceof HideableMutableTreeNode)
      {
        HideableMutableTreeNode n = (HideableMutableTreeNode) node;
        if (v != n.isVisible())
        {
          TreeNode parent = n.getParent();
          if (v)
          {
            // need to get index after showing...
            n.setVisible(v);
            int index = getIndexOfChild(parent, n);
            nodeInserted(parent, n, index);
          }
          else
          {
            // need to get index before hiding...
            int index = getIndexOfChild(parent, n);
            n.setVisible(v);
            nodeRemoved(parent, n, index);
          }
        }
        return true;
      }
    }
    return false;
  }

  public boolean isPathToNodeVisible(Object node)
  {
    Object[] path = getPathToRoot(node);
    for (int i = 0; i < path.length; i++)
    {
      if (!isNodeVisible(path[i]))
      {
        return false;
      }
    }
    return true;
  }

  public void ensurePathToNodeVisible(Object node)
  {
    Object[] path = getPathToRoot(node);
    for (int i = 0; i < path.length; i++)
    {
      setNodeVisible(path[i], true);
    }
  }

  public Object getChild(Object parent, int index)
  {
    if (parent instanceof TreeNode)
    {
      TreeNode p = (TreeNode) parent;
      for (int i = 0,  j = -1; i < p.getChildCount(); i++)
      {
        TreeNode pc = (TreeNode) p.getChildAt(i);
        if (isNodeVisible(pc))
        {
          j++;
        }
        if (j == index)
        {
          return pc;
        }
      }
    }
    return null;
  }

  public int getChildCount(Object parent)
  {
    int count = 0;
    if (parent instanceof TreeNode)
    {
      TreeNode p = (TreeNode) parent;
      for (int i = 0; i < p.getChildCount(); i++)
      {
        TreeNode pc = (TreeNode) p.getChildAt(i);
        if (isNodeVisible(pc))
        {
          count++;
        }
      }
    }
    return count;
  }

  public int getIndexOfChild(Object parent, Object child)
  {
    int index = -1;
    if (parent instanceof TreeNode && child instanceof TreeNode)
    {
      TreeNode p = (TreeNode) parent;
      TreeNode c = (TreeNode) child;
      if (isNodeVisible(c))
      {
        index = 0;
        for (int i = 0; i < p.getChildCount(); i++)
        {
          TreeNode pc = (TreeNode) p.getChildAt(i);
          if (pc.equals(c))
          {
            return index;
          }
          if (isNodeVisible(pc))
          {
            index++;
          }
        }
      }
    }
    return index;
  }
  
  public void setFilter(String filter)
  {
    ((HideableMutableTreeNode)getRoot()).setFilter(filter);
  }
  
}
