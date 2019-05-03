package de.hhu.bsinfo.dxddl.test;

public interface Suite {
    String getName();
    void start(short nodeId, int numOfRuns);
    TestMetadata prepare(short nodeId, int numOfRuns);
    void load(TestMetadata testMetadata);
    void run();
    TestSuiteReport createReport();
}
