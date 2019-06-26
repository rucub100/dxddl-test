package de.hhu.bsinfo.dxddl.test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Report from a single test case run.
 *
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 06.05.2019
 *
 */
public class TestCaseReport {

    private Test test;
    private List<Long> regularAccessDuration = new ArrayList<>();
    private List<Long> directAccessDuration = new ArrayList<>();

    public TestCaseReport(Test test) {
        this.test = test;
    }

    public Test getTest() { return test; }

    public void addRegularAccessDuration(long duration) {
        regularAccessDuration.add(duration);
    }

    public void addDirectAccessDuration(long duration) {
        directAccessDuration.add(duration);
    }

    public long getRegularAccessMin() {
        return regularAccessDuration
                .stream()
                .mapToLong(x -> x)
                .min()
                .orElseThrow(NoSuchElementException::new);
    }

    public long getRegularAccessMax() {
        return regularAccessDuration
                .stream()
                .mapToLong(x -> x)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }

    public long getRegularAccessAvg() {
        return (long) regularAccessDuration
                .stream()
                .mapToLong(x -> x)
                .average()
                .orElseThrow(NoSuchElementException::new);
    }

    public long getDirectAccessMin() {
        return directAccessDuration
                .stream()
                .mapToLong(x -> x)
                .min()
                .orElseThrow(NoSuchElementException::new);
    }

    public long getDirectAccessMax() {
        return directAccessDuration
                .stream()
                .mapToLong(x -> x)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }

    public long getDirectAccessAvg() {
        return (long) directAccessDuration
                .stream()
                .mapToLong(x -> x)
                .average()
                .orElseThrow(NoSuchElementException::new);
    }
}
