package de.hhu.bsinfo.dxddl.test.api;

import de.hhu.bsinfo.dxram.boot.BootService;
import de.hhu.bsinfo.dxram.chunk.operation.CreateLocal;
import de.hhu.bsinfo.dxram.chunk.operation.CreateReservedLocal;
import de.hhu.bsinfo.dxram.chunk.operation.ReserveLocal;
import de.hhu.bsinfo.dxram.chunk.operation.Remove;
import de.hhu.bsinfo.dxram.chunk.operation.PinningLocal;
import de.hhu.bsinfo.dxram.chunk.operation.RawReadLocal;
import de.hhu.bsinfo.dxram.chunk.operation.RawWriteLocal;

public final class DirectAccessSecurityManager {

    private static boolean INITIALIZED = false;
    static long NID;
    private static short NEXT;

    public static void init(
            final BootService boot, 
            final CreateLocal create, 
            final CreateReservedLocal create_reserved, 
            final ReserveLocal reserve, 
            final Remove remove, 
            final PinningLocal pinning, 
            final RawReadLocal rawread, 
            final RawWriteLocal rawwrite) {
        if (!INITIALIZED) {
            INITIALIZED = true;

            DirectTestChunk3.init(create, create_reserved, reserve, remove, pinning, rawread, rawwrite);
            DirectTestChunk4.init(create, create_reserved, reserve, remove, pinning, rawread, rawwrite);
            DirectTestChunk5.init(create, create_reserved, reserve, remove, pinning, rawread, rawwrite);
            DirectTestChunk6.init(create, create_reserved, reserve, remove, pinning, rawread, rawwrite);
            DirectTestChunk7.init(create, create_reserved, reserve, remove, pinning, rawread, rawwrite);
            DirectTestChunk8.init(create, create_reserved, reserve, remove, pinning, rawread, rawwrite);
            DirectTestChunk1.init(create, create_reserved, reserve, remove, pinning, rawread, rawwrite);
            DirectTestChunk2.init(create, create_reserved, reserve, remove, pinning, rawread, rawwrite);

            NID = (long) boot.getNodeID() << 48;
            NEXT = 0;

            DirectTestChunk3.setTYPE(nextTypeID());
            DirectTestChunk4.setTYPE(nextTypeID());
            DirectTestChunk5.setTYPE(nextTypeID());
            DirectTestChunk6.setTYPE(nextTypeID());
            DirectTestChunk7.setTYPE(nextTypeID());
            DirectTestChunk8.setTYPE(nextTypeID());
            DirectTestChunk1.setTYPE(nextTypeID());
            DirectTestChunk2.setTYPE(nextTypeID());
        }
    }

    private static short nextTypeID() {
        return NEXT++;
    }
}
