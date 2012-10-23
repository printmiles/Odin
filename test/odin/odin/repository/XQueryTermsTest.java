package odin.odin.repository;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class XQueryTermsTest
{  
  private XQueryTerms instance;
  private final String path = "C:/Odin/Repository";
  
  /*
   * 
   *   private static final String namespace = "http://sites.google.com/site/printmiles/Odin";
  private static final String declaration = "declare default element namespace '" + namespace + "'; ";
  private static final String collection = "for $d in collection('file:///C:/Odin/Repository";
  private static final String selection = "/?select=*.xml')";
   * 
   */
  
  public XQueryTermsTest()
  {
    instance = new XQueryTerms(path);
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  /**
   * Test of getMimeType method, of class XQueryTerms.
   */
  @Test
  public void testGetMimeType() {
    System.out.println("getMimeType");
    String guid = "a";
    String expResult = "declare default element namespace 'http://sites.google.com/site/printmiles/Odin'; " +
                       "for $d in collection('file:///C:/Odin/Repository/?select=*.xml')/format " + 
                       "where $d/guid='" + guid + "' return $d/mime";
    String result = instance.getMimeType(guid);
    assertEquals(expResult, result);
  }

  /**
   * Test of getFileExt method, of class XQueryTerms.
   */
  @Test
  public void testGetFileExt() {
    System.out.println("getFileExt");
    String guid = "a";
    String expResult = "declare default element namespace 'http://sites.google.com/site/printmiles/Odin'; " +
                       "for $d in collection('file:///C:/Odin/Repository/?select=*.xml')/format " + 
                       "where $d/guid='" + guid + "' return $d/ext";
    String result = instance.getFileExt(guid);
    assertEquals(expResult, result);
  }

  /**
   * Test of getFormatGuid method, of class XQueryTerms.
   */
  @Test
  public void testGetFormatGuid() {
    System.out.println("getFormatGuid");
    String mimeType = "application/pdf";
    String fileExt = "pdf";
    String expResult = "declare default element namespace 'http://sites.google.com/site/printmiles/Odin'; " +
                       "for $d in collection('file:///C:/Odin/Repository/?select=*.xml')/format " + 
                       "where $d/mime='" + mimeType + "' and $d/ext='" + fileExt + "' return $d/guid";
    String result = instance.getFormatGuid(mimeType, fileExt);
    assertEquals(expResult, result);
  }
}