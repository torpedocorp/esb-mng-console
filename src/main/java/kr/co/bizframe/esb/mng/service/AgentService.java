package kr.co.bizframe.esb.mng.service;
import static kr.co.bizframe.esb.mng.type.Constants.CMD_APP_ID_PATTERN;
import static kr.co.bizframe.esb.mng.type.Constants.CMD_STATUS_PATTERN;
import static kr.co.bizframe.esb.mng.type.Constants.FAIL;
import static kr.co.bizframe.esb.mng.type.Constants.MAS_APP_ID_PATTERN;
import static kr.co.bizframe.esb.mng.type.Constants.SUCCESS;
import static kr.co.bizframe.esb.mng.type.Constants.TOTAL;
import static kr.co.bizframe.esb.mng.utils.Strings.trim;
import static kr.co.bizframe.esb.mng.utils.WebUtils.getRoutePrimaryKey;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kr.co.bizframe.esb.mng.dao.AgentDao;
import kr.co.bizframe.esb.mng.dao.ExchangeInfoDao;
import kr.co.bizframe.esb.mng.model.config.Agent;
import kr.co.bizframe.esb.mng.model.config.MonitoringAgentInfo;
import kr.co.bizframe.esb.mng.model.config.MonitoringData;
import kr.co.bizframe.esb.mng.model.trace.ExchangeStatisticInfo;
import kr.co.bizframe.esb.mng.type.MonitoringURL;
import kr.co.bizframe.esb.mng.utils.MonitorThreadFactory;
import kr.co.bizframe.esb.mng.utils.Strings;
import kr.co.bizframe.esb.mng.utils.TimeUtil;
import kr.co.bizframe.esb.mng.utils.WebUtils;
import kr.co.bizframe.mas.command.Command;
import kr.co.bizframe.mas.command.CommandInvoker;
import kr.co.bizframe.mas.command.CommandResponse;

@Service
public class AgentService {

	@Autowired
	private AgentDao agentDao;

	@Autowired
	private WebUtils webUtils;
	
	@Autowired
	private ExchangeInfoDao exchangeInfoDao;

	private PoolingHttpClientConnectionManager connManager;

	private static boolean init = false;
	private Logger logger = Logger.getLogger(getClass());
	
	private Map<String, MonitoringData> datas = new HashMap<>();

	// route data statistic <routeId, <status, count>>
	private Map<String, Map<String, Long>> routeDatas = new HashMap<>();
	
	public final static String JOLOKIA_CONTENT_TYPE = "application/json;charset=UTF-8";
	public static final String DEFAULT_EXECUTOR_SERVICE_NAME = "ESB";
	
	private Map<String, ScheduledExecutorService> instanceSchedulers = new  HashMap<String, ScheduledExecutorService>();	
	
	private Timer exchangeInfoTimer = null;
	
	@PostConstruct
	public void init() throws Throwable {
		if (init) {
			return;
		}

		// init connection manager
		Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.build();

		this.connManager = new PoolingHttpClientConnectionManager(r);
		this.connManager.setMaxTotal(webUtils.getConnectionMaxTotal());
		this.connManager.setDefaultMaxPerRoute(webUtils.getConnectionDefaultMaxPerRoute());
		//this.connManager.closeIdleConnections(webUtils.getMonitorInterval(), TimeUnit.SECONDS);
		
		// setting todayRouteData
		initTodayExchangeInfo();
		
		setAgentInfos();
		
		for (MonitoringData instance : datas.values()) {
			initMonitoringAgent(instance);
		}
		
		logger.info("----------------AgentMonitoring init ---------------");
		init = true;

	}

