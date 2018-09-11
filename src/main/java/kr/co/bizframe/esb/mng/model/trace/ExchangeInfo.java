package kr.co.bizframe.esb.mng.model.trace;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BIZFRAME_CAMEL_FINISHED_EXCHANGE", indexes = {
		@Index(columnList = "AGENTID", name = "IDX_FINISHED_EXCHANGE_AGENTID"),
		@Index(columnList = "EXCHANGEID", name = "IDX_FINISHED_EXCHANGE_EXCHANGEID"),
		@Index(columnList = "ROUTEID", name = "IDX_FINISHED_EXCHANGE_ROUTEID"),
		@Index(columnList = "SUCCESS", name = "IDX_FINISHED_EXCHANGE_SUCCESS"), })
public class ExchangeInfo {

	@Id
	private String id;

	private String exchangeId;

	private String agentId;

	private String routeId;

	private Date created;
	private Date finished;
	private boolean success;

	private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getId() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getFinished() {
		return finished;
	}

	public void setFinished(Date finished) {
		this.finished = finished;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FinishedExchangeMessage [exchangeId=");
		builder.append(exchangeId);
		builder.append(", routeId=");
		builder.append(routeId);
		builder.append(", created=");
		builder.append(created);
		builder.append(", finished=");
		builder.append(finished);
		builder.append(", success=");
		builder.append(success);
		builder.append("]");
		return builder.toString();
	}

}
