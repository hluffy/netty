package com.dkvan.netty.netty.test;

public class IBeacon {
	private Integer minor;
	private String uuid;
	private Double longitude;
	private Double latitude;
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Integer getMinor() {
		return minor;
	}
	public void setMinor(Integer minor) {
		this.minor = minor;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	

}
