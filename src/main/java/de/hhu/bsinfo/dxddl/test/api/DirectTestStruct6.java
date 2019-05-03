package de.hhu.bsinfo.dxddl.test.api;

import de.hhu.bsinfo.dxram.chunk.operation.CreateLocal;
import de.hhu.bsinfo.dxram.chunk.operation.CreateReservedLocal;
import de.hhu.bsinfo.dxram.chunk.operation.ReserveLocal;
import de.hhu.bsinfo.dxram.chunk.operation.Remove;
import de.hhu.bsinfo.dxram.chunk.operation.PinningLocal;
import de.hhu.bsinfo.dxram.chunk.operation.RawReadLocal;
import de.hhu.bsinfo.dxram.chunk.operation.RawWriteLocal;

final class DirectTestStruct6 {

    private static boolean INITIALIZED = false;
    private static CreateLocal CREATE;
    private static CreateReservedLocal CREATE_RESERVED;
    private static ReserveLocal RESERVE;
    private static Remove REMOVE;
    private static PinningLocal PINNING;
    private static RawReadLocal RAWREAD;
    private static RawWriteLocal RAWWRITE;
    private static final int OFFSET_NUM = 0;
    private static final int OFFSET_DATA = 4;
    private static final int OFFSET_IDENT_LENGTH = 12;
    private static final int OFFSET_IDENT_CID = 16;
    private static final int OFFSET_IDENT_ADDR = 24;
    static final int SIZE = 32;

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
        RAWWRITE.writeLong(p_addr, OFFSET_IDENT_CID, -1);
        RAWWRITE.writeInt(p_addr, OFFSET_IDENT_LENGTH, 0);
        RAWWRITE.writeLong(p_addr, OFFSET_IDENT_ADDR, 0);
    }

    static void remove(final long p_addr) {
        final long cid_OFFSET_IDENT_CID = RAWREAD.readLong(p_addr, OFFSET_IDENT_CID);
        PINNING.unpinCID(cid_OFFSET_IDENT_CID);
        REMOVE.remove(cid_OFFSET_IDENT_CID);
    }

    static int getNum(final long p_addr) {
        return RAWREAD.readInt(p_addr, OFFSET_NUM);
    }

    static void setNum(final long p_addr, final int p_num) {
        RAWWRITE.writeInt(p_addr, OFFSET_NUM, p_num);
    }

    static long getData(final long p_addr) {
        return RAWREAD.readLong(p_addr, OFFSET_DATA);
    }

    static void setData(final long p_addr, final long p_data) {
        RAWWRITE.writeLong(p_addr, OFFSET_DATA, p_data);
    }

    static String getIdent(final long p_addr) {
        final int len = RAWREAD.readInt(p_addr, OFFSET_IDENT_LENGTH);

        if (len < 0) {
            return null;
        } else if (len == 0) {
            return "";
        }

        return new String(RAWREAD.readByteArray(RAWREAD.readLong(p_addr, OFFSET_IDENT_ADDR), 0, len));
    }

    static void setIdent(final long p_addr, final String p_ident) {
        final int len = RAWREAD.readInt(p_addr, OFFSET_IDENT_LENGTH);
        final long cid = RAWREAD.readLong(p_addr, OFFSET_IDENT_CID);

        if (cid != -1) {
            PINNING.unpinCID(cid);
            REMOVE.remove(cid);
            RAWWRITE.writeLong(p_addr, OFFSET_IDENT_CID, -1);
            RAWWRITE.writeLong(p_addr, OFFSET_IDENT_ADDR, 0);
            RAWWRITE.writeInt(p_addr, OFFSET_IDENT_LENGTH, (p_ident == null ? -1 : 0));
        }

        if (p_ident == null || p_ident.length() == 0) {
            return;
        }

        final byte[] str = p_ident.getBytes();
        final long[] new_cid = new long[1];
        CREATE.create(new_cid, 1, str.length);
        final long addr2 = PINNING.pin(new_cid[0]).getAddress();
        RAWWRITE.writeLong(p_addr, OFFSET_IDENT_CID, new_cid[0]);
        RAWWRITE.writeLong(p_addr, OFFSET_IDENT_ADDR, addr2);
        RAWWRITE.writeInt(p_addr, OFFSET_IDENT_LENGTH, str.length);
        RAWWRITE.writeByteArray(addr2, 0, str);
    }
}
