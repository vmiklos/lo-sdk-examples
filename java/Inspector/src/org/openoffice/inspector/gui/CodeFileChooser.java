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

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.openoffice.inspector.codegen.Language;

/**
 *
 * @author Christian Lins (cli@openoffice.org)
 */
public class CodeFileChooser extends JFileChooser
{
  
  static abstract class CodeFileFilter extends FileFilter
  {
    public boolean accept(File file)
    {
      return file.isDirectory() || file.getName().endsWith(getExtension());
    }
        
    public abstract String getExtension();
  }
  
  static class JavaFileFilter extends CodeFileFilter
  {   
    public String getDescription()
    {
      return "Java source (*.java)";
    }
    
    public String getExtension()
    {
      return ".java";
    }
  }
  
  static class CPlusPlusFileFilter extends CodeFileFilter
  {
    @Override
    public boolean accept(File file)
    {
      return file.getName().endsWith(".cpp") || super.accept(file);
    }
    
    public String getDescription()
    {
      return "C++ source (*.cpp|*.cxx)";
    }
    
    public String getExtension()
    {
      return ".cxx";
    }
  }
  
  static class PythonFileFilter extends CodeFileFilter
  {
    public String getDescription()
    {
      return "Python script (*.py)";
    }
    
    public String getExtension()
    {
      return ".py";
    }
  }
  
  static class BasicFileFilter extends CodeFileFilter
  {
    public String getDescription()
    {
      return "Basic script (*.bas)";
    }
    
    public String getExtension()
    {
      return ".bas";
    }
  }
  
  public CodeFileChooser(Language lang)
  {
    CodeFileFilter cff;
    
    switch(lang)
    {
      case Java:
      {
        cff = new JavaFileFilter();
        addChoosableFileFilter(cff);
        setFileFilter(cff);
        setSelectedFile(new File("SampleProgram.java"));
        break;
      }
      case CPlusPlus:
      {
        cff = new CPlusPlusFileFilter();
        addChoosableFileFilter(cff);
        setFileFilter(cff);
        setSelectedFile(new File("sample.cxx"));
        break;
      }
      case Python:
      {
        cff = new PythonFileFilter();
        addChoosableFileFilter(cff);
        setFileFilter(cff);
        setSelectedFile(new File("sample.py"));
        break;
      }
      case StarBasic:
      {
        cff = new BasicFileFilter();
        addChoosableFileFilter(cff);
        setFileFilter(cff);
        setSelectedFile(new File("sample.bas"));
        break;
      }
    }
  }
  
  @Override
  public File getSelectedFile()
  {
    if(getFileFilter() != null)
    {
      File   file = super.getSelectedFile();
      String ext  = ((CodeFileFilter)getFileFilter()).getExtension();

      if(file != null && !file.getName().endsWith(ext))
      {
        file = new File(file.getAbsolutePath() + ext);
      }

      return file;
    }
    else
      return super.getSelectedFile();
  }

}
