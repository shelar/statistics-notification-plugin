package org.jenkins.plugins.statistics.model;

/**
 * Summary of the tests run in the current job.
 */
public class StatsTestResult {

  static public String TESTNG_REPORT_FILTER = "**/testng-results.xml";
  static public String JUNIT_ANT_REPORT_FILTER = "**/TESTS-TestSuites.xml";

  // Number of tests failed.
  private int failed;

  // Number of tests passed.
  private int passed;

  // Number of tests skipped.
  private int skipped;

  // Total number of tests run.
  private int total;

  public int getFailed() {
    return failed;
  }

  private void setFailed(int failed) {
    this.failed = failed;
  }

  public int getPassed() {
    return passed;
  }

  private void setPassed(int passed) {
    this.passed = passed;
  }

  public int getSkipped() {
    return skipped;
  }

  private void setSkipped(int skipped) {
    this.skipped = skipped;
  }

  public int getTotal() {
    return total;
  }

  private void setTotal(int total) {
    this.total = total;
  }

  public void setResult(int total, int passed, int failed, int skipped) {
    setTotal(total);
    setPassed(passed);
    setFailed(failed);
    setSkipped(skipped);
  }

  @Override
  public String toString() {
    return "Test Result Summary: Total:" + getTotal() + " Passed:" +
        getPassed() + " Failed:" + getFailed()
        + " Skipped:" + getSkipped();
  }
}
