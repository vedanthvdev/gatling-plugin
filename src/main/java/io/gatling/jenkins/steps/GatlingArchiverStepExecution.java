/**
 * Copyright 2011-2020 GatlingCorp (http://gatling.io)
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.jenkins.steps;

import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.gatling.jenkins.GatlingPublisher;

public class GatlingArchiverStepExecution
    extends AbstractSynchronousNonBlockingStepExecution<Void> {
  private static final long serialVersionUID = 1L;

  @StepContextParameter private transient TaskListener listener;

  @StepContextParameter private transient FilePath ws;

  @StepContextParameter private transient Run build;

  @StepContextParameter private transient Launcher launcher;

  @Override
  protected Void run() throws Exception {
    listener.getLogger().println("Running Gatling archiver step.");

    GatlingPublisher publisher = new GatlingPublisher(true);
    publisher.perform(build, ws, launcher, listener);

    return null;
  }
}
