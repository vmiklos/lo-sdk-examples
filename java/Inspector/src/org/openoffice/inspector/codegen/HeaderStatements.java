/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openoffice.inspector.codegen;

import java.util.HashSet;

/**
 * TODO: Check whether ordering is necessary or not as HashSet
 * cannot guarantee the ordering of the elements.
 * @author Christian Lins (cli@openoffice.org)
 */
class HeaderStatements extends HashSet<Object>
{

  /*public boolean contains(Object _oElement)
  {
    String sCompName = (String) _oElement;
    for (int i = 0; i < this.size(); i++)
    {
      String sElement = (String) this.get(i);
      if (sElement.equals(sCompName))
      {
        return true;
      }
    }
    return false;
  }

  public boolean add(Object _oElement)
  {
    if (_oElement instanceof String)
    {
      if (!contains(_oElement))
      {
        super.add(_oElement);
        return true;
      }
    }
    return false;
  }*/
}
