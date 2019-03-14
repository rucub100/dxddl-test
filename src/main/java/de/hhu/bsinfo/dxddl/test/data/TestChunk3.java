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
public class TestChunk3 extends AbstractChunk {

    private byte m_testByte;
    private boolean m_testBoolean;
    private char m_testChar;
    private double m_testDouble;
    private float m_testFloat;
    private int m_testInt;
    private long m_testLong;
    private short m_testShort;


    public byte getTestByte() {
        return m_testByte;
    }

    public void setTestByte(byte m_testByte) {
        this.m_testByte = m_testByte;
    }

    public boolean isTestBoolean() {
        return m_testBoolean;
    }

    public void setTestBoolean(boolean m_testBoolean) {
        this.m_testBoolean = m_testBoolean;
    }

    public char getTestChar() {
        return m_testChar;
    }

    public void setTestChar(char m_testChar) {
        this.m_testChar = m_testChar;
    }

    public double getTestDouble() {
        return m_testDouble;
    }

    public void setTestDouble(double m_testDouble) {
        this.m_testDouble = m_testDouble;
    }

    public float getTestFloat() {
        return m_testFloat;
    }

    public void setTestFloat(float m_testFloat) {
        this.m_testFloat = m_testFloat;
    }

    public int getTestInt() {
        return m_testInt;
    }

    public void setTestInt(int m_testInt) {
        this.m_testInt = m_testInt;
    }

    public long getTestLong() {
        return m_testLong;
    }

    public void setTestLong(long m_testLong) {
        this.m_testLong = m_testLong;
    }

    public short getTestShort() {
        return m_testShort;
    }

    public void setTestShort(short m_testShort) {
        this.m_testShort = m_testShort;
    }

    public TestChunk3() {}

    @Override
    public void importObject(Importer p_importer) {
        m_testBoolean = p_importer.readBoolean(m_testBoolean);
        m_testByte = p_importer.readByte(m_testByte);
        m_testChar = p_importer.readChar(m_testChar);
        m_testDouble = p_importer.readDouble(m_testDouble);
        m_testFloat = p_importer.readFloat(m_testFloat);
        m_testInt = p_importer.readInt(m_testInt);
        m_testLong = p_importer.readLong(m_testLong);
        m_testShort = p_importer.readShort(m_testShort);
    }

    @Override
    public int sizeofObject() {
        return Byte.BYTES +
                Byte.BYTES +
                Character.BYTES +
                Double.BYTES +
                Float.BYTES +
                Integer.BYTES +
                Long.BYTES +
                Short.BYTES;
    }

    @Override
    public void exportObject(Exporter p_exporter) {
        p_exporter.writeBoolean(m_testBoolean);
        p_exporter.writeByte(m_testByte);
        p_exporter.writeChar(m_testChar);
        p_exporter.writeDouble(m_testDouble);
        p_exporter.writeFloat(m_testFloat);
        p_exporter.writeInt(m_testInt);
        p_exporter.writeLong(m_testLong);
        p_exporter.writeShort(m_testShort);
    }
}
