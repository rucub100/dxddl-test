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

public class TestChunk5 extends AbstractChunk {

    private long[] numbers;
    private double weight;
    private String name;
    private TestStruct5 testStruct;

    public TestChunk5() {
        this.numbers = new long[100];
        this.name = new String();
        this.testStruct = new TestStruct5();
    }

    public long[] getNumbers() {
        return this.numbers;
    }

    public void setNumbers(long[] numbers) {
        this.numbers = numbers;
    }

    public void setNumbers(int index, long value) {
        this.numbers[index] = value;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null)
            throw new NullPointerException("Parameter name must not be null");
        this.name = name;
    }

    public TestStruct5 getTestStruct() {
        return this.testStruct;
    }

    public void setTestStruct(TestStruct5 testStruct) {
        if (testStruct == null)
            throw new NullPointerException("Parameter testStruct must not be null");
        this.testStruct = testStruct;
    }

    @Override
    public void importObject(final Importer p_importer) {
        this.numbers = p_importer.readLongArray(this.numbers);
        this.weight = p_importer.readDouble(this.weight);
        this.name = p_importer.readString(this.name);
        p_importer.importObject(this.testStruct);
        
    }

    @Override
    public void exportObject(final Exporter p_exporter) {
        p_exporter.writeLongArray(this.numbers);
        p_exporter.writeDouble(this.weight);
        p_exporter.writeString(this.name);
        p_exporter.exportObject(this.testStruct);
    }

    @Override
    public int sizeofObject() {
        int size = 0;

        // size of basic types
        size += 8;

        // size of complex types
        size += sizeofString(this.name);
        size += this.testStruct.sizeofObject();
        size += sizeofLongArray(numbers);
        return size;
    }
}