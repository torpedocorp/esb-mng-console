package kr.co.bizframe.esb.mng.type;

public class MonitoringURL {
	public static final String JOLOKIA_API_READ_PREFIX = "/jolokia/read/";
	public static final String MAS_SERVER_INFO = "kr.co.bizframe.mas:type=server,*";
	public static final String APPS_INFO = "kr.co.bizframe.mas:type=application,*";
	public static final String CAMEL_ROUTE_INFO = "org.apache.camel:context=[CONTEXT_ID],type=routes,*";
	public static final String CAMEL_APPTYPE = "kr.co.bizframe.mas.camel.CamelApplication";
	
}
