/* Class name: Policy
 * File name:  Policy.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    05-Jun-2011 21:47:13
 * Modified:   05-Jun-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  05-Jun-2011 Initial build
 */

package odin.shared.ws;

/**
 * Javadoc to follow.
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public enum Policy
{
  ZIP_REJECT_ALL(1),
  ZIP_STORE_ALL(2),
  ZIP_EXTRACT_OR_REJECT(3),
  ZIP_EXTRACT_OR_STORE(4),
  XML_REJECT(5),
  XML_STORE_ALL(6),
  XML_DEMAND_NAMESPACES(7),
  TRUST_REJECT(8),
  TRUST_ALLOW(9);
  
  private int policy;
  
  private Policy(int p) { policy = p; }
  public int getCode() { return policy; }
}
