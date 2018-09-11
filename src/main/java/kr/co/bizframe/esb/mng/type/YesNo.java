package kr.co.bizframe.esb.mng.type;

public enum YesNo {
	Yes(1),
	No(0);

	private int val;

	private YesNo(int val) {
		this.val = val;
	}

	public int getValue() {
		return val;
	}
	
	public String getStringValue() {
		return String.valueOf(val);
	}
}
