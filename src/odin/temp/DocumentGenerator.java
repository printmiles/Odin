/* Class name: DocumentGenerator
 * File name:  DocumentGenerator.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    24-Aug-2011 18:54:50
 * Modified:   24-Aug-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  24-Aug-2011 Initial build
 */
package odin.temp;

import java.io.File;
import java.math.BigInteger;
import java.sql.*;
import java.util.GregorianCalendar;
import java.util.UUID;
import javax.xml.bind.*;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamSource;
import odin.odin.repository.MimeTypeDatabase;
import odin.odin.xml.AddedDetails;
import odin.odin.xml.Format;
import odin.odin.xml.Item;
import odin.odin.xml.Root;

/**
 * JavaDoc to follow.
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class DocumentGenerator
{
  private static final long NUMBER_OF_DOCUMENTS = 10000;
  private static final long NUMBER_OF_RUNS = 100;
  private static final int RUN_ON_DISC = 1;
  private static final int RUN_ON_DATABASE = 2;
  private Root rootDoc;
  private MimeTypeDatabase mr;
  
  public static void main(String[] args)
  {
    DocumentGenerator dg = new DocumentGenerator();
    dg.runGenerator(RUN_ON_DATABASE);
  }
  
  private void runGenerator(int destination)
  {
    switch (destination)
    {
      case RUN_ON_DISC:
      {
        DocumentGenerator dg = new DocumentGenerator();
        dg.mr = new MimeTypeDatabase();
        System.out.println("Initialising Mime database.");
        MimeTypeDatabase.initialiseDatabase();
        System.out.println("Database initialised.");
        for (int i = 0; i < NUMBER_OF_DOCUMENTS; i++)
        {
          System.out.print("n=" + i + ", ");
          dg.createRandomDiscItem();
        }
        break;
      }
      case RUN_ON_DATABASE:
      {
        DocumentGenerator dg = new DocumentGenerator();
        for (int i = 0; i < NUMBER_OF_RUNS; i++)
        {
          System.out.println("Runs = " + i);
          dg.createRandomDatabaseItems();
        }
        break;
      }
      default:
      {
        System.err.println("Invalid destination type.");
      }
    }
    
  }
  
  private void createRandomDiscItem()
  {
    // Create factories for JAXB data types
    odin.odin.xml.ObjectFactory odinFactory = new odin.odin.xml.ObjectFactory();
    try
    {
      if (rootDoc == null) { getRootDocument(); }
      String repositoryDir = rootDoc.getLocations().getRepository();
      String guid = UUID.randomUUID().toString();
      // Check to see if the guid already exists
      File fMeta = new File(repositoryDir+guid+".xml");
      while (fMeta.exists())
      {
        guid = UUID.randomUUID().toString();
        fMeta = new File(repositoryDir+guid+".xml");
      }
      // guid will now contain authorNode unique id within the repository that we can use to store both the file and meta-data
/**************************************************************************************************/      
      // Create all of the parts that need to be populated
      Item i = odinFactory.createItem();
      Item.Origin iO = odinFactory.createItemOrigin();
      Item.Origin.Author iOA = odinFactory.createItemOriginAuthor();
      Item.Origin.Locale iOL = odinFactory.createItemOriginLocale();
      AddedDetails iAD = odinFactory.createAddedDetails();
      Item.Attributes iA = odinFactory.createItemAttributes();
      Item.Keywords iK = odinFactory.createItemKeywords();
      Item.Location iL = odinFactory.createItemLocation();
      Item.Location.Repository iLR = odinFactory.createItemLocationRepository();
      Item.Location.Archive iLA = odinFactory.createItemLocationArchive();

      // Start populating the sections
      // Populate the author section
      iOA.setFirstName("Alexander");
      iOA.setSurname("Harris");
      iOA.setPrefix("Mr");
      iOA.setCompany("King's College London");
      iOA.setEmail("alexander.harris@kcl.ac.uk");
      iOA.setWebsite("http://nms.kcl.ac.uk");
      // Populate the locale section
      iOL.setLanguage("en");
      iOL.setCountry("GB");
      // Populate the AddedDetails section
      try
      {
        // And then try to put the right ones in
        GregorianCalendar gCal = new GregorianCalendar();
        DatatypeFactory dataFactory;
        dataFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar xgc = dataFactory.newXMLGregorianCalendar(gCal);
        iAD.setAt(xgc);
        iAD.setBy("Alex");
        iAD.setFrom("192.168.1.64/Gremlin");
      }
      catch (DatatypeConfigurationException dcX)
      {
        dcX.printStackTrace();
      }
      // Populate the Attributes
      Item.Attributes.Attribute a = odinFactory.createItemAttributesAttribute();
      a.setKey("a");
      a.setValue("1");
      iA.getAttribute().add(a);
      // Set the keywords
      iK.getKeyword().add("test");
      iK.getKeyword().add("performance");
      iO.setAdded(iAD);
      iO.setAuthor(iOA);
      iO.setLocale(iOL);
      i.setGuid(guid);
      i.setTitle(guid);
      Format f = MimeTypeDatabase.getFormatByFileExtension("pdf");
      i.setFormat(f.getGuid());
      i.setOrigin(iO);
      i.setTrust(new BigInteger("500"));
      i.setAttributes(iA);
      i.setKeywords(iK);
