
package odin.shared.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getServerInfoResponse", namespace = "http://sites.google.com/site/printmiles/Odin")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getServerInfoResponse", namespace = "http://sites.google.com/site/printmiles/Odin")
public class GetServerInfoResponse {

    @XmlElement(name = "return", namespace = "http://sites.google.com/site/printmiles/Odin")
    private odin.shared.xml.Response _return;

    /**
     * 
     * @return
     *     returns Response
     */
    public odin.shared.xml.Response getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(odin.shared.xml.Response _return) {
        this._return = _return;
    }

}
