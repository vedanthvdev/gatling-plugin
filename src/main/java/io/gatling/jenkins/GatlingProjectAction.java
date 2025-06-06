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
package io.gatling.jenkins;

import static io.gatling.jenkins.PluginConstants.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import hudson.model.*;
import io.gatling.jenkins.chart.Graph;

public class GatlingProjectAction implements Action {

  private final Job<?, ?> job;

  public GatlingProjectAction(Job<?, ?> job) {
    this.job = job;
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

  public Job<?, ?> getJob() {
    return job;
  }

  @SuppressWarnings("unused")
  public boolean isVisible() {
    for (Run<?, ?> build : getJob().getBuilds()) {
      GatlingBuildAction gatlingBuildAction = build.getAction(GatlingBuildAction.class);
      if (gatlingBuildAction != null) {
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("unused")
  public Graph<Long> getDashboardGraph() {
    return new Graph<Long>(job, MAX_BUILDS_TO_DISPLAY_DASHBOARD) {
      @Override
      public Long getValue(RequestReport requestReport) {
        return requestReport.getMeanResponseTime().getTotal();
      }
    };
  }

  @SuppressWarnings("unused")
  public Graph<Long> getMeanResponseTimeGraph() {
    return new Graph<Long>(job, MAX_BUILDS_TO_DISPLAY) {
      @Override
      public Long getValue(RequestReport requestReport) {
        return requestReport.getMeanResponseTime().getTotal();
      }
    };
  }

  @SuppressWarnings("unused")
  public Graph<Long> getPercentileResponseTimeGraph() {
    return new Graph<Long>(job, MAX_BUILDS_TO_DISPLAY) {
      @Override
      public Long getValue(RequestReport requestReport) {
        return requestReport.getPercentiles3().getTotal();
      }
    };
  }

  @SuppressWarnings("unused")
  public Graph<Long> getRequestKOPercentageGraph() {
    return new Graph<Long>(job, MAX_BUILDS_TO_DISPLAY) {
      @Override
      public Long getValue(RequestReport requestReport) {
        return Math.round(
            requestReport.getNumberOfRequests().getKO()
                * 100.0
                / requestReport.getNumberOfRequests().getTotal());
      }
    };
  }

  public Map<Run<?, ?>, List<String>> getReports() {
    Map<Run<?, ?>, List<String>> reports = new LinkedHashMap<>();

    for (Run<?, ?> build : job.getBuilds()) {
      GatlingBuildAction action = build.getAction(GatlingBuildAction.class);
      if (action != null) {
        List<String> simNames = new ArrayList<>();
        for (BuildSimulation sim : action.getSimulations()) {
          simNames.add(sim.getSimulationDirectory().getName());
        }
        reports.put(build, simNames);
      }
    }

    return reports;
  }

  @SuppressWarnings("unused")
  public String getReportURL(int build, String simName) {
    return build + "/" + URL_NAME + "/report/" + simName;
  }
}
