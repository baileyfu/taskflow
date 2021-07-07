package taskflow.routing.match;

import java.util.HashMap;
import java.util.Map;

import taskflow.exception.TaskFlowException;

/**
 * 创建PatternMatch的工厂
 * Created by lizhou on 2017/3/14/014.
 */
public class PatternFactory {
    // do not update map, after initialized
    private static Map<PatternType, PatternMatch> patternMatchMap = new HashMap<PatternType, PatternMatch>();

    static {
        for (PatternType patternType : PatternType.values()) {
            Class<? extends PatternMatch> clazz = patternType.getClazz();
            try {
            	patternMatchMap.put(patternType, clazz.newInstance());
            } catch (Exception e) {
                throw new TaskFlowException("PatternFactory init error,can not newInstance " + clazz.getName());
            }
        }
    }

    public static PatternMatch getPatternMatch(PatternType patternType) {
        PatternMatch ret = patternMatchMap.get(patternType);
        if (ret == null) {
            throw new TaskFlowException("not find " + patternType + "in PatternFactory!");
        }
        return ret;
    }

    public static void checkPattern(PatternType patternType) {
        if (!patternMatchMap.containsKey(patternType)) {
            throw new TaskFlowException("not find " + patternType + "in PatternFactory!");
        }
    }

}
