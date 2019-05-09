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
import de.hhu.bsinfo.dxddl.test.api.DirectTestChunk2;
import de.hhu.bsinfo.dxddl.test.api.TestChunk2;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 13.03.2019
 *
 */
public final class TestCase8 extends AbstractTest {

    private static final Logger LOGGER = LogManager.getFormatterLogger(TestCase8.class);

    @Override
    protected void runViaRegularAccess(
            final RegularOps regularOps,
            final int numOfChunks,
            final int numOfOps,
            final long startCID) {
        TestChunk2 testChunk = new TestChunk2();
        for (int i = 0; i < numOfOps; i++) {
            testChunk.setID(startCID + (i % numOfChunks));
            testChunk.setI(i);
            regularOps.getPut().put(testChunk);
        }
    }

    @Override
    protected void runViaDirectAccess(final int numOfChunks, final int numOfOps, final long[] ids) {
        for (int i = 0; i < numOfOps; i++) {
            DirectTestChunk2.setI(ids[i % numOfChunks], i);
        }
    }

    @Override
    public String getName() {
        return "Test case 8 (" + new TestChunk2().sizeofObject() + ")";
    }
}
