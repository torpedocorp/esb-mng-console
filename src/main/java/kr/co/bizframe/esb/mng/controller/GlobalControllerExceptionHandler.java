package kr.co.bizframe.esb.mng.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
	private Logger logger = Logger.getLogger(getClass());

	@ExceptionHandler(value = { Throwable.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected void handleConflict(Throwable ex) {
		logger.error("GlobalControllerExceptionHandler error " + ex.getMessage(), ex);
	}
}
