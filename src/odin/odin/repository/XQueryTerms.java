/* Class name: XQueryTerms
 * File name:  XQueryTerms.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    24-May-2011 12:22:51
 * Modified:   24-May-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  24-May-2011 Initial build
 */
package odin.odin.repository;

import java.util.logging.Level;
import java.util.logging.Logger;
import odin.odin.object.logging.LoggerFactory;

/**
 * Used as a centralised stored for XQuery search strings within the application.
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class XQueryTerms
{
  private static final String namespace = "http://sites.google.com/site/printmiles/Odin";
  private static final String declaration = "declare default element namespace '" + namespace + "'; ";
  private static final String collection = "for $d in collection('file:///";
  private static final String selection = "/?select=*.xml')";
  private String path;
  private Logger log;
  
  /**
   * Defines the path of the Odin repository and initialises the logger.
   * @param path 
   */
  public XQueryTerms(String path)
  {
    log = LoggerFactory.getLogger(getClass().getName());
    this.path = path;
    log.log(Level.FINEST, "Setting path to {0}", path);
  }
  
  /**
   * Obtain the XQuery search string used to fetch the MIME type from a given identifier
   * @param guid The identifier of the format
   * @return The corresponding MIME type
   */
  public String getMimeType(String guid)
  {
    String query = declaration + collection + path + selection + "/format where $d/guid='" + guid + "' " + "return $d/mime";
    log.log(Level.CONFIG, "Returning XQuery expression: {0}", query);
    return query;
  }
  
  /**
   * Obtain the XQuery search string used to fetch the file extension from a given identifier
   * @param guid The identifier of the format
   * @return The corresponding file extension
   */
  public String getFileExt(String guid)
  {
    String query = declaration + collection + path + selection + "/format where $d/guid='" + guid + "' " + "return $d/ext";
    log.log(Level.CONFIG, "Returning XQuery expression: {0}", query);
    return query;
  }
  
  /**
   * Obtain the XQuery search string used to fetch the unique identifier for a given file extension and MIME type
   * @param mimeType The MIME type
   * @param fileExt The file extension
   * @return The corresponding unique identifier
   */
  public String getFormatGuid(String mimeType, String fileExt)
  {
    String query = declaration + collection + path + selection + "/format where $d/mime='" + mimeType + "' and $d/ext='" + fileExt + "' return $d/guid";
    log.log(Level.CONFIG, "Returning XQuery expression: {0}", query);
    return query;
  }
  
}
