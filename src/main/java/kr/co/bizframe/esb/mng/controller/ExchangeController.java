package kr.co.bizframe.esb.mng.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.bizframe.esb.mng.dao.ExchangeInfoDao;
import kr.co.bizframe.esb.mng.model.PagingModel;
import kr.co.bizframe.esb.mng.model.SearchOptions;
import kr.co.bizframe.esb.mng.model.trace.ExchangeInfo;

@RestController
public class ExchangeController {

	@Autowired
	private ExchangeInfoDao exchangeInfoDao;

	/*---get all msgs---*/
	@PostMapping("/exchanges")
	public ResponseEntity<Map<String, Object>> list(@RequestBody SearchOptions options) throws Throwable {
		PagingModel<ExchangeInfo> vo = exchangeInfoDao.pagedList(options);
		if (vo == null) {
			throw new Exception("/exchanges api error ");
		}
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		jsonResponse.put("messages", vo.getModels());
		jsonResponse.put("totalRows", vo.getCount());
		return ResponseEntity.ok().body(jsonResponse);
	}
	
	/*---get all msgs---*/
	@PostMapping("/statistic/exchanges")
	public ResponseEntity<Map<String, Object>> statistic(@RequestBody SearchOptions options) throws Throwable {		
		Map<String, Object> jsonResponse = new HashMap<String, Object>();		
		jsonResponse.put("messages", exchangeInfoDao.getExchageStatisticInfos(options));
		return ResponseEntity.ok().body(jsonResponse);
	}
}