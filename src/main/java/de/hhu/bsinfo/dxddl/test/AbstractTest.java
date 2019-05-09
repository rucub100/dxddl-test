/*
 * Copyright (C) 2019 Heinrich-Heine-Universitaet Duesseldorf, Institute of Computer Science, Department Operating Systems
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package de.hhu.bsinfo.dxddl.test;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hhu.bsinfo.dxmem.data.ChunkID;
import de.hhu.bsinfo.dxram.boot.BootService;
import de.hhu.bsinfo.dxram.chunk.ChunkService;
import de.hhu.bsinfo.dxram.chunk.ChunkLocalService;
import de.hhu.bsinfo.dxram.nameservice.NameserviceService;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 13.03.2019
 *
 */
public abstract class AbstractTest implements Test {

    private static final Logger LOGGER = LogManager.getFormatterLogger(AbstractTest.class);

    private int cntRuns;
    private int cntChunks;
    private int cntOps;
    private long startCID;
    private long[] directIDs;
    private TestCaseReport testCaseReport;

    public static int MIN = Integer.MAX_VALUE;
    public static int MAX = Integer.MIN_VALUE;

    @Override
    public void prepare(TestMetadata testMetadata) {
        cntChunks = testMetadata.getNumberOfChunks(this);
        cntOps = testMetadata.getNumberOfOps(this);
        cntRuns = testMetadata.getNumberOfRuns();
        startCID = testMetadata.getStartID(this);
        directIDs = testMetadata.getDirectIDs(this);
        testCaseReport = new TestCaseReport(this);
    }

    @Override
    public void run(RegularOps regularOps) {
        LOGGER.info("Run test case \"%s\" [regular access method]...", getName());
        Stopwatch stopwatch = new Stopwatch();
        // perform regular access tests
        for (int i = 0; i < cntRuns; i++) {
            stopwatch.start();
            runViaRegularAccess(regularOps, cntChunks, cntOps, startCID);
            stopwatch.stop();
            testCaseReport.addRegularAccessDuration(stopwatch.getTotalDuration());
            stopwatch.reset();
        }

        LOGGER.info("Run test case \"%s\" [direct access method]...", getName());
        // perform direct access tests
        for (int i = 0; i < cntRuns; i++) {
            stopwatch.start();
            runViaDirectAccess(cntChunks, cntOps, directIDs);
            stopwatch.stop();
            testCaseReport.addDirectAccessDuration(stopwatch.getTotalDuration());
            stopwatch.reset();
        }
    }

    protected abstract void runViaRegularAccess(
            RegularOps regularOps, int numOfChunks, int numOfOps, long startCID);

    protected abstract void runViaDirectAccess(int numOfChunks, int numOfOps, long[] ids);

    @Override
    public TestCaseReport report() {
        return testCaseReport;
    }
}
