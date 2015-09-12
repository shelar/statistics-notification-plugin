package org.jenkins.plugins.statistics.util;

import hudson.FilePath;
import hudson.FilePath.FileCallable;
import hudson.remoting.VirtualChannel;
import hudson.util.DirScanner;
import hudson.util.FileVisitor;
import org.jenkins.plugins.statistics.model.StatsTestResult;
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

      try {
        currentWorkspace.act(new FileCallable<Void>() {
          private static final long serialVersionUID = 3L;

          public Void invoke(File f, VirtualChannel channel) {
            // TODO: Add for loop to check various report formats
            try {
              new DirScanner.Glob(StatsTestResult.TESTNG_REPORT_FILTER, null)
                  .scan(f, new FileVisitor() {
                    @Override
                    public void visit(File f, String relativePath) throws
                        IOException {
                      if (f.isFile()) {
                        LOGGER.log(Level.INFO, "Found file " + f.getName());

                        try {
                          JAXBContext cntx = JAXBContext.newInstance
                              (TestngReportSummaryModel.class);
                          Unmarshaller unm = cntx.createUnmarshaller();

                          TestngReportSummaryModel ts =
                              (TestngReportSummaryModel) unm.unmarshal(f);
                          result.setResult(ts.getTotal(), ts.getPassed(), ts
                              .getFailed(), ts.getSkipped());
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
    return result;
  }
}
