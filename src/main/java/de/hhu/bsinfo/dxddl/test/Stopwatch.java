/* 
 * Copyright (C) 2019 Heinrich-Heine-Universitaet Duesseldorf, 
 * Institute of Computer Science, Department Operating Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.hhu.bsinfo.dxddl.test;

import java.util.LinkedList;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Mar 9, 2019
 *
 */
public final class Stopwatch {

    private long m_start = 0;
    private long m_stop = 0;
    private final LinkedList<StopwatchEntry> m_splits = new LinkedList<StopwatchEntry>();

    public void start() {
        m_start = System.nanoTime();
    }

    public void split() {
        m_splits.add(new StopwatchEntry(System.nanoTime(), null));
    }

    public void split(String comment) {
        m_splits.add(new StopwatchEntry(System.nanoTime(), comment));
    }

    public void stop() {
        m_stop = System.nanoTime();
        m_splits.addFirst(new StopwatchEntry(m_start, null));
        m_splits.addLast(new StopwatchEntry(m_stop, "STOP"));
    }

    public void reset() {
        m_splits.clear();
    }

    public static String format(long duration) {
        String unit = "ns";
        if (duration > 10000000) {
            unit = "ms";
            duration /= 1000000;
        } else if (duration > 10000) {
            unit = "µs";
            duration /= 1000;
        }

        return duration + " " + unit;
    }

    public long getTotalDuration() {
        return (m_stop - m_start);
    }

    public String history() {
        long total = (m_stop - m_start);
        String unit = "ns";
        if (total > 10000000) {
            unit = "ms";
            total /= 1000000;
        } else if (total > 10000) {
            unit = "µs";
            total /= 1000;
        }
        StringBuilder stmp = new StringBuilder(String.format("Stopwatch [%d %s]:\n", total, unit));
        for (int i = 1; i < m_splits.size(); i++) {
            if (m_splits.get(i).getComment() == null) {
                continue;
            }
            long delta = m_splits.get(i).getTime() - m_splits.get(i-1).getTime();
            if (delta > 10000000) {
                unit = "ms";
                delta /= 1000000;
            } else if (delta > 10000) {
                unit = "µs";
                delta /= 1000;
            } else {
                unit = "ns";
            }
            stmp.append(String.format(">>> [%s] - %d %s\n", m_splits.get(i).getComment(), delta, unit));
        }
        return stmp.toString();
    }

    public final class StopwatchEntry {
        private final long m_time;
        private final String m_comment;

        public StopwatchEntry(final long time, final String comment) {
            m_time = time;
            m_comment = comment;
        }

        public long getTime() {
            return m_time;
        }

        public String getComment() {
            return m_comment;
        }
    }
}
