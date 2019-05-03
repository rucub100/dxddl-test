package de.hhu.bsinfo.dxddl.test.api;

import de.hhu.bsinfo.dxram.chunk.operation.CreateLocal;
import de.hhu.bsinfo.dxram.chunk.operation.CreateReservedLocal;
import de.hhu.bsinfo.dxram.chunk.operation.ReserveLocal;
import de.hhu.bsinfo.dxram.chunk.operation.Remove;
import de.hhu.bsinfo.dxram.chunk.operation.PinningLocal;
import de.hhu.bsinfo.dxram.chunk.operation.RawReadLocal;
import de.hhu.bsinfo.dxram.chunk.operation.RawWriteLocal;

final class DirectTestStruct5 {

    private static boolean INITIALIZED = false;
    private static CreateLocal CREATE;
    private static CreateReservedLocal CREATE_RESERVED;
    private static ReserveLocal RESERVE;
    private static Remove REMOVE;
    private static PinningLocal PINNING;
    private static RawReadLocal RAWREAD;
    private static RawWriteLocal RAWWRITE;
    private static final int OFFSET_NUM = 0;
    private static final int OFFSET_DATA_LENGTH = 4;
    private static final int OFFSET_DATA_CID = 8;
    private static final int OFFSET_DATA_ADDR = 16;
    static final int SIZE = 24;

    static void init(
            final CreateLocal create, 
            final CreateReservedLocal create_reserved, 
            final ReserveLocal reserve, 
            final Remove remove, 
            final PinningLocal pinning, 
            final RawReadLocal rawread, 
            final RawWriteLocal rawwrite) {
        if (!INITIALIZED) {
            INITIALIZED = true;
            CREATE = create;
            CREATE_RESERVED = create_reserved;
            RESERVE = reserve;
            REMOVE = remove;
            PINNING = pinning;
            RAWREAD = rawread;
            RAWWRITE = rawwrite;
        }
    }

    static void create(final long p_addr) {
        RAWWRITE.writeLong(p_addr, OFFSET_DATA_CID, -1);
        RAWWRITE.writeInt(p_addr, OFFSET_DATA_LENGTH, 0);
        RAWWRITE.writeLong(p_addr, OFFSET_DATA_ADDR, 0);
    }

    static void remove(final long p_addr) {
        final long cid_OFFSET_DATA_CID = RAWREAD.readLong(p_addr, OFFSET_DATA_CID);
        PINNING.unpinCID(cid_OFFSET_DATA_CID);
        REMOVE.remove(cid_OFFSET_DATA_CID);
    }

    static int getNum(final long p_addr) {
        return RAWREAD.readInt(p_addr, OFFSET_NUM);
    }

    static void setNum(final long p_addr, final int p_num) {
        RAWWRITE.writeInt(p_addr, OFFSET_NUM, p_num);
    }

    static int getDataLength(final long p_addr) {
        return RAWREAD.readInt(p_addr, OFFSET_DATA_LENGTH);
    }

    static byte getData(final long p_addr, final int index) {
        final int len = RAWREAD.readInt(p_addr, OFFSET_DATA_LENGTH);

        if (index < 0 || index >= len) {
            throw new ArrayIndexOutOfBoundsException();
        }

        final long addr2 = RAWREAD.readLong(p_addr, OFFSET_DATA_ADDR);
        return RAWREAD.readByte(addr2, (1 * index));
    }

    static byte[] getData(final long p_addr) {
        final int len = RAWREAD.readInt(p_addr, OFFSET_DATA_LENGTH);

        if (len <= 0) {
            return null;
        }

        return RAWREAD.readByteArray(RAWREAD.readLong(p_addr, OFFSET_DATA_ADDR), 0, len);
    }

    static void setData(final long p_addr, final int index, final byte value) {
        final int len = RAWREAD.readInt(p_addr, OFFSET_DATA_LENGTH);

        if (index < 0 || index >= len) {
            throw new ArrayIndexOutOfBoundsException();
        }

        final long addr2 = RAWREAD.readLong(p_addr, OFFSET_DATA_ADDR);
        RAWWRITE.writeByte(addr2, (1 * index), value);
    }

    static void setData(final long p_addr, final byte[] p_data) {
        final int len = RAWREAD.readInt(p_addr, OFFSET_DATA_LENGTH);
        final long cid = RAWREAD.readLong(p_addr, OFFSET_DATA_CID);

        if (cid != -1) {
            PINNING.unpinCID(cid);
            REMOVE.remove(cid);
            RAWWRITE.writeLong(p_addr, OFFSET_DATA_CID, -1);
            RAWWRITE.writeLong(p_addr, OFFSET_DATA_ADDR, 0);
            RAWWRITE.writeInt(p_addr, OFFSET_DATA_LENGTH, 0);
        }

        if (p_data == null || p_data.length == 0) {
            return;
        }

        final long[] new_cid = new long[1];
        CREATE.create(new_cid, 1, (1 * p_data.length));
        final long addr2 = PINNING.pin(new_cid[0]).getAddress();
        RAWWRITE.writeLong(p_addr, OFFSET_DATA_CID, new_cid[0]);
        RAWWRITE.writeLong(p_addr, OFFSET_DATA_ADDR, addr2);
        RAWWRITE.writeInt(p_addr, OFFSET_DATA_LENGTH, p_data.length);
        RAWWRITE.writeByteArray(addr2, 0, p_data);
    }
}
