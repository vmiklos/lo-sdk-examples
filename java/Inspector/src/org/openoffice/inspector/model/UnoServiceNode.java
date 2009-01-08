/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openoffice.inspector.model;

/**
 *
 * @author chris
 */
public class UnoServiceNode extends UnoNode
{
  
  private String serviceName;
  
  public UnoServiceNode(String serviceName, Object unoObject)
  {
    super(unoObject);
    
    this.serviceName = serviceName;
  }
  
  @Override
  public String getNodeDescription()
  {
    return this.serviceName;
  }
}
