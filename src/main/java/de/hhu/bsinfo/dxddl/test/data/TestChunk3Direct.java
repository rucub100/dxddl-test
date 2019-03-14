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

import java.util.HashMap;

import de.hhu.bsinfo.dxram.chunk.ChunkLocalService;
import de.hhu.bsinfo.dxram.chunk.ChunkService;
import de.hhu.bsinfo.dxram.chunk.operation.PinningLocal;
import de.hhu.bsinfo.dxram.chunk.operation.RawReadLocal;
import de.hhu.bsinfo.dxram.chunk.operation.RawWriteLocal;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 14.03.2019
 *
 */
public final class TestChunk3Direct implements AutoCloseable{

    private static final int OFFSET_TEST_BOOLEAN = 0;
    private static final int OFFSET_TEST_BYTE = 1;
    private static final int OFFSET_TEST_CHAR = 2;
    private static final int OFFSET_TEST_DOUBLE = 4;
    private static final int OFFSET_TEST_FLOAT = 12;
    private static final int OFFSET_TEST_INT = 16;
    private static final int OFFSET_TEST_LONG = 20;
    private static final int OFFSET_TEST_SHORT = 28;

    private static boolean INITIALIZED = false;
    private static ChunkLocalService CHUNK_LOCAL_SERVICE = null;
    private static ChunkService CHUNK_SERVICE = null;
    private static PinningLocal PINNING = null;
    private static RawReadLocal RAW_READ = null;
    private static RawWriteLocal RAW_WRITE = null;

    public static void init(final ChunkLocalService p_chunkLocalService, final ChunkService p_chunkService) {
        if (!INITIALIZED) {
            CHUNK_LOCAL_SERVICE = p_chunkLocalService;
            CHUNK_SERVICE = p_chunkService;
            PINNING = CHUNK_LOCAL_SERVICE.pinningLocal();
            RAW_READ = CHUNK_LOCAL_SERVICE.rawReadLocal();
            RAW_WRITE = CHUNK_LOCAL_SERVICE.rawWriteLocal();
            INITIALIZED = true;
        }
    }

    public static int size() {
        return Byte.BYTES +       // testBoolean
                Byte.BYTES +      // testByte
                Character.BYTES + // testChar
                Double.BYTES +    // testDouble
                Float.BYTES +     // testFloat
                Integer.BYTES +   // testInt
                Long.BYTES +      // testLong
                Short.BYTES;      // testShort
    }

    public static void pin(final long[] p_cids) {
        for (int i = 0; i < p_cids.length; i++) {
            CHUNK_LOCAL_SERVICE.pinningLocal().pin(p_cids[i]);
        }
    }

    public static long[] create(final int p_count) {
        final long[] cids = new long[p_count];
        CHUNK_LOCAL_SERVICE.createLocal().create(cids, p_count, size());
        pin(cids);
        return cids;
    }

    public static void createReserved(final long[] p_reserved_cids, final int p_count) {
        final int[] sizes = new int[p_count];
        for (int i = 0; i < p_count; i++) {
            sizes[i] = size();
        }

        CHUNK_LOCAL_SERVICE.createReservedLocal().create(p_reserved_cids, p_count, sizes);
        pin(p_reserved_cids);
    }

    public static TestChunk3Direct use(final long p_cid) {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        long address = CHUNK_LOCAL_SERVICE.pinningLocal().pin(p_cid).getAddress();
        return new TestChunk3Direct(p_cid, address);
    }

