package kr.co.bizframe.esb.mng.service;

import static kr.co.bizframe.esb.mng.type.Constants.TRACE_DB_KEY;
import static kr.co.bizframe.esb.mng.type.Constants.TRANSACTIONMANAGER_NAME;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import kr.co.bizframe.esb.mng.dao.ExchangeInfoDao;
import kr.co.bizframe.esb.mng.model.trace.ExchangeInfo;

@WebServlet(name = "traceServlet", urlPatterns = { "/trace" })
public class AgentTraceServlet extends HttpServlet {

	private static final long serialVersionUID = 2534142558974517722L;
	private Logger logger = Logger.getLogger(getClass());
	
	private WebApplicationContext springContext;
	
	@Autowired
	private ExchangeInfoDao exchangeInfoDao;

	@Autowired
	private AgentService agentService;

	
	@Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        final AutowireCapableBeanFactory beanFactory = springContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(this);
    }
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		OutputStream out = null;
		String result = "";
		String requestBody = null;
		int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

		try {
			out = response.getOutputStream();
			response.setContentType("text/html");

			if (request.getInputStream().available() > 0) {
				requestBody = convertStreamToString(request.getInputStream());
			}
			List<ExchangeInfo> infos = parseReceiveJson(requestBody);

			List<String> savedIds = new ArrayList<String>();
			for (ExchangeInfo info : infos) {
				try {
					saveExchangeInfo(info);
					savedIds.add(info.getExchangeId());
				} catch (Throwable e) {
					logger.error("=====finishedExchange insert error : " + info, e);
				}
			}

			status = HttpServletResponse.SC_OK;
			result = new Gson().toJson(savedIds);

		} catch (Throwable e) {
			logger.error("AgentTraceServlet doPost Error " + e.getMessage(), e);

		} finally {
			try {
				response.setStatus(status);
				out.write(result.getBytes());
				out.flush();
				out.close();
				logger.debug("============= receive msg = " + requestBody + "...." + " output " + result + " send");
			} catch (Throwable e) {
				throw new ServletException(e);
			}
		}
	}
	
	private List<ExchangeInfo> parseReceiveJson(String json) {
		Gson gson = new Gson();
		Type listType = new TypeToken<List<ExchangeInfo>>() {}.getType();
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(json).getAsJsonObject();
		String agentId = obj.get("agentId").getAsString();
		String dataJson = obj.get("data").getAsJsonArray().toString();
		List<ExchangeInfo> infos = gson.fromJson(dataJson, listType);
		for (ExchangeInfo info : infos) {
			info.setId(UUID.randomUUID().toString());
			info.setAgentId(agentId);
		}
		return infos;
	}
	
	@Transactional(transactionManager = TRACE_DB_KEY + TRANSACTIONMANAGER_NAME)
	public void saveExchangeInfo(ExchangeInfo info) {
		// put memory
		String agentId = info.getAgentId();
		String routeId = info.getRouteId();
		agentService.addExchangeInfo(agentId, routeId, info.isSuccess(), 1);
		
		exchangeInfoDao.save(info);
	}
	
	private String convertStreamToString(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

}
