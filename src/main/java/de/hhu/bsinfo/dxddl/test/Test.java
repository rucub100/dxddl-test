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

/**
 * A test case.
 *
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, 13.03.2019
 *
 */
public interface Test {
    /**
     * Gets the name of the test case
     *
     * @return The name
     */
    String getName();

    /**
     * Prepares a test case for the run
     *
     * @param testMetadata The required run parameters
     */
    void prepare(TestMetadata testMetadata);

    /**
     * Runs the test case
     *
     * @param regularOps A helper object
     */
    void run(RegularOps regularOps);

    /**
     * Returns a report with the results from the run
     *
     * @return The report
     */
    TestCaseReport report();
}
