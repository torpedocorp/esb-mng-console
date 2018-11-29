/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe esb-mng-console project licenses this file to you under the Apache License,     
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:                   
 *                                                                              
 *   http://www.apache.org/licenses/LICENSE-2.0                                 
 *                                                                              
 * Unless required by applicable law or agreed to in writing, software          
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the     
 * License for the specific language governing permissions and limitations      
 * under the License.                                                           
 */ 

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