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
package io.gatling.jenkins;

import static io.gatling.jenkins.PluginConstants.*;

import hudson.model.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import io.gatling.jenkins.chart.Graph;

public class GatlingProjectAction implements Action {

  private final Job<?, ?> project;

  public GatlingProjectAction(Job<?, ?> project) {
    this.project = project;
  }

  public String getIconFileName() {
    return ICON_URL;
  }

  public String getDisplayName() {
    return DISPLAY_NAME;
  }

  public String getUrlName() {
    return URL_NAME;
  }

  public Job<?, ?> getProject() {
    return project;
  }

  public boolean isVisible() {
    for (Run<?, ?> build : getProject().getBuilds()) {
      GatlingBuildAction gatlingBuildAction = build.getAction(GatlingBuildAction.class);
      if (gatlingBuildAction != null) {
        return true;
      }
    }
    return false;
  }

  public Graph<Long> getDashboardGraph() {
    return new Graph<Long>(project, MAX_BUILDS_TO_DISPLAY_DASHBOARD) {
      @Override
      public Long getValue(RequestReport requestReport) {
        return requestReport.getMeanResponseTime().getTotal();
      }
    };
  }

  public Graph<Long> getMeanResponseTimeGraph() {
    return new Graph<Long>(project, MAX_BUILDS_TO_DISPLAY) {
      @Override
      public Long getValue(RequestReport requestReport) {
        return requestReport.getMeanResponseTime().getTotal();
      }
    };
  }

  public Graph<Long> getPercentileResponseTimeGraph() {
    return new Graph<Long>(project, MAX_BUILDS_TO_DISPLAY) {
      @Override
      public Long getValue(RequestReport requestReport) {
        return requestReport.getPercentiles1().getTotal();
      }
    };
  }

  public Graph<Long> getRequestKOPercentageGraph() {
    return new Graph<Long>(project, MAX_BUILDS_TO_DISPLAY) {
      @Override
      public Long getValue(RequestReport requestReport) {
        return Math.round(requestReport.getNumberOfRequests().getKO() * 100.0 / requestReport.getNumberOfRequests().getTotal());
      }
    };
  }

  public Map<Run<?, ?>, List<String>> getReports() {
    Map<Run<?, ?>, List<String>> reports = new LinkedHashMap<Run<?, ?>, List<String>>();

    for (Run<?, ?> build : project.getBuilds()) {
      GatlingBuildAction action = build.getAction(GatlingBuildAction.class);
      if (action != null) {
        List<String> simNames = new ArrayList<String>();
        for (BuildSimulation sim : action.getSimulations()) {
          simNames.add(sim.getSimulationName());
        }
        reports.put(build, simNames);
      }
    }

    return reports;
  }

  public String getReportURL(int build, String simName) {
    return new StringBuilder().append(build).append("/").append(URL_NAME).append("/report/").append(simName).toString();
  }
}
