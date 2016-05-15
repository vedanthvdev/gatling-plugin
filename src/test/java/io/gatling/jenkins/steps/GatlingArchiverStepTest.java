/**
 * Copyright 2011-2015 GatlingCorp (http://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.jenkins.steps;

import hudson.model.Action;
import io.gatling.jenkins.GatlingBuildAction;
import jenkins.util.VirtualFile;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GatlingArchiverStepTest extends Assert {
    @Rule public JenkinsRule j = new JenkinsRule();

    /**
     * Test archiving of gatling reports
     */
    @Test
    public void archive() throws Exception {
        // job setup
        WorkflowJob foo = j.jenkins.createProject(WorkflowJob.class, "foo");
        foo.setDefinition(new CpsFlowDefinition(StringUtils.join(Arrays.asList(
                "node {",
                "  writeFile file: 'results/foo-1234/js/global_stats.json', text: '{}'",
                "  writeFile file: 'results/bar-5678/js/global_stats.json', text: '{}'",
                "  gatlingArchive()",
                "}"), "\n")));

        // get the build going, and wait until workflow pauses
        WorkflowRun b = j.assertBuildStatusSuccess(foo.scheduleBuild2(0).get());

        File fooArchiveDir = new File(b.getRootDir(), "simulations/foo-1234");
        assertTrue("foo archive dir doesn't exist: " + fooArchiveDir,
                fooArchiveDir.isDirectory());
        File barArchiveDir = new File(b.getRootDir(), "simulations/bar-5678");
        assertTrue("bar archive dir doesn't exist: " + barArchiveDir,
                barArchiveDir.isDirectory());

        List<GatlingBuildAction> gbas = new ArrayList<>();
        for (Action a : b.getAllActions()) {
            if (a instanceof GatlingBuildAction) {
                gbas.add((GatlingBuildAction) a);
            }
        }
        assertEquals("Should be exactly one GatlingBuildAction",
                1, gbas.size());

        GatlingBuildAction buildAction = gbas.get(0);
        assertEquals("BuildAction should have exactly one ProjectAction",
                1, buildAction.getProjectActions().size());
    }
}

