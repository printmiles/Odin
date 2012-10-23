/* Class name: BasicSearchWorker
 * File name:  BasicSearchWorker.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    05-May-2011 18:16:23
 * Modified:   05-May-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  05-May-2011 Initial build
 */

package odin.sleipnir;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import odin.shared.ws.*;
import odin.shared.xml.*;

/**
 * This is the worker thread handling searches using the "Basic" search form.
 * Basic searches look for matching keywords anywhere within the document against
 * a delimited string supplied by the user.
 * <p>As this is a worker thread it extends <code>javax.swing.SwingWorker</code> class.
 * but does not return a value. Instead it notifies any windows through their own methods.
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 * @see javax.swing.SwingWorker
 */

public class BasicSearchWorker extends SwingWorker
{
  private ProgressForm pf;
  private Basic.Document criteria;
  private ResourceBundle rb;

  /**
   * Initialises the class and provides information such as the progress window,
   * search criteria and localisation details.
   * @param pf The progress window
   * @param sd The search criteria
   * @param rb The localisation
   * @see odin.sleipnir.ProgressForm
   * @see odin.shared.xml.Basic.Document
   * @see java.util.ResourceBundle
   */
  public BasicSearchWorker(ProgressForm pf, Basic.Document sd, ResourceBundle rb)
  {
    this.pf = pf;
    this.criteria = sd;
    this.rb = rb;
  }

  /**
   * Performs the tasks of:
   * <ol>
   *   <li>Establishing a connection to the Odin server.</li>
   *   <li>Sending the search criteria.</li>
   *   <li>Waiting for a response from the server.</li>
   *   <li>Displaying the results in a ResultsFrame.</li>
   * </ol>
   * @return Nothing is returned from this method (Void)
   * @see odin.sleipnir.ResultsFrame
   */
  public Void doInBackground()
  {
    try
    {
      pf.setStatus(rb.getString("gui.progress.search.connect"));
      pf.increaseProgress();
      // Get the server information
      OdinServer odinSvr = MainWindow.getMainWindow().getServer();
      // Form the URL
      URL url = new URL(odinSvr.getSleipnirServiceURL());
      // Qualified name for the service: Service URI, Service name
      QName qnSleipnir = new QName(odinSvr.getPackageUrl(),"SleipnirServiceImplService");
      // Create the service
      Service service = Service.create(url, qnSleipnir);
      SleipnirService sleipnir = service.getPort(SleipnirService.class);
      pf.setStatus(rb.getString("gui.progress.search.send"));
      pf.increaseProgress();
      ObjectFactory sharedFactory = new ObjectFactory();
      Basic basicSearchDoc = sharedFactory.createBasic();
      basicSearchDoc.setUsername(System.getProperty("user.name"));
      String localaddress = "127.0.0.1";
      try
      {
        localaddress = InetAddress.getLocalHost().toString();
      }
      catch (UnknownHostException ex)
      {
        System.err.println(rb.getString("gui.error.local"));
      }
      finally
      {
        basicSearchDoc.setIpaddress(localaddress);
      }
      basicSearchDoc.setDocument(criteria);
      pf.setStatus(rb.getString("gui.progress.search.wait"));
      pf.increaseProgress();
      // Send document to the server and wait for a response
      odin.shared.xml.Results r = sleipnir.basicSearch(basicSearchDoc);
      // Got something to display
      pf.setStatus(rb.getString("gui.progress.search.display"));
      pf.increaseProgress();
      GenericTable gT = new GenericTable();
      ArrayList<String> alColumns = new ArrayList<String>();
      alColumns.add(rb.getString("gui.results.column.guid"));
      alColumns.add(rb.getString("gui.results.column.title"));
      alColumns.add(rb.getString("gui.results.column.lang"));
      alColumns.add(rb.getString("gui.results.column.country"));
      alColumns.add(rb.getString("gui.results.column.ext"));
      alColumns.add(rb.getString("gui.results.column.mime"));
      alColumns.add(rb.getString("gui.results.column.author"));
      ArrayList<Object[]> alRows = new ArrayList<Object[]>();
      for (Results.Document d: r.getDocument())
      {
        String guid = d.getGuid();
        String title = d.getTitle();
        Locale localeLang = new Locale(d.getLocale().getLanguage());
        String lang = localeLang.getDisplayLanguage(rb.getLocale());
        String country = d.getLocale().getCountry();
        String fileExt = d.getFileextension();
        String mime = d.getMimetype();
        String author = d.getAuthor().getFirstName() + " " + d.getAuthor().getSurname();
        alRows.add(new Object[] {guid, title, lang, country, fileExt, mime, author});
      }
      gT.setData(alColumns, alRows);
      ResultsFrame rf = new ResultsFrame(r.getCount().toString() + " " + rb.getString("gui.results"), gT, rb);
      pf.increaseProgress();
      pf.setVisible(false);
    }
    catch (MalformedURLException murlX)
    {
      murlX.printStackTrace();
    }
    finally
    {
      pf.setVisible(false);
      return null;
    }
  }
}