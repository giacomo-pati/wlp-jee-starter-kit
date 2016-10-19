package model;

import util.BaseObject;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"operation", "result"})
public class CalculationResult extends BaseObject {

    @XmlElement(name = "opertion", required = true)
    public final String operation;

    @XmlElement(name = "result", required = true)
    public final int result;

    /**
     * Constructor for instantiation via JAXB
     */
    @SuppressWarnings("unused")
    private CalculationResult() {
        this(null, 0);
    }

    public CalculationResult(String operation, int result) {
        this.operation = operation;
        this.result = result;
    }

}