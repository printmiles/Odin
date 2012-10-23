/* Class name: OdinService
 * File name:  OdinService.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    25-Mar-2011 13:11:45
 * Modified:   25-Mar-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  25-Mar-2011 Initial build
 */

package odin.odin.ws;

import javax.jws.*;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.*;

/**
 * JavaDoc to follow.
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

@WebService(name="OdinService", targetNamespace="http://sites.google.com/site/printmiles/Odin")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
public interface OdinService
{
  @WebMethod
  @WebResult(targetNamespace="http://sites.google.com/site/printmiles/Odin")
  public Integer getTrust(@WebParam(targetNamespace="http://sites.google.com/site/printmiles/Odin",
                                     mode=WebParam.Mode.IN)
                           String serverName);

  @WebMethod
  public void getRules();

  @WebMethod
  public void getRule(@WebParam(targetNamespace="http://sites.google.com/site/printmiles/Odin",
                                     mode=WebParam.Mode.IN)
                       String ruleName);
}
