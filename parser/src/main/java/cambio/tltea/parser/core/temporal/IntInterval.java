package cambio.tltea.parser.core.temporal;

public class IntInterval extends Interval<Integer> {
    public IntInterval(int start, int end) {
        super(start, end);
    }

    @Override
    public Integer getDuration() {
        return end - start;
    }
}
