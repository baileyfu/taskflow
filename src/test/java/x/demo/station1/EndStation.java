package x.demo.station1;

import taskflow.work.Work;
import x.demo.StatusHolderBus;

public class EndStation {

	public void over(Work bus) {
		StatusHolderBus sBus=(StatusHolderBus)bus;
		if(sBus.getStatus().equals(StatusHolderBus.STATUS_PASSED_THREE)) {
			sBus.setStatus(StatusHolderBus.STATUS_PASSED_END);
		}
        System.out.println("UserName:"+sBus.getUserName()+" has already fineshed the work flow!");
        bus.getWorkContext().setResult("OVER");
    }
}
