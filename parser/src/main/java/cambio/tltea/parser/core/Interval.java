package cambio.tltea.parser.core;

/**
 * @author Lion Wagner
 */
public class Interval {

    public final double start;
    public final double end;
    public final double length;

    public Interval(double start, double end) {
        this.start = start;
        this.end = end;

        this.length = end - start;

        if(this.start<0){
            throw new IllegalArgumentException("Start must be greater than 0");
        }
        if (length < 0) {
            throw new IllegalArgumentException("start must be less than end");
        }
    }

    public Interval(String start, String end) {
        this(Double.parseDouble(start),Double.parseDouble(end));
    }
}
