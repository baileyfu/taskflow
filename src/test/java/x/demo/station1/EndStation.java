package x.demo.station1;

import taskflow.bus.Bus;
import taskflow.task.Task;
import x.demo.StatusHolderBus;

public class EndStation implements Task{

	public void over(Bus bus) {
		StatusHolderBus sBus=(StatusHolderBus)bus;
		if(sBus.getStart().equals(StatusHolderBus.STATUS_PASSED_THREE)) {
			sBus.setStatus(StatusHolderBus.STATUS_PASSED_END);
		}
        System.out.println("UserName:"+sBus.getUserName()+" has already fineshed the work flow!");
    }
	
	@Override
	public String getName() {
		return "End";
	}

}