	private void initTodayExchangeInfo() {
		try {
			List<ExchangeStatisticInfo> todayList = exchangeInfoDao.getExchageStatisticInfos(TimeUtil.getCurrentDateTime("yyyy-MM-dd"));
			for (ExchangeStatisticInfo info : todayList) {
				String agentId = info.getAgentId();
				String routeId = info.getRouteId();
				addExchangeInfo(agentId, routeId, info.isSuccess(), info.getCount());
			}
		} catch (Throwable e) {
			logger.error("initTodayExchangeInfo error " + e.getMessage(), e);
		}
			
		exchangeInfoTimer = new Timer();
		try {
			String today = TimeUtil.getCurrentDateTime("yyyyMMdd");
			Calendar c = Calendar.getInstance();
			c.setTime(TimeUtil.parseDateTime(today + "000000000", "yyyyMMddHHmmssSSS"));
			c.add(Calendar.DAY_OF_MONTH, 1);
			long tomorrorMils = c.getTimeInMillis();
			long nowMils = System.currentTimeMillis();
			long diff = tomorrorMils - nowMils;
			long nextTime = 24 * 60 * 60 * 1000;
			exchangeInfoTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					try {

						todayClear();

					} catch (Throwable e) {						
						logger.error("Clear Today Infos error!", e);

						try {
							todayClear();
						} catch (Throwable e1) {
							logger.error("Clear Today Infos[2] error!", e);
						}
					}

					logger.debug("Clear Today Infos");
				}
			}, diff, nextTime);

		} catch (Throwable ignore) {
			logger.error("[AgentService TodayExchagneInfo init error] " + ignore.getMessage(), ignore);
		}
	}
	
	@PreDestroy
	public void destroy() {
		if (connManager != null) {
			connManager.close();
		}

		if (exchangeInfoTimer != null) {
			exchangeInfoTimer.cancel();
		}
		
		logger.info("exchangeInfoTimer successful shutdown !!");
		
		if (instanceSchedulers != null) {
			for (Entry<String, ScheduledExecutorService> set : instanceSchedulers.entrySet()) {
				try {
					set.getValue().shutdownNow();					
					logger.info(set.getKey() + " scheduler shutdown!! ["+set.getValue().isShutdown()+"]");
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
		
		/*ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
		ThreadGroup parent;
		while ((parent = threadGroup.getParent()) != null) {
			if (null != threadGroup) {
				threadGroup = parent;
				if (null != threadGroup) {
					Thread[] threadList = new Thread[threadGroup.activeCount()];
					threadGroup.enumerate(threadList);
					for (Thread thread : threadList) {
						String name = thread.getName();
						if (name.indexOf("pool-") > -1) {
							try {
								thread.interrupt();
								logger.debug("== thread[" + name + "] interrupt !!");
							} catch (Throwable e) {
								logger.error("thread interrupt error " + e.getMessage());
							}
						}
					}

				}
			}
		}*/
	}
	
	public void addExchangeInfo(String agentId, String routeId, boolean success, long count) {
		String id = getRoutePrimaryKey(agentId, routeId);
		Map<String, Long> datas = getRouteDatas().get(id);
		if (datas == null) {
			datas = new HashMap<>();
			datas.put(TOTAL, 0L);
			datas.put(SUCCESS, 0L);
			datas.put(FAIL, 0L);

			getRouteDatas().put(id, datas);
		}
		
		datas.put(TOTAL, datas.get(TOTAL) + count);

		if (success) {
			datas.put(SUCCESS, datas.get(SUCCESS) + count);
		} else {
			datas.put(FAIL, datas.get(FAIL) + count);
		}
	}

	public void todayClear() {
		routeDatas.clear();
	}

	public Map<String, Map<String, Long>> getRouteDatas() {
		return routeDatas;
	}

	public void setRouteDatas(Map<String, Map<String, Long>> routeDatas) {
		this.routeDatas = routeDatas;
	}

	private void initMonitoringAgent(final MonitoringData instance) {
		if (trim(instance.getJolokiaUrl()) == null) {
			return;
		}
		
		ScheduledExecutorService instanceScheduler = Executors.newSingleThreadScheduledExecutor(
				new MonitorThreadFactory(DEFAULT_EXECUTOR_SERVICE_NAME + "_" + instance.getAgentId() + "_MonitoringScheduler"));
		instanceScheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					monitoringAgent(instance);
				} catch (Throwable e) {
					logger.error("monitoringAgent [" + instance.getAgentId() + "] error " + e.getMessage(), e);
				}
			}

		}, webUtils.getMonitorInterval(), webUtils.getMonitorInterval(), TimeUnit.SECONDS);
		instanceSchedulers.put(instance.getAgentId(), instanceScheduler);
	}
	
	public void removeAgentScheduler(String agentId) throws Throwable {

		if (instanceSchedulers.containsKey(agentId)) {
			ScheduledExecutorService instanceScheduler = instanceSchedulers.remove(agentId);
			instanceScheduler.shutdownNow();
		}
	}
	
	public void monitoringAgent(MonitoringData instance) throws Throwable {
		// 1. call api
		monitoringMasInfo(instance);
		String url = null;
		
		try {
			instance.getCamelContextInfos().clear();
			instance.getServiceStatus().clear();
			instance.getRouteStatus().clear();

			// 2. call route status
			StringBuilder sb = new StringBuilder();
			sb.append(instance.getJolokiaUrl());
			sb.append(MonitoringURL.JOLOKIA_API_READ_PREFIX);
			sb.append(MonitoringURL.APPS_INFO);
			url = sb.toString();
			String result = callGetApi(url, JOLOKIA_CONTENT_TYPE);
			if (result == null) {
				return;
			}

			JSONObject json = new JSONObject(result);
			JSONObject value = json.getJSONObject("value");

			for (String key : value.keySet()) {
				String id = null;
				Pattern ptn = Pattern.compile(MAS_APP_ID_PATTERN);
				Matcher matcher0 = ptn.matcher(key);
				if (matcher0.find()) {
					id = matcher0.group(1);
				}

				if (Strings.trim(id) != null) {
					JSONObject value0 = value.getJSONObject(key);
					if (value0.has("Status")) {
						instance.getServiceStatus().put(id, value0.getString("Status"));
					}

					if (value0.has("ApplicationType")) {
						if (MonitoringURL.CAMEL_APPTYPE.equals(value0.getString("ApplicationType"))) {
							if (value0.has("SubManagementInfos")) {
								setRouteStatusJson(instance, value0.getString("SubManagementInfos"));
							}
						}
					}
				}
			}

			instance.setLastUpdated(new Date());

		} catch (Throwable e) {
			logger.error("monitoringAgent appList " + instance.getAgentId() + ", url=" + url + " error " + e.getMessage());
		}

		datas.put(instance.getAgentId(), instance);
	}
	
	private void setRouteStatusJson(MonitoringData instance, String json) {
		// {"came-http-server":{"http-server":"Started","sendTrace":"Started"}}
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(json).getAsJsonObject();
		Map<String, String> status = new HashMap<>();
		for (Entry<String, JsonElement> entry : obj.entrySet()) {
			String camelContextId = entry.getKey();
			List<String> camelRoutes = instance.getCamelContextInfos().get(camelContextId);
			if (camelRoutes == null) {
				camelRoutes = new ArrayList<>();
				instance.getCamelContextInfos().put(camelContextId, camelRoutes);
			}
			
			JsonObject obj0 = entry.getValue().getAsJsonObject();
			for (Entry<String, JsonElement> entry0 : obj0.entrySet()) {
				String routeId = entry0.getKey();
				status.put(routeId, entry0.getValue().getAsString());
				camelRoutes.add(routeId);
			}
		}
		instance.getRouteStatus().putAll(status);		
	}
	

	private void status(MonitoringData instance, String host, int port, int timeout) {
		try {
			CommandInvoker invoker = new CommandInvoker(host, port, timeout);
			CommandResponse response = invoker.invoke(new Command.STATUS());
			if (response.isOK()) {
				String result = (String) response.getResult();
				Pattern statusPtn = Pattern.compile(CMD_STATUS_PATTERN);
				Pattern cmdAppIdPtn = Pattern.compile(CMD_APP_ID_PATTERN);
				for (String str : result.split("\n")) {
					String status = null;
					Matcher matcher = statusPtn.matcher(str);
					if (matcher.find()) {
						status = matcher.group(1);
					}

					if (str.contains("engine")) {
						instance.setStatus(status);

					} else if (str.contains("application id=")) {
						String id = null;
						Matcher matcher0 = cmdAppIdPtn.matcher(str);
						if (matcher0.find()) {
							id = matcher0.group(1);
						}
						if (Strings.trim(id) != null) {
							instance.getServiceStatus().put(id, status);
						}
					}
				}
				instance.setLastUpdated(new Date());
			}
		} catch (Throwable e) {
			logger.error("[" + host + ", " + port + "] status error " + e.getMessage(), e);
		}
	}
	
	private void setAgentInfos() throws Throwable {
		for (Agent agent : agentDao.list()) {
			MonitoringData data = new MonitoringData(agent);
			datas.put(agent.getAgentId(), data);
		}
	}

	private void monitoringMasInfo(MonitoringData instance) throws Throwable {
		StringBuilder sb = new StringBuilder();
		sb.append(instance.getJolokiaUrl());
		sb.append(MonitoringURL.JOLOKIA_API_READ_PREFIX);
		sb.append(MonitoringURL.MAS_SERVER_INFO);
		String result = null;
		String url = sb.toString();
		URI uri = new URI(url);
		String host = uri.getHost();
		int timeout = 3000;
		try {
			result = callGetApi(sb.toString(), JOLOKIA_CONTENT_TYPE);
			if (result == null) {
				throw new Exception("Call API result is null");
			}

		} catch (Throwable e) {
			logger.error("monitoringAgent url=" + url + " error " + e.getMessage());
			instance.setStatus("");
			instance.getServiceStatus().clear();

			// 1-1. call cmd status
			// status(instance, host, instance.getAdminPort(), timeout);
			return;
		}

		JSONObject json = new JSONObject(result);
		JSONObject value = json.getJSONObject("value");

		for (String key : value.keySet()) {
			JSONObject value0 = value.getJSONObject(key);
			if (value0.has("Status")) {
				instance.setStatus(value0.getString("Status"));				
			}
			if (value0.has("Id")) {
				instance.setAgentId(value0.getString("Id"));				
			}
			if (value0.has("Port")) {
				instance.setAdminPort(value0.getInt("Port"));
			}
		}
	}
	
	public String callGetApi(String url, String contentType) throws Throwable {
		CloseableHttpClient httpClient = getHttpClient();

		HttpResponse response = null;

		HttpGet getRequest = new HttpGet(url);

		getRequest.addHeader("accept", contentType);
		getRequest.addHeader("content-type", contentType);

		response = httpClient.execute(getRequest);

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			throw new APIResponseException(statusCode);
		}

		HttpEntity httpEntity = response.getEntity();
		return EntityUtils.toString(httpEntity);

	}

	private CloseableHttpClient getHttpClient() {
		return getHttpClient(-1, -1);
	}

	private CloseableHttpClient getHttpClient(int connto, int to) {

		if (connto <= 0)
			connto = webUtils.getConnectionTimeout();
		if (to <= 0)
			to = webUtils.getTimeout();

		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(connto * 1000)
				.setConnectionRequestTimeout(webUtils.getConnectionRequestTimeout() * 1000)
				.setSocketTimeout(to * 1000)
				.build();

		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setConnectionManager(connManager)
				.setDefaultRequestConfig(config)
				.build();
		return httpClient;
	}

	public List<MonitoringData> getMonitoringAgents() {
		return new ArrayList<MonitoringData>(datas.values());
	}

	public MonitoringData getMonitoringAgent(String id) {
		try {
			String agentId = null;
			for (MonitoringData instance : datas.values()) {
				if (id.startsWith(instance.getAgentId() + "_")) {
					for (String routeId : instance.getRouteStatus().keySet()) {
						if (id.equals(getRoutePrimaryKey(instance.getAgentId(), routeId))) {
							agentId = instance.getAgentId();
							break;
						}
					}
					if (agentId == null) {
						agentId = instance.getAgentId();
					}
				}
			}
			
			if (agentId == null) {
				return null;
			}
			monitoringAgent(datas.get(agentId));
			return datas.get(agentId);

		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public void save(MonitoringAgentInfo info) {		
		boolean isFromInsert = false;
		boolean isToInsert = false;
		Agent from = get(info.getFromAgentId());
		if (from == null) {
			from = new Agent();
			isFromInsert = true;
		}
		
		Gson gson = new Gson();
		// <fromRouteId, List<toRouteId>>
		Map<String, List<String>> fromRouteToList = from.getRouteToList();
		
		from.setAgentId(info.getFromAgentId());
		if (trim(info.getFromJolokiaUrl()) != null) {
			from.setJolokiaUrl(info.getFromJolokiaUrl());	
		}
		
		if (trim(info.getFromLabel()) != null) {
			from.setLabel(info.getFromLabel());	
		}
				 
		List<String> fromToList = fromRouteToList.get(info.getFromRouteId());
		if (fromToList == null) {
			fromToList = new ArrayList<String>();				
		}		
		
		
		if (trim(info.getToRouteId()) != null) {
			// agent.agentId + '_' + routeId
			String key = getRoutePrimaryKey(info.getToAgentId(), info.getToRouteId());
			if (fromToList.contains(key)) {
			} else {
				fromToList.add(key);
			}
		}
		
		fromRouteToList.put(info.getFromRouteId(), fromToList);
		
		from.setToList(gson.toJson(fromRouteToList));
		save(isFromInsert, from);
		
		if (trim(info.getToRouteId()) == null) {
			return;
		}
		
		Agent to = null;
		to = get(info.getToAgentId());
		if (to == null) {
			to = new Agent();
			isToInsert = true;
		}
		
		// toList				
		Map<String, List<String>> toRouteToList = to.getRouteToList();
		toRouteToList.put(info.getToRouteId(), new ArrayList<String>());
		to.setToList(gson.toJson(toRouteToList));
		
		to.setAgentId(info.getToAgentId());
		if (trim(info.getToJolokiaUrl()) != null) {
			to.setJolokiaUrl(info.getToJolokiaUrl());
		}
		
		if (trim(info.getToLabel()) != null) {
			to.setLabel(info.getToLabel());
		}		
		
		save(isToInsert, to);
	}	

	public void save(boolean insert, Agent agent) {
		if (insert) {
			agentDao.save(agent);

		} else {
			agentDao.update(agent);
			try {
				removeAgentScheduler(agent.getAgentId());
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}

		initMonitoringAgent(agent);
	}
	
	private void initMonitoringAgent(Agent agent) {
		MonitoringData instance = new MonitoringData(agent);
		datas.put(agent.getAgentId(), new MonitoringData(agent));
		initMonitoringAgent(instance);
	}
	
	public Agent get(String id) {
		return agentDao.get(id);
	}

	public List<Agent> list() {
		return agentDao.list();
	}

	public void delete(String agentId, String routeId) {
		Agent agent = get(agentId);
		if (agent == null) {
			return;
		}
		
		//{"rest-1":["camel-esb-perf-server_route1"],"route1":[]}
		Map<String, List<String>> routeToList = agent.getRouteToList();
		if (routeToList.containsKey(routeId)) {
			routeToList.remove(routeId);
		}
		for (String routeId0 : routeToList.keySet()) {
			List<String> list = routeToList.get(routeId0);
			list.remove(getRoutePrimaryKey(agentId, routeId));
		}
		
		// refresh monitoring data
		if (routeToList.size() == 0) {
			agentDao.delete(agentId);
			
			try {
				removeAgentScheduler(agentId);
				datas.remove(agentId);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		} else {
			Gson gson = new Gson();
			agent.setToList(gson.toJson(routeToList));
			logger.debug("============= agent " + agent.getToList());
			agentDao.update(agent);
			
			try {
				removeAgentScheduler(agentId);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
			initMonitoringAgent(agent);		
		}
	}

	public Map<String, MonitoringData> getDatas() {
		return datas;
	}
}
