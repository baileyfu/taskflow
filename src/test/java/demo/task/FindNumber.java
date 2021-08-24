package demo.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import demo.service.NumberService;
import taskflow.annotation.Taskparam;
import taskflow.work.Work;
import taskflow.work.context.WorkContext;

/**
 * 推荐Task不涉及业务逻辑;在Task的方法中调用Service完成相应的任务<br/>
 * Task的作用是将若干Service的方法自由组合成适当粒度的功能模块
 * 
 * @author bailey
 * @date 2021年8月19日
 */
public class FindNumber {
	@Autowired
	private NumberService numberService;
	
	public void setNumberService(NumberService numberService) {
		this.numberService = numberService;
	}

	public void findMax(Work work) {
		List<Integer> input = work.getContext("intList");
		int maxValue = numberService.findMax(input);
		work.putContext("maxValue", maxValue);
	}

	public void findMin(Work work) {
		List<Integer> input = work.getContext("intList");
		int minValue = numberService.findMin(input);
		work.putContext("minValue", minValue);
	}

	public void getDiff(@Taskparam("maxValue") int a, int minValue,WorkContext workContext) {
		int diff = workContext.getRuntimeArgsJSON().getInteger("threshold");
        if (numberService.checkNumber(a, minValue, diff)) {
            workContext.setRoutingKey("ok");
        } else {
            workContext.setRoutingKey("no");
        }
    }

	public void soutOutOk(WorkContext workContext) {
		String show=workContext.getRuntimeArgs();
		System.out.println("OK_"+show);
	}

	public void soutOutNo(WorkContext workContext) {
		String show=workContext.getRuntimeArgs();
		System.out.println("NO_"+show);
	}

}
