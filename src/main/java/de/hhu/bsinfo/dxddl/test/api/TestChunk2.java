/*
 * Copyright (C) 2018 Heinrich-Heine-Universitaet Duesseldorf, Institute of Computer Science,
 * Department Operating Systems
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package de.hhu.bsinfo.dxddl.test.api;

import de.hhu.bsinfo.dxmem.data.AbstractChunk;
import de.hhu.bsinfo.dxutils.serialization.Importer;
import de.hhu.bsinfo.dxutils.serialization.Exporter;

import static de.hhu.bsinfo.dxutils.serialization.ObjectSizeUtil.*;

public class TestChunk2 extends AbstractChunk {



    private boolean b;
    private byte b2;
    private char c;
    private double d;
    private float f;
    private int i;
    private long l;
    private short s;

    public TestChunk2() {
        
    }

    public boolean getB() {
        return this.b;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public byte getB2() {
        return this.b2;
    }

    public void setB2(byte b2) {
        this.b2 = b2;
    }

    public char getC() {
        return this.c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public double getD() {
        return this.d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public float getF() {
        return this.f;
    }

    public void setF(float f) {
        this.f = f;
    }

    public int getI() {
        return this.i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public long getL() {
        return this.l;
    }

    public void setL(long l) {
        this.l = l;
    }

    public short getS() {
        return this.s;
    }

    public void setS(short s) {
        this.s = s;
    }



    @Override
    public void importObject(final Importer p_importer) {
        this.b = p_importer.readBoolean(this.b);
        this.b2 = p_importer.readByte(this.b2);
        this.c = p_importer.readChar(this.c);
        this.d = p_importer.readDouble(this.d);
        this.f = p_importer.readFloat(this.f);
        this.i = p_importer.readInt(this.i);
        this.l = p_importer.readLong(this.l);
        this.s = p_importer.readShort(this.s);
    }

    @Override
    public void exportObject(final Exporter p_exporter) {
        p_exporter.writeBoolean(this.b);
        p_exporter.writeByte(this.b2);
        p_exporter.writeChar(this.c);
        p_exporter.writeDouble(this.d);
        p_exporter.writeFloat(this.f);
        p_exporter.writeInt(this.i);
        p_exporter.writeLong(this.l);
        p_exporter.writeShort(this.s);
    }

    @Override
    public int sizeofObject() {
        int size = 0;

        // size of basic types
        size += 30;
        return size;
    }
}