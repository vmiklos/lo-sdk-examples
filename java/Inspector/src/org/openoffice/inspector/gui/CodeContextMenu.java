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

package org.openoffice.inspector.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 *
 * @author Christian Lins (cli@openoffice.org)
 */
class CodeContextMenu 
  extends JPopupMenu
  implements ActionListener
{
  
  private CodePane  codePane        = null;
  private JMenuItem itemSaveToFile  = new JMenuItem("Save to file...");
  
  public CodeContextMenu(CodePane codePane)
  {
    this.codePane = codePane;
    add(itemSaveToFile);
    itemSaveToFile.addActionListener(this);
  }
  
  public void actionPerformed(ActionEvent event)
  {
    if(event.getSource().equals(this.itemSaveToFile))
    {
      MenuBar mb = (MenuBar)InspectorFrame.getInstance().getJMenuBar();
      CodeFileChooser cfc = new CodeFileChooser(mb.getSelectedLanguage());
      if(cfc.showSaveDialog(codePane) == CodeFileChooser.APPROVE_OPTION)
      {
        try
        {
          FileOutputStream out = new FileOutputStream(cfc.getSelectedFile());
          out.write(codePane.getCode().getBytes(Charset.forName("UTF-8")));
          out.flush();
          out.close();
        }
        catch(IOException ex)
        {
          String[] msg = 
            {
              "An exception occurred while saving:",
              ex.getLocalizedMessage()
            };
          JOptionPane.showMessageDialog(null, msg);
        }
      }
    }
  }

}