    public static boolean getTestBoolean(final long p_cid) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readByte(address, OFFSET_TEST_BOOLEAN) != 0;
    }

    public static void setTestBoolean(final long p_cid, boolean p_value) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeByte(address, OFFSET_TEST_BOOLEAN, (byte)(p_value ? 1 : 0));
    }

    public static byte getTestByte(final long p_cid) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readByte(address, OFFSET_TEST_BYTE);
    }

    public static void setTestByte(final long p_cid, byte p_value) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeByte(address, OFFSET_TEST_BYTE, p_value);
    }

    public static char getTestChar(final long p_cid) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readChar(address, OFFSET_TEST_CHAR);
    }

    public static void setTestChar(final long p_cid, char p_value) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeChar(address, OFFSET_TEST_CHAR, p_value);
    }

    public static double getTestDouble(final long p_cid) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readDouble(address, OFFSET_TEST_DOUBLE);
    }

    public static void setTestDouble(final long p_cid, double p_value) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeDouble(address, OFFSET_TEST_DOUBLE, p_value);
    }

    public static float getTestFloat(final long p_cid) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readFloat(address, OFFSET_TEST_FLOAT);
    }

    public static void setTestFloat(final long p_cid, float p_value) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeFloat(address, OFFSET_TEST_FLOAT, p_value);
    }

    public static void translate(final long[] p_cids, final long[] addr) {
        final int len = p_cids.length > addr.length ? addr.length : p_cids.length;
        for (int i = 0; i < len; i++) {
            addr[i] = PINNING.translate(p_cids[i]);
        }
    }

    public static int getTestInt(final long p_cid) {
        return RAW_READ.readInt(
                PINNING.translate(p_cid),
                OFFSET_TEST_INT);
    }

    public static int getTestIntAddr(final long p_addr) {
        return RAW_READ.readInt(p_addr, OFFSET_TEST_INT);
    }

    public static void setTestInt(final long p_cid, int p_value) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeInt(address, OFFSET_TEST_INT, p_value);
    }

    public static int getTestLong(final long p_cid) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readInt(address, OFFSET_TEST_LONG);
    }

    public static void setTestLong(final long p_cid, int p_value) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeInt(address, OFFSET_TEST_LONG, p_value);
    }

    public static short getTestShort(final long p_cid) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readShort(address, OFFSET_TEST_SHORT);
    }

    public static void setTestShort(final long p_cid, short p_value) {
        final long address = CHUNK_LOCAL_SERVICE.pinningLocal().translate(p_cid);
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeShort(address, OFFSET_TEST_SHORT, p_value);
    }

/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    private final long m_cid;
    private final long m_address;

    private TestChunk3Direct(final long p_cid, final long p_address) {
        m_cid = p_cid;
        m_address = p_address;
    }

    public boolean getTestBoolean() {
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readByte(m_address, OFFSET_TEST_BOOLEAN) != 0;
    }

    public void setTestBoolean(boolean p_value) {
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeByte(m_address, OFFSET_TEST_BOOLEAN, (byte)(p_value ? 1 : 0));
    }

    public int getTestByte() {
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readInt(m_address, OFFSET_TEST_BYTE);
    }

    public void setTestByte(byte p_value) {
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeByte(m_address, OFFSET_TEST_BYTE, p_value);
    }

    public int getTestChar() {
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readInt(m_address, OFFSET_TEST_CHAR);
    }

    public void setTestChar(char p_value) {
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeChar(m_address, OFFSET_TEST_CHAR, p_value);
    }

    public int getTestDouble() {
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readInt(m_address, OFFSET_TEST_DOUBLE);
    }

    public void setTestDouble(double p_value) {
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeDouble(m_address, OFFSET_TEST_DOUBLE, p_value);
    }

    public int getTestFloat() {
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readInt(m_address, OFFSET_TEST_FLOAT);
    }

    public void setTestFloat(float p_value) {
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeFloat(m_address, OFFSET_TEST_FLOAT, p_value);
    }

    public int getTestInt() {
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readInt(m_address, OFFSET_TEST_INT);
    }

    public void setTestInt(int p_value) {
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeInt(m_address, OFFSET_TEST_INT, p_value);
    }

    public int getTestLong() {
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readInt(m_address, OFFSET_TEST_LONG);
    }

    public void setTestLong(int p_value) {
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeInt(m_address, OFFSET_TEST_LONG, p_value);
    }

    public int getTestShort() {
        return CHUNK_LOCAL_SERVICE.rawReadLocal().readInt(m_address, OFFSET_TEST_SHORT);
    }

    public void setTestShort(short p_value) {
        CHUNK_LOCAL_SERVICE.rawWriteLocal().writeShort(m_address, OFFSET_TEST_SHORT, p_value);
    }

    @Override
    public void close() throws Exception {
        // unpin via CID instead of address is faster
        CHUNK_LOCAL_SERVICE.pinningLocal().unpinCID(m_cid);
    }
}
