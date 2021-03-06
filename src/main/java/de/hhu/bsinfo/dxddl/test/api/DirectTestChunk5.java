package de.hhu.bsinfo.dxddl.test.api;

import de.hhu.bsinfo.dxram.chunk.operation.CreateLocal;
import de.hhu.bsinfo.dxram.chunk.operation.CreateReservedLocal;
import de.hhu.bsinfo.dxram.chunk.operation.ReserveLocal;
import de.hhu.bsinfo.dxram.chunk.operation.Remove;
import de.hhu.bsinfo.dxram.chunk.operation.PinningLocal;
import de.hhu.bsinfo.dxram.chunk.operation.RawReadLocal;
import de.hhu.bsinfo.dxram.chunk.operation.RawWriteLocal;

public final class DirectTestChunk5 implements AutoCloseable {

    private static final int HEADER_LID = 0;
    private static final int HEADER_TYPE = 6;
    private static final int OFFSET_NUMBERS_LENGTH = 8;
    private static final int OFFSET_NUMBERS_CID = 12;
    private static final int OFFSET_NUMBERS_ADDR = 20;
    private static final int OFFSET_WEIGHT = 28;
    private static final int OFFSET_NAME_LENGTH = 36;
    private static final int OFFSET_NAME_CID = 40;
    private static final int OFFSET_NAME_ADDR = 48;
    private static final int OFFSET_TESTSTRUCT_STRUCT = 56;
    private static final int SIZE = 80;
    private static short TYPE = 0;
    private static boolean INITIALIZED = false;
    private static CreateLocal CREATE;
    private static CreateReservedLocal CREATE_RESERVED;
    private static ReserveLocal RESERVE;
    private static Remove REMOVE;
    private static PinningLocal PINNING;
    private static RawReadLocal RAWREAD;
    private static RawWriteLocal RAWWRITE;
    private long m_addr = 0x0L;

    public static int size() {
        return SIZE;
    }

