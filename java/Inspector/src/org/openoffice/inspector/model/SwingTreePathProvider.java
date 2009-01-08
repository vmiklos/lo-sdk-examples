package org.openoffice.inspector.model;

import javax.swing.tree.TreePath;
import org.openoffice.inspector.model.SwingUnoNode;

public class SwingTreePathProvider
{

  TreePath m_aTreePath;

  /** Creates a new instance of TreePathProvider */
  public SwingTreePathProvider(TreePath _aTreePath)
  {
    m_aTreePath = _aTreePath;
  }

  public SwingUnoNode getLastPathComponent()
  {
    return (SwingUnoNode) m_aTreePath.getLastPathComponent();
  }

  public SwingUnoNode getPathComponent(int i)
  {
    return (SwingUnoNode) m_aTreePath.getPathComponent(i);
  }

  public int getPathCount()
  {
    return m_aTreePath.getPathCount();
  }

  public SwingTreePathProvider getParentPath()
  {
    return new SwingTreePathProvider(m_aTreePath.getParentPath());
  }

  public SwingTreePathProvider pathByAddingChild(SwingUnoNode _oUnoNode)
  {
    TreePath aTreePath = m_aTreePath.pathByAddingChild(_oUnoNode);
    return new SwingTreePathProvider(aTreePath);
  }

  public TreePath getSwingTreePath()
  {
    return m_aTreePath;
  }
}
