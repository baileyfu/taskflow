package x.demo;

import taskflow.work.CustomRouteWork;
import x.demo.station1.InitStatus;

public class StatusHolderBus extends CustomRouteWork {

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
	
	public StatusHolderBus(String userName) {
		this.userName=userName;
		this.status = STATUS_INIT;
	}
	
	public StatusHolderBus(String userName,int times,InitStatus is) {
		this.userName=userName;
		System.out.println("times : "+times+" - InitStatus : "+is);
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
	public boolean hasFinished() {
		return status.equals(STATUS_PASSED_END);
	}
	public String getCacheName(String id) {
		System.out.println("INVOKE.....................");
		return "KAISER";
	}
}
