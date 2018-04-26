package taskflow.pattern;

import taskflow.pattern.match.impl.RegexPatternMatch;
import taskflow.pattern.match.impl.StringPatternMatch;

/**
 * Created by lizhou on 2017/3/14/014.
 */
public enum PatternType {
    regex(RegexPatternMatch.class),
    string(StringPatternMatch.class);

    private Class clazz;

    PatternType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }
}
