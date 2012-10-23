/* Class name: OdinRepository
 * File name:  OdinRepository.java
 * Project:    Open Document and Information Network (Odin)
 * Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    24-May-2011 12:14:39
 * Modified:   06-Feb-2012
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.004  13-Aug-2012 Changed class name from XmlRepository to SaxonRepository
 * 0.003  06-Feb-2012 Changed both of the search methods to use a common
 *                    function to execute the search. Renamed the class from
 *                    OdinRepository to SaxonRepository.
 * 0.002  05-Jul-2011 Removed Mime lookup functions and converted code to use
 *                    the MimeTypeDatabase class in its place
 * 0.001  24-May-2011 Initial build
 */
package odin.odin.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.xml.bind.*;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;
import net.sf.saxon.xqj.SaxonXQDataSource;
import odin.odin.object.logging.LoggerFactory;
import odin.odin.xml.AddedDetails;
import odin.odin.xml.Format;
import odin.odin.xml.Item;
import odin.odin.xml.Root;
import odin.shared.xml.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides the majority of interactions between Odin and the back-end database.
 * <p>The repository database is not a formal RDBMS but a XML database using
 * JAX-B and XQuery for interaction.
 * Saxon and XQuery code has been adapted from
 * http://www.ibm.com/developerworks/opensource/library/x-xquerymaven/index.html
 * [Accessed: 13/08/2010]
 * @version 0.004
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class SaxonRepository implements Repository
{
  private Logger log;
  private Root rootDoc;
  private final String namespace = "http://sites.google.com/site/printmiles/Odin";
  
  /**
   * Initialise the class and associated loggers.
   */
  public SaxonRepository()
  {
    log = LoggerFactory.getLogger(getClass().getName());
  }
  
  /**
   * Retrieve the server information document (root document) which is used to
   * obtain settings, policies and locations pertinent to this server.
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
        log.throwing(getClass().getName(), "getRootDocument", jaxbX);
      }
    }
  }
  
  /**
   * This method returns the document meta-data and file matching the supplied GUID.
   * This isn't intended to be called directly from a user but instead from the client
   * once the user has selected a particular document from the list of search results.
   * <p>The method should always return the document but if, for any reason, the GUID
   * cannot be found in the repository then this method will return NULL to the client.
   * <p>NOTE It is the responsibility of the client to rename the received byte stream
   * to the original name of the file as contained within the Item\Title element of the
   * meta-data.
   * @param guid The Id of the document within the repository
   * @return The document itself and its meta-data OR null if it cannot be obtained.
   */
  public Document getDocument(String guid)
  {
    log.log(Level.FINEST, "Searching for document with GUID {0}", guid);
    odin.shared.xml.ObjectFactory sharedFactory = new odin.shared.xml.ObjectFactory();
    Document d = sharedFactory.createDocument();
    try
    {
      if (rootDoc == null) { getRootDocument(); }
      String repositoryDir = rootDoc.getLocations().getRepository();
      File fMeta = new File(repositoryDir+guid+".xml");
      if (fMeta.exists())
      {
        JAXBContext jaxbCtxt = JAXBContext.newInstance("odin.odin.xml");
        Unmarshaller jaxbU = jaxbCtxt.createUnmarshaller();
        jaxbU.setEventHandler(new DefaultValidationEventHandler());
        JAXBElement<Item> iElement = jaxbU.unmarshal(new StreamSource(fMeta),Item.class);
        Item i = iElement.getValue();
        d = cast(i);
        DataHandler dh = new DataHandler(new URL(i.getLocation().getPath()));
        d.setDocument(dh);
      }
      else
      {
        return null;
      }
    }
    catch (MalformedURLException murlX)
    {
      log.throwing(getClass().getName(), "getDocument(String)", murlX);
    }
    catch (JAXBException jaxbX)
    {
      log.throwing(getClass().getName(), "getDocument(String)", jaxbX);
    }
    finally
    {
      return d;
    }
  }
  
  /**
   * This is like <code>getDocument(String)</code> with one key exception. It
   * does not return a <code>DataHandler</code> value for the
   * <code>Document.getDocument()</code> method.
   * @param guid The requested document identifier
   * @return odin.shared.xml.Document 
   * @see odin.shared.xml.Document
   * @see #getDocument(java.lang.String)
   */
  public Document getDocumentMetadata(String guid)
  {
    log.log(Level.FINEST, "Searching for document metadata with GUID {0}", guid);
    odin.shared.xml.ObjectFactory sharedFactory = new odin.shared.xml.ObjectFactory();
    Document d = sharedFactory.createDocument();
    try
    {
      if (rootDoc == null) { getRootDocument(); }
      String repositoryDir = rootDoc.getLocations().getRepository();
      File fMeta = new File(repositoryDir+guid+".xml");
      if (fMeta.exists())
      {
        JAXBContext jaxbCtxt = JAXBContext.newInstance("odin.odin.xml");
        Unmarshaller jaxbU = jaxbCtxt.createUnmarshaller();
        jaxbU.setEventHandler(new DefaultValidationEventHandler());
        JAXBElement<Item> iElement = jaxbU.unmarshal(new StreamSource(fMeta),Item.class);
        Item i = iElement.getValue();
        d = cast(i);
        // Strip the DataHandler from the Document
        d.setDocument(null);
      }
      else
      {
        return null;
      }
    }
    catch (JAXBException jaxbX)
    {
      log.throwing(getClass().getName(), "getDocumentMetadata(String)", jaxbX);
    }
    finally
    {
      return d;
    }
  }

  /**
   * Translates one instance of Item (repository document) to Document (suitable for transmission via SOAP)
   * @param input The repository list item
   * @return A Document instance suitable for transmission via SOAP
   */
  private Document cast(Item input)
  {
    // Create authorNode factory instance for the sub-parts of the Document
    odin.shared.xml.ObjectFactory sharedFactory = new odin.shared.xml.ObjectFactory();
    // Create the sub-sections of Document
    Document d = sharedFactory.createDocument();
    Document.Metadata dMeta = sharedFactory.createDocumentMetadata();
    Document.Metadata.Author auth = sharedFactory.createDocumentMetadataAuthor();
    Document.Metadata.Locale loc = sharedFactory.createDocumentMetadataLocale();
    Document.Metadata.Attributes attr = sharedFactory.createDocumentMetadataAttributes();
    Document.Metadata.Keywords key = sharedFactory.createDocumentMetadataKeywords();

    // Get the sub-parts from the input Item
    Item.Origin iOrigin = input.getOrigin();
    Item.Origin.Author iAuth = iOrigin.getAuthor();
    Item.Origin.Locale iLoc = iOrigin.getLocale();
    AddedDetails ad = iOrigin.getAdded();
    Item.Attributes attributes = input.getAttributes();
    Item.Keywords keywords = input.getKeywords();
    
    // Set the first section values
    d.setIpaddress(ad.getFrom());
    d.setUsername(ad.getBy());
    dMeta.setTitle(input.getTitle());
    Format f = MimeTypeDatabase.getFormatByGuid(input.getFormat());
    dMeta.setMimetype(f.getMime());
    dMeta.setFileextension(f.getExt());

    // Set the author details
    auth.setFirstName(iAuth.getFirstName());
    auth.setSurname(iAuth.getSurname());
    auth.setPrefix(iAuth.getPrefix());
    auth.setCompany(iAuth.getCompany());
    auth.setEmail(iAuth.getEmail());
    auth.setWebsite(iAuth.getWebsite());
    dMeta.setAuthor(auth);

    // Set the locale details
    loc.setLanguage(iLoc.getLanguage());
    loc.setCountry(iLoc.getCountry());
    dMeta.setLocale(loc);

    // Set the attributes
    for (int i = 0; i < attributes.getAttribute().size(); i++)
    {
      Document.Metadata.Attributes.Attribute a = sharedFactory.createDocumentMetadataAttributesAttribute();
      a.setKey(attributes.getAttribute().get(i).getKey());
      a.setValue(attributes.getAttribute().get(i).getValue());
      attr.getAttribute().add(a);
    }
    dMeta.setAttributes(attr);

    // Set the keywords
    for (int i = 0; i < keywords.getKeyword().size(); i++)
    {
      key.getKeyword().add(keywords.getKeyword().get(i));
    }
    dMeta.setKeywords(key);
    d.setMetadata(dMeta);

    // Try getting the file from the repository
    try
    {
      URL urlDocument = new URL(input.getLocation().getPath());
      DataHandler dh = new DataHandler(urlDocument);
      d.setDocument(dh);
    }
    catch (MalformedURLException murlX)
    {
      log.throwing(getClass().getName(), "cast(Item)", murlX);
    }
    return d;
  }

  /**
   * Translates one instance of Document (suitable for transmission via SOAP) to Item (repository document)
   * @param input The repository list item
   * @return A Document instance suitable for transmission via SOAP
   */
  private Item cast(Document input, String guid)
  {
    odin.odin.xml.ObjectFactory odinFactory = new odin.odin.xml.ObjectFactory();
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
    iOA.setFirstName(input.getMetadata().getAuthor().getFirstName());
    iOA.setSurname(input.getMetadata().getAuthor().getSurname());
    iOA.setPrefix(input.getMetadata().getAuthor().getPrefix());
    iOA.setCompany(input.getMetadata().getAuthor().getCompany());
    iOA.setEmail(input.getMetadata().getAuthor().getEmail());
    iOA.setWebsite(input.getMetadata().getAuthor().getWebsite());
    // Populate the locale section
    iOL.setLanguage(input.getMetadata().getLocale().getLanguage());
    iOL.setCountry(input.getMetadata().getLocale().getCountry());
    // Populate the AddedDetails section
    try
    {
      // And then try to put the right ones in
      GregorianCalendar gCal = new GregorianCalendar();
      DatatypeFactory dataFactory;
      dataFactory = DatatypeFactory.newInstance();
      XMLGregorianCalendar xgc = dataFactory.newXMLGregorianCalendar(gCal);
      iAD.setAt(xgc);
      iAD.setBy(input.getUsername());
      iAD.setFrom(input.getIpaddress());
    }
    catch (DatatypeConfigurationException dcX)
    {
      log.throwing(getClass().getName(), "cast(Document,String)", dcX);
    }
    // Populate the Attributes
    for (int x = 0; x < input.getMetadata().getAttributes().getAttribute().size(); x++)
    {
      Item.Attributes.Attribute a = odinFactory.createItemAttributesAttribute();
      a.setKey(input.getMetadata().getAttributes().getAttribute().get(x).getKey());
      a.setValue(input.getMetadata().getAttributes().getAttribute().get(x).getValue());
      iA.getAttribute().add(a);
    }
    // Set the keywords
    for (int x = 0; x < input.getMetadata().getKeywords().getKeyword().size(); x++)
    {
      iK.getKeyword().add(input.getMetadata().getKeywords().getKeyword().get(x));
    }
    iO.setAdded(iAD);
    iO.setAuthor(iOA);
    iO.setLocale(iOL);
    i.setGuid(guid);
    i.setTitle(input.getMetadata().getTitle());
    Format f = MimeTypeDatabase.getFormatByFileExtension(input.getMetadata().getFileextension().toLowerCase());
    i.setFormat(f.getGuid());
    i.setOrigin(iO);
    i.setTrust(new BigInteger("500"));
    i.setAttributes(iA);
    i.setKeywords(iK);
    return i;
  }
  
  /**
   * Parses an XML <code>Node</code> into a corresponding Result document.
   * This is particularly used when converting XQuery results into their corresponding
   * JAX-B classes.
   * @param input
   * @return The document to be included in the results
   * @see odin.shared.xml.Results.Document
   * @see org.w3c.dom.Node
   */
  private Results.Document castToResult(Node input)
  {
    odin.shared.xml.ObjectFactory sharedFactory = new odin.shared.xml.ObjectFactory();
    // Check to see if the root element is correct
    if (!input.getNodeName().equals("item"))
    {
      // Return authorNode blank document as the document isn't of the right type
      return sharedFactory.createResultsDocument();
    }
    
    Results.Document doc = sharedFactory.createResultsDocument();
    NodeList nl = input.getChildNodes();
    for (int i = 0; i < nl.getLength(); i++)
    {
      Node node = nl.item(i);
      if (node.getNodeName().toLowerCase().equals("guid"))
      {
        doc.setGuid(node.getTextContent());
      }
      if (node.getNodeName().toLowerCase().equals("title"))
      {
        doc.setTitle(node.getTextContent());
      }
      if (node.getNodeName().toLowerCase().equals("format"))
      {
        Format f = MimeTypeDatabase.getFormatByGuid(node.getTextContent());
        if (f != null)
        {
          doc.setFileextension(f.getExt());
          doc.setMimetype(f.getMime());
        }
        else
        {
          doc.setFileextension(".???");
          doc.setMimetype("Unknown");
        }
      }
      if (node.getNodeName().toLowerCase().equals("origin"))
      {
        NodeList nlOrigin = node.getChildNodes();
        for (int k = 0; k < nlOrigin.getLength(); k++)
        {
          Node nOrigin = nlOrigin.item(k);
          if (nOrigin.getNodeName().toLowerCase().equals("author"))
          { 
            NodeList nlAuth = nOrigin.getChildNodes();
            Results.Document.Author authorNode = sharedFactory.createResultsDocumentAuthor();
            for (int j = 0; j < nlAuth.getLength(); j++)
            {
              Node nAuth = nlAuth.item(j);
              if (nAuth.getNodeName().toLowerCase().equals("prefix")) { authorNode.setPrefix(nAuth.getTextContent()); }
              if (nAuth.getNodeName().toLowerCase().equals("firstname")) { authorNode.setFirstName(nAuth.getTextContent()); }
              if (nAuth.getNodeName().toLowerCase().equals("surname")) { authorNode.setSurname(nAuth.getTextContent()); }
              if (nAuth.getNodeName().toLowerCase().equals("company")) { authorNode.setCompany(nAuth.getTextContent()); }
              if (nAuth.getNodeName().toLowerCase().equals("email")) { authorNode.setEmail(nAuth.getTextContent()); }
              if (nAuth.getNodeName().toLowerCase().equals("website")) { authorNode.setWebsite(nAuth.getTextContent()); }
            }
            doc.setAuthor(authorNode);
          }
          if (nOrigin.getNodeName().toLowerCase().equals("locale"))
          {
            NodeList nlLocale = nOrigin.getChildNodes();
            Results.Document.Locale localeNode = sharedFactory.createResultsDocumentLocale();
            for (int j = 0; j < nlLocale.getLength(); j++)
            {
              Node nLocale = nlLocale.item(j);
              if (nLocale.getNodeName().toLowerCase().equals("language")) { localeNode.setLanguage(nLocale.getTextContent()); }
              if (nLocale.getNodeName().toLowerCase().equals("country")) { localeNode.setCountry(nLocale.getTextContent()); }
            }
            doc.setLocale(localeNode);
          }
        }
      }
      if (node.getNodeName().toLowerCase().equals("trust"))
      {
        doc.setTrust(new BigInteger(node.getTextContent()));
      }
      if (node.getNodeName().toLowerCase().equals("keywords"))
      {
        NodeList nlKeys = node.getChildNodes();
        Results.Document.Keywords keywordsNode = sharedFactory.createResultsDocumentKeywords();
        for (int j = 0; j < nlKeys.getLength(); j++)
        {
          Node nKey = nlKeys.item(j);
          keywordsNode.getKeyword().add(nKey.getTextContent());
        }
        doc.setKeywords(keywordsNode);
      }
      if (node.getNodeName().toLowerCase().equals("attributes"))
      {
        NodeList nlAttr = node.getChildNodes();
        Results.Document.Attributes attributesNode = sharedFactory.createResultsDocumentAttributes();
        for (int j = 0; j < nlAttr.getLength(); j++)
        {
          Node attribute = nlAttr.item(j);
          NodeList values = attribute.getChildNodes();
          Results.Document.Attributes.Attribute a = sharedFactory.createResultsDocumentAttributesAttribute();
          for (int k = 0; k < values.getLength(); k++)
          {
            Node n = values.item(k);
            if (n.getNodeName().toLowerCase().equals("key")) { a.setKey(n.getTextContent()); }
            if (n.getNodeName().toLowerCase().equals("value")) { a.setValue(n.getTextContent()); }
          }
          attributesNode.getAttribute().add(a);
        }
        doc.setAttributes(attributesNode);
      }
    }
    return doc;
  }
  
  /**
   * Stores the supplied <code>Document</code> within the Odin repository. It
   * is split into two components, the file itself which is contained within a
   * <code>DataHandler</code> and the meta-data which is contained within the
   * rest of the class.
   * <p>The <code>Response</code> object returned from the method indicates
   * whether the document was successfully stored and its identifier within the
   * repository. If the storage action was not successful then an error code and
   * message is returned.
   * @param uploadedDoc The document and meta-data to be stored within Odin
   * @return Returns a Response document containing a variable structure.
   * @see javax.activation.DataHandler
   * @see odin.shared.xml.Document
   * @see odin.shared.xml.Response
   */
  public Response storeDocument(Document uploadedDoc)
  {
    // Create factories for JAXB data types
    odin.shared.xml.ObjectFactory sharedFactory = new odin.shared.xml.ObjectFactory();
    odin.odin.xml.ObjectFactory odinFactory = new odin.odin.xml.ObjectFactory();
    // Initialise authorNode Response document to return to the client
    // FIXME Add code for updating reports document
    Response r = sharedFactory.createResponse();
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
      // TODO Fix finding the file format
      Item i = cast(uploadedDoc, guid);
      
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
      
      // Create the location details
      Item.Location iL = odinFactory.createItemLocation();
      Item.Location.Repository iLR = odinFactory.createItemLocationRepository();
      // As it's the first time we've uploaded the document we can set these two as the same
      iLR.getAdded().add(i.getOrigin().getAdded());

      // This is going to be the name where we output the received file to
      // TODO Allow the admin to choose when extension is used
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

      DataHandler dh = uploadedDoc.getDocument();
      dh.writeTo(new FileOutputStream(fURL));
      // TODO Check whether the file is an XML, whether it contains namespaces and what the policy is around them (allow or reject)
      // TODO Check what the policy is around ZIP files (unzip the archive and store individually OR store the archive)

      log.log(Level.CONFIG, "Received an uploaded document which was successfully stored in the repository under GUID: {0}", guid);
      
      Response.Status s = sharedFactory.createResponseStatus();
      Response.Appendix a = sharedFactory.createResponseAppendix();
      s.setCode(new BigInteger("201"));
      s.setMessage("Created");
      s.setDescription("The resource was successfully stored within the repository with a guid of: " + guid);
      a.setGuid(guid);
      r.setStatus(s);
      r.setAppendix(a);
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
  
  /**
   * Returns a list of matching results for criteria in specific fields. This
   * produces a more specific search than <code>basicSearch(Basic)</code>.
   * @param searchCriteria The list of criteria and the fields to which they should be matched
   * @return A set of results
   * @see odin.shared.xml.Results
   */
  public Results search(Search searchCriteria)
  { 
    odin.shared.xml.ObjectFactory sharedFactory = new odin.shared.xml.ObjectFactory();
    Results r = sharedFactory.createResults();
    log.log(Level.FINEST, "Received search request from {0} on {1}", new Object[]{searchCriteria.getUsername(), searchCriteria.getIpaddress()});
    Search.Document d = searchCriteria.getDocument();
    // We use the ArrayList to store all of the matches(field,String) parameters
    // for our query. We can also use it to find out how many criteria we have for
    // correctly placing the where & and clauses
    ArrayList<String> al = new ArrayList<String>();
    // Fetch the title and/or guid
    if ((d.getGuid() != null) && (!d.getGuid().equals(""))) { al.add("matches($d/guid,'" + d.getGuid() + "')"); }
    if ((d.getTitle() != null) && (!d.getTitle().equals(""))) { al.add("matches($d/title,'" + d.getTitle() + "')"); }
    // Fetch the locale
    if ((d.getLocale().getLanguage()) != null || (d.getLocale().getCountry() != null))
    {
      if ((d.getLocale().getLanguage() != null) && (!d.getLocale().getLanguage().equals(""))) { al.add("matches($d/origin/locale/language,'" + d.getLocale().getLanguage() + "')"); }
      if ((d.getLocale().getCountry() != null) && (!d.getLocale().getCountry().equals(""))) { al.add("matches($d/origin/locale/country,'" + d.getLocale().getCountry() + "')"); }
    }
    // Fetch the author's details
    if (d.getAuthor() != null)
    {
      if (!d.getAuthor().getPrefix().equals("")) { al.add("matches($d/origin/author/prefix,'" + d.getAuthor().getPrefix() + "')"); }
      if (!d.getAuthor().getFirstName().equals("")) { al.add("matches($d/origin/author/firstName,'" + d.getAuthor().getFirstName() + "')"); }
      if (!d.getAuthor().getSurname().equals("")) { al.add("matches($d/origin/author/surname,'" + d.getAuthor().getSurname() + "')"); }
      if (!d.getAuthor().getCompany().equals("")) { al.add("matches($d/origin/author/company,'" + d.getAuthor().getCompany() + "')"); }
      if (!d.getAuthor().getEmail().equals("")) { al.add("matches($d/origin/author/email,'" + d.getAuthor().getEmail() + "')"); }
      if (!d.getAuthor().getWebsite().equals("")) { al.add("matches($d/origin/author/website,'" + d.getAuthor().getWebsite() + "')"); }
    }
    // Fetch added details
    if (d.getAdded() != null)
    {
      if (!d.getAdded().getBy().equals("")) { al.add("matches($d/origin/added/by,'" + d.getAdded().getBy() + "')"); }
      if (!d.getAdded().getFrom().equals("")) { al.add("matches($d/origin/added/from,'" + d.getAdded().getFrom() + "')"); }
      if (d.getAdded().getAt() != null) { al.add("matches($d/origin/added/at,'" + d.getAdded().getAt().toString() + "')"); }
    }
    // Fetch keywords
    if (d.getKeywords() != null)
    {
      for (String s: d.getKeywords().getKeyword())
      {
        if (!s.equals("")) { al.add("matches($d/keywords/keyword,'" + s + "')"); }
      }
    }
    // Fetch attributes
    if (d.getAttributes() != null)
    {
      for (Search.Document.Attributes.Attribute a : d.getAttributes().getAttribute())
      {
        if ((!a.getKey().equals("")) && (!a.getValue().equals("")))
        {
          al.add("matches($d/attributes/attribute/key,'" + a.getKey() + "')");
          al.add("matches($d/attributes/attribute/value,'" + a.getValue() + "')");
        }
      }
    }
    // We've now collated all of the search terms into the ArrayList.
    // We can now retrieve them to create our search expression
    if (rootDoc == null) { getRootDocument(); }
    String repositoryDir = rootDoc.getLocations().getRepository();
    String expression = "declare default element namespace '" + namespace + "'; " +
            "for $d in collection('file:///" + repositoryDir + "?select=*.xml')/item";
    // Check to make sure we've got something in the array
    if (al.size() > 0)
    {
      for (int i = 0; i < al.size(); i++)
      {
        if (i==0)
        {
          expression = expression + " where " + al.get(i);
        }
        else
        {
          expression = expression + " and " + al.get(i);
        }
      }
    }
    expression = expression + " return $d";
    log.log(Level.FINE, "Running query: {0}", expression);
    try
    {
      r = performSearch(expression);
    }
    catch (XQException xqX)
    {
      log.throwing(getClass().getName(), "search", xqX);
    }
    catch (Exception x)
    {
      log.throwing(getClass().getName(), "search", x);
    }
    finally
    {
      return r;
    }
  }

  /**
   * Returns a list of documents that match keywords occurring anywhere within the document
   * @param words The list of words to be found anywhere within the meta-data documents
   * @return A list of results
   * @see odin.shared.xml.Results
   */
  public Results basicSearch(Basic words)
  {
    odin.shared.xml.ObjectFactory sharedFactory = new odin.shared.xml.ObjectFactory();
    Results r = sharedFactory.createResults();
    log.log(Level.FINEST, "Received search request from {0} on {1}", new Object[]{words.getUsername(), words.getIpaddress()});
    if (rootDoc == null) { getRootDocument(); }
    String repositoryDir = rootDoc.getLocations().getRepository();
    String expression = "declare default element namespace '" + namespace + "'; " +
            "for $d in collection('file:///" + repositoryDir + "?select=*.xml')/item";
    ArrayList<String> al = new ArrayList<String>();
    List<String> searchTerms = words.getDocument().getWords().getWord();
    for (String s : searchTerms)
    {
      if (!s.equals(""))
      {
        al.add("matches($d,'" + s + "')");
      }
    }
    if (al.size() > 0)
    {
      for (int i = 0; i < al.size(); i++)
      {
        if (i==0)
        {
          expression = expression + " where " + al.get(i);
        }
        else
        {
          expression = expression + " and " + al.get(i);
        }
      }
    }
    expression = expression + " return $d";
    try
    {
      r = performSearch(expression);
    }
    catch (XQException xqX)
    {
      log.throwing(getClass().getName(), "basicSearch", xqX);
    }
    catch (Exception x)
    {
      log.throwing(getClass().getName(), "basicSearch", x);
    }
    finally
    {
      return r;
    }
  }
  
  /**
   * Performs the actual query on the database and returns a set of <code>Results</code>.
   * @param xQuerySearchExpression - The XQuery search string used to query the database
   * @throws XQException Any issues resulting from the XQuery expression or parser
   * @throws Exception Any general issues
   * @return A Results document containing those items matching the search criteria.
   * @see odin.shared.xml.Results
   */
  private Results performSearch(String xQuerySearchExpression) throws XQException, Exception
  {
    log.log(Level.FINEST, "Running query: {0}", xQuerySearchExpression);
    long split1, split2, split3;
    final long milli = 1000000;
    split1 = System.nanoTime();
    odin.shared.xml.ObjectFactory sharedFactory = new odin.shared.xml.ObjectFactory();
    Results r = sharedFactory.createResults();
    SaxonXQDataSource xqds = new SaxonXQDataSource();
    XQConnection con = xqds.getConnection();
    XQPreparedExpression expr = con.prepareExpression(xQuerySearchExpression);
    XQResultSequence result = expr.executeQuery();
    split2 = System.nanoTime();
    log.log(Level.FINEST, "Query executed in {0}ms", new Float((split2-split1)/milli));
    while (result.next())
    {
      Node node = result.getNode();
      Results.Document rDoc = castToResult(node);
      r.getDocument().add(rDoc);
    }
    r.setCount(new BigInteger("" + r.getDocument().size()));
    split3 = System.nanoTime();
    log.log(Level.FINEST, "Results processed in {0}ms", new Float((split3-split2)/milli));
    log.log(Level.FINEST, "Total time to process query: {0}ms", new Float((split3-split1)/milli));
    return r;
  }
}
