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
