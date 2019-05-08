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

public class TestChunk1 extends AbstractChunk {



    private int num;

    public TestChunk1() {
        
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public void importObject(final Importer p_importer) {
        this.num = p_importer.readInt(this.num);
    }

    @Override
    public void exportObject(final Exporter p_exporter) {
        p_exporter.writeInt(this.num);
    }

    @Override
    public int sizeofObject() {
        int size = 0;

        // size of basic types
        size += 4;
        return size;
    }
}