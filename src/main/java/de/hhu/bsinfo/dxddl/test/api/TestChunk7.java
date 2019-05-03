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

public class TestChunk7 extends AbstractChunk {



    private String name;
    private long[] numbers;

    public TestChunk7() {
        this.name = new String();
        this.numbers = new long[0];
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null)
            throw new NullPointerException("Parameter name must not be null");
        this.name = name;
    }

    public long[] getNumbers() {
        return this.numbers;
    }



    @Override
    public void importObject(final Importer p_importer) {
        this.name = p_importer.readString(this.name);
        for (int i0 = 0; i0 < 0; i0++)
            this.numbers[i0] = p_importer.readLong(this.numbers[i0]);
        
    }

    @Override
    public void exportObject(final Exporter p_exporter) {
        p_exporter.writeString(this.name);
        for (int i0 = 0; i0 < 0; i0++)
            p_exporter.writeLong(this.numbers[i0]);
        
    }

    @Override
    public int sizeofObject() {
        int size = 0;

        // size of complex types
        size += sizeofString(this.name);
        
        return size;
    }
}