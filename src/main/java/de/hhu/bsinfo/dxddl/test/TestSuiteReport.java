package de.hhu.bsinfo.dxddl.test;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSuiteReport {
    public static TestSuiteReport fromTCReports(List<TestCaseReport> testCaseReports) {
        TestSuiteReport testSuiteReport = new TestSuiteReport();

        for (TestCaseReport testCaseReport : testCaseReports) {
            TCSummary summary = new TCSummary();
            summary.regularMin = testCaseReport.getRegularAccessMin();
            summary.regularMax = testCaseReport.getRegularAccessMax();
            summary.regularAvg = testCaseReport.getRegularAccessAvg();
            summary.directMin = testCaseReport.getDirectAccessMin();
            summary.directMax = testCaseReport.getDirectAccessMax();
            summary.directAvg = testCaseReport.getDirectAccessAvg();
            summary.diffMin = (double) testCaseReport.getRegularAccessMin() / testCaseReport.getDirectAccessMin();
            summary.diffMax = (double) testCaseReport.getRegularAccessMax() / testCaseReport.getDirectAccessMax();
            summary.diffAvg = (double) testCaseReport.getRegularAccessAvg() / testCaseReport.getDirectAccessAvg();
            testSuiteReport.report.put(testCaseReport.getTest(), summary);
        }

        return testSuiteReport;
    }

    private Map<Test, TCSummary> report = new HashMap<>();

    private TestSuiteReport() { }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("Test suite run summary:\n");

        for (Test test : report.keySet()) {
            stringBuffer.append("\n");
            stringBuffer.append(test.getName());
            stringBuffer.append("\n");
            stringBuffer.append("regular: ");
            stringBuffer.append(Stopwatch.format(report.get(test).regularMin));
            stringBuffer.append(" min\t\t");
            stringBuffer.append(Stopwatch.format(report.get(test).regularMax));
            stringBuffer.append(" max\t\t");
            stringBuffer.append(Stopwatch.format(report.get(test).regularAvg));
            stringBuffer.append(" avg\n");
            stringBuffer.append("direct:  ");
            stringBuffer.append(Stopwatch.format(report.get(test).directMin));
            stringBuffer.append(" min\t\t");
            stringBuffer.append(Stopwatch.format(report.get(test).directMax));
            stringBuffer.append(" max\t\t");
            stringBuffer.append(Stopwatch.format(report.get(test).directAvg));
            stringBuffer.append(" avg\n");
            stringBuffer.append("diff:    ");
            DecimalFormat factorFormat = new DecimalFormat("0.00");
            stringBuffer.append(factorFormat.format(report.get(test).diffMin));
            stringBuffer.append(" min\t\t");
            stringBuffer.append(factorFormat.format(report.get(test).diffMax));
            stringBuffer.append(" max\t\t");
            stringBuffer.append(factorFormat.format(report.get(test).diffAvg));
            stringBuffer.append(" avg\n");
        }

        return stringBuffer.toString();
    }

    private static class TCSummary {
        long regularMin;
        long regularMax;
        long regularAvg;
        long directMin;
        long directMax;
        long directAvg;
        double diffMin;
        double diffMax;
        double diffAvg;
    }
}
