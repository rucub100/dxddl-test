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
import de.hhu.bsinfo.dxddl.test.api.DirectTestChunk5;
import de.hhu.bsinfo.dxddl.test.api.TestChunk5;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 13.03.2019
 *
 */
public final class TestCase5 extends AbstractTest {

    private static final Logger LOGGER = LogManager.getFormatterLogger(TestCase5.class);

    @Override
    protected void runViaRegularAccess(
            final RegularOps regularOps,
            final int numOfChunks,
            final int numOfOps,
            final long startCID) {
        TestChunk5 testChunk = new TestChunk5();
        long min = Integer.MAX_VALUE;
        for (long i = 0; i < numOfOps; i++) {
            testChunk.setID(startCID + (i % numOfChunks));
            regularOps.getGetLocal().get(testChunk);
            if (testChunk.getNumbers()[90] < min) {
                min = testChunk.getNumbers()[90];
            }
        }
    }

    @Override
    protected void runViaDirectAccess(final int numOfChunks, final int numOfOps, final long[] ids) {
        long min = Integer.MAX_VALUE;
        long num;
        for (int i = 0; i < numOfOps; i++) {
            num = DirectTestChunk5.getNumbers(ids[i % numOfChunks], 0);
            if (num < min) {
                min = num;
            }
        }
    }

    @Override
    public String getName() {
        return "Test case 5 (" + new TestChunk5().sizeofObject() + ")";
    }
}
