package kr.co.bizframe.esb.mng.model.config;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitoringData extends Agent {

	// mas status
	private String status = null;

	// app status <appId, status>
	private Map<String, String> serviceStatus = new HashMap<>();

	// route status <routeId, status>
	private Map<String, String> routeStatus = new HashMap<>();
	
	// camelinfos <camelcontext_id, List<RouteId>>
	private Map<String, List<String>> camelContextInfos = new HashMap<>();

	private Date lastUpdated;

	public MonitoringData() {
	}

	public MonitoringData(Agent agent) {
		this.setAgentId(agent.getAgentId());
		this.setLabel(agent.getLabel());
		this.setJmxUrl(agent.getJmxUrl());
		this.setJolokiaUrl(agent.getJolokiaUrl());
		this.setAdminPort(agent.getAdminPort());
		this.setToList(agent.getToList());
	}

	public Map<String, List<String>> getCamelContextInfos() {
		return camelContextInfos;
	}

	public Map<String, String> getRouteStatus() {
		return routeStatus;
	}

	public void setRouteStatus(Map<String, String> routeStatus) {
		this.routeStatus = routeStatus;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Map<String, String> getServiceStatus() {
		return serviceStatus;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MonitoringData [status=");
		builder.append(status);
		builder.append(", serviceStatus=");
		builder.append(serviceStatus);
		builder.append(", lastUpdated=");
		builder.append(lastUpdated);
		builder.append(", getAgentId()=");
		builder.append(getAgentId());
		builder.append(", getLabel()=");
		builder.append(getLabel());
		builder.append(", getJolokiaUrl()=");
		builder.append(getJolokiaUrl());
		builder.append(", getJmxUrl()=");
		builder.append(getJmxUrl());
		builder.append(", getAdminPort()=");
		builder.append(getAdminPort());
		builder.append("]");
		return builder.toString();
	}

}
