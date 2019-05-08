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

import de.hhu.bsinfo.dxutils.serialization.Importable;
import de.hhu.bsinfo.dxutils.serialization.Exportable;
import de.hhu.bsinfo.dxutils.serialization.Importer;
import de.hhu.bsinfo.dxutils.serialization.Exporter;

import static de.hhu.bsinfo.dxutils.serialization.ObjectSizeUtil.*;

public class TestStruct6 implements Importable, Exportable {

    private int num;
    private long data;
    private String ident;

    public TestStruct6() {
        this.ident = new String();
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getData() {
        return this.data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public String getIdent() {
        return this.ident;
    }

    public void setIdent(String ident) {
        if (ident == null)
            throw new NullPointerException("Parameter ident must not be null");
        this.ident = ident;
    }

    @Override
    public void importObject(final Importer p_importer) {
        this.num = p_importer.readInt(this.num);
        this.data = p_importer.readLong(this.data);
        this.ident = p_importer.readString(this.ident);
    }

    @Override
    public void exportObject(final Exporter p_exporter) {
        p_exporter.writeInt(this.num);
        p_exporter.writeLong(this.data);
        p_exporter.writeString(this.ident);
    }

    @Override
    public int sizeofObject() {
        int size = 0;

        // size of basic types
        size += 12;

        // size of complex types
        size += sizeofString(this.ident);
        
        return size;
    }
}