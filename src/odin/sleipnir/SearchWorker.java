/* Class name: SearchWorker
 * File name:  SearchWorker.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    03-May-2011 13:23:26
 * Modified:   03-May-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  03-May-2011 Initial build
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
 * JavaDoc to follow.
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public class SearchWorker extends SwingWorker
{
  private ProgressForm pf;
  private odin.shared.xml.Search.Document criteria;
  private ResourceBundle rb;

  public SearchWorker(ProgressForm pf, odin.shared.xml.Search.Document sd, ResourceBundle rb)
  {
    this.pf = pf;
    this.criteria = sd;
    this.rb = rb;
  }

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
      odin.shared.xml.Search searchDoc = sharedFactory.createSearch();
      searchDoc.setUsername(System.getProperty("user.name"));
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
        searchDoc.setIpaddress(localaddress);
      }
      searchDoc.setDocument(criteria);
      pf.setStatus(rb.getString("gui.progress.search.wait"));
      pf.increaseProgress();
      // Send document to the server and wait for a response
      odin.shared.xml.Results r = sleipnir.search(searchDoc);
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
      for (Results.Document d : r.getDocument())
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