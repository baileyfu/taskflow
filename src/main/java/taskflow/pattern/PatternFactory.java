package taskflow.pattern;

import java.util.HashMap;
import java.util.Map;

import taskflow.exception.TaskFlowException;
import taskflow.pattern.match.PatternMatch;

/**
 * 创建PatternMatch的工厂
 * Created by lizhou on 2017/3/14/014.
 */
public class PatternFactory {
    // do not update map, after initialized
    private static Map<PatternType, PatternMatch> patternMatchMap = new HashMap<PatternType, PatternMatch>();

    static {
        for (PatternType patternType : PatternType.values()) {
            Class clazz = patternType.getClazz();
            try {
                Object o = clazz.newInstance();
                if (o instanceof PatternMatch) {
                    patternMatchMap.put(patternType, (PatternMatch) o);
                } else {
                    throw new TaskFlowException(o.getClass().getName() + " not instanceof PatternMatch");
                }
            } catch (InstantiationException e) {
                throw new TaskFlowException("PatternFactory init error,can not newInstance " + clazz.getName());
            } catch (IllegalAccessException e) {
                throw new TaskFlowException("PatternFactory init error,can not newInstance " + clazz.getName());
            }

            ;
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
