/* Class name: Repository
 * File name:  Repository.java
 * Project:    Open Document and Information Network (Odin)
 * Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    30-Aug-2011 12:45:45
 * Modified:   30-Aug-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  30-Aug-2011 Initial build
 */
package odin.odin.repository;

import odin.shared.xml.*;

/**
 * Used to define the methods for interacting with a repository. Classes implementing
 * this interface provide the details for a particular back-end repository type.
 * This interface is used by the SEI to work with an unknown back-end type which
 * is defined in the server administrator's preferences.
 * <p>Current implementations of the back-end include:
 * <ul>
 *   <li>XML (using Saxon/XQJ)</li>
 *   <li>Apache Derby</li>
 * </ul>
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public interface Repository
{
  public Results basicSearch(Basic basicSearchWords);
  public Document getDocument(String guid);
  public Document getDocumentMetadata(String guid);
  public Results search(Search advancedSearchWords);
  public Response storeDocument(Document uploadedDocument);
}
