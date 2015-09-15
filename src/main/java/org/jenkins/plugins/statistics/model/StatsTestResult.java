package org.jenkins.plugins.statistics.model;

/**
 * Summary of the tests run in the current job.
 */
public class StatsTestResult {

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

  public void setResult(int totalCount, int passedCount, int failedCount, int
      skippedCount) {
    setTotal(totalCount);
    setPassed(passedCount);
    setFailed(failedCount);
    setSkipped(skippedCount);
  }

  public void addResult(int totalCount, int passedCount, int failedCount, int
      skippedCount) {
    setTotal(totalCount + getTotal());
    setPassed(passedCount + getPassed());
    setFailed(failedCount + getFailed());
    setSkipped(skippedCount + getSkipped());
  }

  @Override
  public String toString() {
    return "Test Result Summary: Total:" + getTotal() + " Passed:" +
        getPassed() + " Failed:" + getFailed()
        + " Skipped:" + getSkipped();
  }
}
