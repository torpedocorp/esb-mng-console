package kr.co.bizframe.esb.mng.type;

import java.util.EnumSet;

public enum Status {
	Disabled(1),
	Enabled(0);

	private int val;

	private Status(int val) {
		this.val = val;
	}

	public int getValue() {
		return val;
	}

	public static Status valueOfCode(int code) {
		for (Status type : EnumSet.allOf(Status.class)) {
			if (type.getValue() == code) {
				return type;
			}
		}
		return Status.Disabled;
	}

}
