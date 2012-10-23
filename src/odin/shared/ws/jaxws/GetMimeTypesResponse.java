
package odin.shared.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getMimeTypesResponse", namespace = "http://sites.google.com/site/printmiles/Odin")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getMimeTypesResponse", namespace = "http://sites.google.com/site/printmiles/Odin")
public class GetMimeTypesResponse {

    @XmlElement(name = "return", namespace = "http://sites.google.com/site/printmiles/Odin")
    private odin.shared.xml.MimeTypes _return;

    /**
     * 
     * @return
     *     returns MimeTypes
     */
    public odin.shared.xml.MimeTypes getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(odin.shared.xml.MimeTypes _return) {
        this._return = _return;
    }

}
