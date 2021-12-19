package cambio.tltea.parser.core.temporal;

import java.util.List;

public final class TemporalPropositionParser {

    public static ITemporalValue parse(String expression) {
        expression = stripBrackets(expression.trim()); // trim and remove brackets

        // check if it is a constant time value
        try {
            return new TimeInstance(Double.parseDouble(expression));
        } catch (NumberFormatException e) {
        }

        // check if it is a directional time value
        try {
            if (expression.startsWith("=")) {
                return new TimeInstance(Double.parseDouble(expression.substring(1)));
            } else if (expression.startsWith(">=")) {
                return new DoubleInterval(Double.parseDouble(expression.substring(2)), Double.POSITIVE_INFINITY);
            } else if (expression.startsWith(">")) {
                return new DoubleInterval(Double.parseDouble(expression.substring(1)), Double.POSITIVE_INFINITY, false);
            } else if (expression.startsWith("<=")) {
                return new DoubleInterval(0, Double.parseDouble(expression.substring(2)));
            } else if (expression.startsWith("<")) {
                return new DoubleInterval(0, Double.parseDouble(expression.substring(1)), true, false);
            }
        } catch (NumberFormatException e) {
        }

        // check if it is an interval description
        if (expression.contains(",")) {
            String[] parts = expression.toLowerCase().replaceAll("\s", "").split(",");
            try {
                //remove + from the start of parts[1]
                if (parts[1].startsWith("+")) {
                    parts[1] = parts[1].substring(1);
                }
                List<String> infList = List.of("inf", "infinity", "∞");
                if (infList.contains(parts[1])) {
                    return new DoubleInterval(Double.parseDouble(parts[0]), Double.POSITIVE_INFINITY);
                } else return new DoubleInterval(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
            } catch (NumberFormatException e) {
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
