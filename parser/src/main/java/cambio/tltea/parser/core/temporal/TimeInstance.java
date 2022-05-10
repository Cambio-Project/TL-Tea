package cambio.tltea.parser.core.temporal;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class TimeInstance implements ITemporalValue {
    private final double time;

    public TimeInstance(double time) {
        this.time = time;
    }

    public TimeInstance(int time) {
        this.time = time;
    }

    public TimeInstance(@NotNull TimeInstance other) {
        this.time = other.time;
    }

    public TimeInstance(@NotNull Number time) {
        this.time = time.doubleValue();
    }

    public double getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeInstance that = (TimeInstance) o;
        return Double.compare(that.time, time) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time);
    }
}
