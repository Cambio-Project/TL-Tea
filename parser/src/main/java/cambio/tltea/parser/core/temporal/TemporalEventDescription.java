package cambio.tltea.parser.core.temporal;

public record TemporalEventDescription(String value) implements ITemporalValue {

    public String getValue() {
        return value;
    }

}
