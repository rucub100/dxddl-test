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

package de.hhu.bsinfo.dxddl.test;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hhu.bsinfo.dxmem.data.ChunkID;
import de.hhu.bsinfo.dxram.boot.BootService;
import de.hhu.bsinfo.dxram.chunk.ChunkService;
import de.hhu.bsinfo.dxram.chunk.ChunkLocalService;
import de.hhu.bsinfo.dxram.nameservice.NameserviceService;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 13.03.2019
 *
 */
public abstract class AbstractTest implements Test {

    private static final Logger LOGGER = LogManager.getFormatterLogger(AbstractTest.class);

    protected final String m_name;
    protected final NameserviceService m_nameService;
    protected final BootService m_bootService;
    protected final ChunkService m_chunkService;
    protected final ChunkLocalService m_chunkLocalService;

    protected final TestMetadata m_meta;
    protected final Stopwatch m_stopwatch;
    protected final Random m_random;

    public AbstractTest(
            final String name,
            final BootService bootService,
            final NameserviceService nameService,
            final ChunkService chunkService,
            final ChunkLocalService chunkLocalService) {
        m_name = name;
        m_bootService = bootService;
        m_nameService = nameService;
        m_chunkService = chunkService;
        m_chunkLocalService = chunkLocalService;
        m_meta = new TestMetadata();
        m_stopwatch = new Stopwatch();
        m_random = new Random();
    }

    public void start() {
        long chunkID = m_nameService.getChunkID(m_name, 500);
        if (chunkID == ChunkID.INVALID_ID) {
            // load test data
            LOGGER.info("No metadata (first run)...");
            load(m_meta);

            if (m_chunkLocalService.createLocal().create(m_meta) != 1) {
                throw new RuntimeException("Failed to create a chunk for test-metadata!");
            }

            if (!m_chunkService.put().put(m_meta)) {
                throw new RuntimeException(String.format("Failed to put test-metadata into chunk 0x%x", m_meta.getID()));
            }

            // register metadata
            m_nameService.register(m_meta, m_name);
        } else {
            LOGGER.info("Found metadata at 0x%x", chunkID);
            m_meta.setID(chunkID);
            if(!m_chunkLocalService.getLocal().get(m_meta)) {
                throw new RuntimeException(String.format("Failed to get test-metadata from chunk 0x%x", chunkID));
            }
        }

        run();
        report();
    }
}
