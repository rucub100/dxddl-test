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
import de.hhu.bsinfo.dxddl.test.TestMetadata;
import de.hhu.bsinfo.dxddl.test.data.TestChunk3;
import de.hhu.bsinfo.dxmem.data.ChunkID;
import de.hhu.bsinfo.dxram.boot.BootService;
import de.hhu.bsinfo.dxram.chunk.ChunkLocalService;
import de.hhu.bsinfo.dxram.chunk.ChunkService;
import de.hhu.bsinfo.dxram.nameservice.NameserviceService;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 14.03.2019
 *
 */
public final class TestCase3 extends AbstractTest {

    private static final Logger LOGGER = LogManager.getFormatterLogger(TestCase3.class);

    public static TestChunk3 TEST_CHUNK_TRICK_GC;

    public TestCase3(
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

        TestChunk3 testChunk = new TestChunk3();
        long reserved_cids[] = new long[meta.getSize()];
        int sizes[] = new int[meta.getSize()];

        for (int i = 0; i < meta.getSize(); i++) {
            reserved_cids[i] = ChunkID.getChunkID(meta.getNodeID(), meta.getStartLID() + i);
            sizes[i] = testChunk.sizeofObject();
        }

        m_chunkLocalService.createReservedLocal().create(reserved_cids, meta.getSize(), sizes);
    }

    @Override
    public void run() {
        LOGGER.info("Run testcase...");
        final int size = 100000000;
        final long cids[] = new long[size];
        for (int i = 0; i < size; i++) {
            cids[i] = ChunkID.getChunkID(m_meta.getNodeID(), m_meta.getStartLID() + m_random.nextInt(m_meta.getSize()));
        }

        int test = 0;
        m_stopwatch.start();
        for (int i = 0; i < size; i++) {
            TestChunk3 testChunk = new TestChunk3();
            TEST_CHUNK_TRICK_GC = testChunk;
            testChunk.setID(cids[i]);
            m_chunkLocalService.getLocal().get(testChunk);
            //m_chunkService.get().get(testChunk);
            int tmp = testChunk.getTestInt();
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
