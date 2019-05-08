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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.hhu.bsinfo.dxmem.data.AbstractChunk;
import de.hhu.bsinfo.dxutils.serialization.Exporter;
import de.hhu.bsinfo.dxutils.serialization.Importer;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 06.05.2019
 *
 */
public class TestMetadata {

    private short m_nodeID;
    private int m_numOfRuns;
    private final Map<Test, TestCaseMD> m_testCasesMD = new HashMap<>();

    public TestMetadata(final short nodeId, final int numOfRuns) {
        this.m_nodeID = nodeId;
        this.m_numOfRuns = numOfRuns;
    }

    public void addTCMetadata(
            final Test test,
            final int numOfChunks,
            final int numOfOps,
            final long startCID,
            final long[] ids) {
        TestCaseMD md = new TestCaseMD();
        md.m_numOfChunks = numOfChunks;
        md.m_numOfOps = numOfOps;
        md.m_startCID = startCID;
        md.m_directCIDs = ids;

        m_testCasesMD.put(test, md);
    }

    public short getNodeID() { return m_nodeID; }

    public int getNumberOfRuns() { return m_numOfRuns; }

    public Collection<Test> getTestCases() {
        return m_testCasesMD.keySet();
    }

    public int getNumberOfChunks(Test tc) {
        return m_testCasesMD.get(tc).m_numOfChunks;
    }

    public int getNumberOfOps(Test tc) {
        return m_testCasesMD.get(tc).m_numOfOps;
    }

    public long getStartID(Test tc) {
        return m_testCasesMD.get(tc).m_startCID;
    }

    public long[] getDirectIDs(Test tc) {
        return m_testCasesMD.get(tc).m_directCIDs;
    }

    private class TestCaseMD {
        private int m_numOfChunks;
        private int m_numOfOps;
        private long m_startCID;
        private long[] m_directCIDs;
    }
}
