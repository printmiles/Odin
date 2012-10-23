
package odin.shared.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "search", namespace = "http://sites.google.com/site/printmiles/Odin")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "search", namespace = "http://sites.google.com/site/printmiles/Odin")
public class Search {

    @XmlElement(name = "arg0", namespace = "http://sites.google.com/site/printmiles/Odin")
    private odin.shared.xml.Search arg0;

    /**
     * 
     * @return
     *     returns Search
     */
    public odin.shared.xml.Search getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(odin.shared.xml.Search arg0) {
        this.arg0 = arg0;
    }

}
