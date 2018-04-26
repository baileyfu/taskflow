package x.demo;

import taskflow.bus.DefaultBus;

public class StatusHolderBus extends DefaultBus {

	public static final String STATUS_INIT="INIT";
	public static final String STATUS_PASSED_ONE="ONE";
	public static final String STATUS_PASSED_TWO="TWO";
	public static final String STATUS_PASSED_THREE="THREE";
	public static final String STATUS_PASSED_END="END";
	
	private String userName;
	private String status;

	public StatusHolderBus() {
		status = STATUS_INIT;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public boolean hasFinished() {
		return status.equals(STATUS_PASSED_END);
	}
	
	public String getCacheName(String id) {
		System.out.println("INVOKE.....................");
		return "KAISER";
	}
}
