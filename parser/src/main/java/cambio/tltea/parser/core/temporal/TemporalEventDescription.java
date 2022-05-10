package cambio.tltea.parser.core.temporal;

import java.util.Objects;

public record TemporalEventDescription(String value) implements ITemporalValue {

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemporalEventDescription)) {
            return false;
        }

        TemporalEventDescription that = (TemporalEventDescription) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
