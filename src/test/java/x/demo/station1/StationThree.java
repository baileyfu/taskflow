package x.demo.station1;

import taskflow.task.Task;
import taskflow.work.Work;
import x.demo.StatusHolderBus;

public class StationThree implements Task{

	public void execute(Work bus) {
		StatusHolderBus sBus=(StatusHolderBus)bus;
        try {
        	System.out.println("Step Three begging...");
        	Thread.sleep(3000l);
        	if(bus!=null) {
        		throw new Exception();
        	}
        	//Success
        	sBus.setStatus(StatusHolderBus.STATUS_PASSED_THREE);
    		System.out.println("Step Three successed!");
        }catch(Exception e) {
        	System.out.println("Step Three Failed!");
        	sBus.setRoutingKey("break");
        }
    }
	
	@Override
	public String getName() {
		return "StationThree";
	}

}
