package kr.co.bizframe.esb.mng.model.trace;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ExchangeStatisticInfo implements Serializable {

	private static final long serialVersionUID = 1636302096624195972L;

	public long count;

	@Id
	public String agentId;

	@Id
	public String routeId;

	@Id
	public String createDate;
	
	@Id
	public boolean success;
	
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExchangeStatisticInfo [count=");
		builder.append(count);
		builder.append(", agentId=");
		builder.append(agentId);
		builder.append(", routeId=");
		builder.append(routeId);
		builder.append(", success=");
		builder.append(success);
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append("]");
		return builder.toString();
	}

}
