package taskflow.constants;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

public class PropertyNameAndValue {

	public static final String WORK_NO_EXISTS_IGNORABLE = "env.taskflow.ignoreNoExists";
	public static final String WORK_MAX_TASKS = "env.taskflow.work.maxTasks";
	public static final String WORK_TRACEABLE = "env.taskflow.work.traceable";
	public static final String TASK_ASYNC_TIMEOUT = "env.taskflow.task.asyncTimeOut";
	public static final String RELOAD_ENABLE = "env.taskflow.reload.enable";

	public static final String LOG_PRINTABLE = "env.taskflow.log.printable";
	public static final String LOG_PRINT_DETAIL = "env.taskflow.log.printDetail";

	static {
		if (System.getProperty(WORK_NO_EXISTS_IGNORABLE) == null) {
			System.setProperty(WORK_NO_EXISTS_IGNORABLE, "false");
		}
		if (System.getProperty(WORK_MAX_TASKS) == null) {
			System.setProperty(WORK_MAX_TASKS, "1000");
		}
		if (System.getProperty(WORK_TRACEABLE) == null) {
			System.setProperty(WORK_TRACEABLE, "false");
		}
		if (System.getProperty(TASK_ASYNC_TIMEOUT) == null) {
			System.setProperty(TASK_ASYNC_TIMEOUT, "30000");
		}
		if (System.getProperty(RELOAD_ENABLE) == null) {
			System.setProperty(RELOAD_ENABLE, "true");
		}
		if (System.getProperty(LOG_PRINTABLE) == null) {
			System.setProperty(LOG_PRINTABLE, "true");
		}
		if (System.getProperty(LOG_PRINT_DETAIL) == null) {
			System.setProperty(LOG_PRINT_DETAIL, "false");
		}
	}
	public static void init() {}
	public static void setProperties(Function<String, String> getter) {
		if (getter != null) {
			String ignorable = getter.apply("taskflow.ignoreNoExists");
			if (!StringUtils.isEmpty(ignorable)) {
				System.setProperty(WORK_NO_EXISTS_IGNORABLE, ignorable);
			}
			String traceable = getter.apply("taskflow.work.traceable");
			if (!StringUtils.isEmpty(traceable)) {
				System.setProperty(WORK_TRACEABLE, traceable);
			}
			String maxTasks = getter.apply("taskflow.work.maxTasks");
			if (!StringUtils.isEmpty(maxTasks)) {
				System.setProperty(WORK_MAX_TASKS, maxTasks);
			}
			String asyncTimeOut = getter.apply("taskflow.task.asyncTimeOut");
			if (StringUtils.isNumeric(asyncTimeOut)) {
				System.setProperty(TASK_ASYNC_TIMEOUT, asyncTimeOut);
			}
			String reloadable = getter.apply("taskflow.reload.enable");
			if (!StringUtils.isEmpty(reloadable)) {
				System.setProperty(RELOAD_ENABLE, reloadable);
			}
			String printalbe = getter.apply("taskflow.log.printable");
			if (!StringUtils.isEmpty(printalbe)) {
				System.setProperty(LOG_PRINTABLE, printalbe);
			}
			String printdetail = getter.apply("taskflow.log.printDetail");
			if (!StringUtils.isEmpty(printdetail)) {
				System.setProperty(LOG_PRINT_DETAIL, printdetail);
			}
		}
	}
}
