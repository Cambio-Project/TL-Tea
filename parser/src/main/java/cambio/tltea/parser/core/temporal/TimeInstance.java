package cambio.tltea.parser.core.temporal;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class TimeInstance implements ITemporalValue, Comparable<TimeInstance> {
    private final double time;
    private final boolean plusEpsilon;

    public TimeInstance(double time, boolean plusEpsilon) {
        this.time = time;
        this.plusEpsilon = plusEpsilon;
    }

    public TimeInstance(double time) {
        this(time, false);
    }

    public TimeInstance(int time, boolean plusEpsilon) {
        this.time = time;
        this.plusEpsilon = plusEpsilon;
    }

    public TimeInstance(int time) {
        this(time, false);
    }

    public TimeInstance(@NotNull TimeInstance other) {
        this.time = other.time;
        this.plusEpsilon = other.plusEpsilon;
    }

    public TimeInstance(@NotNull TimeInstance other, boolean plusEpsilon) {
        this.time = other.time;
        this.plusEpsilon = plusEpsilon;
    }

    public TimeInstance(@NotNull Number time, boolean plusEpsilon) {
        this.time = time.doubleValue();
        this.plusEpsilon = plusEpsilon;
    }

    public TimeInstance(@NotNull Number time) {
        this(time, false);
    }

    public double getTime() {
        return time;
    }

    public boolean isPlusEpsilon() {
        return plusEpsilon;
    }

    public TimeInstance add(@NotNull TimeInstance timeToAdd) {
        return new TimeInstance(this.time + timeToAdd.time, this.plusEpsilon);
    }

    public TimeInstance subtract(@NotNull TimeInstance timeToSubtract) {
        return new TimeInstance(Math.max(0, this.time - timeToSubtract.time), this.plusEpsilon);
    }

    public boolean subtractOverflow(TimeInstance timeToSubtract) {
        return timeToSubtract.time > this.time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeInstance that = (TimeInstance) o;
        return Double.compare(that.time, this.time) == 0 && Boolean.compare(that.plusEpsilon, this.plusEpsilon) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, plusEpsilon);
    }

    @Override
    public int compareTo(@NotNull TimeInstance o) {
        int timeComparison = Double.compare(this.time, o.time);
        boolean equalTime = timeComparison == 0;

        if (equalTime) {
            if (this.plusEpsilon && !o.plusEpsilon) {
                return 1;
            } else if (!this.plusEpsilon && o.plusEpsilon) {
                return -1;
            } else {
                return 0;
            }
        } else {
            return timeComparison;
        }
    }

    public boolean isAfter(TimeInstance fromPoint) {
        return this.compareTo(fromPoint) > 0;
    }

    public boolean isBefore(TimeInstance fromPoint) {
        return this.compareTo(fromPoint) < 0;
    }

    public boolean isBeforeEquals(TimeInstance fromPoint) {
        return this.compareTo(fromPoint) <= 0;
    }

    public boolean isAfterEquals(TimeInstance fromPoint) {
        return this.compareTo(fromPoint) >= 0;
    }

    @Override
    public String toString() {
        return "TimeInstance{" +
            "time=" + time +
            "epsilon=" + plusEpsilon +
            '}';
    }
}
