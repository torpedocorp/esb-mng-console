package kr.co.bizframe.esb.mng.model;

public class Login {

	private String loginid;
	private String loginpw;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Login [loginid=");
		builder.append(loginid);
		builder.append(", loginpw=");
		builder.append(loginpw);
		builder.append("]");
		return builder.toString();
	}

	public String getLoginid() {
		return loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	public String getLoginpw() {
		return loginpw;
	}

	public void setLoginpw(String loginpw) {
		this.loginpw = loginpw;
	}

}
