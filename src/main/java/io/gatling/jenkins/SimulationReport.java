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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import hudson.FilePath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SimulationReport {

  private final FilePath reportDirectory;

  private static final String STATS_FILE_PATTERN = "**/global_stats.json";

  private RequestReport globalReport;

  private final String simulation;

  public SimulationReport(FilePath reportDirectory, String simulation) {
    this.reportDirectory = reportDirectory;
    this.simulation = simulation;
  }

  public void readStatsFile() throws IOException, InterruptedException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    File jsonFile = locateStatsFile();
    globalReport = mapper.readValue(jsonFile, new TypeReference<RequestReport>() {
    });
  }

  private File locateStatsFile() throws IOException, InterruptedException {
    FilePath[] files = reportDirectory.list(STATS_FILE_PATTERN);

    if (files.length == 0)
      throw new FileNotFoundException("Unable to locate the simulation results for " + simulation);

    return new File(files[0].getRemote());
  }

  public String getSimulationPath() {
    return simulation;
  }

  public RequestReport getGlobalReport() {
    return globalReport;
  }
}
