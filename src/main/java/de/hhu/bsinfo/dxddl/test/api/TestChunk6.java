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

public class TestChunk6 extends AbstractChunk {

    private String name;
    private int[] numbers;
    private TestStruct6 testStruct;

    public TestChunk6() {
        this.name = new String();
        this.numbers = new int[25000];
        this.testStruct = new TestStruct6();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null)
            throw new NullPointerException("Parameter name must not be null");
        this.name = name;
    }

    public int[] getNumbers() {
        return this.numbers;
    }

    public TestStruct6 getTestStruct() {
        return this.testStruct;
    }

    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }

    public void setNumbers(int index, int value) {
        this.numbers[index] = value;
    }

    public void setTestStruct(TestStruct6 testStruct) {
        if (testStruct == null)
            throw new NullPointerException("Parameter testStruct must not be null");
        this.testStruct = testStruct;
    }

    @Override
    public void importObject(final Importer p_importer) {
        this.name = p_importer.readString(this.name);
        this.numbers = p_importer.readIntArray(this.numbers);
        p_importer.importObject(this.testStruct);
    }

    @Override
    public void exportObject(final Exporter p_exporter) {
        p_exporter.writeString(this.name);
        p_exporter.writeIntArray(this.numbers);
        p_exporter.exportObject(this.testStruct);
    }

    @Override
    public int sizeofObject() {
        int size = 0;

        // size of complex types
        size += sizeofString(this.name);
        size += this.testStruct.sizeofObject();
        size += sizeofIntArray(numbers);

        return size;
    }
}