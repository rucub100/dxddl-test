package de.hhu.bsinfo.dxddl.test.suites;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hhu.bsinfo.dxddl.test.AbstractSuite;
import de.hhu.bsinfo.dxddl.test.Test;
import de.hhu.bsinfo.dxddl.test.TestMetadata;
import de.hhu.bsinfo.dxddl.test.api.DirectTestChunk1;
import de.hhu.bsinfo.dxddl.test.api.DirectTestChunk2;
import de.hhu.bsinfo.dxddl.test.api.DirectTestChunk3;
import de.hhu.bsinfo.dxddl.test.api.DirectTestChunk4;
import de.hhu.bsinfo.dxddl.test.api.DirectTestChunk5;
import de.hhu.bsinfo.dxddl.test.api.DirectTestChunk6;
import de.hhu.bsinfo.dxddl.test.api.TestChunk1;
import de.hhu.bsinfo.dxddl.test.api.TestChunk2;
import de.hhu.bsinfo.dxddl.test.api.TestChunk3;
import de.hhu.bsinfo.dxddl.test.api.TestChunk4;
import de.hhu.bsinfo.dxddl.test.api.TestChunk5;
import de.hhu.bsinfo.dxddl.test.api.TestChunk6;
import de.hhu.bsinfo.dxddl.test.cases.TestCase10;
import de.hhu.bsinfo.dxddl.test.cases.TestCase11;
import de.hhu.bsinfo.dxddl.test.cases.TestCase12;
import de.hhu.bsinfo.dxddl.test.cases.TestCase7;
import de.hhu.bsinfo.dxddl.test.cases.TestCase8;
import de.hhu.bsinfo.dxddl.test.cases.TestCase9;
import de.hhu.bsinfo.dxram.chunk.ChunkLocalService;
import de.hhu.bsinfo.dxram.chunk.ChunkService;

/**
 * Test put with different chunk sizes via addresses instead of CIDs.
 **/
public class TestSuite4 extends AbstractSuite {

    private static final Logger LOGGER = LogManager.getFormatterLogger(TestSuite4.class);

    public TestSuite4(ChunkService chunkService, ChunkLocalService chunkLocalService) {
        super(chunkService, chunkLocalService);
    }

    @Override
    public String getName() {
        return "Test suite 4";
    }

    @Override
    public TestMetadata prepare(short nodeId, int numOfRuns) {
        LOGGER.info("Prepare test suite...");

        TestMetadata testMetadata = new TestMetadata(nodeId, numOfRuns);

        // test case 7
        final int numOfChunks1 = 1000;
        final int numOfOps1 = 1000000;
        TestCase7 testCase1 = new TestCase7();
        addTestCase(testCase1);

        long[] tmp = new long[numOfChunks1];
        regularOps.getCreateLocal().create(tmp, tmp.length, new TestChunk1().sizeofObject(), true);
        long startCID = tmp[0];
        testMetadata.addTCMetadata(
                testCase1,
                numOfChunks1,
                numOfOps1,
                startCID,
                DirectTestChunk1.create(numOfChunks1));

        // test case 8
        final int numOfChunks2 = 1000;
        final int numOfOps2 = 1000000;
        TestCase8 testCase2 = new TestCase8();
        addTestCase(testCase2);

        tmp = new long[numOfChunks2];
        regularOps.getCreateLocal().create(tmp, tmp.length, new TestChunk2().sizeofObject(), true);
        startCID = tmp[0];
        testMetadata.addTCMetadata(
                testCase2,
                numOfChunks2,
                numOfOps2,
                startCID,
                DirectTestChunk2.create(numOfChunks2));

        // test case 9
        final int numOfChunks3 = 1000;
        final int numOfOps3 = 1000000;
        TestCase9 testCase3 = new TestCase9();
        addTestCase(testCase3);

        tmp = new long[numOfChunks3];
        regularOps.getCreateLocal().create(tmp, tmp.length, new TestChunk3().sizeofObject(), true);
        startCID = tmp[0];
        testMetadata.addTCMetadata(
                testCase3,
                numOfChunks3,
                numOfOps3,
                startCID,
                DirectTestChunk3.create(numOfChunks3));

        // test case 10
        final int numOfChunks4 = 1000;
        final int numOfOps4 = 1000000;
        TestCase10 testCase4 = new TestCase10();
        addTestCase(testCase4);

        tmp = new long[numOfChunks4];
        regularOps.getCreateLocal().create(tmp, tmp.length, new TestChunk4().sizeofObject(), true);
        startCID = tmp[0];
        testMetadata.addTCMetadata(
                testCase4,
                numOfChunks4,
                numOfOps4,
                startCID,
                DirectTestChunk4.create(numOfChunks4));

        // test case 11
        final int numOfChunks5 = 1000;
        final int numOfOps5 = 1000000;
        TestCase11 testCase5 = new TestCase11();
        addTestCase(testCase5);

        tmp = new long[numOfChunks5];
        regularOps.getCreateLocal().create(tmp, tmp.length, new TestChunk5().sizeofObject(), true);
        startCID = tmp[0];
        testMetadata.addTCMetadata(
                testCase5,
                numOfChunks5,
                numOfOps5,
                startCID,
                DirectTestChunk5.create(numOfChunks5));

        // test case 12
        final int numOfChunks6 = 1000;
        final int numOfOps6 = 1000000;
        TestCase12 testCase6 = new TestCase12();
        addTestCase(testCase6);

        tmp = new long[numOfChunks6];
        regularOps.getCreateLocal().create(tmp, tmp.length, new TestChunk6().sizeofObject(), true);
        startCID = tmp[0];
        testMetadata.addTCMetadata(
                testCase6,
                numOfChunks6,
                numOfOps6,
                startCID,
                DirectTestChunk6.create(numOfChunks6));

        return testMetadata;
    }

    @Override
    public void load(TestMetadata testMetadata) {
        LOGGER.info("Load test suite...");

        // test case 9
        Test testCase3 = tests.get(2);
        int n = testMetadata.getNumberOfChunks(testCase3);
        long[] directIDs = testMetadata.getDirectIDs(testCase3);

        for (int i = 0; i < n; i++) {
            // chunk size does not matter (direct access)
            DirectTestChunk3.setI(directIDs[i], new int[] { 42, 42 });
        }

        // test case 10
        Test testCase4 = tests.get(3);
        n = testMetadata.getNumberOfChunks(testCase4);
        directIDs = testMetadata.getDirectIDs(testCase4);

        for (int i = 0; i < n; i++) {
            // chunk size does not matter (direct access)
            DirectTestChunk4.setI(directIDs[i], new int[] { 42, 42 });
        }

        // test case 11
        Test testCase5 = tests.get(4);
        n = testMetadata.getNumberOfChunks(testCase5);
        directIDs = testMetadata.getDirectIDs(testCase5);

        for (int i = 0; i < n; i++) {
            // chunk size does not matter (direct access)
            DirectTestChunk5.setNumbers(directIDs[i], new long[] { 42, 42 });
        }

        // test case 12
        Test testCase6 = tests.get(5);
        n = testMetadata.getNumberOfChunks(testCase6);
        directIDs = testMetadata.getDirectIDs(testCase6);

        for (int i = 0; i < n; i++) {
            // chunk size does not matter (direct access)
            DirectTestChunk6.setNumbers(directIDs[i], new int[] { 42, 42 });
        }
    }
}