/**************************************************************************************************/
      // Fix the title if there's a file extension on the end
      String title = i.getTitle();
      int pos = title.lastIndexOf('.');
      if (pos != -1) // There isn't a file extension in the title
      {
        if ((pos - title.length()) <=4)
        {
          title = title.substring(0, pos);
          i.setTitle(title);
        }
      }
      
      // As it's the first time we've uploaded the document we can set these two as the same
      iLR.getAdded().add(i.getOrigin().getAdded());

      // This is going to be the name where we output the received file to
      // TODO Allow the admin to choose what extension is to be used
      File fURL = new File(repositoryDir+guid+".odin");

      // Set the last items in the Item document
      iL.setPath(fURL.toURI().toString());
      iL.setRepository(iLR);
      i.setLocation(iL);

      // Marshal the document to the XML repository
      JAXBContext jaxbCtxt = JAXBContext.newInstance("odin.odin.xml");
      Marshaller m = jaxbCtxt.createMarshaller();
      m.setProperty("jaxb.formatted.output", Boolean.TRUE);
      m.marshal(i, fMeta);
      
      System.out.println("Document created: " + guid);
    }
    catch (Exception x)
    {
      x.printStackTrace();
    }
  }
  
  private void getRootDocument()
  {
    if (rootDoc == null)
    {
      try
      {
        JAXBContext jaxbCtxt = JAXBContext.newInstance("odin.odin.xml");
        Unmarshaller jaxbU = jaxbCtxt.createUnmarshaller();
        jaxbU.setEventHandler(new DefaultValidationEventHandler());
        File fRoot = new File("home.root.xml");
        JAXBElement<Root> rElement = jaxbU.unmarshal(new StreamSource(fRoot),Root.class);
        rootDoc = rElement.getValue();
      }
      catch (JAXBException jaxbX)
      {
        jaxbX.printStackTrace();
      }
    }
  }
  
  private void createRandomDatabaseItems()
  {
    /* The class-name of the MySQL database driver */
    String mySqlDriver = "com.mysql.jdbc.Driver";
    /* The MySQL connection string */
    String mySqlDbName = "jdbc:mysql://localhost/odin";
    /* The connection to the back-end database */
    Connection con;
    try
    {
      Class.forName(mySqlDriver).newInstance();
      con = DriverManager.getConnection(mySqlDbName,"root","Crysta1C0ve");
      
      PreparedStatement pst = con.prepareStatement("SELECT format_id FROM format WHERE fileExt='pdf' AND mimeType='application/pdf'");
      ResultSet rs = pst.executeQuery();
      String formatId = "";
      while (rs.next())
      {
        formatId = rs.getString(1);
      }
      
      String localeId = new String();
      boolean hasEntries = true;
      String language = "en";
      String country = "GB";
      String insertSql = "INSERT INTO `locale` (`locale_id`,`language`,`country`) VALUES (?,?,?)";
      int counter = 0;
      while (hasEntries)
      {
        counter = 0;
        String selectSql = "SELECT locale_id FROM locale WHERE language=? AND country=?";
        PreparedStatement pstSelect = con.prepareStatement(selectSql);
        pstSelect.setString(1, language);
        pstSelect.setString(2, country);
        rs = pstSelect.executeQuery();
        while (rs.next())
        {
          localeId = rs.getString(1);
          counter++;
        }
        hasEntries = false;
      }
      if (counter == 0)
      {
        PreparedStatement pstUpdate = con.prepareStatement(insertSql);
        localeId = UUID.randomUUID().toString();
        pstUpdate.setString(1, localeId);
        pstUpdate.setString(2, language);
        pstUpdate.setString(3, country);
        pstUpdate.executeUpdate();
      }
      
      String authorId = "";
      hasEntries = true;
      String firstName = "Alexander";
      String surname ="Harris";
      String prefix = "Mr";
      String company = "King's College London";
      String email = "alexander.harris@kcl.ac.uk";
      String website = "http://nms.kcl.ac.uk";
      insertSql = "INSERT INTO `author` (`author_id`,`name`,`surname`,`prefix`,`company`,`email`,`website`) VALUES (?,?,?,?,?,?,?)";
      while (hasEntries)
      {
        counter = 0;
        String selectSql = "SELECT author_id FROM author WHERE prefix=? AND name=? AND surname=? AND company=? AND website=? AND email=?";
        PreparedStatement pstSelect = con.prepareStatement(selectSql);
        pstSelect.setString(1,prefix);
        pstSelect.setString(2,firstName);
        pstSelect.setString(3,surname);
        pstSelect.setString(4,company);
        pstSelect.setString(5,website);
        pstSelect.setString(6,email);
        rs = pstSelect.executeQuery();
        while (rs.next())
        {
          authorId = rs.getString(1);
          counter++;
        }
        hasEntries = false;
      }
      if (counter == 0)
      {
        PreparedStatement pstUpdate = con.prepareStatement(insertSql);
        authorId = UUID.randomUUID().toString();
        pstUpdate.setString(1,authorId);
        pstUpdate.setString(2,firstName);
        pstUpdate.setString(3,surname);
        pstUpdate.setString(4,prefix);
        pstUpdate.setString(5,company);
        pstUpdate.setString(6,email);
        pstUpdate.setString(7,website);
        pstUpdate.executeUpdate();
      }
      System.gc();
      
      for (long i = 0; i < NUMBER_OF_DOCUMENTS; i++)
      {
        Class.forName(mySqlDriver).newInstance();
        String documentId = UUID.randomUUID().toString();
        
        String accessId = UUID.randomUUID().toString();
        PreparedStatement pstAccess = con.prepareStatement("INSERT INTO `access` (`access_id`,`by`,`from`) VALUES (?,?,?)");
        pstAccess.setString(1,accessId);
        pstAccess.setString(2,"Alex");
        pstAccess.setString(3,"192.168.1.64/Gremlin");
        pstAccess.executeUpdate();
        
        String checkForCollisions = "INSERT INTO `item` ("
                + "`item_id`,`title`,`format`,`author`,`locale`,`added`,`trust`,`location`,`repository`)"
                + " VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstAuthor = con.prepareStatement(checkForCollisions);
        pstAuthor.setString(1,documentId);
        pstAuthor.setString(2,documentId);
        pstAuthor.setString(3,formatId);
        pstAuthor.setString(4,authorId);
        pstAuthor.setString(5,localeId);
        pstAuthor.setString(6,accessId);
        pstAuthor.setInt(7, 500);
        pstAuthor.setString(8,"C:\\Odin\\Repository\\Something.txt");
        pstAuthor.setString(9,accessId);
        pstAuthor.executeUpdate();
        
        PreparedStatement pstKeywords = con.prepareStatement("INSERT INTO `keywords` (`documentId`,`keyword`) VALUES (?,?)");
        pstKeywords.setString(1, documentId);
        pstKeywords.setString(2, "test");
        pstKeywords.executeUpdate();
        pstKeywords = con.prepareStatement("INSERT INTO `keywords` (`documentId`,`keyword`) VALUES (?,?)");
        pstKeywords.setString(1, documentId);
        pstKeywords.setString(2, "performance");
        pstKeywords.executeUpdate();
        
        PreparedStatement pstAttributes = con.prepareStatement("INSERT INTO `attributes` (`documentId`,`key`,`value`) VALUES (?,?,?)");
        pstAttributes.setString(1,documentId);
        pstAttributes.setString(2,"a");
        pstAttributes.setString(3,"1");
        pstAttributes.executeUpdate();
        pstAttributes = con.prepareStatement("INSERT INTO `attributes` (`documentId`,`key`,`value`) VALUES (?,?,?)");
        pstAttributes.setString(1,documentId);
        pstAttributes.setString(2,"b");
        pstAttributes.setString(3,"2");
        pstAttributes.executeUpdate();
        
        documentId = null;
        accessId = null;
        pstAccess = null;
        checkForCollisions = null;
        pstAuthor = null;
        pstKeywords = null;
        pstAttributes = null;
        
        
        System.out.println("Generated document #" + i);
        if (i % 5000 == 0)
        {
          System.gc();
          System.out.println("Garbage collection done!");
        }
      }
      con.close();
    }
    catch (ClassNotFoundException cnfX)
    {
      System.err.println("DbInterface.getConnection: Unable to load database driver class (" + cnfX.getMessage() + ")");
      cnfX.printStackTrace();
    }
    catch (SQLException sqlX)
    {
      System.err.println("DbInterface.getConnection: SQL exception detected");
      sqlX.printStackTrace();
    }
    catch (Exception eX)
    {
      System.err.println("DbInterface.getConnection: General exception detected");
      eX.printStackTrace();
    }
  }
}