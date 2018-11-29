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

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

public class HashEncryption {

	private static HashEncryption instance = new HashEncryption();

	public static HashEncryption getInstance() {
		return instance;
	}

	public String encryptSHA1(String plainText) throws Exception {
		MessageDigest md = null;

		md = MessageDigest.getInstance("SHA");
		md.update(plainText.getBytes("UTF-8"));

		byte raw[] = md.digest();

		String hash = Base64.encodeBase64String(raw);
		return hash;
	}
	
}
