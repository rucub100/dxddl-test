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

package de.hhu.bsinfo.dxddl.test.data;

import de.hhu.bsinfo.dxmem.data.AbstractChunk;
import de.hhu.bsinfo.dxutils.serialization.Exporter;
import de.hhu.bsinfo.dxutils.serialization.Importer;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 14.03.2019
 *
 */
public class TestChunk2 extends AbstractChunk {

    private int m_testInt;
    private long m_testLong;

    public TestChunk2() {}

    public int getTestInt() {
        return m_testInt;
    }

    public void setTestInt(int p_testInt) {
        this.m_testInt = p_testInt;
    }

    public long getTestLong() {
        return m_testLong;
    }

    public void setTestLong(int p_testLong) {
        this.m_testLong = p_testLong;
    }

    @Override
    public void importObject(Importer p_importer) {
        m_testInt = p_importer.readInt(m_testInt);
        m_testLong = p_importer.readLong(m_testLong);
    }

    @Override
    public int sizeofObject() {
        return Integer.BYTES +
                Long.BYTES;
    }

    @Override
    public void exportObject(Exporter p_exporter) {
        p_exporter.writeInt(m_testInt);
        p_exporter.writeLong(m_testLong);
    }

}
