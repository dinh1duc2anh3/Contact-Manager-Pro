package com.hello.shared.enums;

public enum Address {
	HA_NOI("Hà Nội"),
	HAI_PHONG("Hải Phòng"),
	NGHE_AN("Nghệ An"),
	DA_NANG("Đà Nẵng"),
	HO_CHI_MINH("Hồ Chí Minh"),
	CAN_THO("Cần Thơ");
	
	private final String displayName;

	Address(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return displayName;
	}
}
