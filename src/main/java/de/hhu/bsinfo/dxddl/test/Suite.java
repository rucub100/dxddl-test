package de.hhu.bsinfo.dxddl.test;

/**
 * A test suite that bundles test cases into a single experiment.
 *
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 06.05.2019
 *
 */
public interface Suite {

    /**
     * The name of the suite
     *
     * @return The name
     */
    String getName();

    /**
     * Starts the suite run
     *
     * @param nodeId The current cluster node id
     * @param numOfRuns The number of iterations for statistics
     */
    void start(short nodeId, int numOfRuns);

    /**
     * Prepares the suite run
     * @param nodeId The current cluster node id
     * @param numOfRuns The number of iterations
     * @return The {@link TestMetadata} with parameters for the run
     */
    TestMetadata prepare(short nodeId, int numOfRuns);

    /**
     * Loads the test data for each test case into the key-value storage
     *
     * @param testMetadata The {@link TestMetadata}
     */
    void load(TestMetadata testMetadata);

    /**
     * Runs the test suite
     */
    void run();

    /**
     * Creates the test suite report
     *
     * @return The report
     */
    TestSuiteReport createReport();
}
