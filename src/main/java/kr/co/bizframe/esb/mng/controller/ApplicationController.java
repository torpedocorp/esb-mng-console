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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

	private Logger logger = Logger.getLogger(getClass());

	@GetMapping("/downloadFile")
	public void downloadFile(@RequestParam(value = "appPath") String appPath, HttpServletRequest request, HttpServletResponse response)
			throws Throwable {
		
		appPath = URLDecoder.decode(appPath, StandardCharsets.UTF_8.toString());		
		File file = new File(appPath);
		String fileName = file.getName();
		String contentType = null;

		contentType = request.getServletContext().getMimeType(file.getAbsolutePath());

		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		InputStream myStream = new FileInputStream(file);

		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
		response.setContentType(contentType);

		// Copy the stream to the response's output stream.
		IOUtils.copy(myStream, response.getOutputStream());
		IOUtils.closeQuietly(myStream);
		response.flushBuffer();
	}
}