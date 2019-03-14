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

package de.hhu.bsinfo.dxddl.test.cases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hhu.bsinfo.dxddl.test.AbstractTest;
import de.hhu.bsinfo.dxddl.test.Stopwatch;
import de.hhu.bsinfo.dxddl.test.TestMetadata;
import de.hhu.bsinfo.dxddl.test.data.TestChunk3Direct;
import de.hhu.bsinfo.dxmem.data.ChunkID;
import de.hhu.bsinfo.dxram.boot.BootService;
import de.hhu.bsinfo.dxram.chunk.ChunkLocalService;
import de.hhu.bsinfo.dxram.chunk.ChunkService;
import de.hhu.bsinfo.dxram.nameservice.NameserviceService;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 13.03.2019
 *
 */
public final class TestCase3Direct extends AbstractTest {

    private static final Logger LOGGER = LogManager.getFormatterLogger(TestCase3Direct.class);

    private static long[] addr;
    public TestCase3Direct(
            String name,
            BootService bootService,
            NameserviceService nameService,
            ChunkService chunkService,
            ChunkLocalService chunkLocalService) {
        super(name, bootService, nameService, chunkService, chunkLocalService);
    }

    @Override
    public void load(TestMetadata meta) {
        LOGGER.info("Load test data...");
        meta.setNodeId(m_bootService.getNodeID());
        meta.setSize(1000000);

        long first = m_chunkLocalService.reserveLocal().reserve(meta.getSize())[0];
        meta.setStartLID(ChunkID.getLocalID(first));

        long reserved_cids[] = new long[meta.getSize()];
        int sizes[] = new int[meta.getSize()];

        for (int i = 0; i < meta.getSize(); i++) {
            reserved_cids[i] = ChunkID.getChunkID(meta.getNodeID(), meta.getStartLID() + i);
            sizes[i] = TestChunk3Direct.size();
        }

        //addr = new long[meta.getSize()];
        m_chunkLocalService.createReservedLocal().create(reserved_cids, meta.getSize(), sizes);
        //TestChunk3Direct.translate(reserved_cids, addr);
    }

    @Override
    public void run() {
        LOGGER.info("Run testcase...");
        final int size = 100000000;
        final long cids[] = new long[size];
        for (int i = 0; i < size; i++) {
            cids[i] = ChunkID.getChunkID(m_meta.getNodeID(), m_meta.getStartLID() + m_random.nextInt(m_meta.getSize()));
            //cids[i] = addr[m_random.nextInt(m_meta.getSize())];
        }

        int test = 0;
        m_stopwatch.start();
        for (int j = 0; j < size; j++) {
            int tmp = TestChunk3Direct.getTestInt(cids[j]);
            //int tmp = TestChunk3Direct.getTestIntAddr(cids[j]);
            if (tmp > test)
                test = tmp;
        }
        m_stopwatch.stop();
        System.out.println(test);
    }

    @Override
    public void report() {
        LOGGER.info("Report results...");
        System.out.println(m_stopwatch.history());
    }

}
