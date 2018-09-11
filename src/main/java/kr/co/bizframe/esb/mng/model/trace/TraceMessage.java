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
		@Index(columnList = "TONODE", name = "IDX_MESSAGETRACED_TONODE"), })
public class TraceMessage {

	private String id;
	private String agentId;
	private Date timestamp;
	private String fromEndpointUri;
	private String previousNode;
	private String toNode;
	private String exchangeId;
	private String shortExchangeId;
	private String exchangePattern;
	private String properties;
	private String headers;
	private String body;
	private String bodyType;
	private String outHeaders;
	private String outBody;
	private String outBodyType;
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

	@Id
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

	// @Lob
	@Column(columnDefinition = "VARCHAR(32672)")
	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	// @Lob
	@Column(columnDefinition = "VARCHAR(32672)")
	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}

	// @Lob
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

	// @Lob
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

	// @Lob
	@Column(columnDefinition = "VARCHAR(32672)")
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TraceMessage [exchangeId=");
		builder.append(exchangeId);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append(", fromEndpointUri=");
		builder.append(fromEndpointUri);
		builder.append(", previousNode=");
		builder.append(previousNode);
		builder.append(", toNode=");
		builder.append(toNode);
		builder.append(", exchangePattern=");
		builder.append(exchangePattern);
		builder.append(", properties=");
		builder.append(properties);
		builder.append(", headers=");
		builder.append(headers);
		builder.append(", body=");
		builder.append(body);
		builder.append(", bodyType=");
		builder.append(bodyType);
		builder.append(", outHeaders=");
		builder.append(outHeaders);
		builder.append(", outBody=");
		builder.append(outBody);
		builder.append(", outBodyType=");
		builder.append(outBodyType);
		builder.append(", causedByException=");
		builder.append(causedByException);
		builder.append(", routeId=");
		builder.append(routeId);
		builder.append("]");
		return builder.toString();
	}

}
