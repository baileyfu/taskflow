package experiment.agent;

import java.util.List;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

public class AttachAgentRunner {

	static void sayHello(String name) {
		System.out.println(name+" Hello World!");
	}
	static {
		sayHello("static block");
	}
	public static void main(String[] args) throws Exception{
		sayHello("main method");
		System.out.println("running JVM start ");
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor vmd : list) {
            //如果虚拟机的名称为 xxx 则 该虚拟机为目标虚拟机，获取该虚拟机的 pid
            //然后加载 agent.jar 发送给该虚拟机
            System.out.println("VM..."+vmd.displayName());
            if (vmd.displayName().endsWith("experiment.agent.AgentRunner")) {
                VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                virtualMachine.loadAgent("/Users/bailey/Downloads/premain.jar");
                virtualMachine.detach();
            }
        }
        Thread.sleep(1000*5);
        sayHello("after main method");
	}

}
