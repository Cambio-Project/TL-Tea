package cambio.tltea.parser.core.temporal;

import java.util.Objects;

public abstract class Interval<T extends Number> implements ITemporalValue {

    protected final T start;
    protected final T end;
    protected final boolean startInclusive;
    protected final boolean endInclusive;

    public Interval(T start, T end) {
        this(start, end, true);
    }

    public Interval(T start, T end, boolean startInclusive) {
        this(start, end, startInclusive, true);
    }

    public Interval(T start, T end, boolean startInclusive, boolean endInclusive) {
        this.start = start;
        this.end = end;
        this.startInclusive = startInclusive;
        this.endInclusive = endInclusive;
    }

    public T getStart() {
        return start;
    }

    public T getEnd() {
        return end;
    }

    public boolean isStartInclusive() {
        return startInclusive;
    }

    public boolean isEndInclusive() {
        return endInclusive;
    }


    public boolean contains(T value) {
        return (startInclusive && value.doubleValue() >= start.doubleValue()) || (!startInclusive && value.doubleValue() > start.doubleValue()) &&
                (endInclusive && value.doubleValue() <= end.doubleValue()) || (!endInclusive && value.doubleValue() < end.doubleValue());

    }


    public boolean contains(Interval<T> interval) {
        return (startInclusive && interval.getStart().doubleValue() >= start.doubleValue()) || (!startInclusive && interval.getStart().doubleValue() > start.doubleValue()) &&
                (endInclusive && interval.getEnd().doubleValue() <= end.doubleValue()) || (!endInclusive && interval.getEnd().doubleValue() < end.doubleValue());
    }


    public boolean overlaps(Interval<T> interval) {
        return !(isBefore(interval) || isAfter(interval));
    }


    public boolean isBefore(Interval<T> interval) {
        //TODO: inclusive intervals behavior
        return end.doubleValue() < interval.getStart().doubleValue();
    }

    public boolean isAfter(Interval<T> interval) {
        //TODO: inclusive intervals behavior
        return start.doubleValue() > interval.getEnd().doubleValue();
    }

    public abstract T getDuration();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interval<?> interval = (Interval<?>) o;
        return startInclusive == interval.startInclusive && endInclusive == interval.endInclusive && start.equals(interval.start) && end.equals(interval.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, startInclusive, endInclusive);
    }
}
