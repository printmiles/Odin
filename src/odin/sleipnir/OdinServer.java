/* Class name: OdinServer
 * File name:  OdinServer.java
 * Project:    Open Document and Information Network (Odin)
 * Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    25-Apr-2011 18:49:00
 * Modified:   25-Apr-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  25-Apr-2011 Initial code
 */

package odin.sleipnir;


/**
 * This class is responsible for providing details about the Odin server we are
 * connecting to to the rest of Sleipnir.
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class OdinServer
{
  private static String ipAddress = "127.0.0.1";
  private static String packageUrl = "http://ws.odin.odin/";
  private static String port = "";
  private static String protocol = "http://";
  private static String serviceWsdl = "/Sleipnir?wsdl";

  public static String getIpAddress() { return ipAddress; }
  public static String getPackageUrl() { return packageUrl; }
  
  public static String getSleipnirServiceURL()
  {
    return protocol + ipAddress + port + serviceWsdl;
  }

  public static void setIpAddress(String newIpAddress) { ipAddress = newIpAddress; }
}
