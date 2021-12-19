package cambio.tltea.parser.core.temporal;

public final class DoubleInterval extends Interval<Double> {

    public DoubleInterval(double start, double end) {
        super(start, end, true, !Double.isInfinite(end));
    }

    public DoubleInterval(double start, double end, boolean startInclusive) {
        super(start, end, startInclusive, !Double.isInfinite(end));
    }

    public DoubleInterval(double start, double end, boolean startInclusive, boolean endInclusive) {
        super(start, end, startInclusive, endInclusive && !Double.isInfinite(end));
    }

    @Override
    public Double getDuration() {
        return end - start;
    }

    @Override
    public String toString() {
        return (startInclusive ? "[" : "(") + start + "," + end + (endInclusive ? "]" : ")");
    }
}
