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

package de.hhu.bsinfo.dxddl.test.cases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hhu.bsinfo.dxddl.test.AbstractTest;
import de.hhu.bsinfo.dxddl.test.RegularOps;
import de.hhu.bsinfo.dxddl.test.TestCaseReport;
import de.hhu.bsinfo.dxddl.test.api.DirectTestChunk1;
import de.hhu.bsinfo.dxddl.test.api.TestChunk1;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 13.03.2019
 *
 */
public final class TestCase1 extends AbstractTest {

    private static final Logger LOGGER = LogManager.getFormatterLogger(TestCase1.class);

    @Override
    protected void runViaRegularAccess(RegularOps regularOps, int numOfChunks, long startCID) {
        TestChunk1 testChunk1 = new TestChunk1();
        int min = Integer.MAX_VALUE;
        for (long i = 0; i < numOfChunks; i++) {
            testChunk1.setID(startCID + i);
            regularOps.getGetLocal().get(testChunk1);
            if (testChunk1.getNum() < min) {
                min = testChunk1.getNum();
            }
        }
    }

    @Override
    protected void runViaDirectAccess(int numOfChunks, long startID) {
        int min = Integer.MAX_VALUE;
        int num;
        for (long i = 0; i < numOfChunks; i++) {
            num = DirectTestChunk1.getNum(startID + i);
            if (num < min) {
                min = num;
            }
        }
    }

    @Override
    public String getName() {
        return "Test case 1";
    }
}
