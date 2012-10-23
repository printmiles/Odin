/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package odin.temp;

import java.io.File;
import javax.swing.JFileChooser;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author Alex
 */
public class XSLTRunner
{
  public static void main(String[] args)
  {
    XSLTRunner me = new XSLTRunner();
    me.runXsltScript();
  }
  
  private void runXsltScript()
  {
    JFileChooser fc = new JFileChooser();
    fc.setDialogTitle("Select XSLT script");
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int returnVal = fc.showOpenDialog(null);
    if (returnVal == 1)
    {
      return;
    }
    File fXSLT = fc.getSelectedFile();
    
    File fXML = new File("C:/Odin/Repository/");
    boolean isDirectory = fXML.isDirectory();
    /* Code adapted from: http://blog.msbbc.co.uk/2007/06/simple-saxon-java-example.html
     * [Accessed 3rd-Sept-2010]
     */
    for (File format : fXML.listFiles())
    {
      boolean isSuccessful = false;
      try
      {
        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        TransformerFactory tFact = TransformerFactory.newInstance();
        Transformer tf = tFact.newTransformer(new StreamSource(fXSLT));
        tf.transform(new StreamSource(format), new StreamResult(format));
        isSuccessful = true;
      }
      catch (TransformerException tX)
      {
        tX.printStackTrace();
      }
      finally
      {
        System.out.println("Change of file " + format.getName() + " was successful? " + isSuccessful);
      }
    }
  }
}
