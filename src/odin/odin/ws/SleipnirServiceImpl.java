/* Class name: SleipnirServiceImpl
 * File name:  SleipnirServiceImpl.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    12-Apr-2011 13:24:47
 * Modified:   24-May-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.006  24-May-2011 Shifted majorty of the code to the OdinRepository class
 * 0.005  05-May-2011 Remove getMimeTypes from the client
 * 0.004  04-May-2011 Added new method for basic word searches
 * 0.003  03-May-2011 Added code for search method
 * 0.002  24-Apr-2011 Added getServerInfo method
 * 0.001  12-Apr-2011 Initial build
 */

package odin.odin.ws;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.xml.bind.*;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.transform.stream.StreamSource;
import odin.odin.object.logging.LoggerFactory;
import odin.odin.repository.SaxonRepository;
import odin.odin.repository.Repository;
import odin.odin.xml.*;
import odin.shared.ws.Policy;
import odin.shared.ws.SleipnirService;
import odin.shared.xml.*;

/**
 * JavaDoc to follow.
 * @version 0.006
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

@WebService(endpointInterface="odin.shared.ws.SleipnirService")
public class SleipnirServiceImpl implements SleipnirService
{
  private Logger log;
  private Root rootDoc;
  private Repository database;

  public SleipnirServiceImpl()
  {
    log = LoggerFactory.getLogger(getClass().getName());
    //FIXME Change the initialisation code to read what type of repository we should connect to from the root config
    database = new SaxonRepository();
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
        log.throwing(getClass().getName(), "getRootDocument", jaxbX);
      }
    }
  }

  public Response upload(Document uploadedDoc)
  {
    return database.storeDocument(uploadedDoc);
  }

  /**
   * Returns a list of documents that match the contents of specific fields.
   * <p>As the database can take some time to return the results they are returned
   * asynchronously to allow the client GUI to handle requests as they're available
   * rather than when the complete set is finished.
   * @param searchCriteria The set of criteria and the fields to which the criteria corresponds
   * @return A set of results
   */
  public Results search(Search searchCriteria)
  {
    return database.search(searchCriteria);
  }

  /**
   * Returns a list of documents that match keywords occurring anywhere within the document.
   * <p>As the database can take some time to return results they are returned individually and so
   * asynchronously.
   * @param words The list of words to be found anywhere within the meta-data documents
   * @return A set of results
   */
  public Results basicSearch(Basic words)
  {
    return database.basicSearch(words);
  }
  
  /**
   * This method returns the document meta-data and file matching the supplied GUID.
   * This isn't intended to be called directly from authorNode user but instead from the client
   * once the user has selected authorNode particular document from the list of search results.
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
    Document d = database.getDocument(guid);
    return d;
  }
  
  public Document getDocumentMetadata(String guid)
  {
    log.log(Level.FINEST, "Searching for document metadata with GUID {0}", guid);
    Document d = database.getDocumentMetadata(guid);
    return d;
  }

  public Response getServerInfo(String ipAddress)
  {
    log.log(Level.FINEST, "Got getServerInfo(String) request from {0}", ipAddress);
    if (rootDoc == null) { getRootDocument(); }
    odin.shared.xml.ObjectFactory sharedFactory = new odin.shared.xml.ObjectFactory();
    Response r = sharedFactory.createResponse();
    Response.Status rS = sharedFactory.createResponseStatus();
    rS.setCode(new BigInteger("200"));
    rS.setMessage("OK");
    rS.setDescription("The server information was completed without any problems");
    r.setStatus(rS);
    Response.Appendix rA = sharedFactory.createResponseAppendix();
    Response.Appendix.Info rAI = sharedFactory.createResponseAppendixInfo();
    Response.Appendix.Info.Subjects rAIS = sharedFactory.createResponseAppendixInfoSubjects();
    List<String> subjects = rootDoc.getSubjects().getSubject();
    Iterator i = subjects.iterator();
    while (i.hasNext())
    {
      rAIS.getSubject().add((String) i.next());
    }
    rAI.setSubjects(rAIS);
    rAI.setAdminName(rootDoc.getDetails().getAdminName());
    rAI.setEmail(rootDoc.getDetails().getEmail());
    rAI.setName(rootDoc.getDetails().getServerName());
    rAI.setWebsite(rootDoc.getDetails().getWebsite());
    rAI.setPhone(rootDoc.getDetails().getPhone());
    rAI.setOdinUri(ServicePublisher.getOdinURL());
    rAI.setSleipnirUri(ServicePublisher.getSleipnirURL());
    rAI.setBarcodeUri(rootDoc.getLocations().getBarcode());
    
    Response.Appendix.Info.Policies raiPolicies = sharedFactory.createResponseAppendixInfoPolicies();
    Response.Appendix.Info.Policies.Trust raipT = sharedFactory.createResponseAppendixInfoPoliciesTrust();
    Response.Appendix.Info.Policies.Xml raipX = sharedFactory.createResponseAppendixInfoPoliciesXml();
    Response.Appendix.Info.Policies.Zip raipZ = sharedFactory.createResponseAppendixInfoPoliciesZip();
    
    raipT.setAccept(rootDoc.getPolicies().getTrust().getAccept());
    raipT.setReject(rootDoc.getPolicies().getTrust().getReject());
    
    raipX.setAll(rootDoc.getPolicies().getXml().isAll());
    raipX.setNamespace(rootDoc.getPolicies().getXml().isNamespace());
    raipX.setReject(rootDoc.getPolicies().getXml().isReject());
    
    raipZ.setExtract(rootDoc.getPolicies().getZip().isExtract());
    raipZ.setReject(rootDoc.getPolicies().getZip().isReject());
    raipZ.setStore(rootDoc.getPolicies().getZip().isStore());
    
    raiPolicies.setTrust(raipT);
    raiPolicies.setXml(raipX);
    raiPolicies.setZip(raipZ);
    
    rAI.setPolicies(raiPolicies);
    rA.setInfo(rAI);
    r.setAppendix(rA);
    return r;
  }
  
  public int getZipPolicy(String ipAddress)
  {
    log.log(Level.FINEST, "Got getZipPolicy(String) request from {0}", ipAddress);
    if (rootDoc == null) { getRootDocument(); }
    Root.Policies.Zip zipPolicy = rootDoc.getPolicies().getZip();
    boolean reject = zipPolicy.isReject(); // This is the default
    boolean extract = zipPolicy.isExtract();
    boolean store = zipPolicy.isStore();
    
    if ((extract) && (!store)) { return Policy.ZIP_EXTRACT_OR_REJECT.getCode(); }
    else if (extract && store) { return Policy.ZIP_EXTRACT_OR_STORE.getCode(); }
    else if (store) { return Policy.ZIP_STORE_ALL.getCode(); }
    else { return Policy.ZIP_REJECT_ALL.getCode(); }
  }
  
  public int getXmlPolicy(String ipAddress)
  {
    log.log(Level.FINEST, "Got getXmlPolicy(String) request from {0}", ipAddress);
    if (rootDoc == null) { getRootDocument(); }
    Root.Policies.Xml xmlPolicy = rootDoc.getPolicies().getXml();
    boolean reject = xmlPolicy.isReject().booleanValue(); // This is the default
    boolean store = xmlPolicy.isAll().booleanValue();
    boolean namespace = xmlPolicy.isNamespace().booleanValue();
    
    if (store) { return Policy.XML_STORE_ALL.getCode(); }
    else if (namespace) { return Policy.XML_DEMAND_NAMESPACES.getCode(); }
    else { return Policy.XML_REJECT.getCode(); }
  }
  
  public String getBarcodeUrl(String ipAddress)
  {
    log.log(Level.FINEST, "Got getBarcodeUrl(String) request from {0}", ipAddress);
    return ServicePublisher.getSleipnirWebpage();
  }
}