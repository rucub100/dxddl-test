/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package de.hhu.bsinfo.dxddl.test;

import java.util.Arrays;

import de.hhu.bsinfo.dxddl.test.api.DirectAccessApplication;
import de.hhu.bsinfo.dxddl.test.suites.TestSuite1;
import de.hhu.bsinfo.dxddl.test.suites.TestSuite2;
import de.hhu.bsinfo.dxddl.test.suites.TestSuite3;
import de.hhu.bsinfo.dxddl.test.suites.TestSuite4;
import de.hhu.bsinfo.dxram.boot.BootService;
import de.hhu.bsinfo.dxram.chunk.ChunkLocalService;
import de.hhu.bsinfo.dxram.chunk.ChunkService;
import de.hhu.bsinfo.dxram.engine.DXRAMVersion;
import de.hhu.bsinfo.dxutils.NodeID;

/**
 * The main class and a DXRAM application.
 *
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 06.05.2019
 *
 */
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

        if (p_args.length < 1 || p_args.length > 2) {
            help();
            return;
        }

        Suite suite;
        int numOfRuns = 10;

        try {
            int id = Integer.parseInt(p_args[0]);
            suite = getSuite(id);
            if (suite == null) {
                throw new Exception(String.format("Suite with ID %d not found", id));
            }
            if (p_args.length > 1) {
                numOfRuns = Integer.parseInt(p_args[1]);
                if (numOfRuns < 1) {
                    throw new Exception("Invalid number of iterations specified");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            help();
            return;
        }

        System.out.println("  Run direct memory-access micro-benchmarking test");
        suite.start(bootService.getNodeID(), numOfRuns);
        TestSuiteReport report = suite.createReport();
        System.out.println(report);
    }

    private void help() {
        System.out.println("usage:\t\t <suite id> <number of iterations>");
    }

    private Suite getSuite(int id) {
        switch (id) {
            case 1:
                return new TestSuite1(getService(ChunkService.class), getService(ChunkLocalService.class));
            case 2:
                return new TestSuite2(getService(ChunkService.class), getService(ChunkLocalService.class));
            case 3:
                return new TestSuite3(getService(ChunkService.class), getService(ChunkLocalService.class));
            case 4:
                return new TestSuite4(getService(ChunkService.class), getService(ChunkLocalService.class));
            default:
                return null;
        }
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
