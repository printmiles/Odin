/* Class name: MySqlRepository
 * File name:  MySqlRepository.java
 * Project:    Open Document and Information Network (Odin)
 * Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    28-Mar-2012 13:34:12
 * Modified:   13-Sep-2012
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.004  13-Sep-2012 Altered methods to work with MySQL
 * 0.003  13-Aug-2012 Changed class name from DatabaseRepository to MySqlRepository
 * 0.002  14-May-2012 Amended methods to work with MySQL and added SQL statements
 * 0.001  28-Mar-2012 Initial build
 */
package odin.odin.repository;

import java.io.File;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.transform.stream.StreamSource;
import odin.odin.object.logging.LoggerFactory;
import odin.odin.xml.Root;
import odin.shared.xml.*;

/**
 * Provides the majority of interactions between Odin and a back-end MySQL database.
 * <p>The repository database in this case supports traditional RDBMS systems through JDBC.
 * Initially supported databases are:
 * <ul>
 *   <li>MySQL (5.1)</li>
 * </ul>
 * @version 0.004
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class MySqlRepository implements Repository
{
  private Logger log;
  private Root rootDoc;
  private final int EXPECTED_UUID_LENGTH = 36;
  
  private String sqlFetchLocationWithGuid = "SELECT location FROM item WHERE item_id = ?";
  private String sqlCheckIfItemGuidExists = "SELECT item_id FROM item WHERE item_id=?";
  private String sqlCheckIfAuthorGuidExists = "SELECT author_id FROM author WHERE author_id=?";
  private String sqlCheckIfLocaleGuidExists = "SELECT locale_id FROM locale WHERE locale_id=?";
  private String sqlCheckIfAccessGuidExists = "SELECT access_id FROM access WHERE access_id=?";
  private String sqlCheckIfFormatGuidExists = "SELECT format_id FROM format WHERE format_id=?";
  private String sqlAddAuthor = "INSERT INTO `author` (`author_id`,`prefix`,`name`,`surname`,`company`,`email`,`website`) VALUES (?,?,?,?,?,?,?)";
  private String sqlAddLocale = "INSERT INTO `locale` (`locale_id`,`country`,`language`) VALUES (?,?,?)";
  private String sqlAddAccess = "INSERT INTO `access` (`access_id`,`by`,`from`) VALUES (?,?,?)";
  private String sqlAddFormat = "INSERT INTO `format` (`format_id`,`fileExt`,`mimeType`,`description`) VALUES (?,?,?,?)";
  private String sqlGetAuthorId = "SELECT author_id FROM author WHERE prefix=? AND name=? AND surname=?";
  private String sqlGetFormatId = "SELECT format_id FROM format WHERE fileExt=? AND mimeType=? AND description=?";
  private String sqlGetLocaleId = "SELECT locale_id FROM locale WHERE country=? AND language=?";
  
  /**
   * Initialise the class with an appropriate logger.
   */
  public MySqlRepository()
  {
    log = LoggerFactory.getLogger(getClass().getName());
  }
  
  public Response storeDocument(Document uploadedDoc)
  {
    odin.shared.xml.ObjectFactory sharedFactory = new odin.shared.xml.ObjectFactory();
    Response r = sharedFactory.createResponse();
    try
    {
    // TODO Write code for Database repository to store a document within the database and return a response to Sleipnir (the client)
    Document.Metadata m = uploadedDoc.getMetadata();
    String accessId = createAccess(uploadedDoc.getUsername(), uploadedDoc.getIpaddress());
    String authorId = createAuthor(m.getAuthor().getPrefix(), m.getAuthor().getFirstName(), m.getAuthor().getSurname(), m.getAuthor().getCompany(), m.getAuthor().getEmail(), m.getAuthor().getWebsite());
    String formatId = createFormat(m.getFileextension(), m.getMimetype(), "");
    String localeId = createLocale(m.getLocale().getCountry(), m.getLocale().getLanguage());
    }
    catch (Exception x)
    {
      log.throwing(getClass().getName(), "upload", x);
      Response.Status s = sharedFactory.createResponseStatus();
      Response.Appendix a = sharedFactory.createResponseAppendix();
      s.setCode(new BigInteger("500"));
      s.setMessage("Internal Server Error");
      s.setDescription("An exception was thrown. " + x.toString());
      a.setGuid("");
      r.setStatus(s);
      r.setAppendix(a); 
    }
    finally
    {
      return r;
    }
  }
  
  public Results search(Search searchCriteria)
  {
    odin.shared.xml.ObjectFactory sharedFactory = new odin.shared.xml.ObjectFactory();
    // TODO Write code for Database repository to return a set of results from a specific set of search criteria
    return sharedFactory.createResults();
  }
  
  public Results basicSearch(Basic words)
  {
    odin.shared.xml.ObjectFactory sharedFactory = new odin.shared.xml.ObjectFactory();
    // TODO Write code for Database repository to return a set of results given a set of search terms
    return sharedFactory.createResults();
  }
  
  /**
   * Performs the actual work of unmarshalling database contents into the Document
   * format that is returned to clients. This method only creates the metadata
   * portion of the document and does NOT attach the actual file.
   * @param guid The document identifier to be retrieved from the database
   * @return The metadata document
   */
  private Document castDatabaseToDocument(String guid)
  {
    odin.shared.xml.ObjectFactory sharedFactory = new odin.shared.xml.ObjectFactory();
    Document doc = sharedFactory.createDocument();
    Connection con = getConnection();
    try
    {
      // Deal with the main Item record to retrieve the IDs of the constituent parts
      PreparedStatement pstItem = con.prepareStatement("SELECT title, format, author, locale FROM item WHERE item_id = ?");
      pstItem.setString(1, guid);
      Document.Metadata m = sharedFactory.createDocumentMetadata(); // This will form the document returned to the client
      ResultSet rsItem = pstItem.executeQuery();
      
      String formatId = "";
      String authorId = "";
      String localeId = ""; 
      
      while (rsItem.next())
      {
        // There should only be one record returned otherwise the last record's details are returned
        m.setTitle(rsItem.getString("title"));
        formatId = rsItem.getString("format");
        authorId = rsItem.getString("author");
        localeId = rsItem.getString("locale");
      }
      
      // Retrieve the author's details
      PreparedStatement pstAuthor = con.prepareStatement("SELECT prefix, name, surname, company, email, website FROM author WHERE author_id = ?");
      pstAuthor.setString(1, authorId);
      Document.Metadata.Author a = sharedFactory.createDocumentMetadataAuthor();
      ResultSet rsAuthor = pstAuthor.executeQuery();
      while (rsAuthor.next())
      {
        // There should only be one record returned otherwise the last record's details are returned
        a.setCompany(rsAuthor.getString("company"));
        a.setEmail(rsAuthor.getString("email"));
        a.setFirstName(rsAuthor.getString("name"));
        a.setPrefix(rsAuthor.getString("prefix"));
        a.setSurname(rsAuthor.getString("surname"));
        a.setWebsite(rsAuthor.getString("website"));
      }
      m.setAuthor(a);
      
      // Retrieve the document's file type information
      PreparedStatement pstMime = con.prepareStatement("SELECT fileExt, mimeType FROM format WHERE format_id=?");
      pstMime.setString(1, formatId);
      ResultSet rsFormat = pstMime.executeQuery();
      while (rsFormat.next())
      {
        // There should only be one record returned otherwise the last record's details are returned
        m.setFileextension(rsFormat.getString("fileExt"));
        m.setMimetype(rsFormat.getString("mimeType"));
      }
      
      // Retrieve the document's locale details
      PreparedStatement pstLocale = con.prepareStatement("SELECT language, country FROM locale WHERE locale_id=?");
      pstLocale.setString(1, localeId);
      Document.Metadata.Locale l = sharedFactory.createDocumentMetadataLocale();
      ResultSet rsLocale = pstLocale.executeQuery();
      while (rsLocale.next())
      {
        // There should only be one record returned otherwise the last record's details are returned
        l.setCountry(rsLocale.getString("country"));
        l.setLanguage(rsLocale.getString("language"));
      }
      m.setLocale(l);
      
      // Retrieve the document's keywords
      PreparedStatement pstKeywords = con.prepareStatement("SELECT keyword FROM keywords WHERE documentId=?");
      pstKeywords.setString(1, guid);
      Document.Metadata.Keywords k = sharedFactory.createDocumentMetadataKeywords();
      ResultSet rsKeywords = pstKeywords.executeQuery();
      while (rsKeywords.next())
      {
        // This will return multiple keywords in the majorty of cases
        k.getKeyword().add(rsKeywords.getString("keyword"));
      }
      m.setKeywords(k);
      
      // Retrieve the document's attributes
      PreparedStatement pstAttributes = con.prepareStatement("SELECT key, value FROM attributes WHERE documentId=?");
      pstAttributes.setString(1, guid);
      Document.Metadata.Attributes attr = sharedFactory.createDocumentMetadataAttributes();
      ResultSet rsAttributes = pstAttributes.executeQuery();
      while (rsAttributes.next())
      {
        Document.Metadata.Attributes.Attribute newAttribute = sharedFactory.createDocumentMetadataAttributesAttribute();
        newAttribute.setKey(rsAttributes.getString("key"));
        newAttribute.setValue(rsAttributes.getString("attribute"));
        attr.getAttribute().add(newAttribute);
      }
      m.setAttributes(attr);
      
      doc.setMetadata(m);
      con.close();
    }
    catch (Exception x)
    {
      log.throwing(getClass().getName(), "castDatabaseToDocument(String)", x);
    }
    finally
    {
      return doc;
    }
  }
  
  /**
   * Fetches a document and its meta-data from the repository. This method differs
   * from the <code>getDocumentMetadata(String)</code> method in that it returns
   * both the meta-data and the actual document which is attached to the response.
   * @param guid The unique identifier of the document to be retrieved.
   * @return The requested document along with its meta-data.
   * @see odin.shared.xml.Document
   * @see MySqlRepository#getDocumentMetadata(java.lang.String)
   */
  public Document getDocument(String guid)
  {
    log.log(Level.FINEST, "Searching for document with GUID {0}", guid);
    odin.shared.xml.ObjectFactory sharedFactory = new odin.shared.xml.ObjectFactory();
    Document d = sharedFactory.createDocument();
    try
    {
      Connection con = getConnection();
      d = getDocumentMetadata(guid);
      DataHandler dh = new DataHandler(new URL(getLocation(guid)));
      d.setDocument(dh);
      con.close();
    }
    catch (MalformedURLException murlX)
    {
      log.throwing(getClass().getName(), "getDocument(String)", murlX);
    }
    catch (SQLException sqlX)
    {
      log.throwing(getClass().getName(), "getDocument(String)", sqlX);
    }
    finally
    {
      return d;
    }
  }
  
  /**
   * Fetches a document meta-data from the repository. This method differs from
   * the <code>getDocument(String)</code> method in that only the meta-data is
   * returned in the response.
   * @param guid The unique identifier of the document to be retrieved.
   * @return The requested document's meta-data.
   * @see odin.shared.xml.Document
   * @see MySqlRepository#getDocument(java.lang.String) 
   */
  public Document getDocumentMetadata(String guid)
  {
    log.log(Level.FINEST, "Searching for document with GUID {0}", guid);
    odin.shared.xml.ObjectFactory sharedFactory = new odin.shared.xml.ObjectFactory();
    Document d = sharedFactory.createDocument();
    try
    {
      Connection con = getConnection();
      d = castDatabaseToDocument(guid);      
      con.close();
    }
    catch (SQLException sqlX)
    {
      log.throwing(getClass().getName(), "getDocument(String)", sqlX);
    }
    finally
    {
      return d;
    }
  }
  
  /**
   * Fetch the root configuration document and extract the contents.
   */
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
  
  /**
   * Establish a connection to a database using the details in the root document.
   * @return The connection to the database.
   * @see java.sql.Connection
   */
  private Connection getConnection()
  {
    try
    {
      getRootDocument();
      Class.forName(rootDoc.getPolicies().getRepository().getDatabase().getDriver()).newInstance();
      Connection con = DriverManager.getConnection(rootDoc.getPolicies().getRepository().getDatabase().getConnection(),
                                                   rootDoc.getPolicies().getRepository().getDatabase().getUser(),
                                                   rootDoc.getPolicies().getRepository().getDatabase().getPw());
      return con;
    }
    catch (Exception x)
    {
      log.throwing(getClass().getName(), "Connection getConnection()", x);
      return null;
    }
  }
  
  /**
   * Stores a record of activity (access) within the repository.
   * <p>The method does not require a timestamp to be included as this is added
   * automatically by the database.
   * <p>If this method receives an unexpected identifier length from the database
   * the method will return null by way of error detection.
   * @param user The user name the document was submitted by
   * @param ipAddress The IP address of the machine that the document was sent from
   * @return The id of the created access record
   */
  private String createAccess(String user, String ipAddress)
  {
    String accessId = "";
    Connection con = getConnection();
    ArrayList<String> params = new ArrayList<String>();
    params.add(user);
    params.add(ipAddress);
    try
    {
      accessId = generateUniqueId(sqlCheckIfAccessGuidExists);
      if (accessId.length() != EXPECTED_UUID_LENGTH)
      {
        con.close();
        return null;
      }
      PreparedStatement pstInsert = con.prepareStatement(sqlAddAccess);
      params.add(0, accessId);
      boolean isCommitted = storeInDatabase(pstInsert, params);
      if (!isCommitted)
      {
        // Something wrong happened while storing the values in the database.
        con.close();
        return null;
      }
      con.close();
    }
    catch(Exception x)
    {
      log.throwing(getClass().getName(),"String createAccess(String,String)", x);
      return null;
    }
    finally
    {
      return accessId;
    }
  }
  
  /**
   * Stores a document's author within the database. Prior to storing an entry
   * within the database it is first checked for matching entries. If a match is
   * found then the identifier of the match is returned. If no match is found
   * then a new author record is created and the generated identifier returned.
   * <p>If this method receives an unexpected identifier length from the database
   * the method will return null by way of error detection.
   * @param prefix The author's prefix (Mr, Mrs, Miss, Ms, Dr, Prof, etc.)
   * @param firstName The author's first name
   * @param surname The author's surname (family name)
   * @param company The author's company/organisation/affiliation
   * @param email The author's contact email
   * @param website The author's web-site
   * @return The identifier of the given author or NULL if an error occurs.
   */
  private String createAuthor(String prefix, String firstName, String surname,
                              String company, String email, String website)
  {
    String authorId = "";
    Connection con = getConnection();
    ArrayList<String> params = new ArrayList<String>();
    params.add(prefix);
    params.add(firstName);
    params.add(surname);
    params.add(company);
    params.add(email);
    params.add(website);
    try
    {
      PreparedStatement pstCheck = con.prepareStatement(sqlGetAuthorId);
      authorId = getId(pstCheck, params);
      if (!authorId.equals(""))
      {
        // The author table has returned an id which we will return to the user.
        con.close();
        return authorId;
      }
      authorId = generateUniqueId(sqlCheckIfAuthorGuidExists);
      if (authorId.length() != EXPECTED_UUID_LENGTH)
      {
        con.close();
        return null;
      }
      PreparedStatement pstInsert = con.prepareStatement(sqlAddAuthor);
      params.add(0, authorId);
      boolean isCommitted = storeInDatabase(pstInsert, params);
      if (!isCommitted)
      {
        // Something wrong happened while storing the values in the database.
        con.close();
        return null;
      }
      con.close();
    }
    catch(Exception x)
    {
      log.throwing(getClass().getName(),"String createAuthor(String,String,String,String,String,String)", x);
      return null;
    }
    finally
    {
      return authorId;
    }
  }
  
  /**
   * Stores a document's format type within the database. Prior to storing an
   * entry within the database it is first checked for matching entries. If a
   * match is found then the identifier of the match is return. If no match is
   * found then a new format record is created and the generated identifier is
   * returned.
   * <p>If this method receives an unexpected identifier length from the database
   * the method will return null by way of error detection.
   * @param fileExtension The file extension (.doc, .xls etc)
   * @param mimeType The recognised MIME type corresponding to the file (application/zip)
   * @param description Any user supplied description to be stored within the database
   * @return The identifier of the given format or NULL if an error occurs.
   */
  private String createFormat(String fileExtension, String mimeType, String description)
  {
    String formatId = "";
    Connection con = getConnection();
    ArrayList<String> params = new ArrayList<String>();
    params.add(fileExtension);
    params.add(mimeType);
    params.add(description);
    try
    {
      PreparedStatement pstCheck = con.prepareStatement(sqlGetFormatId);
      formatId = getId(pstCheck, params);
      if (!formatId.equals(""))
      {
        // The format table has returned an id which we will return to the user.
        con.close();
        return formatId;
      }
      formatId = generateUniqueId(sqlCheckIfFormatGuidExists);
      if (formatId.length() != EXPECTED_UUID_LENGTH)
      {
        con.close();
        return null;
      }
      PreparedStatement pstInsert = con.prepareStatement(sqlAddFormat);
      params.add(0, formatId);
      boolean isCommitted = storeInDatabase(pstInsert, params);
      if (!isCommitted)
      {
        // Something wrong happened while storing the values in the database.
        con.close();
        return null;
      }
      con.close();
    }
    catch(Exception x)
    {
      log.throwing(getClass().getName(),"String createFormat(String,String,String)", x);
      return null;
    }
    finally
    {
      return formatId;
    }
  }
  
  /**
   * Stores a locale within the database. Prior to storing an entry within the
   * database it is first checked for matching entries. If a match is found then
   * the identifier of the match is returned. If no match is found then a new
   * locale record is created and the generated identifier returned.
   * <p>If this method receives an unexpected identifier length from the database
   * the method will return null by way of error detection.
   * @param country The originating country of the document (as per ISO3166-1:2006)
   * @param language The native language of the document (as per ISO639-1:2002)
   * @return The identifier of the given locale
   */
  private String createLocale(String country, String language)
  {
    String localeId = "";
    Connection con = getConnection();
    ArrayList<String> params = new ArrayList<String>();
    params.add(country);
    params.add(language);
    try
    {
      PreparedStatement pstCheck = con.prepareStatement(sqlGetLocaleId);
      localeId = getId(pstCheck, params);
      if (!localeId.equals(""))
      {
        // The locale table has returned an id which we will return to the user.
        con.close();
        return localeId;
      }
      localeId = generateUniqueId(sqlCheckIfLocaleGuidExists);
      if (localeId.length() != EXPECTED_UUID_LENGTH)
      {
        con.close();
        return null;
      }
      PreparedStatement pstInsert = con.prepareStatement(sqlAddLocale);
      params.add(0, localeId);
      boolean isCommitted = storeInDatabase(pstInsert, params);
      if (!isCommitted)
      {
        // Something wrong happened while storing the values in the database.
        con.close();
        return null;
      }
      con.close();
    }
    catch (Exception x)
    {
      log.throwing(getClass().getName(),"String createLocale(String,String)", x);
      return null;
    }
    finally
    {
      return localeId;
    }
  }
  
  /**
   * Returns the MIME type ID used within the database that matches a given set
   * of arguments. The file extension, MIME type and description should be supplied
   * for a matching to be found.
   * @param fileExtension The requested file extension
   * @param mimeType The requested MIME type
   * @param description The requested description
   * @return The format guid within the database
   */
  private String getFormatId(String fileExtension, String mimeType, String description)
  {
    String formatId = "";
    Connection con = getConnection();
    try
    {
      PreparedStatement pst = con.prepareStatement(sqlGetFormatId);
      ArrayList<String> params = new ArrayList<String>();
      params.add(fileExtension);
      params.add(mimeType);
      params.add(description);
      formatId = getId(pst, params);
    }
    catch (Exception x)
    {
      log.throwing(getClass().getName(), "String getFormatId(String,String,String)", x);
    }
    finally
    {
      return formatId;
    }
  }
  
  /**
   * Uses the supplied PreparedStatement and String array to compose a SQL command
   * that checks the contents of a given domain (table) for matching values.
   * This can then be used across any tables to check for existing keys given the
   * values contained within the String array.
   * This method only supports String values within the array and so the SQL command
   * should a) accommodate the same number of array elements and b) expect String arguments.
   * This should be compatible with all tables except `odin`.`access`
   * (as the table contains a TIMESTAMP column),
   * @param sqlCommand The prepared SQL command to be executed
   * @param params The argument array to be appended to the SQL command
   * @return The guid of the matching key (if any), empty otherwise.
   */
  private String getId(PreparedStatement sqlCommand, ArrayList<String> params)
  {
    String retrievedId = "";
    Connection con = getConnection();
    try
    {
      for(int i=0; i< params.size(); i++)
      {
        sqlCommand.setString(i+1, params.get(i));
      }
      log.log(Level.FINE, "Running query {0}", sqlCommand.toString());
      ResultSet rs = sqlCommand.executeQuery();
      int counter = 0;
      while (rs.next())
      {
        retrievedId = rs.getString(1);
        counter++;
      }
      log.log(Level.FINEST, "Returning a ID of {0} from {1} result(s).",new Object[]{retrievedId, counter});
      con.close();
    }
    catch (Exception x)
    {
      log.throwing(getClass().getName(), "String getId(PreparedStatement,String[])", x);
      retrievedId = "";
      con.close();
    }
    finally
    {
      return retrievedId;
    }
  }
  
  /**
   * Provides a single point for updating the database tables.
   * Takes the supplied PreparedStatement and String to add a tuple (row) to the
   * database. The array MUST:
   * <ul>
   *   <li>Contain the same amount of arguments as contained in the PreparedStatement</li>
   *   <li>Contain the contents in the same order as in the PreparedStatement</li>
   * </ul>
   * @param sqlCommand The raw SQL command to update the database
   * @param params The parameters to be inserted into the PreparedStatement
   * @return Whether the operation executed successfully or not.
   */
  private boolean storeInDatabase(PreparedStatement sqlCommand, ArrayList<String> params)
  {
    boolean runOkay = true;
    Connection con = getConnection();
    try
    {
      for (int i=0; i < params.size(); i++)
      {
        sqlCommand.setString(i+1, params.get(i));
      }
      log.log(Level.FINE, "Running update {0}", sqlCommand.toString());
      sqlCommand.executeUpdate();
      con.close();
    }
    catch (Exception x)
    {
      log.throwing(getClass().getName(), "String storeInDatabase(PreparedStatement,String[])", x);
      runOkay = false;
      con.close();
    }
    finally
    {
      return runOkay;
    }
  }
  
  /**
   * Takes a SQL command representing a domain and primary key to generate a
   * new generally unique identifier (GUID) within that domain.
   * @param sqlCommand The SQL command to be used for checking a domain for existing keys.
   * @return A new and unique key for the specified domain.
   */
  private String generateUniqueId(String sqlCommand)
  {
    String newId = "";
    boolean hasFailed = true;
    Connection con = getConnection();
    try
    {
      while (hasFailed)
      {
        // Generate a new id and check to see if it exists within the database
        newId = UUID.randomUUID().toString();
        PreparedStatement pst = con.prepareStatement(sqlCommand);
        pst.setString(1,newId);
        log.log(Level.FINE,"Running query {0}",pst.toString());
        ResultSet rs = pst.executeQuery();
        // Use a counter to see if there are any existing records with this ID
        int counter = 0;
        while (rs.next())
        {
          // For every row in the returned ResultSet we increment the counter
          counter++;
        }
        if (counter == 0)
        {
          // The chosen id doesn't already exist so we're safe to use it
          hasFailed = false;
        }
        if (counter > 1)
        {
          // If this happens then there's a serious error in the database
          // We use the metadata to show the table name we're working on
          log.log(Level.SEVERE,"Duplicate entries found in '{0}': {1}",
                  new Object[] {rs.getMetaData().getTableName(1),newId});
        }
      }
    }
    catch (Exception x)
    {
      log.throwing(getClass().getName(),"String generateUniqueId(String)", x);
    }
    finally
    {
      return newId;
    }
  }
  
  /**
   * Retrieves the location of the file associated with a given document.
   * @param guid The identifier of the record to be retrieved
   * @return The file path
   */
  private String getLocation(String guid)
  {
    Connection con = getConnection();
    String location = "";
    try
    {
      PreparedStatement pst = con.prepareStatement(sqlFetchLocationWithGuid);
      pst.setString(1,guid);
      log.log(Level.FINE,"Running query {0}",pst.toString());
      ResultSet rs = pst.executeQuery();
      while (rs.next())
      {
        location = rs.getString(1);
      }
      con.close();
    }
    catch(Exception x)
    {
      log.throwing(getClass().getName(),"String getLocation(String)", x);
    }
    finally
    {
      return location;
    }
  }
}