package org.jenkins.plugins.statistics.model.report;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "testsuite")
@XmlAccessorType(XmlAccessType.FIELD)
public class JunitReportAndRspecJunitReportSummaryModel {
  @XmlAttribute
  private int failures;

  @XmlAttribute
  private int errors;

  @XmlAttribute
  private int skipped;

  @XmlAttribute
  private int tests;

  public int getFailed() { return failures + errors; }

  public int getPassed() { return tests - (failures + errors + skipped); }

  public int getSkipped() { return skipped; }

  public int getTotal() { return tests; }


}