package de.hhu.bsinfo.dxddl.test.suites;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hhu.bsinfo.dxddl.test.AbstractSuite;
import de.hhu.bsinfo.dxddl.test.Test;
import de.hhu.bsinfo.dxddl.test.TestMetadata;
import de.hhu.bsinfo.dxddl.test.api.DirectTestChunk1;
import de.hhu.bsinfo.dxddl.test.api.TestChunk1;
import de.hhu.bsinfo.dxddl.test.cases.TestCase1;
import de.hhu.bsinfo.dxram.chunk.ChunkLocalService;
import de.hhu.bsinfo.dxram.chunk.ChunkService;

public class TestSuite1 extends AbstractSuite {

    private static final Logger LOGGER = LogManager.getFormatterLogger(TestSuite1.class);

    public TestSuite1(ChunkService chunkService, ChunkLocalService chunkLocalService) {
        super(chunkService, chunkLocalService);
    }

    @Override
    public String getName() {
        return "Test suite 1";
    }

    @Override
    public TestMetadata prepare(short nodeId, int numOfRuns) {
        LOGGER.info("Prepare test suite...");

        TestMetadata testMetadata = new TestMetadata(nodeId, numOfRuns);

        final int numOfChunks1 = 1000000;
        final int numOfOps1 = 1000000;
        TestCase1 testCase1 = new TestCase1();
        addTestCase(testCase1);

        long startCID = regularOps.getReserveLocal().reserve(numOfChunks1)[0];
        testMetadata.addTCMetadata(
                testCase1, numOfChunks1, numOfOps1, startCID, DirectTestChunk1.reserve(numOfChunks1));

        return testMetadata;
    }

    @Override
    public void load(TestMetadata testMetadata) {
        LOGGER.info("Load test suite...");

        Test testCase1 = tests.get(0);
        int n = testMetadata.getNumberOfChunks(testCase1);
        long startCID = testMetadata.getStartID(testCase1);
        final long[] directIDs = testMetadata.getDirectIDs(testCase1);

        //long[] tmp = new long[n];
        //int[] sizes = new int[n];
        TestChunk1 testChunk1 = new TestChunk1();
        for (int i = 0; i < n; i++) {
            testChunk1.setID(startCID + i);
            testChunk1.setNum(i);
            regularOps.getCreateReservedLocal().create(testChunk1);
            regularOps.getPut().put(testChunk1);
        }
        DirectTestChunk1.createReserved(directIDs);
        for (int i = 0; i < n; i++) {
            DirectTestChunk1.setNum(directIDs[i], i);
        }
    }
}
