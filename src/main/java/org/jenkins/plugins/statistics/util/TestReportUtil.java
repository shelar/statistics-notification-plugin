package org.jenkins.plugins.statistics.util;

import hudson.FilePath;
import hudson.FilePath.FileCallable;
import hudson.remoting.VirtualChannel;
import hudson.util.DirScanner;
import hudson.util.FileVisitor;
import org.jenkins.plugins.statistics.model.StatsTestResult;
import org.jenkins.plugins.statistics.model.report.JunitReportAndRspecJunitReportSummaryModel;
import org.jenkins.plugins.statistics.model.report.TestngReportSummaryModel;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains methods for processing Test Report for the build.
 */
public class TestReportUtil {

  private static final Logger LOGGER = Logger.getLogger(TestReportUtil.class
      .getName());
  private static String TESTNG_REPORT_FILTER = "**/testng-results.xml";
  private static String JUNIT_ANT_REPORT_FILTER =
      "**/TEST-*.xml";
  private static String RSPEC_JUNIT_REPORT_FILTER = "**/rspec.xml";

  /**
   * Finds the <code>currentWorkspace</code> for supported test reports and
   * processes the tests result.
   *
   * @param currentWorkspace
   * @return
   */
  public static StatsTestResult getTestResults(FilePath currentWorkspace) {
    final StatsTestResult result = new StatsTestResult();

    if (null != currentWorkspace) {
      LOGGER.log(Level.INFO, "Searching files in " + currentWorkspace);

      boolean processedReport = processTestngReport(currentWorkspace, result);
      if (!processedReport) {
        processedReport = processJunitReport(currentWorkspace, result);
      }
      if (!processedReport) {
        processRspecReport(currentWorkspace, result);
      }
    }
    return result;
  }

  /**
   * Finds the <code>currentWorkspace</code> for TestNG report and
   * processes the tests result.
   *
   * @param currentWorkspace FilePath to the current workspace.
   * @param testResult Holds the results of the test.
   * @return A boolean indicating whether TestNG report was processed.
   */
  private static boolean processTestngReport(FilePath currentWorkspace,
                                             final StatsTestResult testResult) {
    final boolean[] processedResult = new boolean[1];
    if (null != currentWorkspace) {
      LOGGER.log(Level.INFO, "Searching TestNG report in " + currentWorkspace);

      try {
        currentWorkspace.act(new FileCallable<Void>() {

          public Void invoke(File f, VirtualChannel channel) {
            try {
              new DirScanner.Glob(TestReportUtil.TESTNG_REPORT_FILTER, null)
                  .scan(f, new FileVisitor() {
                    @Override
                    public void visit(File f, String relativePath) throws
                        IOException {
                      if (f.isFile()) {
                        try {
                          JAXBContext cntx = JAXBContext.newInstance
                              (TestngReportSummaryModel.class);
                          Unmarshaller unm = cntx.createUnmarshaller();

                          TestngReportSummaryModel ts =
                              (TestngReportSummaryModel) unm.unmarshal(f);
                          testResult.setResult(ts.getTotal(), ts.getPassed(), ts
                              .getFailed(), ts.getSkipped());
                          processedResult[0] = true;
                          LOGGER.log(Level.INFO, "Parsed file " + f.getName());
                        } catch (JAXBException e) {
                          LOGGER.log(Level.WARNING, "Unable to parse report " +
                              "file: " + f.getName());
                        }
                      }
                    }
                  });
            } catch (IOException e) {
              LOGGER.log(Level.WARNING,
                  "An error occurred when reading the workspace.");
            }
            return null;
          }
        });
      } catch (IOException e) {
        LOGGER.log(Level.WARNING,
            "An error occurred when reading the workspace: " + e.getMessage());
      } catch (InterruptedException e) {
        LOGGER.log(Level.WARNING,
            "An interruption occurred when reading the workspace: "
                + e.getMessage());
      }
    }
    return processedResult[0];
  }

  /**
   * Finds the <code>currentWorkspace</code> for JUnit style report and
   * processes the tests result.
   *
   * @param junitFileFilter Ant style pattern to match file.
   * @param currentWorkspace FilePath to the current workspace.
   * @param testResult Holds the results of the test.
   * @return A boolean indicating whether JUnit report was processed.
   */
  private static boolean processJunitStyleReport(final String junitFileFilter,
                                            FilePath
                                                currentWorkspace,
                                             final StatsTestResult testResult) {
    final boolean[] processedResult = new boolean[1];
    if (null != currentWorkspace) {
      LOGGER.log(Level.INFO, "Searching Junit report in " + currentWorkspace);
      try {
        currentWorkspace.act(new FileCallable<Void>() {

          public Void invoke(File f, VirtualChannel channel) {
            try {
              new DirScanner.Glob(junitFileFilter, null)
                  .scan(f, new FileVisitor() {
                    @Override
                    public void visit(File f, String relativePath) throws
                        IOException {
                      if (f.isFile()) {
                        try {
                          JAXBContext cntx = JAXBContext.newInstance
                              (JunitReportAndRspecJunitReportSummaryModel.class);
                          Unmarshaller unm = cntx.createUnmarshaller();

                          JunitReportAndRspecJunitReportSummaryModel ts =
                              (JunitReportAndRspecJunitReportSummaryModel) unm.unmarshal(f);
                          testResult.addResult(ts.getTotal(), ts.getPassed()
                              , ts.getFailed(), ts.getSkipped());
                          processedResult[0] = true;
                          LOGGER.log(Level.INFO, "Parsed file " + f.getName());
                        } catch (JAXBException e) {
                          LOGGER.log(Level.WARNING, "Unable to parse report " +
                              "file: " + f.getName());
                        }
                      }
                    }
                  });
            } catch (IOException e) {
              LOGGER.log(Level.WARNING,
                  "An error occurred when reading the workspace.");
            }
            return null;
          }
        });
      } catch (IOException e) {
        LOGGER.log(Level.WARNING,
            "An error occurred when reading the workspace: " + e.getMessage());
      } catch (InterruptedException e) {
        LOGGER.log(Level.WARNING,
            "An interruption occurred when reading the workspace: "
                + e.getMessage());
      }
    }
    return processedResult[0];
  }

  /**
   * Finds the <code>currentWorkspace</code> for JUnit report and
   * processes the tests result.
   *
   * @param currentWorkspace FilePath to the current workspace.
   * @param testResult Holds the results of the test.
   * @return A boolean indicating whether JUnit report was processed.
   */
  private static boolean processJunitReport(FilePath
                                                currentWorkspace,
                                            final StatsTestResult testResult)
  {
    return processJunitStyleReport(TestReportUtil.JUNIT_ANT_REPORT_FILTER,
        currentWorkspace,
        testResult);
  }

  /**
   * Finds the <code>currentWorkspace</code> for JUnit report and
   * processes the tests result.
   *
   * @param currentWorkspace FilePath to the current workspace.
   * @param testResult Holds the results of the test.
   * @return A boolean indicating whether JUnit report was processed.
   */
  private static boolean processRspecReport(FilePath
                                                currentWorkspace,
                                            final StatsTestResult testResult)
  {
    return processJunitStyleReport(TestReportUtil.RSPEC_JUNIT_REPORT_FILTER,
        currentWorkspace,
        testResult);
  }
}

