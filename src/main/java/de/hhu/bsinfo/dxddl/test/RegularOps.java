package de.hhu.bsinfo.dxddl.test;

import de.hhu.bsinfo.dxram.chunk.ChunkLocalService;
import de.hhu.bsinfo.dxram.chunk.ChunkService;
import de.hhu.bsinfo.dxram.chunk.operation.Create;
import de.hhu.bsinfo.dxram.chunk.operation.CreateLocal;
import de.hhu.bsinfo.dxram.chunk.operation.CreateReservedLocal;
import de.hhu.bsinfo.dxram.chunk.operation.Get;
import de.hhu.bsinfo.dxram.chunk.operation.GetLocal;
import de.hhu.bsinfo.dxram.chunk.operation.Put;
import de.hhu.bsinfo.dxram.chunk.operation.ReserveLocal;

/**
 * Helper class for the regular (non direct-memory) access.
 *
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 06.05.2019
 *
 */
public class RegularOps {

    private final Get m_get;
    private final GetLocal m_getLocal;
    private final Put m_put;
    private final Create m_create;
    private final CreateLocal m_createLocal;
    private final CreateReservedLocal m_createReservedLocal;
    private final ReserveLocal m_reserveLocal;

    public RegularOps(ChunkService chunkService, ChunkLocalService chunkLocalService) {
        m_get = chunkService.get();
        m_getLocal = chunkLocalService.getLocal();
        m_put = chunkService.put();
        m_create = chunkService.create();
        m_createLocal = chunkLocalService.createLocal();
        m_createReservedLocal = chunkLocalService.createReservedLocal();
        m_reserveLocal = chunkLocalService.reserveLocal();
    }

    public GetLocal getGetLocal() { return m_getLocal; }

    public CreateLocal getCreateLocal() { return m_createLocal; }

    public CreateReservedLocal getCreateReservedLocal() { return m_createReservedLocal; }

    public ReserveLocal getReserveLocal() { return m_reserveLocal; }

    public Put getPut() { return m_put; }
}
