package kr.co.bizframe.esb.mng.model.trace;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BIZFRAME_CAMEL_MESSAGETRACED", indexes = {
		@Index(columnList = "AGENTID", name = "IDX_MESSAGETRACED_AGENTID"),
		@Index(columnList = "EXCHANGEID", name = "IDX_MESSAGETRACED_EXCHANGEID"),
		@Index(columnList = "ROUTEID", name = "IDX_MESSAGETRACED_ROUTEID"),
		@Index(columnList = "FROMENDPOINTURI", name = "IDX_MESSAGETRACED_FROMENDPOINTURI"),
		@Index(columnList = "TONODE", name = "IDX_MESSAGETRACED_TONODE"),
		@Index(columnList = "TIMESTAMP", name = "IDX_TIMESTAMP"),})
public class TraceMessage {
	
	@Id
	private String id;
	
	private String agentId;
	private Date timestamp;
	private String fromEndpointUri;
	private String previousNode;
	private String toNode;
	private String exchangeId;
	private String shortExchangeId;
	private String exchangePattern;
	
	@Column(length = 32672)
	private String properties;
	
	@Column(length = 32672)
	private String headers;
	
	@Column(length = 32672)
	private String body;
	
	private String bodyType;
	
	@Column(length = 32672)
	private String outHeaders;
	
	@Column(length = 32672)
	private String outBody;
	
	private String outBodyType;
	
	@Column(length = 32672)
	private String causedByException;
	private String routeId;
	private String traceInOut;

	public String getTraceInOut() {
		return traceInOut;
	}

	public void setTraceInOut(String traceInOut) {
		this.traceInOut = traceInOut;
	}

	public TraceMessage() {
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getPreviousNode() {
		return previousNode;
	}

	public void setPreviousNode(String previousNode) {
		this.previousNode = previousNode;
	}

	public String getFromEndpointUri() {
		return fromEndpointUri;
	}

	public void setFromEndpointUri(String fromEndpointUri) {
		this.fromEndpointUri = fromEndpointUri;
	}

	public String getToNode() {
		return toNode;
	}

	public void setToNode(String toNode) {
		this.toNode = toNode;
	}

	public String getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}

	public String getShortExchangeId() {
		return shortExchangeId;
	}

	public void setShortExchangeId(String shortExchangeId) {
		this.shortExchangeId = shortExchangeId;
	}

	public String getExchangePattern() {
		return exchangePattern;
	}

	public void setExchangePattern(String exchangePattern) {
		this.exchangePattern = exchangePattern;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBodyType() {
		return bodyType;
	}

	public void setBodyType(String bodyType) {
		this.bodyType = bodyType;
	}

	public String getOutBody() {
		return outBody;
	}

	public void setOutBody(String outBody) {
		this.outBody = outBody;
	}

	public String getOutBodyType() {
		return outBodyType;
	}

	public void setOutBodyType(String outBodyType) {
		this.outBodyType = outBodyType;
	}

	public String getOutHeaders() {
		return outHeaders;
	}

	public void setOutHeaders(String outHeaders) {
		this.outHeaders = outHeaders;
	}

	public String getCausedByException() {
		return causedByException;
	}

	public void setCausedByException(String causedByException) {
		this.causedByException = causedByException;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}


}
