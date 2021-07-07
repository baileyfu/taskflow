package experiment.jmx;

import javax.management.Notification;

public class CommonNotificationFilter implements javax.management.NotificationFilter{
	private static final long serialVersionUID = 1428344484037973195L;

	@Override
	public boolean isNotificationEnabled(Notification notification) {
		return notification.getMessage()!=null;
	}
}
