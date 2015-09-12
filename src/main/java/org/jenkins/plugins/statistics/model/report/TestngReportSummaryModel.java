package org.jenkins.plugins.statistics.model.report;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "testng-results")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestngReportSummaryModel {
    @XmlAttribute
    private int failed;

    @XmlAttribute
    private int passed;

    @XmlAttribute
    private int skipped;

    @XmlAttribute
    private int total;


    public int getFailed() {
        return failed;
    }

    public int getPassed() {
        return passed;
    }

    public int getSkipped() {
        return skipped;
    }

    public int getTotal() {
        return total;
    }

}
