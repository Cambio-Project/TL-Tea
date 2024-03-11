package cambio.tltea.parser.core.temporal;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record TimeInstance(double time, boolean plusEpsilon) implements ITemporalValue, Comparable<TimeInstance> {

    public TimeInstance(int time, boolean plusEpsilon) {
        this(Double.valueOf(time), plusEpsilon);
    }

    public TimeInstance(double time) {
        this(time, false);
    }

    public TimeInstance(int time) {
        this(time, false);
    }

    public TimeInstance(@NotNull TimeInstance other) {
        this(other.time, other.plusEpsilon);
    }

    public TimeInstance(@NotNull TimeInstance other, boolean plusEpsilon) {
        this(other.time, plusEpsilon);
    }

    public TimeInstance(@NotNull Number time, boolean plusEpsilon) {
        this(time.doubleValue(), plusEpsilon);
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

    @Override
    public String toString() {
        return "TimeInstance{" +
            "time=" + time +
            "epsilon=" + plusEpsilon +
            '}';
    }
}
