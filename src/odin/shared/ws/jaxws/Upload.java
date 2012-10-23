
package odin.shared.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "upload", namespace = "http://sites.google.com/site/printmiles/Odin")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "upload", namespace = "http://sites.google.com/site/printmiles/Odin")
public class Upload {

    @XmlElement(name = "arg0", namespace = "http://sites.google.com/site/printmiles/Odin")
    private odin.shared.xml.Document arg0;

    /**
     * 
     * @return
     *     returns Document
     */
    public odin.shared.xml.Document getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(odin.shared.xml.Document arg0) {
        this.arg0 = arg0;
    }

}
