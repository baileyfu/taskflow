package x.demo.station1;

import taskflow.task.Task;
import taskflow.work.Work;
import x.demo.StatusHolderBus;

public class StationOne implements Task{

	public void doBusiness(Work bus) {
		StatusHolderBus sBus=(StatusHolderBus)bus;
		String routingKey="twoStop";
        try {
        	System.out.println("Step One begging...");
        	Thread.sleep(3000l);
        	//Success
        	sBus.setStatus(StatusHolderBus.STATUS_PASSED_ONE);
    		System.out.println("Step One successed!");
        }catch(Exception e) {
        	System.out.println("Step One Failed!");
        	routingKey="break";
        }
        sBus.setRoutingKey(routingKey);
    }
	
	@Override
	public String getName() {
		return "StationOne";
	}

}
