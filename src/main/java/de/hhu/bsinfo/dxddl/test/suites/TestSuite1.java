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
import de.hhu.bsinfo.dxddl.test.cases.TestCase1;
import de.hhu.bsinfo.dxddl.test.cases.TestCase2;
import de.hhu.bsinfo.dxddl.test.cases.TestCase3;
import de.hhu.bsinfo.dxddl.test.cases.TestCase4;
import de.hhu.bsinfo.dxddl.test.cases.TestCase5;
import de.hhu.bsinfo.dxddl.test.cases.TestCase6;
import de.hhu.bsinfo.dxram.chunk.ChunkLocalService;
import de.hhu.bsinfo.dxram.chunk.ChunkService;

/**
 * Test GET with different chunk sizes.
 *
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 06.05.2019
 *
 */
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

        // test case 1
        final int numOfChunks1 = 1000;
        final int numOfOps1 = 1000000;
        TestCase1 testCase1 = new TestCase1();
        addTestCase(testCase1);

        long[] tmp = new long[numOfChunks1];
        regularOps.getCreateLocal().create(tmp, tmp.length, new TestChunk1().sizeofObject(), true);
        long startCID = tmp[0];
        testMetadata.addTCMetadata(
                testCase1,
                numOfChunks1,
                numOfOps1,
                startCID,
                DirectTestChunk1.getCIDs(DirectTestChunk1.create(numOfChunks1)));

        // test case 2
        final int numOfChunks2 = 1000;
        final int numOfOps2 = 1000000;
        TestCase2 testCase2 = new TestCase2();
        addTestCase(testCase2);

        tmp = new long[numOfChunks2];
        regularOps.getCreateLocal().create(tmp, tmp.length, new TestChunk2().sizeofObject(), true);
        startCID = tmp[0];
        testMetadata.addTCMetadata(
                testCase2,
                numOfChunks2,
                numOfOps2,
                startCID,
                DirectTestChunk2.getCIDs(DirectTestChunk2.create(numOfChunks2)));

        // test case 3
        final int numOfChunks3 = 1000;
        final int numOfOps3 = 1000000;
        TestCase3 testCase3 = new TestCase3();
        addTestCase(testCase3);

        tmp = new long[numOfChunks3];
        regularOps.getCreateLocal().create(tmp, tmp.length, new TestChunk3().sizeofObject(), true);
        startCID = tmp[0];
        testMetadata.addTCMetadata(
                testCase3,
                numOfChunks3,
                numOfOps3,
                startCID,
                DirectTestChunk3.getCIDs(DirectTestChunk3.create(numOfChunks3)));

        // test case 4
        final int numOfChunks4 = 1000;
        final int numOfOps4 = 1000000;
        TestCase4 testCase4 = new TestCase4();
        addTestCase(testCase4);

        tmp = new long[numOfChunks4];
        regularOps.getCreateLocal().create(tmp, tmp.length, new TestChunk4().sizeofObject(), true);
        startCID = tmp[0];
        testMetadata.addTCMetadata(
                testCase4,
                numOfChunks4,
                numOfOps4,
                startCID,
                DirectTestChunk4.getCIDs(DirectTestChunk4.create(numOfChunks4)));

        // test case 5
        final int numOfChunks5 = 1000;
        final int numOfOps5 = 1000000;
        TestCase5 testCase5 = new TestCase5();
        addTestCase(testCase5);

        tmp = new long[numOfChunks5];
        regularOps.getCreateLocal().create(tmp, tmp.length, new TestChunk5().sizeofObject(), true);
        startCID = tmp[0];
        testMetadata.addTCMetadata(
                testCase5,
                numOfChunks5,
                numOfOps5,
                startCID,
                DirectTestChunk5.getCIDs(DirectTestChunk5.create(numOfChunks5)));

        // test case 6
        final int numOfChunks6 = 1000;
        final int numOfOps6 = 1000000;
        TestCase6 testCase6 = new TestCase6();
        addTestCase(testCase6);

        tmp = new long[numOfChunks6];
        regularOps.getCreateLocal().create(tmp, tmp.length, new TestChunk6().sizeofObject(), true);
        startCID = tmp[0];
        testMetadata.addTCMetadata(
                testCase6,
                numOfChunks6,
                numOfOps6,
                startCID,
                DirectTestChunk6.getCIDs(DirectTestChunk6.create(numOfChunks6)));

        return testMetadata;
    }

    @Override
    public void load(TestMetadata testMetadata) {
        LOGGER.info("Load test suite...");

        // test case 1
        Test testCase1 = tests.get(0);
        int n = testMetadata.getNumberOfChunks(testCase1);
        long startCID = testMetadata.getStartID(testCase1);
        long[] directIDs = testMetadata.getDirectIDs(testCase1);

        TestChunk1 testChunk1 = new TestChunk1();
        for (int i = 0; i < n; i++) {
            testChunk1.setID(startCID + i);
            testChunk1.setNum(i);
            regularOps.getPut().put(testChunk1);
            DirectTestChunk1.setNum(directIDs[i], i);
        }

        // test case 2
        Test testCase2 = tests.get(1);
        n = testMetadata.getNumberOfChunks(testCase2);
        startCID = testMetadata.getStartID(testCase2);
        directIDs = testMetadata.getDirectIDs(testCase2);

        TestChunk2 testChunk2 = new TestChunk2();
        for (int i = 0; i < n; i++) {
            testChunk2.setID(startCID + i);
            testChunk2.setI(i);
            regularOps.getPut().put(testChunk2);
            DirectTestChunk2.setI(directIDs[i], i);
        }

        // test case 3
        Test testCase3 = tests.get(2);
        n = testMetadata.getNumberOfChunks(testCase3);
        startCID = testMetadata.getStartID(testCase3);
        directIDs = testMetadata.getDirectIDs(testCase3);

        TestChunk3 testChunk3 = new TestChunk3();
        for (int i = 0; i < n; i++) {
            testChunk3.setID(startCID + i);
            testChunk3.setI(0, i);
            regularOps.getPut().put(testChunk3);
            // chunk size does not matter (direct access)
            DirectTestChunk3.setI(directIDs[i], new int[] { i, 1 });
        }

        // test case 4
        Test testCase4 = tests.get(3);
        n = testMetadata.getNumberOfChunks(testCase4);
        startCID = testMetadata.getStartID(testCase4);
        directIDs = testMetadata.getDirectIDs(testCase4);

        TestChunk4 testChunk4 = new TestChunk4();
        for (int i = 0; i < n; i++) {
            testChunk4.setID(startCID + i);
            testChunk4.setI(90, i);
            regularOps.getPut().put(testChunk4);
            // chunk size does not matter (direct access)
            DirectTestChunk4.setI(directIDs[i], new int[] { i, 1 });
        }

        // test case 5
        Test testCase5 = tests.get(4);
        n = testMetadata.getNumberOfChunks(testCase5);
        startCID = testMetadata.getStartID(testCase5);
        directIDs = testMetadata.getDirectIDs(testCase5);

        TestChunk5 testChunk5 = new TestChunk5();
        for (int i = 0; i < n; i++) {
            testChunk5.setID(startCID + i);
            testChunk5.setNumbers(90, i);
            regularOps.getPut().put(testChunk5);
            // chunk size does not matter (direct access)
            DirectTestChunk5.setNumbers(directIDs[i], new long[] { i, 1 });
        }

        // test case 6
        Test testCase6 = tests.get(5);
        n = testMetadata.getNumberOfChunks(testCase6);
        startCID = testMetadata.getStartID(testCase6);
        directIDs = testMetadata.getDirectIDs(testCase6);

        TestChunk6 testChunk6 = new TestChunk6();
        for (int i = 0; i < n; i++) {
            testChunk6.setID(startCID + i);
            testChunk6.setNumbers(1000, i);
            regularOps.getPut().put(testChunk6);
            // chunk size does not matter (direct access)
            DirectTestChunk6.setNumbers(directIDs[i], new int[] { i, 1 });
        }
    }
}
