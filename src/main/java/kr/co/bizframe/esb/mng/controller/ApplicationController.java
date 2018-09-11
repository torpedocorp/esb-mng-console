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