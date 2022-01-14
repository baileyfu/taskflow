package demo.work;

import java.util.Map;

import taskflow.work.SequentialRouteWork;

public class MySequentialWork extends SequentialRouteWork {
	private String tagName;
	private int type;

	public MySequentialWork() {
	}

	public MySequentialWork(Map<String,String> extraArgsMap) {
		super(extraArgsMap);
	}
	
	public MySequentialWork(String tagName, int type,Map<String,String> extraArgsMap) {
		super(extraArgsMap);
		this.tagName = tagName;
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
}
