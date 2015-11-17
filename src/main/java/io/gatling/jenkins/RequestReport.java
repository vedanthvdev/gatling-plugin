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

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestReport {

  private String name;
  private Statistics numberOfRequests;
  private Statistics minResponseTime;
  private Statistics maxResponseTime;
  private Statistics meanResponseTime;
  private Statistics standardDeviation;
  private Statistics percentiles95;
  private Statistics percentiles99;
  @JsonProperty("percentiles1")
  private Statistics percentiles1;
  @JsonProperty("percentiles2")
  private Statistics percentiles2;
  @JsonProperty("percentiles3")
  private Statistics percentiles3;
  @JsonProperty("percentiles4")
  private Statistics percentiles4;
  private Statistics meanNumberOfRequestsPerSecond;
  private ResponseTimeGroup group1;
  private ResponseTimeGroup group2;
  private ResponseTimeGroup group3;
  private ResponseTimeGroup group4;

  public Statistics getNumberOfRequests() {
    return numberOfRequests;
  }

  public void setNumberOfRequests(Statistics numberOfRequests) {
    this.numberOfRequests = numberOfRequests;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Statistics getMinResponseTime() {
    return minResponseTime;
  }

  public void setMinResponseTime(Statistics minResponseTime) {
    this.minResponseTime = minResponseTime;
  }

  public Statistics getMaxResponseTime() {
    return maxResponseTime;
  }

  public void setMaxResponseTime(Statistics maxResponseTime) {
    this.maxResponseTime = maxResponseTime;
  }

  public Statistics getMeanResponseTime() {
    return meanResponseTime;
  }

  public void setMeanResponseTime(Statistics meanResponseTime) {
    this.meanResponseTime = meanResponseTime;
  }

  public Statistics getStandardDeviation() {
    return standardDeviation;
  }

  public void setStandardDeviation(Statistics standardDeviation) {
    this.standardDeviation = standardDeviation;
  }

  public Statistics getPercentiles1() {
    return percentiles1;
  }

  public void setPercentiles1(Statistics percentiles1) {
    this.percentiles1 = percentiles1;
  }

  public Statistics getPercentiles2() {
    return percentiles2;
  }

  public void setPercentiles2(Statistics percentiles2) {
    this.percentiles2 = percentiles2;
  }

  public Statistics getPercentiles3() {
    return percentiles3;
  }

  public void setPercentiles3(Statistics percentiles3) {
    this.percentiles3 = percentiles3;
  }

  public Statistics getPercentiles4() {
    return percentiles4;
  }

  public void setPercentiles4(Statistics percentiles4) {
    this.percentiles4 = percentiles4;
  }

  public Statistics getMeanNumberOfRequestsPerSecond() {
    return meanNumberOfRequestsPerSecond;
  }

  public void setMeanNumberOfRequestsPerSecond(Statistics meanNumberOfRequestsPerSecond) {
    this.meanNumberOfRequestsPerSecond = meanNumberOfRequestsPerSecond;
  }

  public ResponseTimeGroup getGroup1() {
    return group1;
  }

  public void setGroup1(ResponseTimeGroup group1) {
    this.group1 = group1;
  }

  public ResponseTimeGroup getGroup2() {
    return group2;
  }

  public void setGroup2(ResponseTimeGroup group2) {
    this.group2 = group2;
  }

  public ResponseTimeGroup getGroup3() {
    return group3;
  }

  public void setGroup3(ResponseTimeGroup group3) {
    this.group3 = group3;
  }

  public ResponseTimeGroup getGroup4() {
    return group4;
  }

  public void setGroup4(ResponseTimeGroup group4) {
    this.group4 = group4;
  }

  private Object readResolve() {
    // Migration from 1.0.3 to 1.0.4
    if(percentiles95 != null) {
      percentiles1 = percentiles95;
    }
    if(percentiles99 != null) {
      percentiles2 = percentiles99;
    }
    return this;
  }
}
