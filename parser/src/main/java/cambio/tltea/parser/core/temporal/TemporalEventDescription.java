package cambio.tltea.parser.core.temporal;

import java.util.Objects;

public class TemporalEventDescription implements ITemporalValue {

    private final String value;

    public TemporalEventDescription(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TemporalEvent: "+ value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemporalEventDescription that = (TemporalEventDescription) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
