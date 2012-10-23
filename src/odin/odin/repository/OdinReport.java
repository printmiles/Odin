/* Class name: OdinReport
 * File name:  OdinReport.java
 * Project:    Open Document and Information Network (Odin)
 * Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    08-Jul-2011 07:40:55
 * Modified:   08-Jul-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  08-Jul-2011 Initial build
 */
package odin.odin.repository;

import java.io.File;
import java.util.*;
import java.util.logging.*;
import javax.xml.bind.*;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.transform.stream.StreamSource;
import odin.odin.object.logging.LoggerFactory;
import odin.odin.xml.*;

/**
 * Used to track statistics about the repository: the types of documents, keywords,
 * attributes, authors and activity.
 * <p>CURRENTLY UNDER DEVELOPMENT
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class OdinReport
{
  private static Logger log;
  private static Root rootDoc;
  
  private static Calendar lastUpdate;
  private static int numberOfFiles;
  
  private static HashMap<String,Integer> hmFileReport;
  private static HashMap<String,Integer> hmMimeReport;
  private static HashMap<String,Integer> hmCountryReport;
  private static HashMap<String,Integer> hmLanguageReport;
  private static HashMap<String,Integer> hmAuthorReport;
  private static HashMap<String,Integer> hmOrganisationReport;
  private static HashMap<String,Integer> hmSubmittedReport;
  private static HashMap<String,Integer> hmTrustReport;
  
  public OdinReport()
  {
    log = LoggerFactory.getLogger(getClass().getName());
    
    // Initialise the variables
    numberOfFiles = 0;
    lastUpdate = Calendar.getInstance();
    hmFileReport = new HashMap<String,Integer>();
    hmMimeReport = new HashMap<String,Integer>();
    hmCountryReport = new HashMap<String,Integer>();
    hmLanguageReport = new HashMap<String,Integer>();
    hmAuthorReport = new HashMap<String,Integer>();
    hmOrganisationReport = new HashMap<String,Integer>();
    hmSubmittedReport = new HashMap<String,Integer>();
    hmTrustReport = new HashMap<String,Integer>();
  }
  
  private static void getRootDocument()
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
        log.throwing(new MimeTypeDatabase().getClass().getName(), "getRootDocument", jaxbX);
      }
    }
  }
  
  public static void initialiseStatsDatabase()
  {
    // Get the Root information document if it isn't already initialised
    if (rootDoc == null) { getRootDocument(); }
    // Get the report file location
    File fReport = new File(rootDoc.getLocations().getReport());
    try
    {
      // Configure the JAXB environment
      JAXBContext jaxbCtxt = JAXBContext.newInstance("odin.odin.xml");
      Unmarshaller jaxbU = jaxbCtxt.createUnmarshaller();
      jaxbU.setEventHandler(new DefaultValidationEventHandler());
      JAXBElement<Report> rElement = jaxbU.unmarshal(new StreamSource(fReport),Report.class);
      Report r = rElement.getValue();

      // Store the data
      numberOfFiles = r.getSummary().getNumberOfFiles().intValue();
      javax.xml.datatype.XMLGregorianCalendar xmlgcUpdated = r.getSummary().getLastUpdate();
      lastUpdate.set(xmlgcUpdated.getYear(),
                     xmlgcUpdated.getMonth(),
                     xmlgcUpdated.getDay(),
                     xmlgcUpdated.getHour(),
                     xmlgcUpdated.getMinute(),
                     xmlgcUpdated.getSecond());
    }
    catch (JAXBException jaxbX)
    {
      log.throwing(new MimeTypeDatabase().getClass().getName(), "initialiseStatsDatabase()", jaxbX);
    }
  }
  
}
