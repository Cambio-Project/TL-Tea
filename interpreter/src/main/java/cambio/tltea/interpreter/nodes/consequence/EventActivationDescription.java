package cambio.tltea.interpreter.nodes.consequence;

import cambio.tltea.parser.core.temporal.TemporalOperatorInfo;

/**
 * @author Lion Wagner
 */
public record EventActivationDescription(String eventName, TemporalOperatorInfo temporalOperatorInfo) {
}
