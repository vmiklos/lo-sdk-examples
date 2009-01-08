/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openoffice.inspector.model;

import com.sun.star.reflection.XIdlMethod;
import com.sun.star.uno.Type;
import java.util.ArrayList;
import java.util.List;
import org.openoffice.inspector.Introspector;

/**
 *
 * @author chris
 */
public class UnoInterfaceNode extends UnoNode
{
  
  private Type type;
  
  public UnoInterfaceNode(Type type, Object unoObject)
  {
    super(unoObject);
    
    this.type = type;
  }
  
  @Override
  public String getNodeDescription()
  {
    return type.getTypeName();
  }
  
  @Override
  public List<UnoMethodNode> getMethods()
  {
    List<UnoMethodNode> methods = new ArrayList<UnoMethodNode>();
    XIdlMethod[] xidlmethods = Introspector.getIntrospector().getMethodsOfInterface(type);
    
    for(XIdlMethod method : xidlmethods)
      methods.add(new UnoMethodNode(method, getUnoObject()));
    
    return methods.size() > 0 ? methods : null;
  }
}
