/* Class name: StaxWorker
 * File name:  StaxWorker.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    25-May-2011 20:43:17
 * Modified:   30-May-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  30-May-2011 Changed output to be CSV compatible
 * 0.001  25-May-2011 Initial build
 */
package odin.odin.gui.workers;

import java.io.*;
import java.util.logging.*;
import javax.swing.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.*;
import odin.odin.object.logging.LoggerFactory;

/**
 * Used to check supplied XML formatted files for structure.
 * @version 0.002
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class StaxWorker extends SwingWorker
{
  private Logger log;
  private File f;
  
  /**
   * Initialises the class and defines the file to be checked
   * @param f The XML file to be checked for its structure.
   */
  public StaxWorker(File f)
  {
    log = LoggerFactory.getLogger(getClass().getName());
    log.log(Level.FINE, "Scanning file: {0}", f.getAbsolutePath());
    this.f = f;
  }
  
  /**
   * Uses a separate worker thread to check the structure of the supplied file.
   * This thread specifically checks XML-type documents for name-spaces.
   * <p>All nodes contained within the document must be contained within a name-space
   * for it to be considered well-formed and so return a true value. Otherwise
   * false will be returned.
   * @return Whether the document is considered well-formed by having all nodes within a name-space.
   */
  public Boolean doInBackground()
  {
    int nodeCount = 0;
    int nsCount = 0;
    int noNsCount = 0;
    boolean hasPassed = false;
    
    try
    {
      XMLInputFactory xiFactory = XMLInputFactory.newInstance();
      XMLEventReader xsReader = null;
      FileInputStream fis = new FileInputStream(f);
      xsReader = xiFactory.createXMLEventReader(fis);
      
      while(xsReader.hasNext())
      {
        XMLEvent e = xsReader.nextEvent();
        if (e.isStartElement())
        {
          StartElement se = e.asStartElement();
          nodeCount++;
          if ((se.getName().getNamespaceURI() == null) || (se.getName().getNamespaceURI().equals("")))
          {
            noNsCount++;
          }
          else
          {
            nsCount++;
          }
        }
      }
      if (nodeCount == nsCount) { hasPassed = true; }
      log.log(Level.FINEST,"Scanned {0} node(s)",nodeCount);
      log.log(Level.FINEST,"Found {0} node(s) with",nsCount);
      log.log(Level.FINEST,"Found {0} node(s) without namespaces",noNsCount);
      log.log(Level.FINEST,"Document passed? {0}",hasPassed);
    }
    catch (Exception x)
    {
      log.log(Level.INFO,"Error detected in file {0}. Error details: {1}", new Object[] {f.getAbsolutePath(), x.getMessage()});
    }
    finally
    {
      return hasPassed;
    }
  }
}