    public static boolean isValidType(final long p_id) {
        long addr;

        if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
            if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            addr = PINNING.translate(p_id);
        } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
            addr = p_id & 0xFFFFFFFFFFFFL;
        } else {
            throw new RuntimeException("The given CID is not valid or not a local CID!");
        }

        return (addr != 0x0L) && (RAWREAD.readShort(addr, HEADER_TYPE) == TYPE);
    }

    static void setTYPE(final short p_type) {
        TYPE = p_type;
    }

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
            DirectTestStruct5.init(create, create_reserved, reserve, remove, pinning, rawread, rawwrite);
        }
    }

    public static long getAddress(final long p_id) {
        if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
            return 0xFFFF000000000000L | PINNING.translate(p_id);
        }

        return p_id;
    }

    public static long[] getAddresses(final long[] p_ids) {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        final long[] addresses = new long[p_ids.length];
        for (int i = 0; i < p_ids.length; i ++) {
            if ((p_ids[i] & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
                addresses[i] = 0xFFFF000000000000L | PINNING.translate(p_ids[i]);
            } else {
                addresses[i] = p_ids[i];
            }
        }

        return addresses;
    }

    public static long getCID(final long p_id) {
        if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L || p_id == 0xFFFFFFFFFFFFFFFFL) {
            return p_id;
        } else {
            return (RAWREAD.readLong(p_id & 0xFFFFFFFFFFFFL, HEADER_LID) & 0xFFFFFFFFFFFFL) | DirectAccessSecurityManager.NID;
        }
    }

    public static long[] getCIDs(final long[] p_ids) {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        final long[] cids = new long[p_ids.length];
        for (int i = 0; i < p_ids.length; i ++) {
            if ((p_ids[i] & 0xFFFF000000000000L) != 0xFFFF000000000000L || p_ids[i] == 0xFFFFFFFFFFFFFFFFL) {
                cids[i] = p_ids[i];
            } else {
                cids[i] = (RAWREAD.readLong(p_ids[i] & 0xFFFFFFFFFFFFL, HEADER_LID) & 0xFFFFFFFFFFFFL) | DirectAccessSecurityManager.NID;
            }
        }

        return cids;
    }

    public static long[] reserve(final int p_count) {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        // TODO: REMOVE THIS WHEN DXRAM BUG IS FIXED (NID set for reserved CIDs)
        final long[] cids = new long[p_count];
        RESERVE.reserve(p_count);

        for (int i = 0; i < p_count; i ++) {
            cids[i] |= DirectAccessSecurityManager.NID;
        }

        return cids;
    }

    public static long create() {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        final long[] cids = new long[1];
        CREATE.create(cids, 1, SIZE);
        final long addr = PINNING.pin(cids[0]).getAddress();
        RAWWRITE.writeLong(addr, HEADER_LID, ((long) TYPE << 48) | (cids[0] & 0xFFFFFFFFFFFFL));
        RAWWRITE.writeLong(addr, OFFSET_NUMBERS_CID, 0xFFFFFFFFFFFFFFFFL);
        RAWWRITE.writeLong(addr, OFFSET_NAME_CID, 0xFFFFFFFFFFFFFFFFL);
        RAWWRITE.writeInt(addr, OFFSET_NUMBERS_LENGTH, 0);
        RAWWRITE.writeInt(addr, OFFSET_NAME_LENGTH, -1);
        RAWWRITE.writeLong(addr, OFFSET_NUMBERS_ADDR, 0x0L);
        RAWWRITE.writeLong(addr, OFFSET_NAME_ADDR, 0x0L);
        DirectTestStruct5.create(addr + OFFSET_TESTSTRUCT_STRUCT);
        return addr | 0xFFFF000000000000L;
    }

    public static long[] create(final int p_count) {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        final long[] cids = new long[p_count];
        CREATE.create(cids, p_count, SIZE);

        for (int i = 0; i < p_count; i ++) {
            final long addr = PINNING.pin(cids[i]).getAddress();
            RAWWRITE.writeLong(addr, HEADER_LID, ((long) TYPE << 48) | (cids[i] & 0xFFFFFFFFFFFFL));
            RAWWRITE.writeLong(addr, OFFSET_NUMBERS_CID, 0xFFFFFFFFFFFFFFFFL);
            RAWWRITE.writeLong(addr, OFFSET_NAME_CID, 0xFFFFFFFFFFFFFFFFL);
            RAWWRITE.writeInt(addr, OFFSET_NUMBERS_LENGTH, 0);
            RAWWRITE.writeInt(addr, OFFSET_NAME_LENGTH, -1);
            RAWWRITE.writeLong(addr, OFFSET_NUMBERS_ADDR, 0x0L);
            RAWWRITE.writeLong(addr, OFFSET_NAME_ADDR, 0x0L);
            DirectTestStruct5.create(addr + OFFSET_TESTSTRUCT_STRUCT);
            cids[i] = addr | 0xFFFF000000000000L;
        }

        return cids;
    }

    public static void createReserved(final long[] p_reserved_cids) {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        final int[] sizes = new int[p_reserved_cids.length];
        for (int i = 0; i < p_reserved_cids.length; i ++) {
            sizes[i] = SIZE;
        }
        CREATE_RESERVED.create(p_reserved_cids, p_reserved_cids.length, sizes);

        for (int i = 0; i < p_reserved_cids.length; i ++) {
            final long addr = PINNING.pin(p_reserved_cids[i]).getAddress();
            RAWWRITE.writeLong(addr, HEADER_TYPE, ((long) TYPE << 48) | (p_reserved_cids[i] & 0xFFFFFFFFFFFFL));
            RAWWRITE.writeLong(addr, OFFSET_NUMBERS_CID, 0xFFFFFFFFFFFFFFFFL);
            RAWWRITE.writeLong(addr, OFFSET_NAME_CID, 0xFFFFFFFFFFFFFFFFL);
            RAWWRITE.writeInt(addr, OFFSET_NUMBERS_LENGTH, 0);
            RAWWRITE.writeInt(addr, OFFSET_NAME_LENGTH, -1);
            RAWWRITE.writeLong(addr, OFFSET_NUMBERS_ADDR, 0x0L);
            RAWWRITE.writeLong(addr, OFFSET_NAME_ADDR, 0x0L);
            DirectTestStruct5.create(addr + OFFSET_TESTSTRUCT_STRUCT);
        }
    }

    public static void remove(final long p_id) {
        long addr;
        long cid;

        if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
            if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            cid = p_id;
            addr = PINNING.translate(p_id);
        } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
            addr = p_id & 0xFFFFFFFFFFFFL;
            cid = (RAWREAD.readLong(addr, HEADER_LID) & 0xFFFFFFFFFFFFL) | DirectAccessSecurityManager.NID;
        } else {
            throw new RuntimeException("The given CID is not valid or not a local CID!");
        }

        final long cid_OFFSET_NUMBERS_CID = RAWREAD.readLong(addr, OFFSET_NUMBERS_CID);
        PINNING.unpinCID(cid_OFFSET_NUMBERS_CID);
        REMOVE.remove(cid_OFFSET_NUMBERS_CID);
        final long cid_OFFSET_NAME_CID = RAWREAD.readLong(addr, OFFSET_NAME_CID);
        PINNING.unpinCID(cid_OFFSET_NAME_CID);
        REMOVE.remove(cid_OFFSET_NAME_CID);
        DirectTestStruct5.remove(addr + OFFSET_TESTSTRUCT_STRUCT);
        PINNING.unpinCID(cid);
        REMOVE.remove(cid);
    }

    public static void remove(final long[] p_ids) {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        for (int i = 0; i < p_ids.length; i ++) {
            long addr;
            long cid;

            if ((p_ids[i] & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
                if ((p_ids[i] & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                    throw new RuntimeException("The given CID is not valid or not a local CID!");
                }

                cid = p_ids[i];
                addr = PINNING.translate(p_ids[i]);
            } else if (p_ids[i] != 0xFFFFFFFFFFFFFFFFL) {
                addr = p_ids[i] & 0xFFFFFFFFFFFFL;
                cid = (RAWREAD.readLong(addr, HEADER_LID) & 0xFFFFFFFFFFFFL) | DirectAccessSecurityManager.NID;
            } else {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            final long cid_OFFSET_NUMBERS_CID = RAWREAD.readLong(addr, OFFSET_NUMBERS_CID);
            PINNING.unpinCID(cid_OFFSET_NUMBERS_CID);
            REMOVE.remove(cid_OFFSET_NUMBERS_CID);
            final long cid_OFFSET_NAME_CID = RAWREAD.readLong(addr, OFFSET_NAME_CID);
            PINNING.unpinCID(cid_OFFSET_NAME_CID);
            REMOVE.remove(cid_OFFSET_NAME_CID);
            DirectTestStruct5.remove(addr + OFFSET_TESTSTRUCT_STRUCT);
            PINNING.unpinCID(cid);
            REMOVE.remove(cid);
        }
    }

    public static int getNumbersLength(final long p_id) {
        long addr;

        if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
            if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            addr = PINNING.translate(p_id);
        } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
            addr = p_id & 0xFFFFFFFFFFFFL;
        } else {
            throw new RuntimeException("The given CID is not valid or not a local CID!");
        }

        return RAWREAD.readInt(addr, OFFSET_NUMBERS_LENGTH);
    }

    public static long getNumbers(final long p_id, final int index) {
        long addr;

        if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
            if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            addr = PINNING.translate(p_id);
        } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
            addr = p_id & 0xFFFFFFFFFFFFL;
        } else {
            throw new RuntimeException("The given CID is not valid or not a local CID!");
        }

        final int len = RAWREAD.readInt(addr, OFFSET_NUMBERS_LENGTH);

        if (index < 0 || index >= len) {
            throw new ArrayIndexOutOfBoundsException();
        }

        final long addr2 = RAWREAD.readLong(addr, OFFSET_NUMBERS_ADDR);
        return RAWREAD.readLong(addr2, (8 * index));
    }

    public static long[] getNumbers(final long p_id) {
        long addr;

        if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
            if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            addr = PINNING.translate(p_id);
        } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
            addr = p_id & 0xFFFFFFFFFFFFL;
        } else {
            throw new RuntimeException("The given CID is not valid or not a local CID!");
        }

        final int len = RAWREAD.readInt(addr, OFFSET_NUMBERS_LENGTH);

        if (len <= 0) {
            return null;
        }

        return RAWREAD.readLongArray(RAWREAD.readLong(addr, OFFSET_NUMBERS_ADDR), 0, len);
    }

    public static void setNumbers(final long p_id, final int index, final long value) {
        long addr;

        if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
            if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            addr = PINNING.translate(p_id);
        } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
            addr = p_id & 0xFFFFFFFFFFFFL;
        } else {
            throw new RuntimeException("The given CID is not valid or not a local CID!");
        }

        final int len = RAWREAD.readInt(addr, OFFSET_NUMBERS_LENGTH);

        if (index < 0 || index >= len) {
            throw new ArrayIndexOutOfBoundsException();
        }

        final long addr2 = RAWREAD.readLong(addr, OFFSET_NUMBERS_ADDR);
        RAWWRITE.writeLong(addr2, (8 * index), value);
    }

    public static void setNumbers(final long p_id, final long[] p_numbers) {
        long addr;

        if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
            if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            addr = PINNING.translate(p_id);
        } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
            addr = p_id & 0xFFFFFFFFFFFFL;
        } else {
            throw new RuntimeException("The given CID is not valid or not a local CID!");
        }

        final int len = RAWREAD.readInt(addr, OFFSET_NUMBERS_LENGTH);
        final long array_cid = RAWREAD.readLong(addr, OFFSET_NUMBERS_CID);

        if (array_cid != 0xFFFFFFFFFFFFFFFFL) {
            PINNING.unpinCID(array_cid);
            REMOVE.remove(array_cid);
            RAWWRITE.writeLong(addr, OFFSET_NUMBERS_CID, 0xFFFFFFFFFFFFFFFFL);
            RAWWRITE.writeLong(addr, OFFSET_NUMBERS_ADDR, 0x0L);
            RAWWRITE.writeInt(addr, OFFSET_NUMBERS_LENGTH, 0);
        }

        if (p_numbers == null || p_numbers.length == 0) {
            return;
        }

        final long[] new_cid = new long[1];
        CREATE.create(new_cid, 1, (8 * p_numbers.length));
        final long addr2 = PINNING.pin(new_cid[0]).getAddress();
        RAWWRITE.writeLong(addr, OFFSET_NUMBERS_CID, new_cid[0]);
        RAWWRITE.writeLong(addr, OFFSET_NUMBERS_ADDR, addr2);
        RAWWRITE.writeInt(addr, OFFSET_NUMBERS_LENGTH, p_numbers.length);
        RAWWRITE.writeLongArray(addr2, 0, p_numbers);
    }

    public static double getWeight(final long p_id) {
        long addr;

        if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
            if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            addr = PINNING.translate(p_id);
        } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
            addr = p_id & 0xFFFFFFFFFFFFL;
        } else {
            throw new RuntimeException("The given CID is not valid or not a local CID!");
        }

        return RAWREAD.readDouble(addr, OFFSET_WEIGHT);
    }

    public static void setWeight(final long p_id, final double p_weight) {
        long addr;

        if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
            if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            addr = PINNING.translate(p_id);
        } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
            addr = p_id & 0xFFFFFFFFFFFFL;
        } else {
            throw new RuntimeException("The given CID is not valid or not a local CID!");
        }

        RAWWRITE.writeDouble(addr, OFFSET_WEIGHT, p_weight);
    }

    public static String getName(final long p_id) {
        long addr;

        if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
            if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            addr = PINNING.translate(p_id);
        } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
            addr = p_id & 0xFFFFFFFFFFFFL;
        } else {
            throw new RuntimeException("The given CID is not valid or not a local CID!");
        }

        final int len = RAWREAD.readInt(addr, OFFSET_NAME_LENGTH);

        if (len < 0) {
            return null;
        } else if (len == 0) {
            return "";
        }

        return new String(RAWREAD.readByteArray(RAWREAD.readLong(addr, OFFSET_NAME_ADDR), 0, len));
    }

    public static void setName(final long p_id, final String p_name) {
        long addr;

        if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
            if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            addr = PINNING.translate(p_id);
        } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
            addr = p_id & 0xFFFFFFFFFFFFL;
        } else {
            throw new RuntimeException("The given CID is not valid or not a local CID!");
        }

        final int len = RAWREAD.readInt(addr, OFFSET_NAME_LENGTH);
        final long array_cid = RAWREAD.readLong(addr, OFFSET_NAME_CID);

        if (array_cid != 0xFFFFFFFFFFFFFFFFL) {
            PINNING.unpinCID(array_cid);
            REMOVE.remove(array_cid);
        }

        if (p_name == null || p_name.length() == 0) {
            RAWWRITE.writeLong(addr, OFFSET_NAME_CID, 0xFFFFFFFFFFFFFFFFL);
            RAWWRITE.writeLong(addr, OFFSET_NAME_ADDR, 0x0L);
            RAWWRITE.writeInt(addr, OFFSET_NAME_LENGTH, (p_name == null ? -1 : 0));
            return;
        }

        final byte[] str = p_name.getBytes();
        final long[] new_cid = new long[1];
        CREATE.create(new_cid, 1, str.length);
        final long addr2 = PINNING.pin(new_cid[0]).getAddress();
        RAWWRITE.writeLong(addr, OFFSET_NAME_CID, new_cid[0]);
        RAWWRITE.writeLong(addr, OFFSET_NAME_ADDR, addr2);
        RAWWRITE.writeInt(addr, OFFSET_NAME_LENGTH, str.length);
        RAWWRITE.writeByteArray(addr2, 0, str);
    }

    private DirectTestChunk5() {}

    public static DirectTestChunk5 use(final long p_id) {
        long addr;

        if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
            if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            addr = PINNING.translate(p_id);
        } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
            addr = p_id & 0xFFFFFFFFFFFFL;
        } else {
            throw new RuntimeException("The given CID is not valid or not a local CID!");
        }

        DirectTestChunk5 tmp = new DirectTestChunk5();
        tmp.m_addr = addr;
        return tmp;
    }

    public int getNumbersLength() {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        return RAWREAD.readInt(m_addr, OFFSET_NUMBERS_LENGTH);
    }

    public long getNumbers(final int index) {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        final int len = RAWREAD.readInt(m_addr, OFFSET_NUMBERS_LENGTH);

        if (index < 0 || index >= len) {
            throw new ArrayIndexOutOfBoundsException();
        }

        final long addr = RAWREAD.readLong(m_addr, OFFSET_NUMBERS_ADDR);
        return RAWREAD.readLong(addr, (8 * index));
    }

    public long[] getNumbers() {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        final int len = RAWREAD.readInt(m_addr, OFFSET_NUMBERS_LENGTH);

        if (len <= 0) {
            return null;
        }

        return RAWREAD.readLongArray(RAWREAD.readLong(m_addr, OFFSET_NUMBERS_ADDR), 0, len);
    }

    public void setNumbers(final int index, final long value) {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        final int len = RAWREAD.readInt(m_addr, OFFSET_NUMBERS_LENGTH);

        if (index < 0 || index >= len) {
            throw new ArrayIndexOutOfBoundsException();
        }

        final long addr = RAWREAD.readLong(m_addr, OFFSET_NUMBERS_ADDR);
        RAWWRITE.writeLong(addr, (8 * index), value);
    }

    public void setNumbers(final long[] p_numbers) {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        final int len = RAWREAD.readInt(m_addr, OFFSET_NUMBERS_LENGTH);
        final long cid = RAWREAD.readLong(m_addr, OFFSET_NUMBERS_CID);

        if (cid != 0xFFFFFFFFFFFFFFFFL) {
            PINNING.unpinCID(cid);
            REMOVE.remove(cid);
            RAWWRITE.writeLong(m_addr, OFFSET_NUMBERS_CID, 0xFFFFFFFFFFFFFFFFL);
            RAWWRITE.writeLong(m_addr, OFFSET_NUMBERS_ADDR, 0x0L);
            RAWWRITE.writeInt(m_addr, OFFSET_NUMBERS_LENGTH, 0);
        }

        if (p_numbers == null || p_numbers.length == 0) {
            return;
        }

        final long[] new_cid = new long[1];
        CREATE.create(new_cid, 1, (8 * p_numbers.length));
        final long addr = PINNING.pin(new_cid[0]).getAddress();
        RAWWRITE.writeLong(m_addr, OFFSET_NUMBERS_CID, new_cid[0]);
        RAWWRITE.writeLong(m_addr, OFFSET_NUMBERS_ADDR, addr);
        RAWWRITE.writeInt(m_addr, OFFSET_NUMBERS_LENGTH, p_numbers.length);
        RAWWRITE.writeLongArray(addr, 0, p_numbers);
    }

    public double getWeight() {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        return RAWREAD.readDouble(m_addr, OFFSET_WEIGHT);
    }

    public void setWeight(final double p_weight) {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        RAWWRITE.writeDouble(m_addr, OFFSET_WEIGHT, p_weight);
    }

    public String getName() {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        final int len = RAWREAD.readInt(m_addr, OFFSET_NAME_LENGTH);

        if (len < 0) {
            return null;
        } else if (len == 0) {
            return "";
        }

        return new String(RAWREAD.readByteArray(RAWREAD.readLong(m_addr, OFFSET_NAME_ADDR), 0, len));
    }

    public void setName(final String p_name) {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        final int len = RAWREAD.readInt(m_addr, OFFSET_NAME_LENGTH);
        final long cid = RAWREAD.readLong(m_addr, OFFSET_NAME_CID);

        if (cid != 0xFFFFFFFFFFFFFFFFL) {
            PINNING.unpinCID(cid);
            REMOVE.remove(cid);
        }

        if (p_name == null || p_name.length() == 0) {
            RAWWRITE.writeLong(m_addr, OFFSET_NAME_CID, 0xFFFFFFFFFFFFFFFFL);
            RAWWRITE.writeLong(m_addr, OFFSET_NAME_ADDR, 0x0L);
            RAWWRITE.writeInt(m_addr, OFFSET_NAME_LENGTH, (p_name == null ? -1 : 0));
            return;
        }

        final byte[] str = p_name.getBytes();
        final long[] new_cid = new long[1];
        CREATE.create(new_cid, 1, str.length);
        final long addr = PINNING.pin(new_cid[0]).getAddress();
        RAWWRITE.writeLong(m_addr, OFFSET_NAME_CID, new_cid[0]);
        RAWWRITE.writeLong(m_addr, OFFSET_NAME_ADDR, addr);
        RAWWRITE.writeInt(m_addr, OFFSET_NAME_LENGTH, str.length);
        RAWWRITE.writeByteArray(addr, 0, str);
    }

    testStruct testStruct() {
        return new testStruct(m_addr + OFFSET_TESTSTRUCT_STRUCT);
    }

    @Override
    public void close() {
        if (!INITIALIZED) {
            throw new RuntimeException("Not initialized!");
        }

        m_addr = 0x0L;
    }

    public static final class testStruct {

        private final long m_addr;

        private testStruct(final long p_addr) {
            m_addr = p_addr;
        }

        public int getNum() {
            return DirectTestStruct5.getNum(m_addr);
        }

        public void setNum(final int p_num) {
            DirectTestStruct5.setNum(m_addr, p_num);
        }

        public static int getNum(final long p_id) {
            long addr;

            if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
                if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                    throw new RuntimeException("The given CID is not valid or not a local CID!");
                }

                addr = PINNING.translate(p_id);
            } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
                addr = p_id & 0xFFFFFFFFFFFFL;
            } else {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            return DirectTestStruct5.getNum(addr + OFFSET_TESTSTRUCT_STRUCT);
        }

        public static void setNum(final long p_id, final int p_num) {
            long addr;

            if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
                if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                    throw new RuntimeException("The given CID is not valid or not a local CID!");
                }

                addr = PINNING.translate(p_id);
            } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
                addr = p_id & 0xFFFFFFFFFFFFL;
            } else {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            DirectTestStruct5.setNum(addr + OFFSET_TESTSTRUCT_STRUCT, p_num);
        }

        public int getDataLength() {
            return DirectTestStruct5.getDataLength(m_addr);
        }

        public static int getDataLength(final long p_id) {
            long addr;

            if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
                if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                    throw new RuntimeException("The given CID is not valid or not a local CID!");
                }

                addr = PINNING.translate(p_id);
            } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
                addr = p_id & 0xFFFFFFFFFFFFL;
            } else {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            return DirectTestStruct5.getDataLength(addr + OFFSET_TESTSTRUCT_STRUCT);
        }

        public byte getData(final int p_index) {
            return DirectTestStruct5.getData(m_addr, p_index);
        }

        public static byte getData(final long p_id, final int p_index) {
            long addr;

            if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
                if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                    throw new RuntimeException("The given CID is not valid or not a local CID!");
                }

                addr = PINNING.translate(p_id);
            } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
                addr = p_id & 0xFFFFFFFFFFFFL;
            } else {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            return DirectTestStruct5.getData(addr + OFFSET_TESTSTRUCT_STRUCT, p_index);
        }

        public byte[] getData() {
            return DirectTestStruct5.getData(m_addr);
        }

        public static byte[] getData(final long p_id) {
            long addr;

            if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
                if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                    throw new RuntimeException("The given CID is not valid or not a local CID!");
                }

                addr = PINNING.translate(p_id);
            } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
                addr = p_id & 0xFFFFFFFFFFFFL;
            } else {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            return DirectTestStruct5.getData(addr + OFFSET_TESTSTRUCT_STRUCT);
        }

        public void setData(final int p_index, final byte p_value) {
            DirectTestStruct5.setData(m_addr, p_index, p_value);
        }

        public static void setData(final long p_id, final int p_index, final byte p_value) {
            long addr;

            if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
                if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                    throw new RuntimeException("The given CID is not valid or not a local CID!");
                }

                addr = PINNING.translate(p_id);
            } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
                addr = p_id & 0xFFFFFFFFFFFFL;
            } else {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            DirectTestStruct5.setData(addr + OFFSET_TESTSTRUCT_STRUCT, p_index, p_value);
        }

        public void setData(final byte[] p_data) {
            DirectTestStruct5.setData(m_addr, p_data);
        }

        public static void setData(final long p_id, final byte[] p_data) {
            long addr;

            if ((p_id & 0xFFFF000000000000L) != 0xFFFF000000000000L) {
                if ((p_id & 0xFFFF000000000000L) != DirectAccessSecurityManager.NID) {
                    throw new RuntimeException("The given CID is not valid or not a local CID!");
                }

                addr = PINNING.translate(p_id);
            } else if (p_id != 0xFFFFFFFFFFFFFFFFL) {
                addr = p_id & 0xFFFFFFFFFFFFL;
            } else {
                throw new RuntimeException("The given CID is not valid or not a local CID!");
            }

            DirectTestStruct5.setData(addr + OFFSET_TESTSTRUCT_STRUCT, p_data);
        }
    }
}
