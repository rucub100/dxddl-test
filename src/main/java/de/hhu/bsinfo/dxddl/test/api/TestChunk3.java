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

public class TestChunk3 extends AbstractChunk {

    private byte[] b2;
    private char[] c;
    private double[] d;
    private float[] f;
    private int[] i;
    private long[] l;
    private short[] s;

    public TestChunk3() {
        this.b2 = new byte[10];
        this.c = new char[10];
        this.d = new double[10];
        this.f = new float[10];
        this.i = new int[10];
        this.l = new long[10];
        this.s = new short[10];
    }

    public byte[] getB2() {
        return this.b2;
    }

    public char[] getC() {
        return this.c;
    }

    public double[] getD() {
        return this.d;
    }

    public float[] getF() {
        return this.f;
    }

    public int[] getI() {
        return this.i;
    }

    public long[] getL() {
        return this.l;
    }

    public short[] getS() {
        return this.s;
    }

    public void setB2(byte[] b2) {
        this.b2 = b2;
    }

    public void setC(char[] c) {
        this.c = c;
    }

    public void setD(double[] d) {
        this.d = d;
    }

    public void setF(float[] f) {
        this.f = f;
    }

    public void setI(int[] i) {
        this.i = i;
    }

    public void setL(long[] l) {
        this.l = l;
    }

    public void setS(short[] s) {
        this.s = s;
    }

    public void setB2(int index, byte value) {
        this.b2[index] = value;
    }

    public void setC(int index, char value) {
        this.c[index] = value;
    }

    public void setD(int index, double value) {
        this.d[index] = value;
    }

    public void setF(int index, float value) {
        this.f[index] = value;
    }

    public void setI(int index, int value) {
        this.i[index] = value;
    }

    public void setL(int index, long value) {
        this.l[index] = value;
    }

    public void setS(int index, short value) {
        this.s[index] = value;
    }

    @Override
    public void importObject(final Importer p_importer) {
        this.b2 = p_importer.readByteArray(this.b2);
        this.c = p_importer.readCharArray(this.c);
        this.d = p_importer.readDoubleArray(this.d);
        this.f = p_importer.readFloatArray(this.f);
        this.i = p_importer.readIntArray(this.i);
        this.l = p_importer.readLongArray(this.l);
        this.s = p_importer.readShortArray(this.s);
    }

    @Override
    public void exportObject(final Exporter p_exporter) {
        p_exporter.writeByteArray(this.b2);
        p_exporter.writeCharArray(this.c);
        p_exporter.writeDoubleArray(this.d);
        p_exporter.writeFloatArray(this.f);
        p_exporter.writeIntArray(this.i);
        p_exporter.writeLongArray(this.l);
        p_exporter.writeShortArray(this.s);
        
    }

    @Override
    public int sizeofObject() {
        int size = 0;
        size += sizeofByteArray(this.b2);
        size += sizeofCharArray(this.c);
        size += sizeofDoubleArray(this.d);
        size += sizeofFloatArray(this.f);
        size += sizeofIntArray(this.i);
        size += sizeofLongArray(this.l);
        size += sizeofShortArray(this.s);
        return size;
    }
}