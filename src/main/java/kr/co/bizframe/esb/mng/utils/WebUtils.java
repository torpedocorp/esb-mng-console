package kr.co.bizframe.esb.mng.utils;

import java.awt.TrayIcon.MessageType;
import java.io.File;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import kr.co.bizframe.esb.mng.type.Constants;

@Component
public class WebUtils {

	public final static Logger logger = Logger.getLogger(WebUtils.class);

	@Value("${file.save.dir}")
	private String dir;

	@Value("${hawtio.url}")
	private String hawtioUrl;

	// ====for Monitoring API===============
	@Value("${rest.timeout:30}")
	private int timeout = 30;

	@Value("${rest.connection.timeout:10}")
	private int connectionTimeout = 10;

	@Value("${rest.connection.request.timeout:30}")
	private int connectionRequestTimeout = 30;

	@Value("${rest.connection.maxtotal:50}")
	private int connectionMaxTotal = 50;

	@Value("${rest.connection.defaultmaxperroute:25}")
	private int connectionDefaultMaxPerRoute = 25;

	@Value(value = "${monitor.interval:5}")
	private int monitorInterval;

	// ====for Monitoring API===============

	public int getConnectionMaxTotal() {
		return connectionMaxTotal;
	}

	public int getMonitorInterval() {
		return monitorInterval;
	}

	public void setConnectionMaxTotal(int connectionMaxTotal) {
		this.connectionMaxTotal = connectionMaxTotal;
	}

	public int getConnectionDefaultMaxPerRoute() {
		return connectionDefaultMaxPerRoute;
	}

	public void setConnectionDefaultMaxPerRoute(int connectionDefaultMaxPerRoute) {
		this.connectionDefaultMaxPerRoute = connectionDefaultMaxPerRoute;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public String getDir() {
		return dir;
	}

	public String getHawtioUrl() {
		return hawtioUrl;
	}

	public static void login(HttpSession session, String userId, String passwd) {
		session.setMaxInactiveInterval(99999999);
		setSessionValue(session, userId, passwd, "true");
	}

	public static void logout(HttpSession session) {
		setSessionValue(session, null, null, null);
		session.invalidate();
	}

	private static void setSessionValue(HttpSession session, String userId, String passwd, String status) {
		session.setAttribute(Constants.SESSION_USERID, userId);
		session.setAttribute(Constants.SESSION_PASSWD, passwd);
		session.setAttribute(Constants.SESSION_STATUS, status);
	}

	public String getMessageFilePath() throws Throwable {
		String currentString = TimeUtil.getCurrentDateTime("yyyyMMdd");

		StringBuffer sb = new StringBuffer();
		sb.append(currentString.substring(0, 4));
		sb.append("/");
		sb.append(currentString.substring(4, 6));
		sb.append("/");
		sb.append(currentString.substring(6, 8));

		return getMessageFilePath0(sb.toString());
	}

	public String getMessageFilePath0(String reqRes) throws Throwable {
		File saveDir = getFileSaveDirectory(dir, "message/" + reqRes);
		return saveDir.getAbsolutePath();
	}

	private static File getFileSaveDirectory(String savePath, String folderName) throws Throwable {
		folderName = Strings.trim(folderName);
		if (folderName == null) {
			throw new Exception("Id does not allow Null");
		}
		if (savePath == null) {
			throw new Exception("SavePath does not allow Null");
		}

		File saveDir = new File(savePath, folderName);
		if (!saveDir.exists()) {
			boolean result = saveDir.mkdirs();
			logger.debug(saveDir.getCanonicalPath() + " make directory result = " + result);
		}
		return saveDir;
	}

	public static String getMessageFileName(MessageType type, String method, String id) {
		return type.name() + "_" + method + "_" + id;
	}
	
	public static String getRoutePrimaryKey(String agentId, String routeId) {
		return agentId + "_" + routeId;
	}
}
