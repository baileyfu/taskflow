package taskflow.routing.match;

/**
 * Created by lizhou on 2017/3/14/014.
 */
public class StringPatternMatch implements PatternMatch {
    public boolean isMatched(String pattern, String value) {
        return pattern == null ? value == null : pattern.equals(value);
    }
}
