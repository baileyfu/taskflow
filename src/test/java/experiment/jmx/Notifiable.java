package experiment.jmx;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class Notifiable extends NotificationBroadcasterSupport implements NotifiableMBean{
	int seq;
	@Override
	public void doAndNotifySomeOne() {
		System.out.println("has done something and notify broadcaster...");
		//通知名称；谁发起的通知；序列号；发起通知时间；发送的消息
		//通知的作用是主动通知远程客户端。
		//例如我们的程序出现了异常，或者CPU使用率过高，或者程序出现了死锁等一系列问题。
		//这个时候我们希望程序能主动将这些问题发送给远程客户端，将这些问题记录下来，或者执行一些其他的报警操作
		Notification notify = new AttributeChangeNotification(this, seq++, System.currentTimeMillis(),
				"CacheSize changed", "CacheSize", "int", 0, 1);
	    sendNotification(notify);
	}
	//返回这个MBean将会发送的通知类型信息
	//只在注册该MBean的时候，调用一次，用于获取该MBean为特定类型的通知 生成的不同通知的特征
	@Override
	public MBeanNotificationInfo[] getNotificationInfo() {
		String[] types = new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE };
        String name = AttributeChangeNotification.class.getName();
        String description = "An attribute of this MBean has changed";
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name,
                description);
        return new MBeanNotificationInfo[] { info };
	}
}
