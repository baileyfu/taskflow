package taskflow.pattern.match.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import taskflow.pattern.match.PatternMatch;

/**
 * Created by lizhou on 2017/3/14/014.
 */
public class RegexPatternMatch implements PatternMatch {
    public boolean isMatched(String pattern, String value) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(value);
        return matcher.matches();
    }

}
