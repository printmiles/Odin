/* Class name: SleipnirService
 * File name:  SleipnirService.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    25-Mar-2011 13:01:59
 * Modified:   11-Apr-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.003  05-Jun-2011 Added getBarcodeUrl, getZipPolicy and getXmlPolicy methods
 * 0.002  11-Apr-2011 Added methods and annotations for web services
 * 0.001  25-Mar-2011 Initial build
 */

package odin.shared.ws;

import java.util.concurrent.Future;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.xml.ws.BindingType;
import odin.shared.xml.Basic;
import odin.shared.xml.Document;
import odin.shared.xml.Response;
import odin.shared.xml.Results;
import odin.shared.xml.Search;

/**
 * This interface is specifically used for SleipnirService clients connecting to this server.
 * It uses the Document style of SOAP to ensure compatibility with a wide range of possible endpoints.
 * @version 0.003
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

@WebService(name="SleipnirService",
            targetNamespace="http://sites.google.com/site/printmiles/Odin")

@SOAPBinding(style = Style.DOCUMENT,
             use = Use.LITERAL,
             parameterStyle = ParameterStyle.WRAPPED)

@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_MTOM_BINDING)

public interface SleipnirService
{
  @WebMethod
  @WebResult(targetNamespace="http://sites.google.com/site/printmiles/Odin")
  public Response upload(@WebParam(targetNamespace="http://sites.google.com/site/printmiles/Odin",
                                    mode=WebParam.Mode.IN)
                          Document uploadedDoc);

  @WebMethod
  @WebResult(targetNamespace="http://sites.google.com/site/printmiles/Odin")
  public Document getDocument(@WebParam(targetNamespace="http://sites.google.com/site/printmiles/Odin",
                                          mode=WebParam.Mode.IN)
                               String guid);

  @WebMethod
  @WebResult(targetNamespace="http://sites.google.com/site/printmiles/Odin")
  public Response getServerInfo(@WebParam(targetNamespace="http://sites.google.com/site/printmiles/Odin",
                                            mode=WebParam.Mode.IN)
                                  String clientIp);

  @WebMethod
  @WebResult(targetNamespace="http://sites.google.com/site/printmiles/Odin")
  public Results basicSearch(@WebParam(targetNamespace="http://sites.google.com/site/printmiles/Odin", mode=WebParam.Mode.IN) Basic searchCriteria);
  
  @WebMethod
  @WebResult(targetNamespace="http://sites.google.com/site/printmiles/Odin")
  public Results search(@WebParam(targetNamespace="http://sites.google.com/site/printmiles/Odin", mode=WebParam.Mode.IN) Search searchCriteria);
  
  @WebMethod
  @WebResult(targetNamespace="http://sites.google.com/site/printmiles/Odin")
  public int getZipPolicy(@WebParam(targetNamespace="http://sites.google.com/site/printmiles/Odin",
                                      mode=WebParam.Mode.IN)
                           String clientIp);
  
  @WebMethod
  @WebResult(targetNamespace="http://sites.google.com/site/printmiles/Odin")
  public int getXmlPolicy(@WebParam(targetNamespace="http://sites.google.com/site/printmiles/Odin",
                                            mode=WebParam.Mode.IN)
                                  String clientIp);
  
  @WebMethod
  @WebResult(targetNamespace="http://sites.google.com/site/printmiles/Odin")
  public String getBarcodeUrl(@WebParam(targetNamespace="http://sites.google.com/site/printmiles/Odin",
                                            mode=WebParam.Mode.IN)
                                  String clientIp);
  
  @WebMethod
  @WebResult(targetNamespace="http://sites.google.com/site/printmiles/Odin")
  public Document getDocumentMetadata(@WebParam(targetNamespace="http://sites.google.com/site/printmiles/Odin",
                                          mode=WebParam.Mode.IN)
                                         String guid);
}