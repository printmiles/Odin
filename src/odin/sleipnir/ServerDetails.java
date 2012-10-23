/* Class name: ServerDetails
 * File name:  ServerDetails.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    24-Apr-2011 22:24:34
 * Modified:   24-Apr-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  24-Apr-2011 Initial build
 */

package odin.sleipnir;

import java.awt.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import odin.shared.ws.*;
import odin.shared.xml.*;

/**
 * Responsible for providing information about the server to which the
 * client connects.
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public class ServerDetails extends JInternalFrame
{
  private JEditorPane jepHelpText;
  private ResourceBundle rbUser;

  /**
   * This initialises the class contacts the server and obtains the standard information
   * from it. The results are then displayed on-screen.
   */
  public ServerDetails(ResourceBundle rb)
  {
    super("Odin", true, true, true, true);
    super.setLayer(1);
    rbUser = rb;
    JScrollPane jspJEP = new JScrollPane();
    jepHelpText = new JEditorPane();
    jepHelpText.setEditable(false);
    contactServer();
    jspJEP = new JScrollPane(jepHelpText);
    this.add(jspJEP, BorderLayout.CENTER);
    MainWindow parent = MainWindow.getMainWindow();
    parent.addToDesktop(this);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setSize(500, 400);
    this.setVisible(true);
  }

  public void contactServer()
  {
    try
    {
      OdinServer odin = MainWindow.getMainWindow().getServer();
      URL url = new URL(odin.getSleipnirServiceURL());
      // Qualified name for the service: Service URI, Service name
      QName qnSleipnir = new QName(odin.getPackageUrl(),"SleipnirServiceImplService");
      Service service = Service.create(url, qnSleipnir);
      ProgressForm worker = new ProgressForm(rbUser);
      worker.setProgress(4);
      worker.increaseProgress();
      worker.setStatus(rbUser.getString("gui.progress.contactServer"));
      SleipnirService sleipnir = service.getPort(SleipnirService.class);
      worker.increaseProgress();
      Response receiving = sleipnir.getServerInfo(InetAddress.getLocalHost().toString());
      worker.increaseProgress();
      worker.setStatus(rbUser.getString("gui.progress.sent"));
      int returnCode = receiving.getStatus().getCode().intValue();
      if (returnCode==200)
      {
        String content = rbUser.getString("gui.help.server.url.sleipnir") + " - \t" + receiving.getAppendix().getInfo().getSleipnirUri() + "\n" +
                         rbUser.getString("gui.help.server.url.odin") + " - \t" + receiving.getAppendix().getInfo().getOdinUri() + "\n" +
                         rbUser.getString("gui.help.server.name") + " - \t" + receiving.getAppendix().getInfo().getName() + "\n" +
                         rbUser.getString("gui.help.server.tel") + " - \t" + receiving.getAppendix().getInfo().getPhone() + "\n" +
                         rbUser.getString("gui.help.server.web") + " - \t" + receiving.getAppendix().getInfo().getWebsite() + "\n" +
                         rbUser.getString("gui.help.server.admin.name") + " - \t" + receiving.getAppendix().getInfo().getAdminName() + "\n" +
                         rbUser.getString("gui.help.server.admin.email") + " - \t" + receiving.getAppendix().getInfo().getEmail();
        jepHelpText.setText(content);
      }
      else
      {
        jepHelpText.setText(rbUser.getString("gui.upload.status.error"));
      }
      worker.increaseProgress();
      worker.setStatus(rbUser.getString("gui.progress.received"));
      worker.setVisible(false);
    }
    catch (MalformedURLException murlX)
    {
      murlX.printStackTrace();
    }
    catch (Exception x)
    {
      x.printStackTrace();
    }
  }
}