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

import de.hhu.bsinfo.dxmem.data.ChunkID;
import de.hhu.bsinfo.dxram.chunk.ChunkLocalService;
import de.hhu.bsinfo.dxram.chunk.ChunkService;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 14.03.2019
 *
 */
public final class TestChunk1Direct implements AutoCloseable{

    private static final int OFFSET_TESTINT = 0;

    private static boolean INITIALIZED = false;
    private static ChunkLocalService CHUNK_LOCAL_SERVICE = null;
    private static ChunkService CHUNK_SERVICE = null;

    public static void init(final ChunkLocalService p_chunkLocalService, final ChunkService p_chunkService) {
        if (!INITIALIZED) {
            CHUNK_LOCAL_SERVICE = p_chunkLocalService;
            CHUNK_SERVICE = p_chunkService;
            INITIALIZED = true;
        }
    }

    public static int size() {
        return Integer.BYTES; // testInt
    }

    public static long[] create(final int p_count) {
        final long[] cids = new long[p_count];
        CHUNK_LOCAL_SERVICE.createLocal().create(cids, p_count, size());
        for (int i = 0; i < p_count; i++) {
            CHUNK_LOCAL_SERVICE.pinningLocal().pin(cids[i]);
        }
        return cids;
    }

    public static void createReserved(final long[] p_reserved_cids, final int p_count) {
        final int[] sizes = new int[p_count];
        for (int i = 0; i < p_count; i++) {
            CHUNK_LOCAL_SERVICE.pinningLocal().pin(p_reserved_cids[i]);
            sizes[i] = size();
        }

        CHUNK_LOCAL_SERVICE.createReservedLocal().create(p_reserved_cids, p_count, sizes);
    }

    public static TestChunk1Direct use(final long p_cid) {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        long address = CHUNK_LOCAL_SERVICE.pinningLocal().pin(p_cid).getAddress();
        return new TestChunk1Direct(p_cid, address);
    }

    public static int getTestInt(final long p_cid) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readInt(address, OFFSET_TESTINT);
    }

    public static void setTestInt(final long p_cid, int p_value) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeInt(address, OFFSET_TESTINT, p_value);
    }

/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    private final long m_cid;
    private final long m_address;

    private TestChunk1Direct(final long p_cid, final long p_address) {
        m_cid = p_cid;
        m_address = p_address;
    }

    public int getTestInt() {
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readInt(m_address, OFFSET_TESTINT);
    }

    public void setTestInt(int p_value) {
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeInt(m_address, OFFSET_TESTINT, p_value);
    }

    @Override
    public void close() throws Exception {
     // unpin via CID instead of address is faster
        CHUNK_LOCAL_SERVICE.pinningLocal().unpinCID(m_cid);
    }
}
