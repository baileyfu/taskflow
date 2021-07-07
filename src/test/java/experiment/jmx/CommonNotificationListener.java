package experiment.jmx;

import javax.management.Notification;
import javax.management.NotificationListener;

public class CommonNotificationListener implements NotificationListener{
	@Override
	public void handleNotification(Notification notification, Object handback) {
		System.out.println("Received..."+handback);
		System.out.println("from:"+notification.getSource());
		System.out.println("message:"+notification.getMessage());
		if(handback instanceof Hello) {
			((Hello)handback).say(notification.getMessage());
		}
	}
}
