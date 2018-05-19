package x.demo.station1;

import taskflow.work.Work;
import x.demo.StatusHolderBus;

public class InitStatus {

	public void init(Work bus) {
		StatusHolderBus sBus = (StatusHolderBus) bus;
		if (sBus.getStatus().equals(StatusHolderBus.STATUS_INIT)) {
			sBus.setRoutingKey("oneStop");
		} else if (sBus.getStatus().equals(StatusHolderBus.STATUS_PASSED_ONE)) {
			sBus.setRoutingKey("twoStop");
		} else if (sBus.getStatus().equals(StatusHolderBus.STATUS_PASSED_TWO)) {
			sBus.setRoutingKey("threeStop");
		} else {
			sBus.setRoutingKey("end");
			if (sBus.hasFinished()) {
				System.out.println("###Bus has finished!");
			}
		}
	}
}
