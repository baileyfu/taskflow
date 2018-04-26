package taskflow.worker;

import com.alibaba.fastjson.JSON;

import taskflow.context.BusContext;
import taskflow.context.BusPathRecord;
import taskflow.context.MapBusContext;
import taskflow.exception.MaxPathException;
import taskflow.task.StationRoutingWrap;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 任务串行执行
 */
public class DefaultWorker implements Worker {
    private Object start;
    private int maxPath;

    private Object exception;
    private Object finish;
    private BusContext busContext;

    private List<BusPathRecord> busPathRecords = new ArrayList<BusPathRecord>();

    private int arriveStationNums;

    public DefaultWorker() {
        busContext = new MapBusContext();
    }

    public BusContext run() {
        try {
            if (!(start instanceof StationRoutingWrap)) {
                throw new IllegalArgumentException("worker start must be <tf:task id=\"\">");
            }
            ((StationRoutingWrap) start).doBusiness(this);
        } catch (Exception e) {
            dealExcpetion(e);
        } finally {
            if (finish != null && finish instanceof StationRoutingWrap) {
                ((StationRoutingWrap) finish).doBusiness(this);
            }
        }
        return busContext;
    }

    public void dealExcpetion(Exception e) {
        busContext.holderException(e);
        if (exception != null && exception instanceof StationRoutingWrap) {
            ((StationRoutingWrap) exception).doBusiness(this);
        } else {
            System.err.println(e.getMessage());
        }
    }

    public void arrive(StationRoutingWrap stationRoutingWrap) throws Exception {
        if (maxPath <= arriveStationNums++) {
            throw new MaxPathException("max path is:" + maxPath);
        }
        busPathRecords.add(new BusPathRecord(stationRoutingWrap.getName(), JSON.toJSONString(getBusContext())));
    }

    public BusContext getBusContext() {
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

    public void setBusContext(BusContext busContext) {
        this.busContext = busContext;
    }

    public void setRoutingKey(String key) {
        busContext.setRoutingKey(key);
    }
}
