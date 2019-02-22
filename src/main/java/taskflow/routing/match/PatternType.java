package taskflow.routing.match;

/**
 * Created by lizhou on 2017/3/14/014.
 */
public enum PatternType {
    regex(RegexPatternMatch.class),
    string(StringPatternMatch.class);

    private Class<? extends PatternMatch> clazz;

    PatternType(Class<? extends PatternMatch> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends PatternMatch> getClazz() {
        return clazz;
    }
}
