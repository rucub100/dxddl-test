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

import de.hhu.bsinfo.dxmem.data.AbstractChunk;
import de.hhu.bsinfo.dxutils.serialization.Exporter;
import de.hhu.bsinfo.dxutils.serialization.Importer;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 13.03.2019
 *
 */
public class TestMetadata extends AbstractChunk {

    private short m_nodeID;
    private long m_startLID;
    private int m_size;

    public TestMetadata() {}

    public TestMetadata(final short nodeID, final long startLID, final int size) {
        m_nodeID = nodeID;
        m_startLID = startLID;
        m_size = size;
    }

    public short getNodeID() {
        return m_nodeID;
    }

    public void setNodeId(short nodeID) {
        m_nodeID = nodeID;
    }

    public long getStartLID() {
        return m_startLID;
    }

    public void setStartLID(long startLID) {
        m_startLID = startLID;
    }

    public int getSize() {
        return m_size;
    }

    public void setSize(int size) {
        m_size = size;
    }

    @Override
    public void importObject(Importer p_importer) {
        m_nodeID = p_importer.readShort(m_nodeID);
        m_startLID = p_importer.readLong(m_startLID);
        m_size = p_importer.readInt(m_size);
    }

    @Override
    public int sizeofObject() {
        return Short.BYTES +    // node ID
                Long.BYTES +    // start LID
                Integer.BYTES;  // size
    }

    @Override
    public void exportObject(Exporter p_exporter) {
        p_exporter.writeShort(m_nodeID);
        p_exporter.writeLong(m_startLID);
        p_exporter.writeInt(m_size);
    }

}
