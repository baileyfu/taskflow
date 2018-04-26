package taskflow.work;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import taskflow.context.WorkContext;
import taskflow.context.TaskTrace;
import taskflow.context.MapWorkContext;
import taskflow.exception.TaskFlowException;
import taskflow.task.TaskRoutingWrap;

/**
 * <p>
 * 任务串行执行
 */
public class DefaultWork implements Work {
    private Object start;
    private int maxPath;

    private Object exception;
    private Object finish;
    private WorkContext busContext;

    private List<TaskTrace> busPathRecords = new ArrayList<TaskTrace>();

    private int arriveStationNums;

    public DefaultWork() {
        busContext = new MapWorkContext();
    }

    public WorkContext run() {
        try {
            if (!(start instanceof TaskRoutingWrap)) {
                throw new IllegalArgumentException("worker start must be <tf:task id=\"\">");
            }
            ((TaskRoutingWrap) start).doBusiness(this);
        } catch (Exception e) {
            dealExcpetion(e);
        } finally {
            if (finish != null && finish instanceof TaskRoutingWrap) {
                ((TaskRoutingWrap) finish).doBusiness(this);
            }
        }
        return busContext;
    }

    public void dealExcpetion(Exception e) {
        busContext.holderException(e);
        if (exception != null && exception instanceof TaskRoutingWrap) {
            ((TaskRoutingWrap) exception).doBusiness(this);
        } else {
            System.err.println(e.getMessage());
        }
    }

    public void arrive(TaskRoutingWrap stationRoutingWrap) throws Exception {
        if (maxPath <= arriveStationNums++) {
            throw new TaskFlowException("max path is:" + maxPath);
        }
        busPathRecords.add(new TaskTrace(stationRoutingWrap.getName(), JSON.toJSONString(getBusContext())));
    }

    public WorkContext getBusContext() {
        return busContext;
    }

    public Object getStart() {
        return start;
    }

    public void setStart(Object start) {
        this.start = start;
    }

    public Object getException() {
        return exception;
    }

    public void setException(Object exception) {
        this.exception = exception;
    }

    public Object getFinish() {
        return finish;
    }

    public void setFinish(Object finish) {
        this.finish = finish;
    }


    public int getArriveStationNums() {
        return arriveStationNums;
    }

    public void setArriveStationNums(int arriveStationNums) {
        this.arriveStationNums = arriveStationNums;
    }

    public int getMaxPath() {
        return maxPath;
    }

    public void setMaxPath(int maxPath) {
        this.maxPath = maxPath;
    }

    public void putContext(String key, Object value) {
        busContext.put(key, value);
    }

    public Object getContextValue(String key) {
        return busContext.getValue(key);
    }

    public void setBusContext(WorkContext busContext) {
        this.busContext = busContext;
    }

    public void setRoutingKey(String key) {
        busContext.setRoutingKey(key);
    }
}
