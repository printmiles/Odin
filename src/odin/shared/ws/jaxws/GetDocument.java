
package odin.shared.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getDocument", namespace = "http://sites.google.com/site/printmiles/Odin")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDocument", namespace = "http://sites.google.com/site/printmiles/Odin")
public class GetDocument {

    @XmlElement(name = "arg0", namespace = "http://sites.google.com/site/printmiles/Odin")
    private String arg0;

    /**
     * 
     * @return
     *     returns String
     */
    public String getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

}
