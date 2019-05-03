/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package de.hhu.bsinfo.dxddl.test;

import java.util.Arrays;

import de.hhu.bsinfo.dxddl.test.api.DirectAccessApplication;
import de.hhu.bsinfo.dxddl.test.suites.TestSuite1;
import de.hhu.bsinfo.dxram.boot.BootService;
import de.hhu.bsinfo.dxram.chunk.ChunkLocalService;
import de.hhu.bsinfo.dxram.chunk.ChunkService;
import de.hhu.bsinfo.dxram.engine.DXRAMVersion;
import de.hhu.bsinfo.dxutils.NodeID;

public class App extends DirectAccessApplication {

    @Override
    public void main(String[] p_args) {
        BootService bootService = getService(BootService.class);
        System.out.printf("\n");
        System.out.printf(
                "  Hello! I am %s running on node %s.\n",
                getApplicationName(),
                NodeID.toHexStringShort(bootService.getNodeID()));
        System.out.printf("  My arguments are: %s\n", Arrays.toString(p_args));
        System.out.printf("\n");

        System.out.println("  Run direct memory-access micro-benchmarking test");
        TestSuite1 testSuite1 = new TestSuite1(getService(ChunkService.class), getService(ChunkLocalService.class));
        testSuite1.start(bootService.getNodeID(), 1000);
        TestSuiteReport report = testSuite1.createReport();
        System.out.println(report);
    }

    @Override
    public DXRAMVersion getBuiltAgainstVersion() {
        return DXRAMVersion.fromString("0.8.0-SNAPSHOT");
    }

    @Override
    public String getApplicationName() {
        return "DXDDL Tests";
    }

    @Override
    public void signalShutdown() {
        // Interrupt any flow of your application and make sure it shuts down.
        // Do not block here or wait for something to shut down. Shutting down of your application
        // must be execute asynchronously
    }
}
