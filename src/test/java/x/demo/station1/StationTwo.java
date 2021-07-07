package x.demo.station1;

import taskflow.task.Task;
import taskflow.work.Work;
import x.demo.StatusHolderBus;

public class StationTwo implements Task{

	public void execute(Work bus) {
		StatusHolderBus sBus=(StatusHolderBus)bus;
		String routingKey="threeStop";
        try {
        	System.out.println("Step Two begging...--->"+bus.getWorkContext().getRuntimeArgs());
        	Thread.sleep(3000l);
        	if(bus!=null) {
        		//throw new Exception("step two break!");
        	}
        	//Success
        	sBus.setStatus(StatusHolderBus.STATUS_PASSED_TWO);
    		System.out.println("Step Two successed!");
        }catch(Exception e) {
        	System.out.println("Step Two Failed!");
        	routingKey="break";
        }
        sBus.setRoutingKey(routingKey);
    }
}
