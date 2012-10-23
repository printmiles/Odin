/* Class name: ServicePublisher
 * File name:  ServicePublisher.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    12-Apr-2011 13:25:11
 * Modified:   12-Apr-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  12-Apr-2011 Initial build
 */

package odin.odin.ws;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.Endpoint;
import odin.odin.object.logging.LoggerFactory;

/**
 * JavaDoc to follow.
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public class ServicePublisher
{
  private static String hostName;
  private static final int SLEIPNIR_TCP_PORT = 13440;
  private static final int ODIN_TCP_PORT = 10584;
  private static final String ODIN_SERVICE_NAME = "Odin";
  private static final String SLEIPNIR_SERVICE_NAME = "Sleipnir";
  private static final int NUMBER_OF_THREADS = 10;
  private static final String SLEIPNIR_WEB = "sleipnir.html";
  private static final int HTTP_PORT = 80;
  private static String odinUrl;
  private static String sleipnirUrl;
  private Logger log;

  public ServicePublisher()
  {
    log = LoggerFactory.getLogger(getClass().getName()); 
    // TODO Allow the admin to change the ports and sleipnirUrl we're binding to
    try
    {
      hostName = InetAddress.getLocalHost().getHostAddress().toString();
    }
    catch (UnknownHostException uhX)
    {
      log.throwing("ServicePublisher", "init()", uhX);
    }
  }

  public String publishOdin()
  {
    odinUrl = "http://"+hostName+":"+ODIN_TCP_PORT+"/"+ODIN_SERVICE_NAME;
    // 1st argument is the URI where we publish the service
    // 2nd argument is the implementing class of the web service (SIB)
//    Endpoint odinEndpoint = Endpoint.create(new OdinServiceImpl());
//    odinEndpoint.setExecutor(Executors.newFixedThreadPool(NUMBER_OF_THREADS));
//    odinEndpoint.publish(sleipnirUrl);
    log.log(Level.CONFIG, "Odin service published to: {0} at: {1}", new Object[]{odinUrl, java.util.Calendar.getInstance().getTime().toString()});
    System.out.println("Odin service published to: " + odinUrl + " at: " + java.util.Calendar.getInstance().getTime().toString());
    return odinUrl;
  }

  public String publishSleipnir()
  {
    sleipnirUrl = "http://"+hostName+":"+SLEIPNIR_TCP_PORT+"/"+SLEIPNIR_SERVICE_NAME;
    // 1st argument is the URI where we publish the service
    // 2nd argument is the implementing class of the web service (SIB)
    Endpoint sleipnirEndpoint = Endpoint.create(new SleipnirServiceImpl());
//    sleipnirEndpoint.setExecutor(Executors.newFixedThreadPool(NUMBER_OF_THREADS));
    sleipnirEndpoint.publish(sleipnirUrl);
    log.log(Level.CONFIG, "Sleipnir service published to: {0} at: {1}", new Object[]{sleipnirUrl, java.util.Calendar.getInstance().getTime().toString()});
    System.out.println("Sleipnir service published to: " + sleipnirUrl + " at: " + java.util.Calendar.getInstance().getTime().toString());
    return sleipnirUrl;
  }

  public static String getOdinURL()
  {
    return odinUrl;
  }

  public static String getSleipnirURL()
  {
    return sleipnirUrl;
  }

  public static int getNumberOfThreads()
  {
    return NUMBER_OF_THREADS;
  }
  
  public static String getSleipnirWebpage()
  {
    return "http://" + hostName + ":" + HTTP_PORT + "/" + SLEIPNIR_WEB;
  }
}