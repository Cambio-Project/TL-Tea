package cambio.tltea.parser.core.temporal;

import java.util.Objects;

public final class TemporalInterval implements ITemporalValue {

    private final double start;
    private final double end;
    private final boolean startInclusive;
    private final boolean endInclusive;

    public TemporalInterval(double start, double end) {
        this(start, end, true);
    }

    public TemporalInterval(double start, double end, boolean startInclusive) {
        this(start, end, startInclusive, true);
    }

    public TemporalInterval(double start, double end, boolean startInclusive, boolean endInclusive) {
        this.start = start;
        this.end = end;
        this.startInclusive = startInclusive && start != Double.NEGATIVE_INFINITY;
        this.endInclusive = endInclusive && end != Double.POSITIVE_INFINITY;

        if (start > end) {
            throw new IllegalArgumentException("Start value must be less than end value");
        }
        else if (start == end) {
            if (!startInclusive || !endInclusive) {
                throw new IllegalArgumentException("Start and end values must be equal if they are not inclusive");
            }
        }
    }

    public Double getStart() {
        return start;
    }

    public Double getEnd() {
        return end;
    }

    public boolean isStartInclusive() {
        return startInclusive;
    }

    public boolean isEndInclusive() {
        return endInclusive;
    }


    public boolean contains(double value) {
        return ((startInclusive && value >= start) ||
               (!startInclusive && value > start))
               &&
               ((endInclusive && value <= end) ||
               (!endInclusive && value < end));

    }


    public boolean contains(TemporalInterval interval) {
        return contains(interval.getStart()) && contains(interval.getEnd());
    }


    public boolean overlaps(TemporalInterval interval) {
        return !(isBefore(interval) || isAfter(interval));
    }


    public boolean isBefore(TemporalInterval interval) {
        if (end <= interval.getStart()) {
            if (end == interval.getStart()) {
                return !(endInclusive && interval.isStartInclusive());
            }
            return true;
        }
        return false;
    }

    public boolean isAfter(TemporalInterval interval) {
        if (start >= interval.getEnd()) {
            if (start == interval.getEnd()) {
                return !(startInclusive && interval.isEndInclusive());
            }
            return true;
        }
        return false;
    }

    public Double getDuration() {
        return end - start;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TemporalInterval interval = (TemporalInterval) o;
        return startInclusive == interval.startInclusive && endInclusive == interval.endInclusive && (start == interval.start) && (end == interval.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, startInclusive, endInclusive);
    }

    @Override
    public String toString() {
        return (startInclusive ? "[" : "(") + start + "," + end + (endInclusive ? "]" : ")");
    }
}
