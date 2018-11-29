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

package kr.co.bizframe.esb.mng.utils;

import static kr.co.bizframe.esb.mng.utils.Strings.trim;

import kr.co.bizframe.esb.mng.model.SearchOptions;

public class PagingUtil {

	public static void getPageArgs(SearchOptions request) throws Throwable {

		int curPage = request.getCurPage();
		int pageCnt = request.getPageCnt();
		int itemCnt = request.getItemCnt();
		curPage = curPage == 0 ? 1 : curPage;
		pageCnt = pageCnt == 0 ? 5 : pageCnt;
		itemCnt = itemCnt == 0 ? 10 : itemCnt;
		request.setIndex((curPage - 1) * itemCnt);
		request.setLimit(itemCnt);

		String today = TimeUtil.getCurrentDateTime("yyyy-MM-dd");
		if (Strings.trim(request.getF_date()) != null) {
			String strFromDate = trim(request.getF_date(), today);
			request.setFromDate(strFromDate);
		}

		if (Strings.trim(request.getT_date()) != null) {
			String strToDate = trim(request.getT_date(), today);
			request.setToDate(strToDate);
		}
	}

}
