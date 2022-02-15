package cambio.tltea.parser.core.temporal;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TemporalPropositionParser {


    private static final List<String> infList = List.of("inf", "infinity", "∞");

    public static ITemporalValue parse(String expression) {
        expression = stripBrackets(expression.trim()); // trim and remove brackets

        // check if it is a constant time value
        try {
            return new TimeInstance(Double.parseDouble(expression));
        } catch (NumberFormatException e) {
        }

        // check if it is a relative time value and parse it to an instance or interval
        try {
            if (expression.startsWith("=")) {
                return new TimeInstance(Double.parseDouble(expression.substring(1)));
            } else if (expression.startsWith(">=")) {
                return new TemporalInterval(Double.parseDouble(expression.substring(2)), Double.POSITIVE_INFINITY);
            } else if (expression.startsWith(">")) {
                return new TemporalInterval(Double.parseDouble(expression.substring(1)), Double.POSITIVE_INFINITY, false);
            } else if (expression.startsWith("<=")) {
                return new TemporalInterval(0, Double.parseDouble(expression.substring(2)));
            } else if (expression.startsWith("<")) {
                return new TemporalInterval(0, Double.parseDouble(expression.substring(1)), true, false);
            }
        } catch (NumberFormatException ignored) {
        }


        //create pattern for <decimal number>[,;:]<decimal number>
        Pattern pattern = Pattern.compile("(\\d+\\.?\\d*)([,;:])(\\+?((\\d+\\.?\\d*)|(inf|infinity|∞)))");
        Matcher matcher = pattern.matcher(expression.toLowerCase().replaceAll("\s", ""));


        // check if it is an interval description

        if (matcher.matches()) {
            MatchResult result = matcher.toMatchResult();
            //remove starting + from result.group(3) if it exists
            String secondNumber = result.group(3).startsWith("+") ? result.group(3).substring(1) : result.group(3);

            if (infList.contains(secondNumber)) {
                return new TemporalInterval(Double.parseDouble(result.group(1)), Double.POSITIVE_INFINITY);
            } else {
                return new TemporalInterval(Double.parseDouble(result.group(1)), Double.parseDouble(result.group(3)));
            }

        }

        // otherwise,  return a TemporalEventDescription wrapper
        return new TemporalEventDescription(expression);
    }

    private static String stripBrackets(String s) {
        if (s.startsWith("[") && s.endsWith("]")) {
            return s.substring(1, s.length() - 1);
        } else return s;
    }
}
