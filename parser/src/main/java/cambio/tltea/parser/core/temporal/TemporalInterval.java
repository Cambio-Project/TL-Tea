package cambio.tltea.parser.core.temporal;

import java.util.Objects;

public final class TemporalInterval implements ITemporalValue {

    private final TimeInstance start;
    private final TimeInstance end;
    private final double rawStart;
    private final double rawEnd;
    private final boolean startInclusive;
    private final boolean endInclusive;

    public TemporalInterval(double start, double end) {
        this(start, end, true);
    }

    public TemporalInterval(double start, double end, boolean startInclusive) {
        this(start, end, startInclusive, true);
    }

    public TemporalInterval(double start, double end, boolean startInclusive, boolean endInclusive) {
        this.start = new TimeInstance(start);
        this.end = new TimeInstance(end);
        this.rawStart = start;
        this.rawEnd = end;
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

    public TimeInstance getStart() {
        return start;
    }

    public TimeInstance getEnd() {
        return end;
    }

    public Double getStartAsDouble() {
        return rawStart;
    }

    public Double getEndAsDouble() {
        return rawEnd;
    }

    public boolean isStartInclusive() {
        return startInclusive;
    }

    public boolean isEndInclusive() {
        return endInclusive;
    }


    public boolean contains(double value) {
        return ((startInclusive && value >= rawStart) ||
               (!startInclusive && value > rawStart))
               &&
               ((endInclusive && value <= rawEnd) ||
               (!endInclusive && value < rawEnd));

    }

    public boolean contains(TimeInstance time){
        return contains(time.getTime());
    }

    public boolean contains(TemporalInterval interval) {
        return contains(interval.getStartAsDouble()) && contains(interval.getEndAsDouble());
    }


    public boolean overlaps(TemporalInterval interval) {
        return !(isBefore(interval) || isAfter(interval));
    }


    public boolean isBefore(TemporalInterval interval) {
        if (rawEnd <= interval.getStartAsDouble()) {
            if (rawEnd == interval.getStartAsDouble()) {
                return !(endInclusive && interval.isStartInclusive());
            }
            return true;
        }
        return false;
    }

    public boolean isAfter(TemporalInterval interval) {
        if (rawStart >= interval.getEndAsDouble()) {
            if (rawStart == interval.getEndAsDouble()) {
                return !(startInclusive && interval.isEndInclusive());
            }
            return true;
        }
        return false;
    }

    public Double getDuration() {
        return rawEnd - rawStart;
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
        return startInclusive == interval.startInclusive && endInclusive == interval.endInclusive && (rawStart
            == interval.rawStart) && (rawEnd == interval.rawEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rawStart, rawEnd, startInclusive, endInclusive);
    }

    @Override
    public String toString() {
        return (startInclusive ? "[" : "(") + rawStart + "," + rawEnd + (endInclusive ? "]" : ")");
    }
}
