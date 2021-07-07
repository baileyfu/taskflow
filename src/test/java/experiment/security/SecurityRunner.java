package experiment.security;

import java.io.File;
import java.io.FileInputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import experiment.security.opt.OptReader;

public class SecurityRunner {

	public static void main(String[] args) throws Exception{
		//-Djava.security.manager 开启沙箱，并指定自定义管理器
		//这个系统属性是在当JVM启动时进行检查的。如果我们用程序手动设置该属性，并不能奏效，因为JVM已经启动了，已经过了检查系统属性的步骤了。
//		System.setProperty("java.security.manager","experiment.security.MySecurityManager");
		System.setProperty("java.security.policy","/Users/bailey/git/taskflow/src/test/java/experiment/security/custom.policy");
		
		System.out.println(System.getSecurityManager());
		System.setSecurityManager(new MySecurityManager());
		File file=new File("/Users/bailey/x.jpg");
		try(FileInputStream fis = new FileInputStream(file)){
			System.out.println(fis.read());
		}
		
		String result=AccessController.doPrivileged(new PrivilegedAction<String>() {
			@Override
			public String run() {
				OptReader or=new OptReader();
				or.read();
				return null;
			}
		});
		//只有在java.policy文件中指定权限 permission java.lang.RuntimePermission "setSecurityManager"; 才会奏效
//		System.setSecurityManager(null);
	}

}
