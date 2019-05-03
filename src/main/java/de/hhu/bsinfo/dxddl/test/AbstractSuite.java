package de.hhu.bsinfo.dxddl.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hhu.bsinfo.dxram.chunk.ChunkLocalService;
import de.hhu.bsinfo.dxram.chunk.ChunkService;

public abstract class AbstractSuite implements Suite {

    private static final Logger LOGGER = LogManager.getFormatterLogger(AbstractSuite.class);

    private TestMetadata testMetadata;
    protected final List<Test> tests = new ArrayList<>();
    private final List<TestCaseReport> testCaseReports = new ArrayList<>();
    protected final RegularOps regularOps;

    protected void addTestCase(Test tc) {
        tests.add(tc);
    }

    public AbstractSuite(ChunkService chunkService, ChunkLocalService chunkLocalService) {
        regularOps = new RegularOps(chunkService, chunkLocalService);
    }

    @Override
    public void start(short nodeId, int numOfRuns) {
        LOGGER.info("Start test suite \"%s\"", getName());
        testMetadata = prepare(nodeId, numOfRuns);
        load(testMetadata);
        run();
    }

    @Override
    public void run() {
        LOGGER.info("Perform suite run...");
        for (Test test : tests) {
            test.prepare(testMetadata);
            test.run(regularOps);
            testCaseReports.add(test.report());
        }
    }

    @Override
    public TestSuiteReport createReport() {
        LOGGER.info("Create suite report...");
        return TestSuiteReport.fromTCReports(testCaseReports);
    }
}
