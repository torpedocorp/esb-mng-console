package kr.co.bizframe.esb.mng.service;

public class APIResponseException extends RuntimeException {

	private static final long serialVersionUID = -657398643605793252L;
	private int statusCode;

	public APIResponseException(int statusCode) {
		super("Failed with HTTP error code : " + statusCode);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

}
