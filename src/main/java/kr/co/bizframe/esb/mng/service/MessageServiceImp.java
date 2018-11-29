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

package kr.co.bizframe.esb.mng.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.bizframe.esb.mng.dao.MessageDao;
import kr.co.bizframe.esb.mng.model.PagingModel;
import kr.co.bizframe.esb.mng.model.SearchOptions;
import kr.co.bizframe.esb.mng.model.trace.TraceMessage;

@Service
public class MessageServiceImp implements MessageService {

	@Autowired
	private MessageDao messageDao;

	@Override
	public TraceMessage get(String id) {
		return messageDao.get(id);
	}

	@Override
	public PagingModel<TraceMessage> paging(SearchOptions options) {
		return messageDao.paging(options);
	}

	@Override
	public List<TraceMessage> exchangeTraces(SearchOptions options) {
		return messageDao.exchangeTraces(options);
	}

}
