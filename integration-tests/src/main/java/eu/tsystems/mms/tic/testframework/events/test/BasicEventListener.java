/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *      Eric Kubenka
 */
package eu.tsystems.mms.tic.testframework.events.test;

import eu.tsystems.mms.tic.testframework.events.TesterraEvent;
import eu.tsystems.mms.tic.testframework.events.TesterraEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple eventlistener implementation for testing.
 *
 * @author Eric Kubenka
 */
public class BasicEventListener implements TesterraEventListener {

    private static List<TesterraEvent> events = Collections.synchronizedList(new ArrayList<TesterraEvent>());

    @Override
    public void fireEvent(TesterraEvent e) {
        events.add(e);
    }

    public static synchronized List<TesterraEvent> getEvents() {
        return events;
    }
}
