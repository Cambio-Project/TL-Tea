package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.interpreter.nodes.StateChangeEvent;
import cambio.tltea.interpreter.nodes.StateChangedPublisher;
import cambio.tltea.parser.core.temporal.*;

/**
 * @author Lion Wagner
 */
public abstract class CauseNode extends StateChangedPublisher<Boolean> {

    private final TemporalOperatorInfo temporalContext;

    public CauseNode(TemporalOperatorInfo temporalContext) {
        this.temporalContext = temporalContext;
    }

    public abstract Boolean getCurrentValue();

    protected final boolean satisfiesTemporalContext(ITemporalValue activationTime) {
        if (temporalContext.temporalValueExpression() instanceof TemporalInterval interval) {
            if (activationTime instanceof TemporalInterval activationInterval) {
                return interval.contains(activationInterval);
            } else if (activationTime instanceof TimeInstance timeInstance) {
                return interval.contains(timeInstance.getTime());
            }
        } else if (temporalContext.temporalValueExpression() instanceof TimeInstance timeInstance) {
            if (activationTime instanceof TimeInstance activationTimeInstance) {
                return timeInstance.equals(activationTimeInstance);
            }
        } else if (temporalContext.temporalValueExpression() instanceof TemporalEventDescription temporalEventDescription) {
            if (activationTime instanceof TemporalEventDescription activationTemporalEventDescription) {
                return temporalEventDescription.equals(activationTemporalEventDescription);
            }
        }
        return false;
    }

    @Override
    protected void notifySubscribers(StateChangeEvent<Boolean> event) {
        if (satisfiesTemporalContext(event.when())) {
            super.notifySubscribers(event);
        }
    }
}
