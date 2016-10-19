package model;

import util.BaseObject;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"application", "version"})
public class About extends BaseObject {

    @XmlElement(name = "application")
    public final String application;

    @XmlElement(name = "version")
    public final String version;

    /**
     * Constructor for instantiation via JAXB
     */
    @SuppressWarnings("unused")
    private About() {
        this(null, null);
    }

    public About(String version, String application) {
        this.application = application;
        this.version = version;
    }

}
