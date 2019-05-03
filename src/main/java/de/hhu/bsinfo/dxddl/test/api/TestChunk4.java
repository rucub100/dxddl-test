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
import de.hhu.bsinfo.dxutils.serialization.Importable;
import de.hhu.bsinfo.dxutils.serialization.Exportable;
import de.hhu.bsinfo.dxutils.serialization.Importer;
import de.hhu.bsinfo.dxutils.serialization.Exporter;

import static de.hhu.bsinfo.dxutils.serialization.ObjectSizeUtil.*;

public class TestChunk4 extends AbstractChunk {



    private byte[] b;
    private char c;
    private double[] d;
    private float f;
    private int[] i;
    private long l;
    private short[] s;

    public TestChunk4() {
        this.b = new byte[0];
        this.d = new double[0];
        this.i = new int[0];
        this.s = new short[0];
    }

    public byte[] getB() {
        return this.b;
    }

    public char getC() {
        return this.c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public double[] getD() {
        return this.d;
    }

    public float getF() {
        return this.f;
    }

    public void setF(float f) {
        this.f = f;
    }

    public int[] getI() {
        return this.i;
    }

    public long getL() {
        return this.l;
    }

    public void setL(long l) {
        this.l = l;
    }

    public short[] getS() {
        return this.s;
    }



    @Override
    public void importObject(final Importer p_importer) {
        for (int i0 = 0; i0 < 0; i0++)
            this.b[i0] = p_importer.readByte(this.b[i0]);
        
        this.c = p_importer.readChar(this.c);
        for (int i0 = 0; i0 < 0; i0++)
            this.d[i0] = p_importer.readDouble(this.d[i0]);
        
        this.f = p_importer.readFloat(this.f);
        for (int i0 = 0; i0 < 0; i0++)
            this.i[i0] = p_importer.readInt(this.i[i0]);
        
        this.l = p_importer.readLong(this.l);
        for (int i0 = 0; i0 < 0; i0++)
            this.s[i0] = p_importer.readShort(this.s[i0]);
        
    }

    @Override
    public void exportObject(final Exporter p_exporter) {
        for (int i0 = 0; i0 < 0; i0++)
            p_exporter.writeByte(this.b[i0]);
        
        p_exporter.writeChar(this.c);
        for (int i0 = 0; i0 < 0; i0++)
            p_exporter.writeDouble(this.d[i0]);
        
        p_exporter.writeFloat(this.f);
        for (int i0 = 0; i0 < 0; i0++)
            p_exporter.writeInt(this.i[i0]);
        
        p_exporter.writeLong(this.l);
        for (int i0 = 0; i0 < 0; i0++)
            p_exporter.writeShort(this.s[i0]);
        
    }

    @Override
    public int sizeofObject() {
        int size = 0;

        // size of basic types
        size += 14;
        return size;
    }
}