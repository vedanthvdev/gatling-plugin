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

public interface PluginConstants {
  String ICON_URL = "/plugin/gatling/img/logo.png";
  String DISPLAY_NAME = "Gatling";
  String URL_NAME = "gatling";

  int MAX_BUILDS_TO_DISPLAY = 30;
  int MAX_BUILDS_TO_DISPLAY_DASHBOARD = 15;
}
