package cambio.tltea.parser.core.temporal;

/**
 * Interface to mark temporal value classes.
 * <p>
 * For available implementations see
 *
 * @see TemporalInterval
 * @see TemporalEventDescription
 * @see TimeInstance
 */
public sealed interface ITemporalValue permits TemporalInterval, TemporalEventDescription, TimeInstance {

}
