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

import static kr.co.bizframe.esb.mng.utils.Strings.trim;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Entity
@Table(name = "BIZFRAME_CAMEL_AGENT")
public class Agent {
	private String agentId;
	private String label;
	private Date timestamp;

	private String jolokiaUrl;
	private int adminPort;
	private String jmxUrl;

	private String toList; // route

	@Temporal(TemporalType.TIMESTAMP)
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Id
	@Column(columnDefinition = "VARCHAR(100)")
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	@Column(columnDefinition = "VARCHAR(200)")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Column(columnDefinition = "VARCHAR(500)")
	public String getJolokiaUrl() {
		return jolokiaUrl;
	}

	public void setJolokiaUrl(String jolokiaUrl) {
		this.jolokiaUrl = jolokiaUrl;
	}

	@Column(columnDefinition = "VARCHAR(500)")
	public String getJmxUrl() {
		return jmxUrl;
	}

	public void setJmxUrl(String jmxUrl) {
		this.jmxUrl = jmxUrl;
	}

	@Column(columnDefinition = "VARCHAR(2000)")
	public String getToList() {
		return toList;
	}

	public void setToList(String toList) {
		this.toList = toList;
	}

	@Transient
	public Map<String, List<String>> getRouteToList() {
		Gson gson = new Gson();
		Map<String, List<String>> routeToList = null;
		String toList = trim(getToList());
		if (toList == null) {
			routeToList = new HashMap<>();
		} else {
			Type listType = new TypeToken<Map<String, List<String>>>() {}.getType();
			routeToList = gson.fromJson(toList, listType);
		}
		return routeToList;
	}

	public int getAdminPort() {
		return adminPort;
	}

	public void setAdminPort(int adminPort) {
		this.adminPort = adminPort;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Agent [agentId=");
		builder.append(agentId);
		builder.append(", label=");
		builder.append(label);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append(", jolokiaUrl=");
		builder.append(jolokiaUrl);
		builder.append(", adminPort=");
		builder.append(adminPort);
		builder.append(", jmxUrl=");
		builder.append(jmxUrl);
		builder.append(", toList=");
		builder.append(toList);
		builder.append("]");
		return builder.toString();
	}

}
