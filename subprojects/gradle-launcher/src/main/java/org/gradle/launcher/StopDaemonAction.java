/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.launcher;

import org.gradle.api.Action;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.launcher.protocol.Stop;
import org.gradle.logging.internal.OutputEventListener;
import org.gradle.messaging.remote.internal.Connection;

public class StopDaemonAction extends DaemonClientAction implements Action<ExecutionListener> {
    private static final Logger LOGGER = Logging.getLogger(Main.class);
    private final DaemonConnector connector;

    public StopDaemonAction(DaemonConnector connector, OutputEventListener outputEventListener) {
        super(outputEventListener);
        this.connector = connector;
    }

    public void execute(ExecutionListener executionListener) {
        Connection<Object> connection = connector.maybeConnect();
        if (connection == null) {
            LOGGER.lifecycle("Gradle daemon is not running.");
            return;
        }
        run(new Stop(), connection, executionListener);
        LOGGER.lifecycle("Gradle daemon stopped.");
    }
}
