/* Class name: RunSleipnir
 * File name:  RunSleipnir.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    16-Jul-2010 18:29:13
 * Modified:   16-Jul-2010
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.000  16-Jul-2010 Initial build
 */

package odin.sleipnir;
import java.util.*;

/**
 * Javadoc to follow.
 * @version 1.000
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class RunSleipnir
{
  public static void main(String args[])
  {
    Hashtable<String,String> htArgs = new Hashtable<String,String>();
    for (String s: args)
    {
      StringTokenizer st = new StringTokenizer(s,"=");
      String param = st.nextToken();
      String value = st.nextToken();
      htArgs.put(param.toLowerCase(), value.toLowerCase());
    }
    SplashScreen splashScreen = new SplashScreen(3);
    splashScreen.increaseProgress("Obtaining Locale");
    String strCountry = System.getProperty("user.country");
    String strLanguage = System.getProperty("user.language");
    Locale homeLocale = new Locale(strLanguage, strCountry);
    if (htArgs.containsKey("language") && !(htArgs.containsKey("country")))
    {
      ArrayList alLanguages = new ArrayList();
      for (String s : Locale.getISOLanguages())
      { alLanguages.add(s); }
      homeLocale = new Locale(strLanguage);
      String tempLang = htArgs.get("language");
      if (alLanguages.contains(tempLang))
      {
        strLanguage = tempLang;
        homeLocale = new Locale(strLanguage);
      }
    }
    if (htArgs.containsKey("language") && htArgs.containsKey("country"))
    {
      ArrayList alCountries = new ArrayList();
      ArrayList alLanguages = new ArrayList();
      for (String s : Locale.getISOCountries())
      { alCountries.add(s); }
      for (String s : Locale.getISOLanguages())
      { alLanguages.add(s); }
      String tempCtry = htArgs.get("country").toUpperCase();
      String tempLang = htArgs.get("language");
      if (alCountries.contains(tempCtry) && alLanguages.contains(tempLang))
      {
        strCountry = tempCtry;
        strLanguage = tempLang;
        homeLocale = new Locale(strLanguage, strCountry);
      }
    }
    ResourceBundle resourceBundle = ResourceBundle.getBundle("odin.sleipnir.Sleipnir", homeLocale);
    splashScreen.increaseProgress(resourceBundle.getString("load.splash.1"));
    if (htArgs.get("home")==null)
    {
      try
      {
        htArgs.put("home", java.net.InetAddress.getLocalHost().getHostAddress().toString());
      }
      catch (Exception x)
      {
        htArgs.put("home", "127.0.0.1");
      }
    }
    MainWindow mw = new MainWindow(splashScreen, resourceBundle, homeLocale, htArgs.get("home"));
    if (htArgs.containsKey("guid"))
    {
      mw.findDoc(htArgs.get("guid"));
    }
  }
}