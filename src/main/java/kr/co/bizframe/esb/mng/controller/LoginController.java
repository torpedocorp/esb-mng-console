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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.bizframe.esb.mng.model.Login;
import kr.co.bizframe.esb.mng.type.Constants;
import kr.co.bizframe.esb.mng.utils.HashEncryption;
import kr.co.bizframe.esb.mng.utils.WebUtils;

/**
 * Login
 * @author bumma
 *
 */
@Controller
public class LoginController {

	private Logger logger = Logger.getLogger(getClass());

	@ResponseBody
	@RequestMapping(value = "/api/login", method = RequestMethod.POST)
	public Map<String, Object> login(@RequestBody Login vo, HttpServletRequest request) throws Exception {
		logger.info(vo.toString());
		HttpSession session = request.getSession();
		String userid = vo.getLoginid();
		String pass = vo.getLoginpw();

		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		if (userid == null) {
			jsonResponse.put("result", "101");
			WebUtils.logout(session);
			return jsonResponse;

		}

		if (pass == null) {
			jsonResponse.put("result", "102");
			WebUtils.logout(session);
			return jsonResponse;
		}

		if (!Constants.ADMIN_USER_ID.equals(userid)) {
			jsonResponse.put("result", "101");
			WebUtils.logout(session);
		}

		if (!HashEncryption.getInstance().encryptSHA1(pass).equals(Constants.ADMIN_USER_PASS)) {
			jsonResponse.put("result", "103");
			return jsonResponse;
		}

		jsonResponse.put("userId", userid);
		WebUtils.login(session, userid, pass);
		jsonResponse.put("result", "001");

		logger.debug("result = " + jsonResponse);
		return jsonResponse;
	}

	@ResponseBody
	@RequestMapping(value = "/api/logout", method = RequestMethod.GET)
	public Map<String, Object> logout(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
		HttpSession session = request.getSession();
		WebUtils.logout(session);

		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		jsonResponse.put("result", "001");
		return jsonResponse;
	}

}
