/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openoffice.inspector.gui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author chris
 */
class FilterPane extends JPanel
{
  
  private JTextField txtFilter = new JTextField();
  
  public FilterPane()
  {
    setLayout(new BorderLayout());
    add(new JLabel("Filter: "), BorderLayout.WEST);
    add(this.txtFilter, BorderLayout.CENTER);
  }
}
