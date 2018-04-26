package x.demo.station1;

import taskflow.task.Task;
import taskflow.worker.Worker;
import x.demo.StatusHolderBus;

public class InitStatus implements Task {

	public void init(Worker bus) {
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

	@Override
	public String getName() {
		return "InitStatus";
	}

}
