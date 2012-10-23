/* Class name: UploadWorker
 * File name:  UploadWorker.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    17-Apr-2011 17:03:33
 * Modified:   17-Apr-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  17-Apr-2011 Initial build
 */

package odin.sleipnir;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
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

public class UploadWorker extends SwingWorker
{
  private ProgressForm worker;
  private Upload parent;
  private Document sending;
  private Response receiving;
  private ResourceBundle rbUser;

  public UploadWorker(ProgressForm pf, Document d, Upload u, ResourceBundle rb)
  {
    rbUser = rb;
    worker = pf;
    parent = u;
    sending = d;
  }

  /**
   * Uploads the document
   * @return Nothing
   */
  public Void doInBackground()
  {
    try
    {
      OdinServer odin = MainWindow.getMainWindow().getServer();
      URL url = new URL(odin.getSleipnirServiceURL());
      // Qualified name for the service: Service URI, Service name
      QName qnSleipnir = new QName(odin.getPackageUrl(),"SleipnirServiceImplService");
      Service service = Service.create(url, qnSleipnir);
      worker.increaseProgress();
      worker.setStatus(rbUser.getString("gui.progress.contactServer"));
      SleipnirService sleipnir = service.getPort(SleipnirService.class);
      worker.increaseProgress();
      worker.setStatus(rbUser.getString("gui.progress.send"));
      receiving = sleipnir.upload(sending);
      worker.increaseProgress();
      worker.setStatus(rbUser.getString("gui.progress.sent"));
      int returnCode = receiving.getStatus().getCode().intValue();
      worker.increaseProgress();
      worker.setStatus(rbUser.getString("gui.progress.received"));
      if (returnCode == 201)
      {
        // Worked ok. Put the guid on screen
        parent.setStatus(rbUser.getString("gui.upload.status.ok") + " " + receiving.getAppendix().getGuid());
      }
      else
      {
        parent.setStatus(rbUser.getString("gui.upload.status.error") + " (" + returnCode + ") " + receiving.getStatus().getMessage());
      }
    }
    catch (MalformedURLException murlX)
    {
      murlX.printStackTrace();
    }
    catch (Exception x)
    {
      x.printStackTrace();
    }
    finally
    {
      worker.setVisible(false);
      return null;
    }
  }
}