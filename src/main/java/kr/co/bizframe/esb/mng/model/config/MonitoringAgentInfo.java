/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe esb-mng-console project licenses this file to you under the Apache License,     
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:                   
 *                                                                              
 *   http://www.apache.org/licenses/LICENSE-2.0                                 
 *                                                                              
 * Unless required by applicable law or agreed to in writing, software          
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the     
 * License for the specific language governing permissions and limitations      
 * under the License.                                                           
 */ 

package kr.co.bizframe.esb.mng.model.config;

public class MonitoringAgentInfo {
	private String fromAgentId;
	private String fromLabel;
	private String fromJolokiaUrl;
	private String fromRouteId;

	private String toAgentId;
	private String toLabel;
	private String toJolokiaUrl;
	private String toRouteId;

	public String getFromAgentId() {
		return fromAgentId;
	}

	public void setFromAgentId(String fromAgentId) {
		this.fromAgentId = fromAgentId;
	}

	public String getFromLabel() {
		return fromLabel;
	}

	public void setFromLabel(String fromLabel) {
		this.fromLabel = fromLabel;
	}

	public String getFromJolokiaUrl() {
		return fromJolokiaUrl;
	}

	public void setFromJolokiaUrl(String fromJolokiaUrl) {
		this.fromJolokiaUrl = fromJolokiaUrl;
	}

	public String getFromRouteId() {
		return fromRouteId;
	}

	public void setFromRouteId(String fromRouteId) {
		this.fromRouteId = fromRouteId;
	}

	public String getToAgentId() {
		return toAgentId;
	}

	public void setToAgentId(String toAgentId) {
		this.toAgentId = toAgentId;
	}

	public String getToLabel() {
		return toLabel;
	}

	public void setToLabel(String toLabel) {
		this.toLabel = toLabel;
	}

	public String getToJolokiaUrl() {
		return toJolokiaUrl;
	}

	public void setToJolokiaUrl(String toJolokiaUrl) {
		this.toJolokiaUrl = toJolokiaUrl;
	}

	public String getToRouteId() {
		return toRouteId;
	}

	public void setToRouteId(String toRouteId) {
		this.toRouteId = toRouteId;
	}

}
